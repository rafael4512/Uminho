package ourrobots.F2;

import robocode.Droid;
import robocode.MessageEvent;

public class Dummy extends TeamFunctions implements Droid {

    public void run() {
        super.broadcastName();
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof TeamColors) {
            TeamColors c = (TeamColors) e.getMessage();

            setColorsTeam(c);

            System.out.println(getName());
        }
        else if (e.getMessage() instanceof Point) {
            System.out.println("Move to " + e.getMessage().toString());
            super.move((Point) e.getMessage());
        }
    }
}
