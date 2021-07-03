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

public class LunarTwinTargeting {
    public static double getAim(Rules rules, AbsolutePoint source, HostileBot target, double bulletpower) {
        RelativePoint rel = RelativePoint.fromPP(source, target.getLocation());
        final Range hitRange = simulateBot(rules, source, target, bulletpower, rel.direction);
        if (hitRange == null) {
            System.out.println("WARNING! TARGETING FAILURE!");
            return rel.direction;
        }
        return normalAbsoluteAngle(rel.direction + hitRange.getCenter());
    }
    
    private static Range simulateBot(Rules rules, AbsolutePoint source, HostileBot target, double bulletpower, double referenceAngle) {
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
        
        Range hitRange = null;
        while(true) {
            radius += bulletspeed;
            if (radius > source.distance(sim.location)+20) {
                break;
            } else if (radius > 0.01) {
                Range newHitRange = WaveIntersect.getHitRange(source, radius, bulletspeed, referenceAngle, sim.location);
                if (newHitRange != null) {
                    if (hitRange != null) {
                        hitRange.grow(newHitRange);
                        return hitRange;
                    } else {
                        hitRange = newHitRange;
                    }
                }
            }
            PhysicsEngine.simulateTick(rules, sim, sim.velocity.magnitude, 0); //target.getAvgAngularVelocity()
        }
        
        return hitRange;
    }
}
