package ourrobots.ags.polylunar.weapons;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.prediction.PhysicsEngine;
import ourrobots.ags.polylunar.prediction.RobotSim;
import ourrobots.ags.polylunar.prediction.WaveIntersect;
import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.Range;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

public class GuaranteedHitTargeting {
    public static double getAim(Rules rules, AbsolutePoint source, HostileBot target, double bulletpower) {
        
        RelativePoint rel = RelativePoint.fromPP(source, target.getLocation());
        Range r1 = getExtremeRange(rules, source, target, bulletpower, rel.direction, 1);
        Range r2 = getExtremeRange(rules, source, target, bulletpower, rel.direction, -1);
        Range middle = Range.getMiddle(r1, r2);
        if (Range.getIntersection(r1, r2) != null) {
            //System.out.println("Guaranteed Hit: "+middle+" asdad "+r1+" : "+r2);
            return normalAbsoluteAngle(rel.direction+middle.getCenter());
        } else {
            //System.out.println("Extreme: "+r1+" : "+r2);
            //  //return normalAbsoluteAngle(rel.direction+middle.getCenter());
        }
        
        return Double.NaN;
    }
    
    private static Range getExtremeRange(Rules rules, AbsolutePoint source, HostileBot target, double bulletpower, double referenceAngle, int polarity) {
        RobotSim sim = new RobotSim();
        double bulletspeed = rules.getBulletSpeed(bulletpower);
        double radius;
        if (target.hasNewData()) {
            sim.location = target.getNewLocation();
            sim.velocity = target.getNewVelocity();
            radius = -2*bulletspeed;
        } else {
            sim.location = target.getLocation();
            sim.velocity = target.getVelocity();
            radius = -3*bulletspeed;
        }
        double goaldirection = normalAbsoluteAngle(RelativePoint.fromPP(source, sim.location).direction+Math.PI/2);
        Range hitRange = null;
        
        while(true) {
            radius += bulletspeed;
            if (radius > source.distance(sim.location)+20) {
                break;
            } else if (radius > 0.01) {
                Range newHitRange = WaveIntersect.getHitRange(source, radius, bulletspeed, referenceAngle, sim.location);
                if (newHitRange != null) {
                    if (hitRange != null)
                        hitRange.grow(newHitRange);
                    else
                        hitRange = newHitRange;
                }
            } 
                
            double turn = -sim.velocity.getDirDist(goaldirection);
            if (Math.abs(turn) > Math.PI/2)
                turn = normalRelativeAngle(turn+Math.PI);
            double ahead = Double.POSITIVE_INFINITY*polarity;
            
            PhysicsEngine.simulateTick(rules, sim, ahead, turn);
        }
        
        
        return hitRange;
    }
}
