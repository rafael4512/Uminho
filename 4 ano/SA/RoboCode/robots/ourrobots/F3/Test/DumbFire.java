package ourrobots.F3.Test;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.RobotInfo;
import ourrobots.F3.Utils.TeamFunctions;
import robocode.Bullet;
import robocode.MessageEvent;

import java.io.IOException;

public class DumbFire extends TeamFunctions {

    @Override
    public void run() {
        broadcastInfo();
        move(100, 200);
        turnTo(0);
        turnRight(90);

        int i = 0;
        while (i < 200) {
            boolean willHit = false;
            for (RobotInfo friend : getTeamInfo().values()) {

                if ((Math.toDegrees(Math.atan((friend.getPosition().getY() - getY()) / (friend.getPosition().getX() - getX()))) + (-getGunHeading() + 450) + 5) % 360 < 10) {
                    willHit = true;
                    break;
                }
            }

            if (!willHit) {
                try {
                    Bullet b = new Bullet(getGunHeadingRadians(), getX(), getY(), 1, getName(), null, true, 1000);
                    broadcastMessage(b);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fire(1);
                i++;
            }
            execute();
        }
    }

    @Override
    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message) {
            Message a = (Message) e.getMessage();
            if (a.getAction().equals("Inform.Position")) {
            }
            //this.addTeamPosition(e.getSender(),a.getPosition());
        }
    }
}
