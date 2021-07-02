package ourrobots.F3.Utils;

public class RobotInfo implements java.io.Serializable {

    private String name = null;
    private Point position = null;
    private double heading = -1;
    private double velocity = -1;
    private double energy = -1;
    private long time = -1;


    public RobotInfo() {
    }

    public RobotInfo(String name) {
        this.name = name;
    }

    public RobotInfo(RobotInfo ri) {
        this.name = ri.getName();
        this.position = ri.getPosition();
        this.heading = ri.getHeading();
        this.velocity = ri.getVelocity();
        this.energy = ri.getEnergy();
        this.time = ri.getTime();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getPosition() {
        return new Point(this.position);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RobotInfo{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", heading=" + heading +
                ", velocity=" + velocity +
                ", energy=" + energy +
                '}';
    }
}
