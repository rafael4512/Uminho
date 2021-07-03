package ourrobots.F2;

import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Tag extends TeamFunctions {
    boolean searching;
    final HashMap<String, Boolean> state_game = new HashMap<>();
    private final ReadyCondition readyCondition = new ReadyCondition("nextStep", this, 4);
    Point direction = null;
    boolean searchRight = true;
    double last_seen = 100;
    final int MAX_LAST_SEEN = 5;
    double turnRadarAngle = 360;

    public void run() {
        addCustomEvent(readyCondition);

        super.broadcastName();

        waitFor(readyCondition);

        setAdjustRadarForGunTurn(true);

        String my_name = getName();
        final ArrayList<String> friends = this.getTeam();
        friends.add(my_name);
        Collections.sort(friends);

        for (String friend : friends) {
            this.state_game.put(friend, false);
        }
        this.state_game.put(friends.get(0), true);
        searching = my_name.equals(friends.get(0));

        if (searching) {
            System.out.println("I'm searching");
            setColors(Color.blue,Color.black,Color.black); // body,gun,radar
        }
        else {
            System.out.println("I'm dumb");
            setColors(Color.pink,Color.white,Color.white); // body,gun,radar
        }

        while (state_game.containsValue(false)) {
            if (last_seen > MAX_LAST_SEEN) {
                turnRadarAngle = 360;
                searchRight = !searchRight;
            }

            if (searchRight) turnRadarRight(turnRadarAngle);
            else turnRadarLeft(turnRadarAngle);

            last_seen++;

            if (direction != null) {
                this.move(direction.add(new Point(getX(), getY())));
            }
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if (!state_game.containsKey(e.getName())) {
            turnRadarAngle = 360;
            return;
        }

        double absoluteBearing = (this.getHeading() + e.getBearing() + 360) % 360;
        turnRadarAngle = (this.getRadarHeading() - absoluteBearing + 360) % 360;
        last_seen = 0;

        if (turnRadarAngle <= 180) {
            searchRight = false;
        }
        else {
            searchRight = true;
            turnRadarAngle = 360 - turnRadarAngle;
        }

        if (state_game.get(e.getName()) != searching) {
            Point myPosition = new Point(this.getX(), this.getY());
            Point robot = calculateFinalPoint(myPosition, e.getDistance(), e.getBearing(), this.getHeading());

            if (searching) {
                direction = robot.difference(myPosition);

                try {
                    broadcastMessage(robot);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else {
                direction = myPosition.difference(robot);
            }

            direction.normalize(50);
        }
    }


    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() == null) {
            state_game.put(e.getSender(), true);
        } else if (e.getMessage() instanceof String) {
            super.addTeammate((String) e.getMessage());
            readyCondition.oneMoreReady();
        } else if (e.getMessage() instanceof Point) {
            if (searching && last_seen > MAX_LAST_SEEN) {
                direction = ((Point) e.getMessage()).difference(new Point(getX(), getY()));
                direction.normalize(50);
            }
        }
    }


    public void onHitWall(HitWallEvent event) {
        double h = getBattleFieldHeight();
        double w = getBattleFieldWidth();
        double x = getX();
        double y = getY();

        // 2 - Cima, 1 - Baixo
        int top_low_wall = 0;
        // 2 - Direita, 1 - Esquerda
        int side_wall = 0;

        if (y/h > 0.9) {
            top_low_wall = 2;
        }
        else if (y/h < 0.1) {
            top_low_wall = 1;
        }

        if (x/w > 0.9) {
            side_wall = 2;
        }
        else if (x/w < 0.1) {
            side_wall = 1;
        }

        if (side_wall != 0 && top_low_wall != 0) {
            direction = new Point(side_wall == 2 ? -20: 20, top_low_wall == 2 ? -10: 10);
        }
        else if (side_wall != 0) {
            direction = new Point(side_wall == 2 ? -20: 20, (y < h/2) ? 100: -100);
        }
        else {
            direction = new Point((x < w/2) ? 100: -100, top_low_wall == 2 ? -20: 20);
        }

        direction.normalize(100);
        this.move(direction.add(new Point(getX(), getY())));
    }

    public void onHitRobot(HitRobotEvent event) {
        turnRight(30);
        back(50);

        if (!searching) {
            System.out.println(event.getName());
            if (state_game.containsKey(event.getName()) && state_game.get(event.getName())) {
                searching = true;
                setColors(Color.blue,Color.black,Color.black); // body,gun,radar

                try {
                    broadcastMessage(null);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("I'm searching");
            }
        }
        else {
            state_game.put(event.getName(), true);
        }
    }
}
