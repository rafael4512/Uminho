package ourrobots.ags.util.points;

import static robocode.util.Utils.normalAbsoluteAngle;
import static robocode.util.Utils.normalRelativeAngle;

/**
 * @author Alexander Schultz
 */
public final class RelativePoint extends java.awt.geom.Point2D implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public double x, y, direction, magnitude;
    
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getDirection() {
        return direction;
    }
    public double getDirDist(double dir) {
        return normalRelativeAngle(direction - dir);
    }
    
    public double getMagnitude() {
        return magnitude;
    }
    public RelativePoint addRelativePoint(RelativePoint rel) {
        return fromXY(getX()+rel.getX(), getY()+rel.getY());
    }
    public RelativePoint incrementDirMag(double direction, double magnitude) {
        return fromDM(this.direction+direction, this.magnitude+magnitude);
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        direction = normalAbsoluteAngle(Math.atan2(x, y));
        magnitude = Math.hypot(x, y);
    }
    
    public void setDirectionMagnitude(double direction, double magnitude) {
        this.direction = normalAbsoluteAngle(direction);
        this.magnitude = magnitude;
        x = magnitude*Math.sin(this.direction);
        y = magnitude*Math.cos(this.direction);
    }
    
    private static RelativePoint getNewInstance() {
        return new RelativePoint();
    }
    
    public static RelativePoint fromXY(double x, double y) {
        RelativePoint newpoint = getNewInstance();
        newpoint.setLocation(x, y);
        return newpoint;
    }
    
    public static RelativePoint fromDM(double direction, double magnitude) {
        RelativePoint newpoint = getNewInstance();
        newpoint.setDirectionMagnitude(direction, magnitude);
        return newpoint;
    }
    
    public static RelativePoint fromPP(AbsolutePoint p1, AbsolutePoint p2) {
        RelativePoint newpoint = getNewInstance();
        newpoint.setLocation(p2.getX() - p1.getX(), p2.getY() - p1.getY());
        return newpoint;
    }    
}

