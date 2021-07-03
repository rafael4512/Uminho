package ourrobots.ags.polylunar.base.actors;

import robocode.TeamRobot;

public class MovementActor {
    private final TeamRobot peer;
    
    public MovementActor(TeamRobot peer) {
        this.peer = peer;
    }
    
    public void setMove(double velocity) {
        setMove(velocity, velocity==0 ? 0 : (velocity > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY));
    }
    
    public void setMove(double velocity, double dist) {
        peer.setMaxVelocity(Math.abs(velocity));
        peer.setAhead(dist);
    }
    public void setTurnBody(double arg0) {
        peer.setTurnRightRadians(arg0);
    }
}
