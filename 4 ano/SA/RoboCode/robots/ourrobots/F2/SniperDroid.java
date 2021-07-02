package ourrobots.F2;

import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;

import java.awt.*;
import java.io.IOException;

public class SniperDroid extends TeamFunctions implements Droid {

    private final ReadyCondition readyCondition = new ReadyCondition("readyToShoot", this, 1);
    private double goToX = 0;
    private double goToY = 0;
    String sender;

    public void run() {
        setColors(Color.WHITE,Color.WHITE,Color.WHITE);
        addCustomEvent(readyCondition);

        waitFor(readyCondition);

        while(Math.abs(this.getX() - goToX) > 0.001 || Math.abs(this.getY() - goToY) > 0.001)
            move(goToX,goToY);

        Action inform = new Action();
        inform.setAction("Inform.ReadyToShoot");

        try {
            this.sendMessage(sender,inform);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Action) {
            Action a = (Action) e.getMessage();
            if(a.getAction().equals("Move"))
            {
                sender = e.getSender();
                Point target = a.getTarget();
                goToX = target.getX();
                goToY = target.getY();
                readyCondition.oneMoreReady();
            }
            else if(a.getAction().equals("Shoot")) {
                Point target = a.getTarget();
                turnTo(target);
                fire(getEnergy());
            }
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        setTurnLeft(30);
        back(40);
        move(goToX,goToY);
    }
}
