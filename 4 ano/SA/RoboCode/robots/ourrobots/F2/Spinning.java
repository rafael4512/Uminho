package ourrobots.F2;

import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;

import java.awt.*;
import java.io.IOException;

public class Spinning extends TeamFunctions implements Droid {
    private final ReadyCondition readyCondition = new ReadyCondition("nextStep", this, 1);
    private final ReadyCondition setCondition = new ReadyCondition("setStep", this, 1);

    Point center = null;
    String leader;
    double angle = -1;
    double radius = -1;
    double goToX;
    double goToY;

    final double step = 30;

    public void run() {
        setColors(Color.orange,Color.orange,Color.orange); // body,gun,radar

        addCustomEvent(readyCondition);
        addCustomEvent(setCondition);

        setMaxVelocity(2);
        super.broadcastName();

        waitFor(readyCondition);

        int max_steps = 1000;
        for (int moves = 0; moves <= max_steps; moves++) {
            if (center != null && radius > 0 && angle >= 0) {
                Point next_point = super.calculateFinalPoint(center, radius, angle, 0);
                goToX = next_point.getX();
                goToY = next_point.getY();

                if (moves == 0) {
                    while (next_point.distance(new Point(getX(), getY())) > 0.001) {
                        super.move(next_point);
                    }

                    turnTo(angle + 90);

                    try {
                        sendMessage(leader, null);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                    waitFor(setCondition);
                    setMaxTurnRate(2);
                }
                else {
                    setTurnMove(next_point);
                }

                angle += step;
            }
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Action) {
            Action a = (Action) e.getMessage();
            if (a.getAction().equals("Inform.InitialState")) {
                center = a.getTarget();
                radius = a.getRadius();
                angle = a.getAngle();
                leader = e.getSender();

                readyCondition.oneMoreReady();
            }
        }
        else if (e.getMessage() instanceof Point) {
            center = (Point) e.getMessage();
            goToX = center.getX();
            goToY = center.getY();

            if (!setCondition.test()) {
                setCondition.oneMoreReady();
            }
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        setTurnLeft(30);
        back(40);

        if (center != null) {
            move(goToX, goToY);

            if (!setCondition.test()) turnTo(angle + 90);
        }
    }
}
