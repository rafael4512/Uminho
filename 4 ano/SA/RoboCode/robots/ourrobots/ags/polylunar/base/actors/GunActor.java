package ourrobots.ags.polylunar.base.actors;

import robocode.Bullet;
import robocode.TeamRobot;

public class GunActor {
    private final TeamRobot peer;
    
    public GunActor(TeamRobot peer) {
        this.peer = peer;
    }
    
    public Bullet setFire(double arg0) {
        return peer.setFireBullet(arg0);
    }
    
    public void setTurnGun(double arg0) {
        peer.setTurnGunRightRadians(arg0);
    }
}
