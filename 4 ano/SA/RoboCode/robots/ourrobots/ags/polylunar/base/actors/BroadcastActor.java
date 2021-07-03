package ourrobots.ags.polylunar.base.actors;

import robocode.TeamRobot;

public class BroadcastActor {
    private final TeamRobot peer;
    
    public BroadcastActor(TeamRobot peer) {
        this.peer = peer;
    }
    
    public void broadcastMessage(java.io.Serializable s) {
        try {
            peer.broadcastMessage(s);
        } catch (java.io.IOException e) {
            System.out.println("Error during sending team message!");
            e.printStackTrace();
        }
    }
}
