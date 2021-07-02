package ourrobots.ags.util.kdtree;

import java.util.Comparator;

/**
 * @author Alexander Schultz
 */
class DistanceComparator implements Comparator<KDEntry> {
    private final HyperPoint origin;
    private final double[] weights;
    
    public DistanceComparator(HyperPoint o, double[] w) {
        origin = o;
        weights = w;
    }
    
    public DistanceComparator(HyperPoint o) {
        origin = o;
        weights = new double[o.getDimensions()];
        for (int i=0; i<o.getDimensions(); i++)
            weights[i] = 1;
    }
    
    public int compare(KDEntry o1, KDEntry o2) {
        double dist1 = o1.getPosition().sqrDist(origin, weights);
        double dist2 = o2.getPosition().sqrDist(origin, weights);
        return Double.compare(dist1, dist2);
    }

}
