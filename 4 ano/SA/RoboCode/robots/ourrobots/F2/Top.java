package ourrobots.F2;

import robocode.HitRobotEvent;
import robocode.MessageEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Top extends TeamFunctions {
    private final ReadyCondition readyCondition = new ReadyCondition("nextStep", this, 4);

    public void run() {
        setColors(Color.black,Color.black,Color.black); // body,gun,radar
        addCustomEvent(readyCondition);

        double half_width = getBattleFieldWidth()/2;
        double half_height = getBattleFieldHeight()/2;

        Point position = new Point(half_width, half_height);
        while (position.distance(new Point(getX(), getY())) > 0.001) {
            super.move(position);
        }

        ArrayList<String> teammates = getTeam();
        double step = (double) 360 / teammates.size();

        Action a = new Action("Inform.InitialState");
        a.setRadius(150);
        a.setTarget(position);

        double angle = 0.0;
        for (String teammate : teammates) {
            try {
                // Send RobotColors object to our entire team
                a.setAngle(angle);
                sendMessage(teammate, a);
            } catch (IOException ignored) {}

            angle += step;
        }

        turnLeft(this.getHeading());
        waitFor(readyCondition);

        setMaxVelocity(1.5);

        while (position.distance(new Point(getX(), getY())) > 0.001) {
            super.move(position);
        }

        Point[] pts = {new Point(20, 0), new Point(20,20), new Point(0,20), new Point(-20,20), new Point(-20,0), new Point(-20,-20), new Point(0,-20), new Point(20,-20)};
        int pts_len = pts.length;

        int max_steps = 1000;
        for (int i = 0; i < max_steps; i++) {
            Point new_point = pts[i % pts_len].add(position);

            try {
                broadcastMessage(new_point);
                position = new_point;
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.move(new_point);
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
    }
}
