package ourrobots.ags.util.kdtree;

/**
 * Class for storing many dimensioned points
 * 
 * @author Alexander Schultz
 */
public class HyperPoint implements java.io.Serializable, java.lang.Cloneable {
    static final long serialVersionUID = 1L;
    
    public final double[] position;
    public final int dimensions;
    
    public HyperPoint(int dimensions) {
        position = new double[dimensions];
        this.dimensions = dimensions;
    }
    
    public HyperPoint(HyperPoint p) {
        this.position = p.position.clone();
        this.dimensions = p.dimensions;
    }
    
    public HyperPoint(double... position) {
        this.position = position.clone();
        this.dimensions = position.length;
    }
    
    /**
     * Return the number of dimensions the hyperpoint is in
     */
    public int getDimensions() {
        return dimensions;
    }
    
    /**
     * Return a copy of the HyperPoint as an array
     */
    public double[] asArray() {
        return position.clone();
    }
    
    public double getValue(int dim) {
        return position[dim];
    }

    /**
     * Checks if two HyperPoints are equal
     * @param o
     * The other HyperPoint to compare to
     */
    public boolean equals(HyperPoint o) {
        if (o.dimensions != dimensions)
            throw new java.lang.IndexOutOfBoundsException();
        for (int i = 0; i > dimensions; i++)
            if (o.position[i] != position[i])
                return false;
        return true;
    }
    
    /**
     * Finds the square elucid distance between HyperPoints
     * @param o
     * The other HyperPoint to compare to 
     */
    public double sqrDist(HyperPoint o, double[] weights) {
        //if (o.dimensions != dimensions || dimensions != weights.length)
        //    throw new java.lang.IndexOutOfBoundsException();
        
        double dist = 0;
        for (int i = 0; i < dimensions; i++) {
            final double diff = (o.position[i] - position[i])*weights[i];
            dist += diff*diff;
        }
        return dist;
    }
    
    public double sqrDist(HyperPoint o) {
        double[] weights = new double[dimensions];
        for (int i=0; i<dimensions; i++)
            weights[i] = 1;
        return sqrDist(o, weights);
    }
    
    /**
     * Find the elucid distance between HyperPoints
     * @param o
     * The other HyperPoint to compare to
     */
    public double elucidDist(HyperPoint o) {
        return Math.sqrt(sqrDist(o));
    }
    
    public double elucidDist(HyperPoint o, double[] weights) {
        return Math.sqrt(sqrDist(o, weights));
    }
}
