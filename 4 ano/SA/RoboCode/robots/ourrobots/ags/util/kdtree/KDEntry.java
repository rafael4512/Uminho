package ourrobots.ags.util.kdtree;

/**
 * Interface for entries in the KDTree.
 * Must have a HyperPoint position and must be serializable 
 * 
 * @author Alexander Schultz
 */
public abstract class KDEntry {
    abstract public HyperPoint getPosition();
    public double distCache;
}
