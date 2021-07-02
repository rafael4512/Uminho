package ourrobots.F2;

import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;

import java.awt.*;
import java.io.IOException;

public class Clank extends TeamFunctions implements Droid {

    private final ReadyCondition readyCondition = new ReadyCondition("readyToShoot", this, 2);
    private boolean selfdestroy = false;

    public void run() {
        setColors(Color.gray, Color.green, Color.green); // body,gun,radar
        addCustomEvent(readyCondition);

        this.move(getBattleFieldWidth()/2,getBattleFieldHeight()/2);

        waitFor(readyCondition);

        while(!selfdestroy)
        {
            setAhead(40);
            execute();
        }

        while(true)
            this.move(getBattleFieldWidth()/2,getBattleFieldHeight()/2);

    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Action) {
            Action a = (Action) e.getMessage();
            if(a.getAction().equals("Inform.Ready"))
                readyCondition.oneMoreReady();
            else if(a.getAction().equals("HitRobot"))
            {
                double turnAngle = (this.getHeading() - a.getAngle() + 360) % 360;

                if(turnAngle <= 180) turnLeft(turnAngle);
                else turnRight(360-turnAngle);
            }
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if (this.getX() < 54 || this.getX() > (this.getBattleFieldWidth() - 54))
        {
            try {
                broadcastMessage(new Action("KillYourself"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            selfdestroy = true;
            this.setAheadMove(this.getBattleFieldWidth()/2,this.getBattleFieldHeight()/2);
        }
        else if(!readyCondition.test() && !selfdestroy)
        {
            setTurnRight(30);
            back(40);
        }
    }

    public void onHitWall(HitWallEvent e) {
        double heading = this.getHeading();

        if (this.getX() < 54 || this.getX() > (this.getBattleFieldWidth() - 54))
        {
            try {
                broadcastMessage(new Action("KillYourself"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            selfdestroy = true;
            this.setAheadMove(this.getBattleFieldWidth()/2,this.getBattleFieldHeight()/2);
            return;
        }

        if (heading>90 && heading<=180) //south wall going right
            turnLeft(2 * (heading - 90));
        else if(heading>180 && heading<=270) //south wall going left
            turnRight(2 * (270 - heading));
        else if (heading>0 && heading<=90) //north wall going right
            turnRight(2 * (90 - heading));
        else if (heading>270 && heading<360) //north wall going left
            turnLeft(2 * (heading - 270));
    }
}
