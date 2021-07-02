package ourrobots.ags.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * A quick KDLimitedList class backed on an ArrayList. Attempts to add to
 * specific indicies will cause an UnsupportedOperationException to be thrown.
 * Insertions are sorted by finding the insertion point with a binary search.
 * The list is set up to only store up to a certain number of items, the one
 * first in the search order.
 * 
 * @author Alexander Schultz
 */
public class SortedLimitedList<T> extends ArrayList<T> {
    static final long serialVersionUID = 1L;
    private final Comparator<T> comparator;
    private final int sizelimit;
    
    public SortedLimitedList(Comparator<T> c, int l) {
        super(l);
        comparator = c;
        sizelimit = l;
    }
    
    /**
     * Adds to a sorted list and limits the size
     */
    @Override
    public boolean add(T e) {
        
        int inspoint = Collections.binarySearch(this, e, comparator);
        if (inspoint < 0)
            inspoint = -inspoint-1;
        if (inspoint < sizelimit) {
            super.add(inspoint, e);
            if (size() > sizelimit)
                super.remove(sizelimit);
        }
        
        return true;
    }
    
    @Override
    public void add(int index, T element) {
        throw new java.lang.UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T e : c)
            add(e);
        return true;
    }
    
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new java.lang.UnsupportedOperationException();
    }    
}
