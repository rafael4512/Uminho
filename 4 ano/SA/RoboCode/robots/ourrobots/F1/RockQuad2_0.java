package ourrobots.F1;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.RobocodeFileOutputStream;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class RockQuad2_0 extends AdvancedRobot {

    private final Color[] colors = new Color[]{Color.gray, Color.black, Color.white};
    private ArrayList<Point> quads = new ArrayList<>();
    private ArrayList<Point> tempQuads = new ArrayList<>();
    private int id = -1;
    private double x, y;
    private boolean readyToScan = false;
    private boolean firstScanDone = false;
    private boolean scanCompleted = false;

    public void run() {

        calculateId();
        System.out.printf("Robot id: %d!\n", id);

        setColors(colors[id],colors[id],colors[id]); // body,gun,radar

        double height = getBattleFieldHeight();
        double width = getBattleFieldWidth();

        int distanceFromWalls = 55; int distanceFromRef = 16;
        if(id == 0) //ne
        {
            x = getRandomNumber(width / 2 + distanceFromRef, width - distanceFromWalls);
            y = getRandomNumber(height / 2 + distanceFromRef, height - distanceFromWalls);
        }
        else if (id == 1) //nw
        {
            x = getRandomNumber(distanceFromWalls, width / 2 - distanceFromRef);
            y = getRandomNumber(height / 2, height - distanceFromWalls);
        }
        else { //se
            x = getRandomNumber(width / 2, width - distanceFromWalls);
            y = getRandomNumber(distanceFromWalls, height / 2 - distanceFromRef);
        }

        while(Math.abs(this.getX()-x) > 0.001 || Math.abs(this.getY()-y) > 0.001)
            move(x,y);

        if(id == 0)
        {
            readyToScan = true;
            while(!scanCompleted) turnRadarRight(9);
            System.out.printf("Perimeter: %.2f pixels!\n", calculatePerimeter());
        }
    }

    public void onPaint(Graphics2D g) {
        if (scanCompleted && id == 0) {
            g.setColor(Color.green);
            g.setStroke(new BasicStroke(5.0F));
            Point p0 = new Point(0,0); //(18,18)
            Point p1 = quads.get(0);
            Point p2 = quads.get(1);

            g.drawLine((int)this.getX(), (int)this.getY(), (int)p1.getX(), (int)p1.getY());
            g.drawLine((int)this.getX(), (int)this.getY(), (int)p2.getX(), (int)p2.getY());
            g.drawLine((int)p0.getX(), (int)p0.getY(), (int)p1.getX(), (int)p1.getY());
            g.drawLine((int)p0.getX(), (int)p0.getY(), (int)p2.getX(), (int)p2.getY());
        }

    }

    public double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    private void move(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading();
        while (turnAngle < 0) turnAngle += 360;

        if(turnAngle <= 180) turnRight(turnAngle);
        else turnLeft(360-turnAngle);

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    private void calculateId() {

        try {
            try (BufferedReader br = new BufferedReader(new FileReader(this.getDataFile("rockquadids.txt")))) {
                id = Integer.parseInt(br.readLine());
            }
        } catch (IOException | NumberFormatException e) {
            id = 0;
        }

        try (PrintStream ps = new PrintStream(new RobocodeFileOutputStream(this.getDataFile("rockquadids.txt")))) {
            ps.println((id + 1) % 3);
            if (ps.checkError())
                this.out.println("Enable to write id!");

        } catch (IOException e) {
            e.printStackTrace(this.out);
        }
    }

    private Point calculateFinalPoint(Point initPoint, double distance, double angle, double heading)
    {
        double radian = Math.toRadians(heading + angle % 360);

        double newX = (initPoint.getX() + Math.sin(radian) * distance);
        double newY = (initPoint.getY() + Math.cos(radian) * distance);

        return new Point(newX, newY);
    }

    private double calculatePerimeter() {

        Point p0 = new Point(0,0);
        Point nw = quads.get(0);
        Point ne = new Point(this.getX(), this.getY());
        Point se = quads.get(1);

        return p0.distance(nw) + nw.distance(ne) + ne.distance(se) + se.distance(p0);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        if(readyToScan && e.getName().contains("RockQuad2") && e.getVelocity() == 0) {
            Point robot = calculateFinalPoint(new Point(this.getX(), this.getY()), e.getDistance(), e.getBearing(), this.getHeading());

            if (!firstScanDone) {
                if (!quads.contains(robot)) quads.add(robot);
                if (quads.size() == 2) firstScanDone = true;
            }
            else {
                if (!tempQuads.contains(robot)) tempQuads.add(robot);

                if (tempQuads.size() == 2) {

                    boolean allQuadsReady = true;

                    for (Point p : quads)
                        if (!tempQuads.contains(p))
                            allQuadsReady = false;

                    if (!allQuadsReady) {
                        quads = new ArrayList<>();
                        quads.addAll(tempQuads);
                        tempQuads = new ArrayList<>();
                    }
                    else {
                        readyToScan = false;
                        scanCompleted = true;
                    }
                }
            }
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        setTurnRight(30);
        back(50);
        move(x,y);
    }
}
