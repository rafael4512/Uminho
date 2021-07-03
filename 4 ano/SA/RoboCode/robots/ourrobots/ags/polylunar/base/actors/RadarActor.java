package ourrobots.ags.polylunar.base.actors;

import robocode.TeamRobot;

public class RadarActor {
    private final TeamRobot peer;
    
    public RadarActor(TeamRobot peer) {
        this.peer = peer;
    }
    
    public void setTurnRadar(double arg0) {
        peer.setTurnRadarRightRadians(arg0);
    }
}
