package ourrobots.ags.util.kdtree;

import ourrobots.ags.util.SortedLimitedList;

import java.util.List;

/**
 * My Bucket KDTree implementation
 * 
 * @author Alexander Schultz
 */
public class KDTree<T extends KDEntry> implements java.io.Serializable {
    static final long serialVersionUID = 1L;

    public final int dimensions;
    public AbstractKDNode<T> root;
    
    /**
     * Constructor for a tree with a given number of dimensions
     */
    public KDTree(int dimensions) {
        this.dimensions = dimensions;
        root = new KDLeafNode<T>();
    }
    
    /**
     * Add an entry to the tree
     */
    public void add(T entry) {
        HyperPoint point = entry.getPosition();
        if (point.getDimensions() != dimensions)
            throw new java.lang.IndexOutOfBoundsException();
        root = root.add(entry);
    }
    
    /**
     * Regenerate the entire tree
     */
    public void rebalance() {
        System.out.println(root.entries.size());
        root = root.regenerate();
        System.out.println(root.entries.size());
    }
    
    
    /**
     * Find nearest-k-neighbors using a plain search-everything algorithm
     */
    @SuppressWarnings("unchecked")
    public List<T> OldNearestNeighbors(HyperPoint p, int n) {
        DistanceComparator comparator = new DistanceComparator(p);
        SortedLimitedList<T> neighbors = new SortedLimitedList(comparator, n);
        neighbors.addAll(root.getAllContents());
        return neighbors.subList(0, Math.min(n, neighbors.size()));
    }
    
    /**
     * Finds the nearest n neighbours to a HyperPoint
     * @param p
     * The HyperPoint to search around
     * @param n
     * The number of near points to find
     */
    @SuppressWarnings("unchecked")
    public List<T> NearestNeighbors(HyperPoint p, int n, double[] weights) {
        // Initialize a comparator for distance to this point
        CachedDistanceComparator comparator = new CachedDistanceComparator();
        
        // Initialize a sorted list using this comparator
        SortedLimitedList<T> neighbors = new SortedLimitedList(comparator, n);
        
        if (root.entries.size() > 0)
            NearestNeighbors(p, n, neighbors, root, weights);
        
        return neighbors;
    }
    
    public List<T> NearestNeighbors(HyperPoint p, int n) {
        double[] weights = new double[dimensions];
        for (int i=0; i<dimensions; i++)
            weights[i] = 1;
        return NearestNeighbors(p, n, weights);
    }
    
    /**
     * Iterates the Nearest-k-Neighbors algorithm within a given node
     */
    private void NearestNeighbors(HyperPoint p, int n, SortedLimitedList<T> neighbors, AbstractKDNode<T> node, double[] weights) {
        // Set the maximum search distance
        double maxdist;
        if (neighbors.size() >= n) {
            maxdist = neighbors.get(n-1).distCache;
        } else {
            maxdist = Double.POSITIVE_INFINITY;
        }
        
        // Check if this node is within the search range, abort if not.
        if (maxdist < node.getBounds().sqrDistanceToPoint(p, weights))
            return;
        
        // Check if this node is a leaf or a stem
        if (node.isLeaf()) {
            // Well this is a leaf node. Dump the whole bucket into the neighbors list and the close ones will "float to the top"
            for (T e : node.getAllContents()) {
                e.distCache = e.getPosition().sqrDist(p, weights);
                if (e.distCache >= maxdist)
                    continue;
                
                neighbors.add(e);
                if (neighbors.size() >= n)
                    maxdist = neighbors.get(n-1).distCache;
            }
            //neighbors.addAll(node.getAllContents());
        } else {
            // Well this is a stem node. Let's recurse to the children
            KDStemNode<T> stemnode = (KDStemNode<T>)node;
            
            if (p.getValue(stemnode.splitdim) >= stemnode.splitvalue) {
                NearestNeighbors(p, n, neighbors, stemnode.rightnode, weights);
                NearestNeighbors(p, n, neighbors, stemnode.leftnode, weights);
            } else {
                NearestNeighbors(p, n, neighbors, stemnode.leftnode, weights);
                NearestNeighbors(p, n, neighbors, stemnode.rightnode, weights);
            }
        }
    }
}
