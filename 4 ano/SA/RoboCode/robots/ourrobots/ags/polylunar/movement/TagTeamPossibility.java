package ourrobots.ags.polylunar.movement;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.robotdata.FriendlyBot;
import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

public class TagTeamPossibility {
    public final FriendlyBot[] team = new FriendlyBot[2];
    public final AbsolutePoint[] goals = new AbsolutePoint[2];
    public final HostileBot target;
    public final double dist;
    
    public TagTeamPossibility(Rules rules, FriendlyBot t1, FriendlyBot t2, HostileBot target) {
        this.team[0] = t1;
        this.team[1] = t2;
        this.target = target;
        //dist = source.getLocation().distance(target.getLocation());
        
        final AbsolutePoint averageLocation = AbsolutePoint.fromXY(
                (t1.getLocation().x+t2.getLocation().x)/2,
                (t1.getLocation().y+t2.getLocation().y)/2);
        
        final AbsolutePoint lPredict = target.lPredict;
        
        final RelativePoint relativeLocation = RelativePoint.fromPP(lPredict, averageLocation);
        
        final double selectedDistance;
        final double totalEnergy = t1.getEnergy() + t2.getEnergy();
        if (totalEnergy > target.getEnergy() && target.getEnergy() < 40)
            selectedDistance = 50;
        else
            selectedDistance = 50;
        
        // Create goal points on each side of the enemy
        RelativePoint rel = RelativePoint.fromDM(relativeLocation.getDirection()+(Math.PI/2), selectedDistance);
        AbsolutePoint goal1 = lPredict.addRelativePoint(rel);
        AbsolutePoint goal2 = lPredict.addRelativePoint(rel.incrementDirMag(Math.PI, 0));
        goal1 = wallAdjust(rules, goal1, lPredict);
        goal2 = wallAdjust(rules, goal2, lPredict);
        
        double dist1 = goal1.distance(t1.getLocation())+goal2.distance(t2.getLocation());
        double dist2 = goal2.distance(t1.getLocation())+goal1.distance(t2.getLocation());
        final double d;
        if (dist2 < dist1) {
            AbsolutePoint tmpgoal = goal1;
            goal1 = goal2;
            goal2 = tmpgoal;
            d = dist2;
        } else {
            d = dist1;
        }
        goals[0] = goal1;
        goals[1] = goal2;
        
        dist = d;
    }
    
    public double getDistance() {
        return dist;
    }
    
    public boolean conflictsWith(TagTeamPossibility other) {
        return (team[0].getName().equals(other.team[0].getName()) ||
                team[1].getName().equals(other.team[1].getName()) ||
                team[0].getName().equals(other.team[1].getName()) ||
                team[1].getName().equals(other.team[0].getName()) ||
                target.getName().equals(other.target.getName())); 
    }
    
    private static final int ROBOT_SIZE = 18;
    private static AbsolutePoint wallAdjust(Rules rules, AbsolutePoint goal, AbsolutePoint target) {
        double x, y;
        
        if (target.getY() > goal.getY())
            y = target.getY() - 50;
        else
            y = target.getY() + 50;
        if (target.getX() > goal.getX())
            x = target.getX() - 50;
        else
            x = target.getX() + 50;
        
        if (Math.round(goal.getX()) > rules.BATTLEFIELD_WIDTH - ROBOT_SIZE) {
            x = rules.BATTLEFIELD_WIDTH - (ROBOT_SIZE+1);
        } else if (Math.round(goal.getX()) < ROBOT_SIZE) {
            x = ROBOT_SIZE+1;
        } else if (Math.round(goal.getY()) > rules.BATTLEFIELD_HEIGHT - ROBOT_SIZE) {
            y = rules.BATTLEFIELD_HEIGHT - (ROBOT_SIZE+1);
        } else if (Math.round(goal.getY()) < ROBOT_SIZE) {
            y = ROBOT_SIZE+1;
        } else {
            return goal;
        }
        return AbsolutePoint.fromXY(x, y);
    }
}
