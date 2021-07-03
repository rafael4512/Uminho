package ourrobots.ags.polylunar.robotdata;

import ourrobots.ags.polylunar.communication.BeconData;
import robocode.Event;
import robocode.MessageEvent;
import robocode.StatusEvent;

import java.util.*;

public class AllyData {
    private final Map<String, FriendlyBot> data = new HashMap<String, FriendlyBot>();
    public final SelfStatus status = new SelfStatus();
    
    public FriendlyBot getData(String name) {
        return data.get(name);
    }
    
    public Collection<FriendlyBot> getAll() {
        return data.values();
    }
    
    public Collection<FriendlyBot> getLiving() {
        ArrayList<FriendlyBot> list = new ArrayList<FriendlyBot>(data.size());
        for (FriendlyBot b : data.values()) {
            if (b.isAlive() && b.isFresh()) {
                list.add(b);
            }
        }
        return list;
    }
    
    public List<FriendlyBot> getSortedLiving() {
        List<FriendlyBot> l = new ArrayList<FriendlyBot>(getLiving());
        Collections.sort(l, new Comparator<FriendlyBot>(){
            public int compare(FriendlyBot o1, FriendlyBot o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return l;
    }
    
    public void updateTick(List<Event> events) {
        // Add new entries if necessary
        for (Event e : events) {
            if (e instanceof MessageEvent) {
                MessageEvent m = (MessageEvent)e;
                if (m.getMessage() instanceof BeconData) {
                    if (!data.containsKey(m.getSender())) {
                        data.put(m.getSender(), new FriendlyBot(m.getSender()));
                    }
                }
            } else if (e instanceof StatusEvent) {
                StatusEvent s = (StatusEvent)e;
                status.update(s.getStatus());
            }
        }
        
        // Update entries
        for (FriendlyBot b : data.values()) {
            b.updateData(events);
        }
    }
}
