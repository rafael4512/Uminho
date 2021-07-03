package ourrobots.ags.polylunar.base;

import robocode.TeamRobot;

/**
 * A consoladated "Rules" class, which includes gun cooling rate, field size,
 * teammate listings, and always uses radians.
 * 
 * Essentially stores all battle information that is known from the very start
 * of the battle that does not change.
 * 
 * @author Alexander Schultz
 */
public class Rules {
    // Redirection to robocode.Rules
    public final double ACCELERATION = robocode.Rules.ACCELERATION;
    public final double DECELERATION = robocode.Rules.DECELERATION;
    public final double GUN_TURN_RATE = robocode.Rules.GUN_TURN_RATE_RADIANS;
    public final double MAX_BULLET_POWER = robocode.Rules.MAX_BULLET_POWER;
    public final double MAX_TURN_RATE = robocode.Rules.MAX_TURN_RATE_RADIANS;
    public final double MAX_VELOCITY = robocode.Rules.MAX_VELOCITY;
    public final double MIN_BULLET_POWER = robocode.Rules.MIN_BULLET_POWER;
    public final double RADAR_SCAN_RADIUS = robocode.Rules.RADAR_SCAN_RADIUS;
    public final double RADAR_TURN_RATE = robocode.Rules.RADAR_TURN_RATE_RADIANS;
    public final double ROBOT_HIT_BONUS = robocode.Rules.ROBOT_HIT_BONUS;
    public final double ROBOT_HIT_DAMAGE = robocode.Rules.ROBOT_HIT_DAMAGE;
    public double getBulletDamage(double bulletPower) { return robocode.Rules.getBulletDamage(bulletPower); }
    public double getBulletHitBonus(double bulletPower) { return robocode.Rules.getBulletHitBonus(bulletPower); }
    public double getBulletSpeed(double bulletPower) { return robocode.Rules.getBulletSpeed(bulletPower); }
    public double getGunHeat(double bulletPower) { return robocode.Rules.getGunHeat(bulletPower); }
    public double getTurnRate(double velocity) { return robocode.Rules.getTurnRateRadians(velocity); }
    public double getWallHitDamage(double velocity) { return robocode.Rules.getWallHitDamage(velocity); }
    
    // New features
    public final double GUN_COOLING_RATE;
    public final double BATTLEFIELD_WIDTH;
    public final double BATTLEFIELD_HEIGHT;
    public final String[] TEAMMATES;
    public final int ENEMIES;
    public final String NAME;
    
    public Rules(TeamRobot peer) {
        GUN_COOLING_RATE = peer.getGunCoolingRate();
        BATTLEFIELD_WIDTH = peer.getBattleFieldWidth();
        BATTLEFIELD_HEIGHT = peer.getBattleFieldHeight();
        if (peer.getTeammates() != null)
            TEAMMATES = peer.getTeammates();
        else
            TEAMMATES = new String[0];
        NAME = peer.getName();
        ENEMIES = peer.getOthers()-TEAMMATES.length;
    }
    
    public boolean isTeammate(String name) {
        if (TEAMMATES == null)
            return false;
        for (String teammate : TEAMMATES) {
            if (teammate.equals(name)) return true;
        }
        return false;
    }
    
    public double energyToBulletPower(double energy) {
        if (energy/4 <= 1)
            return energy/4;
        else
            return (energy+2)/6;
    }
}
