package ourrobots.ags.util.kdtree;

import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Schultz
 */
final class KDStemNode<T extends KDEntry> extends AbstractKDNode<T> implements java.io.Serializable {
    static final long serialVersionUID = 1L;
    
    // The two child nodes
    protected AbstractKDNode<T> leftnode;
    protected AbstractKDNode<T> rightnode;
    
    // The dimension and value of the split
    protected double splitvalue;
    protected int splitdim;
    
    protected KDStemNode(List<T> e) {
        entries = e;
        
        // Set up the bounds
        bounds = new HyperCube(e.get(0).getPosition());
        for (T entry : entries) {
            bounds.extend(entry.getPosition());
        }
        
        // Find the widest dimension
        int dimensions = entries.get(0).getPosition().getDimensions();
        double maxwidth=0;
        for  (int i = 0; i < dimensions; i++) {
            double width = bounds.ubound.position[i] - bounds.lbound.position[i];
            if (width > maxwidth) {
                splitdim = i;
                maxwidth = width;
            }
        }
        
        // Set the split value to the median of the widest dimension
        Collections.sort(entries, new AxisComparator(splitdim));
        int middleindex = entries.size() / 2; 
        splitvalue = entries.get(middleindex).getPosition().getValue(splitdim);
        
        // Create children nodes
        leftnode = newNode(entries.subList(0, middleindex));
        rightnode = newNode(entries.subList(middleindex, entries.size()));
    }
    
    private AbstractKDNode<T> getChild(HyperPoint p) {
        if (p.getValue(splitdim) >= splitvalue) {
            return rightnode;
        } else {
            return leftnode;
        }
    }

    @Override
    public KDLeafNode<T> getLeaf(HyperPoint p) {
        return getChild(p).getLeaf(p);
    }

    @Override
    public boolean isLeaf() {
        return false;
    }
    
    /**
     * Adds a new entry to this node. Recurses to the appropriate child
     * @param entry
     * New entry to add
     * @return
     * Returns itself
     */
    @Override
    public AbstractKDNode<T> add(T entry) {
        super.add(entry);
        if (entry.getPosition().getValue(splitdim) >= splitvalue) {
            rightnode = rightnode.add(entry);
        } else {
            leftnode = leftnode.add(entry);
        }
        return this;
    }

}
