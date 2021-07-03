package ourrobots.F3.StarWars;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.Point;
import ourrobots.F3.Utils.TeamFunctions;
import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;

import java.awt.*;

public class C3PO extends TeamFunctions implements Droid {

    public void run() {
        broadcastName();
        Color golden = new Color(255, 210, 0);
        setColors(golden, golden, golden);

        addCustomEvent(ownPositionRefresher);


    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message) {
            Message a = (Message) e.getMessage();
            if (a.getAction().equals("Inform.Name"))
                this.addTeammate(e.getSender());
            else if (a.getAction().equals("Inform.TeamInfo"))
                this.addTeamInfo(a.getRobotInfo());
            else if (a.getAction().equals("Shoot")) {
                Point target = a.getPosition();
                if (target.distance(this.getX(), this.getY()) < 100) {
                    turnGunTo(target);
                    fire(Math.min(getEnergy() / 2, 1));
                }
            }
        }
    }

    public void onHitRobot(HitRobotEvent e) {

        double absoluteHitAngle = (this.getHeading() + e.getBearing() + 360) % 360;

        turnGunToFast(absoluteHitAngle);

        fire(Math.min(getEnergy() / 2, 1));
    }
}
