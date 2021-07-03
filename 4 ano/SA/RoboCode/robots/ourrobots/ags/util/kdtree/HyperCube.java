package ourrobots.ags.util.kdtree;

import java.util.List;

/**
 * Represents a hypercube shaped region
 * 
 * @author Alexander Schultz
 */
public class HyperCube implements java.io.Serializable {
    static final long serialVersionUID = 1L;
    public final HyperPoint lbound, ubound;
    private final int dimensions;
    
    /**
     * Begin a HyperCube containing a list of HyperPoints
     */
    public HyperCube(List<HyperPoint> l) {
        this(l.get(0));
        extend(l);
    }
    
    /**
     * Begin a 0 sized HyperCube at this HyperPoint
     */
    public HyperCube(HyperPoint p) {
        dimensions = p.getDimensions();
        lbound = new HyperPoint(p);
        ubound = new HyperPoint(p);
    }
    
    public int getDimensions() {
        return dimensions;
    }
    
    /**
     * Checks if a given HyperPoint is contained within the HyperCube
     */
    public boolean contains(HyperPoint p) {
        if (p.dimensions != dimensions)
            throw new java.lang.IndexOutOfBoundsException();
        
        for (int i=0; i<dimensions; i++) {
            if ((p.position[i] > ubound.position[i]) || (p.position[i] < lbound.position[i]))
                return false;
        }
        return true;
    }
    
    /**
     * Extend the HyperCube to contain the given HyperPoint
     */
    public void extend(HyperPoint p) {
        if (p.dimensions != dimensions)
            throw new java.lang.IndexOutOfBoundsException();
        
        for (int i=0; i<dimensions; i++) {
            if (p.position[i] > ubound.position[i])
                ubound.position[i] = p.position[i];
            if (p.position[i] < lbound.position[i])
                lbound.position[i] = p.position[i];
        }
    }
    
    /**
     * Extend the HyperCube to contain a list of HyperPoints
     */
    public void extend(List<HyperPoint> l) {
        for (HyperPoint p : l) {
            extend(p);
        }
    }
    
    /**
     * Finds the smallest squared elcuid distance between a HyperPoint and this HyperCube.
     * This is a shortcut to finding the closest possible point in the cube to the other point.
     */
    public double sqrDistanceToPoint(HyperPoint p, double[] weights) {
        if (p.dimensions != dimensions || dimensions != weights.length)
            throw new java.lang.IndexOutOfBoundsException();
        
        double value = 0;
        for (int i = 0; i < dimensions; i++) {
            if (p.position[i] > ubound.position[i]) {
                final double diff = (ubound.position[i] - p.position[i])*weights[i];
                value += diff*diff;
            } else if (p.position[i] < lbound.position[i]) {
                final double diff = (lbound.position[i] = p.position[i])*weights[i];
                value += diff*diff;
            }
        }
        return value;
    }
    
    public double sqrDistanceToPoint(HyperPoint p) {
        double[] weights = new double[dimensions];
        for (int i=0; i<dimensions; i++)
            weights[i] = 1;
        return sqrDistanceToPoint(p, weights);
    }
    
    /**
     * Finds the smallest elucid distance between a HyperPoint and this HyperCube
     */
    public double elucidDistanceToPoint(HyperPoint p) {
        return Math.sqrt(sqrDistanceToPoint(p));
    }
}
