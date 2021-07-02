package ourrobots.ags.polylunar.robotdata.log;

import ourrobots.ags.util.points.RelativePoint;

/**
 * Represents a tick of time, and stores references to the tick after and
 * before it for use as a linked list.
 * 
 * @author Alex Schutz
 */
public class TickRecord {
    // Linked list references
    private final TickRecord prev;
    private TickRecord next;
    
    // Location and velocity of the enemy at this point in time
    public final RelativePoint velocity;
    
    // Constcuts a TickRecord representing the current status of HostileBot
    public TickRecord(TickRecord lastTick, RelativePoint velocity) {
        prev = lastTick;
        if (prev != null)
            prev.next = this;
        this.velocity = velocity;
    }
    
    public TickRecord getNext() {
        return next;
    }
    
    public TickRecord getPrev() {
        return prev;
    }
}