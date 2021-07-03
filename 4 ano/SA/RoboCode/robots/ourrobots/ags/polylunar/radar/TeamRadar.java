package ourrobots.ags.polylunar.radar;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.base.actors.RadarActor;
import ourrobots.ags.polylunar.robotdata.AllyData;
import ourrobots.ags.polylunar.robotdata.EnemyData;
import ourrobots.ags.polylunar.robotdata.FriendlyBot;
import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import java.util.*;

import static robocode.util.Utils.normalRelativeAngle;

public class TeamRadar {
    private final Rules rules;
    private final AllyData allies;
    private final EnemyData enemies;
    
    public TeamRadar(Rules rules, AllyData allies, EnemyData enemies) {
        this.rules = rules;
        this.allies = allies;
        this.enemies = enemies;
    }
    
    public void run(RadarActor actor) {
        List<FriendlyBot> avaliableAllies = new ArrayList<FriendlyBot>(allies.getLiving());
        List<HostileBot> enemyList = new ArrayList<HostileBot>(enemies.getLiving());
        
        Collections.sort(enemyList, new Comparator<HostileBot>(){
            public int compare(HostileBot o1, HostileBot o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        
        // Find the best configuration of who-scans-who
        List<AimPossibility> list = new ArrayList<AimPossibility>(enemyList.size()*avaliableAllies.size());
        for (HostileBot enemy : enemyList) {
            for (FriendlyBot ally : avaliableAllies) {
                if (enemy.seenBy(ally.getName()))
                    list.add(new AimPossibility(ally, enemy));
            }
        }
        Set<AimPossibility> configuration = CombinationOptimizer.optimizePossibilities(list);
        
        // If we were selected to scan a specific target, aim for it
        for (AimPossibility a : configuration) {
            if (a.source.getName().equals(rules.NAME)) {
                rotateTo(actor, a.target);
                return;
            }
        }
        
        // Well the group doesn't have a scanning task to assign, so we'll do our own thing.
        // Do we see anything new the group isn't scanning?
        for (HostileBot enemy : enemies.getNew()) {
            boolean avaliable = true;
            for (AimPossibility a : configuration) {
                if (a.target.getName().equals(enemy.getName())) {
                    avaliable = false;
                    break;
                }
            }
            if (avaliable) {
                rotateTo(actor, enemy);
                return;
            }
        }
        // If the group is already scanning everthing out there, then let's
        // scan something we may be likely to lose track of soon.
        if (enemies.getEnemyCount() == configuration.size()) {
            double lowestE = Double.POSITIVE_INFINITY;
            HostileBot target = null;
            for (AimPossibility a : configuration) {
                double e = a.source.getEnergy();
                if (e < lowestE) {
                    lowestE = e;
                    target = a.target;
                }
            }
            if (target != null) {
                rotateTo(actor, target);
                return;
            }
        }
        // Well, we weren't chosen to follow a bot, so just spin...
        actor.setTurnRadar(Double.POSITIVE_INFINITY);
    }
    
    private void rotateTo(RadarActor actor, HostileBot target) {
        double currentTurn = allies.status.status.getRadarHeadingRadians();
        AbsolutePoint source = allies.status.getLocation();
        final AbsolutePoint targetPoint;
        if (target.getNewLocation() != null)
            targetPoint = target.getNewLocation();
        else if (target.getLocation() != null)
            targetPoint = target.getLocation();
        else
            return;
        RelativePoint relativeLocation = RelativePoint.fromPP(source, targetPoint);
        double turn = normalRelativeAngle(relativeLocation.direction - currentTurn);
        if (turn >= 0)
            turn += Math.PI/8;
        else
            turn -= Math.PI/8;
        actor.setTurnRadar(turn);
    }
}
