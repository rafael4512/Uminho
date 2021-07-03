package ourrobots.ags.polylunar.radar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombinationOptimizer {
    public static Set<AimPossibility> optimizePossibilities(List<AimPossibility> list) {
        Set<AimPossibility> configuration = new HashSet<AimPossibility>();
        optimizeStep(configuration, list, 0, Double.POSITIVE_INFINITY);
        return configuration;
    }
    
    public static double optimizeStep(Set<AimPossibility> currentSet, List<AimPossibility> list, double dist, double limit) {
        if (list.size() == 0)
            return dist;
        
        Set<AimPossibility> bestSet = null;
        double bestDist = limit;
        for (AimPossibility a : list) {
            Set<AimPossibility> newSet = new HashSet<AimPossibility>(currentSet);
            List<AimPossibility> newList = new ArrayList<AimPossibility>(list);
            newList.remove(a);
            for (AimPossibility b : list) {
                if (b != a && a.conflictsWith(b)) {
                    newList.remove(b);
                }
            }
            newSet.add(a);
            double newDist = optimizeStep(newSet, newList, dist+a.dist, bestDist);
            
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