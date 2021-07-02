package ourrobots.ags.polylunar.movement;

//import static robocode.util.Utils.normalAbsoluteAngle;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.base.actors.MovementActor;
import ourrobots.ags.polylunar.prediction.PhysicsEngine;
import ourrobots.ags.polylunar.prediction.RobotSim;
import ourrobots.ags.polylunar.robotdata.AllyData;
import ourrobots.ags.polylunar.robotdata.EnemyData;
import ourrobots.ags.polylunar.robotdata.FriendlyBot;
import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static robocode.util.Utils.normalRelativeAngle;

public class TeamMovement {
    private final Rules rules;
    private final AllyData allies;
    private final EnemyData enemies;
    private final AntigravEngine antigrav;
    
    public TeamMovement(Rules rules, AllyData allies, EnemyData enemies) {
        this.rules = rules;
        this.allies = allies;
        this.enemies = enemies;
        antigrav = new AntigravEngine(rules, allies, enemies);
    }
    
    public void run(MovementActor actor) {
        Set<TagTeamPossibility> configuration = getConfiguration();
        
        for(TagTeamPossibility ttp : configuration) {
            ttp.target.setChasers(ttp.team[0], ttp.team[1]);
        }
        
        // If we were assigned a task, do it!
        for (TagTeamPossibility ttp : configuration) {
            for (int x=0; x<2; x++) {
                if (!ttp.team[x].getName().equals(rules.NAME))
                    continue;
                AbsolutePoint goal = ttp.goals[x];
                
                // Fix the goal point up a bit...
                /*AbsolutePoint location = ttp.target.getLocation();
                RelativePoint rgoal = RelativePoint.fromPP(location, goal);
                RelativePoint rloc = RelativePoint.fromPP(location, allies.status.getLocation());
                rgoal.direction = fixDirection(rgoal, rloc);
                goal = location.addRelativePoint(rgoal);*/
                
                goToPoint(goal, actor);
                enemies.setTraget(ttp.target);
                
                return;
            }
        }
        
        // Well.. we weren't chosen to chase anything. Let's sit still
        // TODO: DODGE!
        RelativePoint grav = antigrav.runGravity(allies.status);
        goToDirection(grav, actor);
    }
    
    /*private static double fixDirection(RelativePoint g, RelativePoint o) {
        double rel = normalRelativeAngle(o.direction - g.direction);
        if (rel > Math.PI/2) rel = Math.PI/2;
        if (rel < -Math.PI/2) rel = -Math.PI/2;
        return normalAbsoluteAngle(o.direction + rel);
    }*/
    
    public void goToPoint(AbsolutePoint goal, MovementActor actor) {
        RelativePoint rel = RelativePoint.fromPP(goal, allies.status.getLocation());
        double turn = rel.getDirDist(allies.status.status.getHeadingRadians());
        
        double ahead;
        if (Math.abs(turn) > Math.PI/2) {
            turn = normalRelativeAngle(turn+Math.PI);
            ahead = rel.getMagnitude();
        } else {
            ahead = -rel.getMagnitude();
        }
        
        double maxV = Math.max(0, Math.min(8, (10 - Math.toDegrees(turn/10))/0.75)); 
        double velocity = Math.max(Math.min(maxV, ahead), -maxV);
        
        allies.status.setIntention(rules, velocity, turn);
        actor.setMove(velocity, ahead);
        actor.setTurnBody(turn);
    }
    
    public void goToDirection(RelativePoint goal, MovementActor actor) {
        double turn = goal.getDirDist(allies.status.status.getHeadingRadians());
        
        double velocity;
        if (Math.abs(turn) > Math.PI/2) {
            turn = normalRelativeAngle(turn+Math.PI);
            velocity = 8;
        } else {
            velocity = -8;
        }
        
        double maxV = Math.max(0, Math.min(8, (10 - Math.toDegrees(turn/10))/0.75)); 
        velocity = Math.max(Math.min(maxV, velocity), -maxV);
        
        allies.status.setIntention(rules, velocity, turn);
        actor.setMove(velocity);
        actor.setTurnBody(turn);
    }
    
    // Figure out who goes for which enemies
    public Set<TagTeamPossibility> getConfiguration() {
        List<TagTeamPossibility> list = new ArrayList<TagTeamPossibility>();
        List<FriendlyBot> friendlyBots = allies.getSortedLiving();
        List<HostileBot> hostileBots = enemies.getSortedLiving();
        
        // Predict current locations
        for (HostileBot enemy : hostileBots) {
            RobotSim sim = new RobotSim();
            sim.location = enemy.getLocation();
            sim.velocity = enemy.getVelocity();
            PhysicsEngine.simulateTick(rules, sim, sim.velocity.magnitude, 0);
            PhysicsEngine.simulateTick(rules, sim, sim.velocity.magnitude, 0);
            PhysicsEngine.simulateTick(rules, sim, sim.velocity.magnitude, 0);
            enemy.lPredict = sim.location;
        }
        
        // Assemble possible combinations
        for (int x = 0; x<friendlyBots.size(); x++) {
            FriendlyBot b1 = friendlyBots.get(x);
            for (int y = x+1; y<friendlyBots.size(); y++) {
                FriendlyBot b2 = friendlyBots.get(y);
                for (HostileBot enemy : hostileBots) {
                    TagTeamPossibility ttp = new TagTeamPossibility(rules, b1, b2, enemy);
                    list.add(ttp);
                }
            }
        }
        
        return CombinationOptimizer.optimizePossibilities(list, enemies.hasLivingEnemyLeaders());
    }
}
