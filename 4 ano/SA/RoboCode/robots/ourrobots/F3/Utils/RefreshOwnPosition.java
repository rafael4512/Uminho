package ourrobots.F3.Utils;

import robocode.Condition;


class RefreshOwnPosition extends Condition {
    private final TeamFunctions robot;

    public RefreshOwnPosition(String name, TeamFunctions r) {
        super(name);
        this.robot = r;
    }

    public boolean test() {
        robot.broadcastInfo();
        return true;
    }
}
