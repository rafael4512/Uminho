package ourrobots.F3;

import ourrobots.F3.Basic.PokeFunctions;
import ourrobots.F3.Utils.Message;
import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A MiniBot melee specialist. Uses Minimum Risk movement and Shadow/Melee Gun.
 * Movement based on its little brother, BlitzBat.
 */

public class BrokenSword extends PokeFunctions {
    private static final double TWO_PI = Math.PI * 2;

    private static Rectangle2D.Double _battleField;
    private static Point2D.Double _destination;
    private static String _nearestName;
    private static double _nearestDistance;
    private static final Map<String, EnemyData> _enemies =
            new HashMap<>();
    private static List<Point2D.Double> _recentLocations;

    public static double absoluteBearing(
            Point2D.Double source, Point2D.Double target) {
        return Math.atan2(target.x - source.x, target.y - source.y);
    }

    public static Point2D.Double project(Point2D.Double sourceLocation,
                                         double angle, double length) {
        return new Point2D.Double(
                sourceLocation.x + Math.sin(angle) * length,
                sourceLocation.y + Math.cos(angle) * length);
    }

    public static double square(double x) {
        return x * x;
    }

    public void run() {
        broadcastName();
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setColors(Color.black, Color.black, new Color(141, 220, 175));

        _battleField = new Rectangle2D.Double(50, 50,
                getBattleFieldWidth() - 100, getBattleFieldHeight() - 100);
        _recentLocations = new ArrayList<>();
        _nearestDistance = Double.POSITIVE_INFINITY;
        _destination = null;

        do {
            Point2D.Double myLocation = myLocation();
            _recentLocations.add(0, myLocation);

            //***********************************************************************
            // Gun
            double bulletPower = 3 - ((20 - getEnergy()) / 6);
            if (getGunTurnRemaining() == 0) {
                setFire(bulletPower);
            }

            List<MeleeFiringAngle> firingAngles = new ArrayList<>();
            for (EnemyData enemyData : _enemies.values()) {
                if (enemyData.alive) {
                    double enemyDistance = enemyData.distance(myLocation);
                    int bulletTicks =
                            (int) (enemyDistance / Rules.getBulletSpeed(bulletPower));
                    for (Point2D.Double vector : enemyData.lastVectors) {
                        if (vector != null) {
                            Point2D.Double projectedLocation = project(enemyData,
                                    enemyData.heading + vector.x, vector.y * bulletTicks);
                            if (_battleField.contains(projectedLocation)) {
                                firingAngles.add(new MeleeFiringAngle(
                                        absoluteBearing(myLocation, projectedLocation),
                                        enemyDistance, 18 / enemyDistance));
                            }
                        }
                    }
                }
            }

            try {
                double bestDensity = 0;
                for (int x = 0; x < 160; x++) {
                    double angle = Math.PI * x / 80;
                    double density = 0;
                    for (MeleeFiringAngle meleeAngle : firingAngles) {
                        double ux =
                                Math.abs(Utils.normalRelativeAngle(angle - meleeAngle.angle))
                                        / meleeAngle.bandwidth;
                        if (ux < 1) {
                            density += square(1 - square(ux)) / meleeAngle.distance;
                        }
                    }
                    if (density > bestDensity) {
                        bestDensity = density;
                        setTurnGunRightRadians(
                                Utils.normalRelativeAngle(angle - getGunHeadingRadians()));
                    }
                }
            } catch (NullPointerException npe) {
                // expected before any scans
            }

            //***********************************************************************
            // Movement
            double bestRisk;
            try {
                bestRisk = evalDestinationRisk(_destination) * .85;
            } catch (NullPointerException ex) {
                bestRisk = Double.POSITIVE_INFINITY;
            }
            try {
                for (double d = 0; d < TWO_PI; d += 0.1) {
                    Point2D.Double newDest = project(myLocation, d,
                            Math.min(_nearestDistance, 100 + Math.random() * 500));
                    double thisRisk = evalDestinationRisk(newDest);
                    if (_battleField.contains(newDest) && thisRisk < bestRisk) {
                        bestRisk = thisRisk;
                        _destination = newDest;
                    }
                }

                double angle = Utils.normalRelativeAngle(
                        absoluteBearing(myLocation, _destination) - getHeadingRadians());
                setTurnRightRadians(Math.tan(angle));
                setAhead(Math.cos(angle) * Double.POSITIVE_INFINITY);
            } catch (NullPointerException ex) {
                // expected before we have a _destination
            }

            //***********************************************************************
            // Radar
            setTurnRadarRightRadians(1);
            try {
                long stalestTime = Long.MAX_VALUE;
                for (EnemyData enemyData : _enemies.values()) {
                    if (getTime() > 20 && enemyData.alive
                            && enemyData.lastScanTime < stalestTime) {
                        stalestTime = enemyData.lastScanTime;
                        setTurnRadarRightRadians(Math.signum(Utils.normalRelativeAngle(
                                absoluteBearing(myLocation, enemyData)
                                        - getRadarHeadingRadians())));
                    }
                }
            } catch (NullPointerException npe) {
                // expected before we have any scans
            }
            //***********************************************************************
            execute();
        } while (true);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        double distance = e.getDistance();
        String botName = e.getName();

        if (isTeammate(botName)) return;

        if (!_enemies.containsKey(botName)) {
            _enemies.put(botName, new EnemyData());
        }

        DisplacementTimer timer;
        addCustomEvent(timer = new DisplacementTimer());
        EnemyData enemyData = timer.enemyData = _enemies.get(botName);
        enemyData.energy = e.getEnergy();
        enemyData.alive = true;
        enemyData.lastScanTime = getTime();

        timer.displacementVector = (enemyData.lastVectors = enemyData.gunVectors
                [(int) (distance / 300)]
                [(int) (Math.abs(e.getVelocity()) / 4)])
                [enemyData.nextIndex++ % 200] = new Point2D.Double(0, 0);

        enemyData.setLocation(timer.targetLocation = project(
                myLocation(), e.getBearingRadians() + getHeadingRadians(),
                distance));

        timer.bulletTicks = (int) (distance / 11);
        timer.targetHeading = enemyData.heading = e.getHeadingRadians()
                + (e.getVelocity() < 0 ? Math.PI : 0);

        if (distance < _nearestDistance || botName.equals(_nearestName)) {
            _nearestDistance = distance;
            _nearestName = botName;
        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        if (isTeammate(e.getName())) return;

        _enemies.get(e.getName()).alive = false;
        _nearestDistance = Double.POSITIVE_INFINITY;
    }

    private double evalDestinationRisk(Point2D.Double destination) {
        double risk = 0;

        for (EnemyData enemy1 : _enemies.values()) {
            double distSq = enemy1.distanceSq(destination);
            int closer = 0;
            for (EnemyData enemy2 : _enemies.values()) {
                if (enemy1.distanceSq(enemy2) < distSq) {
                    closer++;
                }
            }

            java.awt.geom.Point2D.Double myLocation = myLocation();
            risk += Math.max(0.5, Math.min(enemy1.energy / getEnergy(), 2))
                    * (1 + Math.abs(Math.cos(absoluteBearing(myLocation, destination)
                    - absoluteBearing(myLocation, enemy1))))
                    / closer
                    / distSq
                    / (200000 + destination.distanceSq(
                    getBattleFieldWidth() / 2, getBattleFieldHeight() / 2));
        }

        for (int x = 1; x < 6; x++) {
            try {
                risk *= 1 + (500 / x
                        / _recentLocations.get(x * 10).distanceSq(destination));
            } catch (Exception ex) {
                // ok
            }
        }

        return risk;
    }

    @Override
    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message) {
            Message a = (Message) e.getMessage();
            if(a.getAction().equals("Inform.Name"))
                this.addTeammate(e.getSender());
            else if(a.getAction().equals("Inform.TeamInfo"))
                this.addTeamInfo(a.getRobotInfo());
            else if(a.getAction().equals("Inform.EnemyInfo"))
                this.addEnemyInfo(a.getRobotInfo());
        }
    }

    private Point2D.Double myLocation() {
        return new Point2D.Double(getX(), getY());
    }

    public static class EnemyData extends Point2D.Double {
        public double energy;
        public boolean alive;
        public Point2D.Double[][][] gunVectors = new Point2D.Double[5][5][200];
        public Point2D.Double[] lastVectors;
        public int nextIndex = 0;
        public double heading;
        public long lastScanTime;
    }

    public static class MeleeFiringAngle {
        public double angle;
        public double distance;
        public double bandwidth;

        public MeleeFiringAngle(double angle, double distance, double bandwidth) {
            this.angle = angle;
            this.distance = distance;
            this.bandwidth = bandwidth;
        }
    }

    public class DisplacementTimer extends Condition {
        EnemyData enemyData;
        Point2D.Double targetLocation;
        double targetHeading;
        Point2D.Double displacementVector;
        int bulletTicks;
        int timer;

        public boolean test() {
            if (++timer > bulletTicks && enemyData.alive) {
                displacementVector.setLocation(
                        absoluteBearing(targetLocation, enemyData) - targetHeading,
                        targetLocation.distance(enemyData) / bulletTicks);
                removeCustomEvent(this);
            }
            return false;
        }
    }
}