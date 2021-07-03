package ourrobots.ags.polylunar.radar;

import ourrobots.ags.polylunar.robotdata.FriendlyBot;
import ourrobots.ags.polylunar.robotdata.HostileBot;

public class AimPossibility {
    public final FriendlyBot source;
    public final HostileBot target;
    public final double dist;
    
    public AimPossibility(FriendlyBot source, HostileBot target) {
        this.source = source;
        this.target = target;
        dist = source.getLocation().distance(target.getLocation());
    }
    
    public double getDistance() {
        return dist;
    }
    
    public boolean conflictsWith(AimPossibility other) {
        return source.getName().equals(other.source.getName()) || target.getName().equals(other.target.getName()); 
    }
}
