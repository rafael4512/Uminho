package ourrobots.ags.util.points;

/**
 * @author Alexander Schultz
 */
public class AbsolutePoint extends java.awt.geom.Point2D implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    public double x, y;
    
    private AbsolutePoint() {}
    
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public AbsolutePoint addRelativePoint(RelativePoint rel) {
        return fromXY(x+rel.getX(), y+rel.getY());
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public static AbsolutePoint fromXY(double x, double y) {
        AbsolutePoint newpoint = new AbsolutePoint();
        newpoint.setLocation(x, y);
        return newpoint;
    }
}
