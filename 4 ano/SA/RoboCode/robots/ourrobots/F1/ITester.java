package ourrobots.F1;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.RobocodeFileOutputStream;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class ITester extends AdvancedRobot {

    public double distanceFromWalls = 54.1;
    public int distanceFromRef = 16;
    public int round;
    public double finalX;
    public double finalY;


    public void move(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading();
        while (turnAngle < 0) turnAngle += 360;

        if(turnAngle <= 180) turnRight(turnAngle);
        else turnLeft(360-turnAngle);

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    public double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    public int getRoundNumber() {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(this.getDataFile("testerounds.txt")))) {
                round = Integer.parseInt(br.readLine());
            }
        } catch (IOException | NumberFormatException e) {
            round = 0;
        }

        System.out.println("Round "+ round%5);

        try (PrintStream ps = new PrintStream(new RobocodeFileOutputStream(this.getDataFile("testerounds.txt")))) {
            ps.println(round + 1);
            if (ps.checkError())
                this.out.println("Enable to write id!");

        } catch (IOException e) {
            e.printStackTrace(this.out);
        }
        return round;
    }

    public void onHitRobot(HitRobotEvent event) {
        turnRight(30);
        back(80);
        move(finalX,finalY);
    }


}
