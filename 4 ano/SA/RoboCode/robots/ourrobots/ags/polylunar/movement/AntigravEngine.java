package ourrobots.ags.polylunar.movement;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.robotdata.*;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import java.util.Collection;

public class AntigravEngine {
    private class AGForce {
        private double x=0, y=0;
    }
    
    private final Rules rules;
    private final AllyData allies;
    private final EnemyData enemies;
    
    public AntigravEngine(Rules rules, AllyData allies, EnemyData enemies) {
        this.rules = rules;
        this.allies = allies;
        this.enemies = enemies; 
    }
    
    public RelativePoint runGravity(SelfStatus status) {
        AbsolutePoint location = status.getLocation();
        AGForce force = new AGForce();
        
        Collection<FriendlyBot> friends = allies.getLiving();
        Collection<HostileBot> hostiles = enemies.getLiving();
        
        for (FriendlyBot b : friends) {
            if (!b.getName().equals(rules.NAME))
                repelToDistance(force, location, b.getLocation(), 0.5+Math.max(0, 100-b.getEnergy())/200, 200);
        }
                
        for (HostileBot b : hostiles) {
            final double preferredDistance;
            if (friends.size() == 1)
                preferredDistance = 45;
            else
                preferredDistance = 500; 
            repelToDistance(force, location, b.getLocation(), 0.5+b.getEnergy()/200, preferredDistance);
            
        }
        
        cornerForces(force, location, 1.0);
        
        force.x -= status.getVelocity().x/8000;
        force.y -= status.getVelocity().y/8000;
        
        adjustForWalls(force);
        
        RelativePoint r = RelativePoint.fromXY(force.x, force.y);
        return r;
    }
    
    private void cornerForces(AGForce force, AbsolutePoint location, double strength) {
        repelFromForce(force, location, AbsolutePoint.fromXY(0, 0), strength);
        repelFromForce(force, location, AbsolutePoint.fromXY(0, rules.BATTLEFIELD_HEIGHT), strength);
        repelFromForce(force, location, AbsolutePoint.fromXY(rules.BATTLEFIELD_WIDTH, 0), strength);
        repelFromForce(force, location, AbsolutePoint.fromXY(rules.BATTLEFIELD_WIDTH, rules.BATTLEFIELD_HEIGHT), strength);
    }
    
    // Repel a force from a point
    private void repelFromForce(AGForce force, AbsolutePoint location, AbsolutePoint repelFrom, double strength) {
        RelativePoint rel = RelativePoint.fromPP(location, repelFrom);
        rel = RelativePoint.fromDM(rel.direction, strength/rel.magnitude);
        force.x += rel.x;
        force.y += rel.y;
    }
    
    private void repelToDistance(AGForce force, AbsolutePoint location, AbsolutePoint repelFrom, double strength, double distance) {
        RelativePoint rel = RelativePoint.fromPP(location, repelFrom);
        //rel = RelativePoint.fromDM(rel.direction, sign(distance-rel.magnitude)*strength/Math.abs(rel.magnitude-distance));
        rel = RelativePoint.fromDM(rel.direction, sign(distance-rel.magnitude)*strength*(sqr(rel.magnitude-distance)/sqr(distance)));
        force.x += rel.x;
        force.y += rel.y;
    }
    
    private static double sign(double s) {
        if (s > 0)
            return 1;
        else if (s < 0)
            return -1;
        else
            return 0;
    }
    
    private static double sqr(double s) {
        return s*s;
    }
    
    private void adjustForWalls(AGForce force) {
        AbsolutePoint location = allies.status.getLocation();
        double topDist = topWallDist(location);
        double bottomDist = bottomWallDist(location);
        double leftDist = leftWallDist(location);
        double rightDist = rightWallDist(location);
        
        if (topDist < 100 && force.y > 0)
            force.y *= (topDist/100);
        if (bottomDist < 100 && force.y < 0)
            force.y *= (bottomDist/100);
        if (leftDist < 100 && force.x > 0)
            force.x *= (leftDist/100);
        if (rightDist < 100 && force.x < 0)
            force.x *= (rightDist/100);
    }
    
    private double topWallDist(AbsolutePoint location) {
        return location.getY() - 18;
    }
    private double bottomWallDist(AbsolutePoint location) {
        return rules.BATTLEFIELD_HEIGHT - location.getY() - 18;
    }
    private double leftWallDist(AbsolutePoint location) {
        return location.getX() - 18;
    }
    private double rightWallDist(AbsolutePoint location) {
        return rules.BATTLEFIELD_WIDTH - location.getX() - 18;
    }
}
