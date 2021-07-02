package ourrobots.ags.util.kdtree;

import java.util.Comparator;

/**
 * @author Alexander Schultz
 */
class AxisComparator implements Comparator<KDEntry> {
    private final int dimension;
    
    public AxisComparator(int dimension) {
        this.dimension = dimension;
    }

    public int compare(KDEntry o1, KDEntry o2) {
        return Double.compare(o1.getPosition().getValue(dimension), o1.getPosition().getValue(dimension));
    }
}
