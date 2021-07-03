package ourrobots.ags.polylunar.core;

import ourrobots.ags.polylunar.base.BotBase;
import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.communication.BeconData;
import ourrobots.ags.polylunar.movement.TeamMovement;
import ourrobots.ags.polylunar.radar.TeamRadar;
import ourrobots.ags.polylunar.robotdata.AllyData;
import ourrobots.ags.polylunar.robotdata.EnemyData;
import ourrobots.ags.polylunar.weapons.GunTargeter;
import robocode.Event;

import java.awt.*;
import java.util.List;

public abstract class Moon extends BotBase {
    private Rules rules;
    private AllyData allies;
    private EnemyData enemies;
    private TeamRadar radar;
    private TeamMovement movement;
    private GunTargeter gun;
    
    @Override
    public void _onPaint(Graphics2D g) {
    }

    @Override
    public void init() {
        rules = getRules();
        allies = new AllyData();
        enemies = new EnemyData(rules, allies);
        radar = new TeamRadar(rules, allies, enemies);
        movement = new TeamMovement(rules, allies, enemies);
        gun = new GunTargeter(rules, allies, enemies);
        this.setColors(Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
    }

    @Override
    public void runTick(List<Event> events) {
        updateStatus(events);
        updateScans(events);
        radar.run(getRadarActor());
        movement.run(getMovementActor());
        gun.run(getGunActor());
    }
    
    @Override
    public void broadcastMessage(java.io.Serializable s) {
        try {
            super.broadcastMessage(s);
        } catch (java.io.IOException e) {
            System.out.println("Error during sending team message!");
            e.printStackTrace();
        }
    }

    private void updateStatus(List<Event> events) {
        // Check becons
        allies.updateTick(events);
        
        // Broadcast team becon
        broadcastMessage(new BeconData(allies.status));
    }
    
    private void updateScans(List<Event> events) {
        // Update enemy tracking and broadcast
        enemies.updateTick(events, getBroadcastActor());
    }
}
