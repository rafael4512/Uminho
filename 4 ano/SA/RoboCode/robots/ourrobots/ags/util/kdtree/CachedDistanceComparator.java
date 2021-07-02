package ourrobots.ags.util.kdtree;

import java.util.Comparator;

/**
 * @author Alexander Schultz
 */
class CachedDistanceComparator implements Comparator<KDEntry> {
    public CachedDistanceComparator() {}
    
    public int compare(KDEntry o1, KDEntry o2) {
        return Double.compare(o1.distCache, o2.distCache);
    }

}
