package ourrobots.F3.Utils;

public class Message implements java.io.Serializable {

    private String action = null;
    private RobotInfo robotInfo = null;
    private Point position = null;
    private String name = null;
    private double angle = -1;
    private double radius = -1;
    private int index = -1;


    public Message() {
    }

    public Message(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public RobotInfo getRobotInfo() {
        return this.robotInfo;
    }

    public void setRobotInfo(RobotInfo robotInfo) {
        this.robotInfo = robotInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }


}
