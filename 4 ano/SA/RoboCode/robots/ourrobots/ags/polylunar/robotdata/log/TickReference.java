package ourrobots.ags.polylunar.robotdata.log;

import ourrobots.ags.util.kdtree.HyperPoint;
import ourrobots.ags.util.kdtree.KDEntry;

/**
 * @author Alexander Schultz
 */
public class TickReference extends KDEntry {
    private HyperPoint position;
    private TickRecord tick;
    
    public TickReference(TickRecord tick, HyperPoint position) {
        this.tick = tick;
        this.position = position;
    }
    
    public TickRecord getTick() {
        return tick;
    }
    
    @Override
    public HyperPoint getPosition() {
        return position;
    }

}
