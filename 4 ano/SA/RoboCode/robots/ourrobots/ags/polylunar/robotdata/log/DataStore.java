package ourrobots.ags.polylunar.robotdata.log;

import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.kdtree.KDTree;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    public static final KDTree<TickReference> tree = new KDTree<TickReference>(LogSituation.dimensions);
    
    private static final Map<String, TickLog> logs = new HashMap<String, TickLog>();
    
    public static void startRound() {
        for (TickLog log : logs.values()) {
            log.movieEnded();
        }
    }
    
    public static void lostTrack(HostileBot enemy) {
        TickLog log = logs.get(enemy.getName());
        if (log != null)
            log.movieEnded();
    }
    
    public static void addLog(HostileBot enemy) {
        TickLog log = logs.get(enemy.getName());
        if (log == null) {
            log = new TickLog();
            logs.put(enemy.getName(), log);
        }
        
        TickRecord entry = new TickRecord(log.getLastRecord(), enemy.getVelocity());
        log.recordTick(entry);
        
        for (LogSituation s : enemy.getSituations().values()) {
            tree.add(new TickReference(entry, s));
        }
    }
}
