package ourrobots.F2;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ScannerLeader extends TeamFunctions {

    private final ReadyCondition readyCondition = new ReadyCondition("readyToShoot", this, 4);
    private final ArrayList<String> droidNames = new ArrayList<>();
    private final ArrayList<Point> droidPositions = new ArrayList<>();
    private boolean readyToScanDroids = false;
    private boolean readyToScanEnemies = false;
    private double goToX = 0;
    private double goToY = 0;

    public void run() {
        setColors(Color.BLACK,Color.BLACK,Color.BLACK); // body,gun,radar

        addCustomEvent(readyCondition);

        double distanceToWall = 18;
        double width = this.getBattleFieldWidth();
        double height = this.getBattleFieldHeight();

        this.droidPositions.add(new Point(18,18));
        this.droidPositions.add(new Point(width-distanceToWall,18));
        this.droidPositions.add(new Point(18,height-distanceToWall));
        this.droidPositions.add(new Point(width-distanceToWall,height-distanceToWall));

        goToX = width/2;
        goToY = distanceToWall;

        while(Math.abs(this.getX() - goToX) > 0.001 || Math.abs(this.getY() - goToY) > 0.001)
            move(goToX,goToY);

        readyToScanDroids = true;

        while(droidNames.size() < 4)
            turnRadarRight(10);

        readyToScanDroids = false;

        this.waitFor(readyCondition);

        move(goToX,goToY+0.000001);

        readyToScanEnemies = true;

        while(true) turnRadarRight(10);

    }

    public void onScannedRobot(ScannedRobotEvent e) {
        String droidName = e.getName();

        if(readyToScanDroids && droidName.contains("SniperDroid") && !droidNames.contains(droidName)) { // && droidPositions.size() != 0
            droidNames.add(droidName);

            Action move = new Action();
            move.setAction("Move");
            move.setTarget(droidPositions.get(droidNames.size()-1));

            try {
                this.sendMessage(droidName,move);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        else if(readyToScanEnemies && !droidName.contains("SniperDroid")) {

            Point target = this.calculateFinalPoint(new Point(this.getX(), this.getY()), e.getDistance(), e.getBearing(), this.getHeading());

            Action shoot = new Action();
            shoot.setAction("Shoot");
            shoot.setTarget(target);

            try {
                this.broadcastMessage(shoot);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            turnTo(target);
            fire(1);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Action) {
            Action a = (Action) e.getMessage();
            if(a.getAction().equals("Inform.ReadyToShoot"))
                readyCondition.oneMoreReady();
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        setTurnLeft(30);
        back(40);
        move(goToX,goToY);
    }
}
