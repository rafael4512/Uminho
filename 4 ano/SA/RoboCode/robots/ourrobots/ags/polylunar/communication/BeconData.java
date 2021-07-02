package ourrobots.ags.polylunar.communication;

import ourrobots.ags.polylunar.robotdata.SelfStatus;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import java.io.Serializable;

public class BeconData implements Serializable {
    private static final long serialVersionUID = 1L;
    public final AbsolutePoint location;
    public final RelativePoint velocity;
    public final double energy;
    public final double radarHeading;
    public BeconData(SelfStatus s) {
        location = s.getLocation();
        velocity = s.getVelocity();
        energy = s.status.getEnergy();
        radarHeading = s.status.getRadarHeadingRadians();
    }
}
