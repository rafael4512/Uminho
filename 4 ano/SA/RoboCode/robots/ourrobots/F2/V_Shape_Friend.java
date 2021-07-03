package ourrobots.F2;

import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;

import java.awt.*;
import java.io.IOException;

public class V_Shape_Friend extends TeamFunctions implements Droid {
    Point position = null;
    boolean first_move = true;
    private final ReadyCondition readyCondition = new ReadyCondition("nextStep", this, 1);
    private final ReadyCondition setCondition = new ReadyCondition("nextStep", this, 1);
    String leader;


    public void run() {
        setColors(Color.pink,Color.pink,Color.pink); // body,gun,radar
        super.broadcastName();

        addCustomEvent(readyCondition);

        waitFor(readyCondition);

        readyCondition.setAllNotReady();

        int max_steps = 1000;
        for (int i = 0; i < max_steps; i++) {
            Color color = new Color((int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255));
            setColors(color,color,color); // body,gun,radar

            if (position.distance(new Point(getX(), getY())) > 0.001)
                move(position);

            execute();

            if (first_move) {
                turnTo(90);
                System.out.println(getHeading());

                if (new Point(getX(), getY()).distance(position) < 0.001) setCondition.oneMoreReady();
                waitFor(setCondition);

                turnTo(90);

                try {
                    sendMessage(leader, null);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                first_move = false;
            }
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Point) {
            if (first_move) {
                leader = e.getSender();
                position = (Point) e.getMessage();
                readyCondition.oneMoreReady();
            }
            else {
                position = position.add((Point) e.getMessage());
                readyCondition.oneMoreReady();
            }
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        setTurnLeft(30);
        back(40);

        if (position != null) {
            move(position.getX(), position.getY());

            if (new Point(getX(), getY()).distance(position) < 0.001) {
                setCondition.oneMoreReady();
                turnTo(90);
            }
        }
    }
}
