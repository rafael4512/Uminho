package ourrobots.F2;


public class Point implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

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

    public double distance(double x, double y) {
        double ac = Math.abs(this.y - y);
        double cb = Math.abs(this.x - x);
        return Math.hypot(ac, cb);
    }

	public double distance(Point other) {
		double ac = Math.abs(this.y - other.getY());
		double cb = Math.abs(this.x - other.getX());
		return Math.hypot(ac, cb);
	}

    public Point difference(Point other) {
        return new Point(this.x - other.getX(), this.y - other.getY());
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        return Double.compare(point.y, y) == 0;
    }

	@Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
	public Point clone() {
	    return new Point(this.getX(), this.getY());
	}

	@Override
    public String toString() {
        return "Point (" + "x=" + x + ", y=" + y + ')';
    }

    public Point add(Point next_point) {
        return new Point(this.x + next_point.getX(), this.y + next_point.getY());
    }

    public void normalize(double i) {
        double d = this.distance(0,0);

        this.x *= i/d;
        this.y *= i/d;
    }
}
