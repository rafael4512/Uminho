package ourrobots.ags.polylunar.robotdata;

import ourrobots.ags.polylunar.communication.ScanData;
import ourrobots.ags.polylunar.robotdata.log.DataStore;
import ourrobots.ags.polylunar.robotdata.log.LogSituation;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;
import robocode.Event;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.util.*;

import static robocode.util.Utils.normalAbsoluteAngle;

public class HostileBot {
    private final String name;
    private AbsolutePoint location;
    private RelativePoint velocity;
    private double energy;
    private boolean alive;
    private boolean fresh;
    private Set<String> seenBy = new HashSet<String>();
    private Set<String> chasedBy = new HashSet<String>();
    private boolean newData;
    private AbsolutePoint newLocation;
    private RelativePoint newVelocity;
    private double newEnergy;
    private Map<String, LogSituation> situations = new HashMap<String, LogSituation>();
    private Map<String, LogSituation> newSituations = new HashMap<String, LogSituation>();
    private double newAngularVelocity;
    private double avgAngularVelocity;
    public final boolean isLeader;
    public final boolean isDroid;
    public AbsolutePoint lPredict;
    
    public HostileBot(String name, double startingEnergy) {
        this.name = name;
        alive = true;
        fresh = false;
        isLeader = (startingEnergy >= 200.0);
        isDroid = (startingEnergy == 120.0 || startingEnergy == 220.0);
        DataStore.lostTrack(this);
    }
    
    public void updateData(SelfStatus status, AllyData allies, List<Event> events) {
        // Reset freshness
        fresh = false;
        
        // Reset new stuff
        boolean hadNewData = newData;
        newData = false;
        newLocation = null;
        newVelocity = null;
        newEnergy = 0;
        newSituations = new HashMap<String, LogSituation>();
        
        // Clear seenBy and chasedBy
        seenBy.clear();
        chasedBy.clear();
        
        if (!alive)
            return;
        for (Event e : events) {
            if (e instanceof RobotDeathEvent) {
                if (((RobotDeathEvent)e).getName().equals(name)) {
                    alive = false;
                    return;
                }
            } else if (e instanceof MessageEvent) {
                MessageEvent m = (MessageEvent)e;
                if (m.getMessage() instanceof ScanData) {
                    ScanData scan = (ScanData)m.getMessage();
                    if (scan.name.equals(name)) {
                        fresh = true;
                        location = scan.location;
                        velocity = scan.velocity;
                        energy = scan.energy;
                        situations = scan.situations;
                        seenBy.add(m.getSender());
                        if (!hadNewData) {
                            avgAngularVelocity = (9*avgAngularVelocity + scan.angularVelocity) / 10;
                        }
                    }
                }
            }
        }
        for (Event e : events) {
            if (e instanceof ScannedRobotEvent) {
                ScannedRobotEvent s = (ScannedRobotEvent)e;
                if (s.getName().equals(name)) {
                    RelativePoint rel = RelativePoint.fromDM(status.status.getHeadingRadians()+s.getBearingRadians(), s.getDistance());
                    AbsolutePoint sta = status.getLocation();
                    newLocation = sta.addRelativePoint(rel);
                    if (fresh)
                        newAngularVelocity = -velocity.getDirDist(s.getHeadingRadians());
                    else
                        newAngularVelocity = 0;
                    avgAngularVelocity = (9*avgAngularVelocity + newAngularVelocity) / 10;
                    newVelocity = RelativePoint.fromDM(normalAbsoluteAngle(s.getHeadingRadians()), s.getVelocity());
                    newEnergy = s.getEnergy();
                    newData = true;
                }
            }
        }
        
        if (newData) {
            for (FriendlyBot b : allies.getLiving()) {
                newSituations.put(b.getName(), new LogSituation(this, b));
            }
        }
        
        if (fresh) {
            //DataStore.addLog(this);
        } else {
            DataStore.lostTrack(this);
        }
    }
    
    public String getName() {
        return name;
    }
    
    public AbsolutePoint getLocation() {
        return location;
    }
    
    public RelativePoint getVelocity() {
        return velocity;
    }
    
    public double getEnergy() {
        return energy;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    public boolean isFresh() {
        return fresh;
    }
    
    public boolean seenBy(String name) {
        return seenBy.contains(name);
    }
    
    public Set<String> seenBy() {
        return seenBy;
    }
    
    public Set<String> chasedBy() {
        return chasedBy;
    }
    
    public void setChasers(FriendlyBot b1, FriendlyBot b2) {
        chasedBy.add(b1.getName());
        chasedBy.add(b2.getName());
    }
    
    public AbsolutePoint getNewLocation() {
        return newLocation;
    }
    
    public RelativePoint getNewVelocity() {
        return newVelocity;
    }
    
    public double getNewEnergy() {
        return newEnergy;
    }
    
    public boolean hasNewData() {
        return newData;
    }
    
    public Map<String, LogSituation> getSituations() {
        return situations;
    }
    
    public Map<String, LogSituation> getNewSituations() {
        return newSituations;
    }
    
    public double getNewAngularVelocity() {
        return newAngularVelocity;
    }
    
    public double getAvgAngularVelocity() {
        return avgAngularVelocity;
    }
}
