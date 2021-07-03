package ourrobots.F2;

import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class V_Shape_Leader extends TeamFunctions implements Droid {
    private final ReadyCondition readyCondition = new ReadyCondition("nextStep", this, 4);
    Point position = null;

    public void run() {
        setColors(Color.black, Color.black, Color.black); // body,gun,radar
        addCustomEvent(readyCondition);

        double half_width = getBattleFieldWidth()/2;
        double half_height = getBattleFieldHeight()/2;

        position = new Point(half_width, half_height);
        while (position.distance(new Point(getX(), getY())) > 0.001) {
            super.move(position);
        }

        ArrayList<String> teammates = getTeam();

        Point[] pts = {new Point(-200, -200), new Point(-100, -100), new Point(100, -100), new Point(200, -200)};
        Point p;
        for (int i = 0; i < teammates.size(); i++) {
            String teammate = teammates.get(i);
            p = position.add(pts[i]);

            try {
                // Send RobotColors object to our entire team
                sendMessage(teammate, p);

            } catch (IOException ignored) {}
        }

        turnTo(90);
        waitFor(readyCondition);

        setMaxVelocity(7.9);

        pts = new Point[]{new Point(100, 0), new Point(0, 200), new Point(-200, 0), new Point(0, -200), new Point(100, 0)};
        int pts_len = pts.length;

        int max_steps = 1000;
        for (int i = 0; i < max_steps; i++) {
            position = pts[i % pts_len].add(position);

            try {
                broadcastMessage(pts[i % pts_len]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.move(position);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() == null) {
            readyCondition.oneMoreReady();
        } else if (e.getMessage() instanceof String) {
            super.addTeammate((String) e.getMessage());
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        setTurnLeft(30);
        back(40);

        if (position != null) {
            super.move(position);
            turnTo(90);
        }
    }
}
