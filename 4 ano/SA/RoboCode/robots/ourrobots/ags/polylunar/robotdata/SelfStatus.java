package ourrobots.ags.polylunar.robotdata;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.prediction.PhysicsEngine;
import ourrobots.ags.polylunar.prediction.RobotSim;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;
import robocode.RobotStatus;

public class SelfStatus {
    private AbsolutePoint location;
    private AbsolutePoint nextLocation;
    private RelativePoint velocity;
    public RobotStatus status;
    
    void update(RobotStatus status) {
        location = AbsolutePoint.fromXY(status.getX(), status.getY());
        velocity = RelativePoint.fromDM(status.getHeadingRadians(), status.getVelocity());
        this.status = status;
        nextLocation = location;
    }
    
    public AbsolutePoint getLocation() {
        return location;
    }
    
    public AbsolutePoint getNextLocation() {
        return nextLocation;
    }
    
    public RelativePoint getVelocity() {
        return velocity;
    }
    
    public void setIntention(Rules rules, double ahead, double turn) {
        RobotSim sim = new RobotSim();
        sim.location = location;
        sim.velocity = velocity;
        PhysicsEngine.simulateTick(rules, sim, ahead, turn);
        nextLocation = sim.location;
    }
}
