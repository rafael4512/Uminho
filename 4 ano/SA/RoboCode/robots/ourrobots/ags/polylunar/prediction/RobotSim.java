package ourrobots.ags.polylunar.prediction;

import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

/**
 * Simple storage class, representing the basic location/speed of a hypothetical robot
 */
public class RobotSim {
    public AbsolutePoint location;
    public RelativePoint velocity;
    public boolean hitWallFlag;
}
