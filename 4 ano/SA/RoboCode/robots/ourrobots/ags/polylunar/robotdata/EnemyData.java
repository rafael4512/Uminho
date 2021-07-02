package ourrobots.ags.polylunar.robotdata;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.polylunar.base.actors.BroadcastActor;
import ourrobots.ags.polylunar.communication.ScanData;
import robocode.Event;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;
import robocode.ScannedRobotEvent;

import java.util.*;

public class EnemyData {
    private final AllyData allies;
    private final Map<String, HostileBot> data = new HashMap<String, HostileBot>();
    private final Rules rules;
    private int enemyCount;
    private HostileBot target;
    
    public EnemyData(Rules rules, AllyData allies) {
        this.rules = rules;
        this.allies = allies;
        enemyCount = rules.ENEMIES;
    }
    
    public HostileBot getData(String name) {
        return data.get(name);
    }
    
    public Collection<HostileBot> getAll() {
        return data.values();
    }
    
    public Collection<HostileBot> getLiving() {
        ArrayList<HostileBot> list = new ArrayList<HostileBot>(data.size());
        for (HostileBot b : data.values()) {
            if (b.isAlive() && b.isFresh()) {
                list.add(b);
            }
        }
        return list;
    }
    
    public boolean hasLivingEnemyLeaders() {
        for (HostileBot b : data.values()) {
            if (b.isLeader && b.isAlive() && b.isFresh()) {
                return true;
            }
        }
        return false;
    }
    
    public List<HostileBot> getSortedLiving() {
        List<HostileBot> l = new ArrayList<HostileBot>(getLiving());
        Collections.sort(l, new Comparator<HostileBot>(){
            public int compare(HostileBot o1, HostileBot o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return l;
    }
    
    public Collection<HostileBot> getNew() {
        ArrayList<HostileBot> list = new ArrayList<HostileBot>(data.size());
        for (HostileBot b : data.values()) {
            if (b.isAlive() && b.getNewLocation() != null) {
                list.add(b);
            }
        }
        return list;
    }
    
    public void updateTick(List<Event> events, BroadcastActor actor) {
        target = null;
        // Add new entries if necessary
        for (Event e : events) {
            if (e instanceof MessageEvent) {
                MessageEvent m = (MessageEvent)e;
                if (m.getMessage() instanceof ScanData) {
                    ScanData s = (ScanData)m.getMessage();
                    if (!rules.isTeammate(s.name) && !data.containsKey(s.name)) {
                        data.put(s.name, new HostileBot(s.name, s.energy));
                    }
                }
            } else if (e instanceof ScannedRobotEvent) {
                ScannedRobotEvent s = (ScannedRobotEvent)e;
                if (!rules.isTeammate(s.getName()) && !data.containsKey(s.getName())) {
                    data.put(s.getName(), new HostileBot(s.getName(), s.getEnergy()));
                }
            } else if (e instanceof RobotDeathEvent) {
                RobotDeathEvent r = (RobotDeathEvent)e;
                if (!rules.isTeammate(r.getName()))
                    enemyCount--;
            }
        }
        
        // Update entries
        for (HostileBot b : data.values()) {
            b.updateData(allies.status, allies, events);
        }
        
        // Broadcast scans
        for (HostileBot b : data.values()) {
            if (b.hasNewData()) {
                actor.broadcastMessage(new ScanData(b));
            }
        }
    }
    
    public int getEnemyCount() {
        return enemyCount;
    }
    
    public void setTraget(HostileBot target) {
        this.target = target;
    }
    
    public HostileBot getTarget() {
        return target;
    }
}
