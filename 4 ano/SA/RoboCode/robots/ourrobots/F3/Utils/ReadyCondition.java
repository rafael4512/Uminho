package ourrobots.F3.Utils;

import robocode.Condition;
import robocode.Robot;

public class ReadyCondition extends Condition {
    private final Robot robot;
    private final int maxReady;
    private int ready;

    public ReadyCondition(String name, Robot r, int maxReady) {
        super(name);
        this.robot = r;
        this.ready = 0;
        this.maxReady = maxReady;
    }

    public boolean test() {
        return ready >= maxReady;
    }

    public void oneMoreReady() {
        this.ready++;
        System.out.println(this.getName() + ": " + ready);
    }

    public void setAllNotReady() {
        ready = 0;
    }

}
