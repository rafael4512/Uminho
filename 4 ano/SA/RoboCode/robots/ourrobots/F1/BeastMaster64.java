package ourrobots.F1;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.RobocodeFileWriter;
import robocode.ScannedRobotEvent;
import standardOdometer.Odometer;

import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class BeastMaster64 extends AdvancedRobot {

    private final Odometer odometer = new Odometer("IsRacing", this);
    private final OurOdometer ourOdometer = new OurOdometer("The better Odometer :)", this);

    private final HashMap<String, Point> enemies = new HashMap<>();
    private final ArrayList<Point> path = new ArrayList<>();
    private ArrayList<Point> quads = new ArrayList<>();
    private ArrayList<Point> tempQuads = new ArrayList<>();

    private static final double initCoords = 18.0;
    private static final double distanceToEnemies = 50.912; //(sqrt((36/2)^2+(36/2)^2))*2)
    private boolean readyToScan = false;
    private boolean isRacing = false;
    private boolean firstScanDone = false;
    private double totalDistance = 0;


    private void move(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading()) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if(turnAngle <= 180) turnRight(turnAngle);
        else turnLeft(360-turnAngle);

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);

        if(isRacing) totalDistance += distance;
    }

    public void run() {
        setColors(Color.red,Color.green,Color.black); // body,gun,radar

        addCustomEvent(ourOdometer);

        System.out.println("Travelling to starting point!");

        while(Math.abs(this.getX() - initCoords) > 0.001 || Math.abs(this.getY() - initCoords) > 0.001)
            move(initCoords, initCoords);

        System.out.println("Reached starting point!");

        // Turn Body/Gun/Radar to near 0º
        while(getGunHeadingRadians() > Math.PI / 32)
            turnRightRadians(Math.PI/64);

        System.out.println("Starting to Scan!");

        readyToScan = true;
        while(enemies.isEmpty()) turnRadarRight(7);

        addCustomEvent(odometer);

        isRacing = true;
        System.out.println("All RockQuads set! Go!");

        for(Point p : path)
            move(p.getX(),p.getY());

        isRacing = false;

        System.out.println("Arrived at destination!");

        writeTimeToFile();

        System.out.println("------------------------------");
        System.out.println("Total distance travelled (Odometer): " + odometer.getRaceDistance());
        System.out.printf("Total distance travelled (Our Odometer): %.2f pixels!\n", ourOdometer.getRaceDistance());
        System.out.printf("Total distance travelled (Real): %.2f pixels!\n", this.totalDistance);
        System.out.println("------------------------------");
        System.out.println("Total perimeter: " + this.calculatePerimeter());
        System.out.println("------------------------------");
        System.out.printf("Efficiency (Odometer): %.2f%%\n", (this.calculatePerimeter() / Double.parseDouble(odometer.getRaceDistance().split(" ")[0].replace(",", "."))) * 100);
        System.out.printf("Efficiency (Our Odometer): %.2f%%\n", (this.calculatePerimeter() / ourOdometer.getRaceDistance()) * 100);
        System.out.printf("Efficiency (Real): %.2f%%\n", (this.calculatePerimeter() / this.totalDistance) * 100);
        System.out.println("------------------------------");
    }

    private void writeTimeToFile() {
        double time = this.calculatePerimeter() / Double.parseDouble(odometer.getRaceDistance().split(" ")[0].replace(",", ".")) * 100;
        PrintWriter ps = null;
        PrintWriter ps2 = null;
        try {
            ps = new PrintWriter(new RobocodeFileWriter(getDataFile("times.txt").toString(),true));
            ps.println(time);
            ps2 = new PrintWriter(new RobocodeFileWriter(getDataFile("bettertimes.txt").toString(),true));
            if(time > 92.66 && time < 95)
                ps2.println("1 "+time);
            else
                ps2.println("0 "+time);
            if (ps.checkError())
                this.out.println("Enable to write time!");
        } catch (IOException e) {
            e.printStackTrace(this.out);
        } finally {
            if (ps != null)
                ps.close();
            if (ps2 != null)
                ps2.close();
        }
    }

    public void onPaint(Graphics2D g) {
        if (!path.isEmpty()) {
            g.setColor(Color.white);
            g.setStroke(new BasicStroke(2.0F));

            g.drawLine((int)initCoords, (int)initCoords, (int)path.get(0).getX(), (int)path.get(0).getY());
            for(int i = 0; i < (path.size() - 1); i++)
                g.drawLine((int)this.path.get(i).getX(), (int)this.path.get(i).getY(), (int)path.get(i+1).getX(), (int)path.get(i+1).getY());
        }

    }


    private Point calculateFinalPoint(Point initPoint, double distance, double angle, double heading)
    {
        double radian = Math.toRadians(heading + angle % 360);

        double newX = (initPoint.getX() + Math.sin(radian) * distance);
        double newY = (initPoint.getY() + Math.cos(radian) * distance);

        return new Point(newX, newY);
    }

    private double calculateAngle(Point a, Point b, Point c) {

        double ba_x = a.getX() - b.getX();
        double ba_y = a.getY() - b.getY();

        double bc_x = c.getX() - b.getX();
        double bc_y = c.getY() - b.getY();

        return Math.acos(((ba_x * bc_x) + (ba_y * bc_y)) / (Math.sqrt(Math.pow(ba_x, 2) + Math.pow(ba_y, 2)) * Math.sqrt(Math.pow(bc_x, 2) + Math.pow(bc_y, 2))));
    }

    private void calculatePath() {

        Point nw = enemies.get("northwest");
        Point ne = enemies.get("northeast");
        Point se = enemies.get("southeast");

        double nwAngle = 315;
        double neAngle = 45;
        double seAngle = 135;

        Point newNW = calculateFinalPoint(nw, distanceToEnemies, nwAngle, 0);
        Point newNE = calculateFinalPoint(ne, distanceToEnemies, nwAngle, 0);

        //Init Point
        if(calculateAngle(se,new Point(initCoords,initCoords), newNE) < calculateAngle(se,new Point(initCoords,initCoords), newNW))
            path.add(newNW);

        if(calculateAngle(new Point(initCoords,initCoords),nw, ne) < calculateAngle(new Point(initCoords,initCoords), nw, se)){
            path.add(calculateFinalPoint(nw, distanceToEnemies, neAngle, 0));
            path.add(calculateFinalPoint(se, distanceToEnemies, neAngle, 0));
        }
        else {
            //Second and Third Points
            if (nw.getY() <= ne.getY())
                path.add(calculateFinalPoint(ne, distanceToEnemies, nwAngle, 0));
            else
                path.add(calculateFinalPoint(nw, distanceToEnemies, neAngle, 0));

            path.add(calculateFinalPoint(ne, distanceToEnemies, neAngle, 0));

            //Forth and Fifth Points
            if (ne.getX() <= se.getX())
                path.add(calculateFinalPoint(se, distanceToEnemies, neAngle, 0));
            else
                path.add(calculateFinalPoint(ne, distanceToEnemies, seAngle, 0));
        }

        Point newSE = calculateFinalPoint(se, distanceToEnemies, seAngle, 0);
        Point last = path.get(path.size()-1);
        Point lastButOne = path.get(path.size()-2);

        if(calculateAngle(lastButOne,last,new Point(0,0)) < calculateAngle(lastButOne,last, newSE))
            path.add(newSE);

        // Final Point
        path.add(new Point(initCoords,initCoords));
    }

    private double calculatePerimeter() {

        Point p0 = new Point(0,0); //this.getX(),this.getY()
        Point nw = enemies.get("northwest");
        Point ne = enemies.get("northeast");
        Point se = enemies.get("southeast");

        return p0.distance(nw) + nw.distance(ne) + ne.distance(se) + se.distance(p0);
    }

    private void calculateEnemies() {

        Point nw = null; //smallerX
        Point se = null; //smallerY
        Point ne = null; //other
        double smallerX = getBattleFieldWidth();
        double smallerY = getBattleFieldHeight();

        for(Point p : quads)
        {
            if(p.getX() < smallerX)
            {
                nw = p;
                smallerX = p.getX();
            }
            if(p.getY() < smallerY)
            {
                se = p;
                smallerY = p.getY();
            }
        }
        for(Point p : quads)
            if(!p.equals(nw) && !p.equals(se))
                ne = p;

        enemies.put("northwest", nw);
        enemies.put("southeast", se);
        enemies.put("northeast", ne);
    }

    public void onScannedRobot(ScannedRobotEvent e) {

        if(readyToScan && e.getVelocity() == 0) {
            Point robot = calculateFinalPoint(new Point(this.getX(), this.getY()), e.getDistance(), e.getBearing(), this.getHeading());

            if (!firstScanDone) {
                if (!quads.contains(robot)) quads.add(robot);
                if (quads.size() == 3) firstScanDone = true;
            }
            else {
                if (!tempQuads.contains(robot)) tempQuads.add(robot);

                if (tempQuads.size() == 3) {

                    boolean allQuadsReady = true;

                    for (Point p : quads)
                        if (!tempQuads.contains(p)) {
                            allQuadsReady = false;
                            break;
                        }

                    if (!allQuadsReady) {
                        quads = new ArrayList<>();
                        quads.addAll(tempQuads);
                        tempQuads = new ArrayList<>();
                    }
                    else
                    {
                        readyToScan = false;
                        calculateEnemies();
                        calculatePath();
                    }
                }
            }
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        turnRight(45);
        back(20);
    }
}
