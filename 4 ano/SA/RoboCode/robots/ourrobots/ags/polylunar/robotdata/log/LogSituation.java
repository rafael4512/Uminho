package ourrobots.ags.polylunar.robotdata.log;

import ourrobots.ags.polylunar.robotdata.FriendlyBot;
import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.util.kdtree.HyperPoint;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import static robocode.util.Utils.normalAbsoluteAngle;

public class LogSituation extends HyperPoint {
    static final long serialVersionUID = 1L;
    public static final int dimensions = 4;
    
    public LogSituation(HostileBot target, FriendlyBot perspective) {
        super(getValues(target, perspective));
    }
    
    private static double[] getValues(HostileBot enemy, FriendlyBot perspective) {
        final AbsolutePoint source = perspective.getLocation();
        final AbsolutePoint target = enemy.getNewLocation();
        final RelativePoint targetVelocity = enemy.getNewVelocity();
        final double angle = normalAbsoluteAngle(Math.atan2(target.x-source.x, target.y-source.y));
        final double lateralVelocity = (targetVelocity.magnitude * Math.sin(targetVelocity.getDirection() - angle));
        final double advancingVelocity = (targetVelocity.magnitude * -Math.cos(targetVelocity.getDirection() - angle));
        final double accel;
        if (enemy.getVelocity() != null)
            accel = (targetVelocity.magnitude - enemy.getVelocity().magnitude) * (targetVelocity.magnitude >= 0 ? 1 : -1);
        else
            accel = 0;
        final double distance = source.distance(target);
        
        return new double[]{
                lateralVelocity/8.0,
                advancingVelocity/8.0,
                accel/2.0,
                1.0/Math.max(distance-15,1.0)
        };
    }
}
