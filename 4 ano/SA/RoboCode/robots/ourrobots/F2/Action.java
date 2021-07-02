package ourrobots.F2;

public class Action implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public String action;
    public Point target;
    public int index;
    public int senderID;
    public int targetTeamID;
    public double angle;
    public double radius;

    public Action()
    {
        this.action = null;
        this.target = null;
        this.index = -1;
        this.senderID = -1;
        this.targetTeamID = -1;
        this.angle = -1;
        this.radius = -1;
    }

    public Action(String action)
    {
        this.action = action;
        this.target = null;
        this.index = -1;
        this.senderID = -1;
        this.targetTeamID = -1;
        this.angle = -1;
        this.radius = -1;
    }

    public Action(String action, Point target, int senderID, int targetTeamID)
    {
        this.action = action;
        this.target = target;
        this.index = -1;
        this.senderID = senderID;
        this.targetTeamID = targetTeamID;
        this.angle = -1;
        this.radius = -1;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target) {
        this.target = target.clone();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTargetTeamID() {
        return targetTeamID;
    }

    public void setTargetTeamID(int targetTeamID) {
        this.targetTeamID = targetTeamID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
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

    @Override
    public String toString() {
        return "Action{" +
                "action='" + action + '\'' +
                ", target=" + target +
                ", senderID=" + senderID +
                ", targetTeamID=" + targetTeamID +
                '}';
    }
}
