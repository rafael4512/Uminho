package ourrobots.ags.polylunar.weapons;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.base.actors.GunActor;
import ourrobots.ags.polylunar.robotdata.AllyData;
import ourrobots.ags.polylunar.robotdata.EnemyData;
import ourrobots.ags.polylunar.robotdata.FriendlyBot;
import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.points.AbsolutePoint;

import java.util.Collection;

import static robocode.util.Utils.normalRelativeAngle;

public class GunTargeter {
    private final Rules rules;
    private final AllyData allies;
    private final EnemyData enemies;
    
    public GunTargeter(Rules rules, AllyData allies, EnemyData enemies) {
        this.rules = rules;
        this.allies = allies;
        this.enemies = enemies; 
    }
    
    private double last_bulletpower = 0;
    private boolean ready_to_fire = false;
    public void run(GunActor actor) {
        HostileBot target = getTarget();
        if (target == null)
            return;
        
        if (ready_to_fire && last_bulletpower > 0) {
            actor.setFire(last_bulletpower);
            ready_to_fire = false;
        }
        
        AbsolutePoint nextLocation = allies.status.getNextLocation();
        double bulletpower = getWantedFirepower(nextLocation.distance(target.getLocation()));
        bulletpower = Math.min(bulletpower, rules.energyToBulletPower(target.getEnergy()));
        bulletpower = Math.max(0.1, bulletpower);
        bulletpower = Math.min(allies.status.status.getEnergy()-0.0001, bulletpower);
        double gdirection = GuaranteedHitTargeting.getAim(rules, nextLocation, target, bulletpower);
        if (Double.isNaN(gdirection)) {
            final double direction;
            //if (enemies.getTarget() == null || allies.getLiving().size() == 1 || (target.getLocation().distance(nextLocation) > 400)) {
            //    direction = DCPlayForwardTargeter.getAim(rules, nextLocation, target, bulletpower);
            //} else {
                //direction = SimpleLinearTargeting.getAim(rules, nextLocation, target, bulletpower);
                direction = LunarTwinTargeting.getAim(rules, nextLocation, target, bulletpower);
            //}
            double turn = normalRelativeAngle(direction - allies.status.status.getGunHeadingRadians());
            actor.setTurnGun(turn);
            last_bulletpower = bulletpower;
            ready_to_fire = Math.abs(turn) <= rules.GUN_TURN_RATE;
        } else {
            double turn = normalRelativeAngle(gdirection - allies.status.status.getGunHeadingRadians());
            actor.setTurnGun(turn);
            last_bulletpower = bulletpower;
            ready_to_fire = Math.abs(turn) <= rules.GUN_TURN_RATE;
        }
        /*{
            double direction = DCPlayForwardTargeter.getAim(rules, nextLocation, target, bulletpower);
            double turn = normalRelativeAngle(direction - allies.status.status.getGunHeadingRadians());
            actor.setTurnGun(turn);
            last_bulletpower = bulletpower;
            ready_to_fire = true;
        }*/
    }
    
    public double getWantedFirepower(double distance) {
        if (distance < 75)
            return 3.0;
        else if (distance < 600) 
            return 2.5;
        return 2.0;
    }
    
    public HostileBot getTarget() {
        if (enemies.getTarget() != null)
            return enemies.getTarget();
        
        AbsolutePoint location = allies.status.getLocation();
        if (needTarget(location)) {
            double closestDist = Double.POSITIVE_INFINITY;
            HostileBot closestEnemy = null;
            for (HostileBot h : enemies.getLiving()) {
                double d = location.distance(h.getLocation());
                if (closestDist > d) {
                    closestDist = d;
                    closestEnemy = h;
                }
            }
            if (closestEnemy != null)
                return closestEnemy;
        }
            
        return null;
    }
    
    private boolean needTarget(AbsolutePoint location) {
        Collection<FriendlyBot> friendlyBots = allies.getLiving();
        Collection<HostileBot> hostileBots = enemies.getLiving();
        if (hostileBots.size() == 0)
            return false;
        
        if (friendlyBots.size() == 1)
            return true;
        
        for (HostileBot h : hostileBots) {
            double myDist = h.getLocation().distance(location);
            if (myDist > 100) // 400
                continue;
            boolean anyoneCloser = false;
            for (FriendlyBot f : friendlyBots) {
                if (f.getName().equals(rules.NAME))
                    continue;
                double dist = h.getLocation().distance(f.getLocation());
                if (dist < myDist) {
                    anyoneCloser = true;
                    break;
                }
            }
            if (!anyoneCloser)
                return true;
        }
            
        return false;
    }
}
