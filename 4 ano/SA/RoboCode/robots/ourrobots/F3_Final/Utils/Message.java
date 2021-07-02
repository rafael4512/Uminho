package ourrobots.F3_Final.Utils;

public class Message implements java.io.Serializable {

    private String action;

    public Message(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
