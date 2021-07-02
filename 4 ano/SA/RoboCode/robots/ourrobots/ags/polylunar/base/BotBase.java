package ourrobots.ags.polylunar.base;

import ourrobots.ags.polylunar.base.actors.BroadcastActor;
import ourrobots.ags.polylunar.base.actors.GunActor;
import ourrobots.ags.polylunar.base.actors.MovementActor;
import ourrobots.ags.polylunar.base.actors.RadarActor;
import robocode.Event;
import robocode.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Schultz
 */
abstract public class BotBase extends TeamRobot {
    private Rules rules;
    private GunActor gunActor;
    private MovementActor movementActor;
    private RadarActor radarActor;
    private BroadcastActor broadcastActor;
    
    public Rules getRules() {
        return rules;
    }
    
    public GunActor getGunActor() {
        return gunActor;
    }
    
    public MovementActor getMovementActor() {
        return movementActor;
    }
    
    public RadarActor getRadarActor() {
        return radarActor;
    }
    
    public BroadcastActor getBroadcastActor() {
        return broadcastActor;
    }
    
    private final List<Event> events = new ArrayList<Event>();

    private List<Event> getEvents() {
        List<Event> output = new ArrayList<Event>(events);
        events.clear();
        return output;
    }

    public void onCustomEvent(CustomEvent arg0)                 { events.add(arg0); }
    public void onMessageReceived(MessageEvent arg0)            { events.add(arg0); }
    public void onBulletHit(BulletHitEvent arg0)                { events.add(arg0); }
    public void onBulletHitBullet(BulletHitBulletEvent arg0)    { events.add(arg0); }
    public void onBulletMissed(BulletMissedEvent arg0)          { events.add(arg0); }
    public void onDeath(DeathEvent arg0)                        { events.add(arg0); }
    public void onHitByBullet(HitByBulletEvent arg0)            { events.add(arg0); }
    public void onHitRobot(HitRobotEvent arg0)                  { events.add(arg0); }
    public void onHitWall(HitWallEvent arg0)                    { events.add(arg0); }
    public void onRobotDeath(RobotDeathEvent arg0)              { events.add(arg0); }
    public void onScannedRobot(ScannedRobotEvent arg0)          { events.add(arg0); }
    public void onStatus(StatusEvent arg0)                      { events.add(arg0); }
    public void onWin(WinEvent arg0)                            { events.add(arg0); }
    public void onSkippedTurn(SkippedTurnEvent arg0)            {
        events.add(arg0);
        System.out.println("Warning! Turn skipped!");
    }
    // Echo broadcast messages
    @Override
    public void broadcastMessage(java.io.Serializable s) throws java.io.IOException {
        super.broadcastMessage(s);
        events.add(new MessageEvent(rules.NAME, s));
    }
    
    public void setColors(final Color bodyColor, final Color scanColor, final Color gunColor, final Color radarColor) {
        if (!running)
            throw new UnsupportedOperationException("Bot must be running before colors may be set.");
        
        this.setBodyColor(bodyColor);
        this.setScanColor(scanColor);
        this.setGunColor(gunColor);
        this.setRadarColor(radarColor);
    }
    
    abstract public void init();
    abstract public void runTick(List<Event> events);
    abstract public void _onPaint(Graphics2D g);
    
    private final void preInit() {
        // Set turning to be independent
        this.setAdjustGunForRobotTurn(true);
        this.setAdjustRadarForRobotTurn(true);
        this.setAdjustRadarForGunTurn(true);
        
        // Set up rules class
        rules = new Rules(this);
        
        // Set up actors
        gunActor = new GunActor(this);
        movementActor = new MovementActor(this);
        radarActor = new RadarActor(this);
        broadcastActor = new BroadcastActor(this);
    }
    
    private void ensureVersion() {
        if (getTime() < 2) {
            boolean gotstatus = false;
            for (Event event : events) {
                if (event instanceof robocode.StatusEvent)
                    gotstatus = true;
                if (event instanceof robocode.DeathEvent)
                    gotstatus = true;
                if (event instanceof robocode.WinEvent)
                    gotstatus = true;
            }
            // If we didn't get a StatusEvent something is wrong... (i.e. old robocode version)
            if (!gotstatus) {
                System.out.println("Got no status event! Dying now! :(");
                while (true);
            }
        }
    }
    
    private boolean running = false;
    @Override
    public void run() {
        if (running)
            throw new UnsupportedOperationException("Main loop already running!");

        running = true;

        preInit();

        init();

        while(true) {
            if (events.size() != 0) {
                ensureVersion();
                runTick(getEvents());
            }
            execute();
        }
    }
    
    @Override
    public void onPaint(Graphics2D g) {
        if (events.size() != 0) {
            ensureVersion();
            runTick(getEvents());
        }
        _onPaint(g);
    }
}
