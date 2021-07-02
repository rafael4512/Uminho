package ourrobots.F2;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BeastDoctor64 extends TeamFunctions {


    public void run() {
        TeamColors tc = super.getColorsTeam();
        super.setColorsTeam(tc);

        try {
            // Send RobotColors object to our entire team
            broadcastMessage(tc);
        } catch (IOException ignored) {}


        super.move(50, 50);

        ArrayList<String> teammates = getTeam();
        int coords = 150;
        for (String teammate : teammates) {
            System.out.println(teammate);

           try {
                // Send RobotColors object to our entire team
                sendMessage(teammate, new Point(coords, coords));
            } catch (IOException ignored) {}

            coords += 100;
        }
    }

    public void onPaint(Graphics2D g) {
        /*
        if (!path.isEmpty()) {
            g.setColor(Color.white);
            g.setStroke(new BasicStroke(2.0F));

            g.drawLine((int)initCoords, (int)initCoords, (int)path.get(0).getX(), (int)path.get(0).getY());
            for(int i = 0; i < (path.size() - 1); i++)
                g.drawLine((int)this.path.get(i).getX(), (int)this.path.get(i).getY(), (int)path.get(i+1).getX(), (int)path.get(i+1).getY());
        }
        */
    }

    public void onScannedRobot(ScannedRobotEvent e) {
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof String) {
            super.addTeammate((String) e.getMessage());
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        turnRight(45);
        back(20);
    }
}
