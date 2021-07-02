package ourrobots.ags.polylunar.robotdata.log;

/**
 * Stores a 'movie' of ticks
 * 
 * @author Alex Schultz
 */
public class TickMovie {
    private TickRecord first = null;
    private TickRecord last = null;
    
    public void addEntry(TickRecord entry) {
        if (first == null)
            first = entry;
        last = entry;
    }
    
    public TickRecord getFirst() {
        return first;
    }
    
    public TickRecord getLast() {
        return last;
    }
}