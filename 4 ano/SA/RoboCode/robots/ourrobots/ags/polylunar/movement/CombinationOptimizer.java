package ourrobots.ags.polylunar.movement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinationOptimizer {
    public static Set<TagTeamPossibility> optimizePossibilities(List<TagTeamPossibility> list, boolean needLeader) {
        Set<TagTeamPossibility> configuration = new HashSet<TagTeamPossibility>();
        optimizeStep(configuration, list, needLeader, 0, Double.POSITIVE_INFINITY);
        return configuration;
    }
    
    public static double optimizeStep(Set<TagTeamPossibility> currentSet, List<TagTeamPossibility> list, boolean needLeader, double dist, double limit) {
        if (list.size() == 0) {
            // If we need to be targeting a leader, throw out possibilities that don't target the leader
            if (needLeader) {
                //for (TagTeamPossibility ttp : currentSet) {
                //    if (ttp.target.isLeader)
                //        return Math.max(0, dist-75);
                //}
                return dist;
            } else {
                return dist;
            }
        }
        
        Set<TagTeamPossibility> bestSet = null;
        double bestDist = limit;
        for (TagTeamPossibility a : list) {
            Set<TagTeamPossibility> newSet = new HashSet<TagTeamPossibility>(currentSet);
            List<TagTeamPossibility> newList = new ArrayList<TagTeamPossibility>(list);
            newList.remove(a);
            for (TagTeamPossibility b : list) {
                if (b != a && a.conflictsWith(b)) {
                    newList.remove(b);
                }
            }
            newSet.add(a);
            double newDist = optimizeStep(newSet, newList, needLeader, dist+a.dist, bestDist);
            
            if (newDist < bestDist) {
                bestDist = newDist;
                bestSet = newSet;
            }
        }
        
        if (bestSet != null) {
            currentSet.clear();
            currentSet.addAll(bestSet);
            return bestDist;
        } else {
            return bestDist;
        }
    }
}