package ourrobots.F3_Final.Utils;


public class Point implements java.io.Serializable {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double distance(Point other) {
        double ac = Math.abs(this.y - other.getY());
        double cb = Math.abs(this.x - other.getX());
        return Math.hypot(ac, cb);
    }

    public double distance(double otherX, double otherY) {
        double ac = Math.abs(this.y - otherY);
        double cb = Math.abs(this.x - otherX);
        return Math.hypot(ac, cb);
    }

    public void oscillate(double angle) {
        double k = ((int) (Math.random() * 100)) % 4 * 25;
        this.x -= k * Math.cos(angle);
        this.y -= k * Math.sin(angle);
    }

    @Override
    public String toString() {
        return "Point (" + "x=" + x + ", y=" + y + ')';
    }
}
