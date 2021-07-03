package ourrobots.ags.util.kdtree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander Schultz
 */
final class KDLeafNode<T extends KDEntry> extends AbstractKDNode<T> implements java.io.Serializable {
    static final long serialVersionUID = 1L;
    
    static final int bucketsize = 50;
    
    public KDLeafNode() {
        entries = new ArrayList<T>(bucketsize);
    }
    
    public KDLeafNode(List<T> e) {
        entries = e; 
        
        bounds = new HyperCube(e.get(0).getPosition());
        for (T entry : entries) {
            bounds.extend(entry.getPosition());
        }
    }
    
    @Override
    public KDLeafNode<T> getLeaf(HyperPoint p) {
        return this;
    }

    @Override
    public boolean isLeaf() {
        return true;
    }
    
    /**
     * Adds an entry to this node.
     * @param entry
     * The new entry to add.
     * @return
     * Returns itself if the entry fits in the bucket. Otherwise, returns a KDStemNode to replace this node.
     */
    @Override
    public AbstractKDNode<T> add(T entry) {
        if (entries.size() < bucketsize) {
            return super.add(entry);
        } else {
            ArrayList<T> newentries = new ArrayList<T>(entries);
            newentries.add(entry);
            return new KDStemNode<T>(newentries);
        }
    }

}
