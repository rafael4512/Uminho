package ourrobots.ags.util.kdtree;

import java.util.ArrayList;
import java.util.List;

/**
 * KDTree node
 * 
 * @author Alexander Schultz
 */
abstract class AbstractKDNode<T extends KDEntry> implements java.io.Serializable {
    abstract public boolean isLeaf();
    abstract public KDLeafNode<T> getLeaf(HyperPoint p);
    
    protected List<T> entries;
    protected HyperCube bounds;
    
    /**
     * Adds an entry to this node.
     * This should be overridden by implementing classes
     * @param entry
     * The new entry to add.
     * @return
     * Returns itself. Implementing classes may return new objects to replace themself.
     */
    public AbstractKDNode<T> add(T entry) {
        if (bounds != null)
            bounds.extend(entry.getPosition());
        else
            bounds = new HyperCube(entry.getPosition());
        entries.add(entry);
        return this;
    }
    /**
     * @return
     * Returns all entries in this node.
     */
    public List<T> getAllContents() {
        return entries;
    }
    
    /**
     * Create a new node of a type suitable for storing list e
     * @return
     * The new node
     */
    public AbstractKDNode<T> newNode(List<T> e) {
        if (e.size() <= KDLeafNode.bucketsize)
            return new KDLeafNode<T>(new ArrayList<T>(e));
        else
            return new KDStemNode<T>(new ArrayList<T>(e));
    }
    
    /**
     * Regenerate the tree from this node down
     * @return
     * The new tree
     */
    public AbstractKDNode<T> regenerate() {
        return newNode(entries);
    }
    
    /**
     * Get the bounds of a node
     */
    public HyperCube getBounds() {
        return bounds;
    }
}
