package ourrobots.F3.Evolutions;

import ourrobots.F3.Utils.Point;
import ourrobots.F3.Utils.RobotInfo;
import ourrobots.F3.Utils.TeamFunctions;
import robocode.*;

import java.awt.*;
import java.util.ArrayList;


public abstract class PokeFunctions extends TeamFunctions {
    protected int hit_by_friendly_fire = 0;
    protected int id;
    protected int numEnemies;
    protected Point enemyToStalkPoint = null;
    protected double distanceToShoot = 300;
    protected int haventseenmyenemycounter = 0;


    protected Point getMyGoTo(int id, Point enemy, int no_Robots, double distance, boolean isDividing) {
        double angle_from_center = (Math.toDegrees(Math.atan2(enemy.getX() * 2 - getBattleFieldWidth(), enemy.getY() * 2 - getBattleFieldHeight())) + 360) % 360;
        int k = (int) angle_from_center / 90;

        double angle;
        double step = 90.0 / (no_Robots - 1);

        double stepByAngle = isDividing ? (id / 2) * step : id * step;
        //System.out.println("Step by angle: " + stepByAngle);

        if (k % 2 == 1)
            angle = -stepByAngle + (270 + k * 90.0) % 360;
        else
            angle = stepByAngle + (180 + k * 90.0) % 360;

        return calculateFinalPoint(enemy, distance, angle, 0);
    }

    protected void moveAvoidingTeammates(Point target) {
        Point myPosition = new Point(getX(), getY());
        double angleToTargetPoint = calculateAngleFrom2Positions(myPosition, target);
        double distanceToTargetPoint = myPosition.distance(target);

        double maxDistance = distanceToTargetPoint;
        double forbiddenAngle = 0;
        double forbiddenThreshold = 0;
        boolean freeway = true;

        ArrayList<RobotInfo> teamCopy = new ArrayList<>(getTeamInfo().values());
        for (RobotInfo teammate : teamCopy) {
            double distanceToTeammate = myPosition.distance(teammate.getPosition());
            double angleToTeammate = calculateAngleFrom2Positions(myPosition, teammate.getPosition());
            double threshold = distanceToTeammate > 36 ? Math.toDegrees(Math.atan(52.0 / (distanceToTeammate - 36))) : 90;

            if (((angleToTargetPoint + threshold) % 360 > angleToTeammate) && (angleToTargetPoint < (angleToTeammate + threshold) % 360) && distanceToTeammate < maxDistance) {
                freeway = false;
                maxDistance = distanceToTeammate;
                forbiddenAngle = angleToTeammate;
                forbiddenThreshold = threshold;
            }
        }
        Point intermediate;

        if (!freeway && distanceToTargetPoint - maxDistance < 100 && maxDistance < 60) {
            double difference = forbiddenAngle - angleToTargetPoint;
            if (difference < -180) difference += 360;
            else if (difference > 180) difference -= 360;

            // friend is to the right
            if (difference > 0) angleToTargetPoint = forbiddenAngle - forbiddenThreshold;
                // friend is to the left
            else angleToTargetPoint = forbiddenAngle + forbiddenThreshold;

            intermediate = calculateFinalPoint(myPosition, maxDistance, angleToTargetPoint, 0);
        }
        else if (!freeway) intermediate = calculateFinalPoint(myPosition, maxDistance - 60, angleToTargetPoint, 0);
        else intermediate = target;

        move(intermediate);
    }

    protected void stalkEnemyAvoidingFriendlyFire(ScannedRobotEvent e) {
        isSearchingForMeat = false;
        haventseenmyenemycounter = 0;

        double e_bearing = e.getBearing();
        double heading = this.getHeading();

        double absoluteBearing = (this.getHeading() + e_bearing + 360) % 360;
        turnRadarTo(absoluteBearing);

        Point my_position = new Point(getX(), getY());
        double e_heading = e.getHeading();
        double e_velocity = e.getVelocity();

        Point e_position = calculateFinalPoint(my_position, e.getDistance(), e_bearing, heading);

        double distance = my_position.distance(e_position);
        double power;
        if (e_velocity == 0)
            power = 3;
        else
            power = 3 - (((distance - 36) / distanceToShoot) * 3);

        enemyToStalkPoint = predictFuturePosition(e_position, e_heading, e_velocity, Rules.getBulletSpeed(power), this.getTime() - 1);

        double angleToEnemy = calculateAngleFrom2Positions(my_position, enemyToStalkPoint);
        boolean noneInFront = true;

        ArrayList<RobotInfo> teamCopy = new ArrayList<>(getTeamInfo().values());
        for (RobotInfo teammate : teamCopy) {
            double distanceToTeammate = my_position.distance(teammate.getPosition());
            double threshold = distanceToTeammate > 36 ? Math.atan(26.0 / (distanceToTeammate - 36)) : 45;

            double angleToTeammate = calculateAngleFrom2Positions(my_position, teammate.getPosition());

            if ((angleToEnemy > (angleToTeammate - threshold)) && (angleToEnemy < (angleToTeammate + threshold)) && distanceToTeammate < distance) {
                noneInFront = false;
                break;
            }
        }

        if (this.getGunHeat() <= 0 && power > 0 && noneInFront) {
            turnGunTo(enemyToStalkPoint);
            fire(Math.min(power, this.getEnergy() / 2));
        }
    }

    protected void stalkEnemy(ScannedRobotEvent e) {
        isSearchingForMeat = false;
        haventseenmyenemycounter = 0;

        double e_bearing = e.getBearing();
        double heading = this.getHeading();

        double absoluteBearing = (this.getHeading() + e_bearing + 360) % 360;
        turnRadarTo(absoluteBearing);

        Point my_position = new Point(getX(), getY());
        double e_heading = e.getHeading();
        double e_velocity = e.getVelocity();

        Point e_position = calculateFinalPoint(my_position, e.getDistance(), e_bearing, heading);

        double distance = my_position.distance(e_position);
        double power;
        if (e_velocity == 0)
            power = 3;
        else
            power = 3 - (distance * distance) * 3 / (distanceToShoot * distanceToShoot);

        enemyToStalkPoint = predictFuturePosition(e_position, e_heading, e_velocity, Rules.getBulletSpeed(power), this.getTime() - 1);

        if (this.getGunHeat() <= 0 && power > 0) {
            turnGunTo(enemyToStalkPoint);
            fire(Math.min(power, this.getEnergy() / 2));
        }

    }

    private double calculateAngleFrom2Positions(Point point1, Point point2) {
        return (Math.toDegrees(Math.atan2(point2.getX() - point1.getX(), point2.getY() - point1.getY())) + 360) % 360;
    }

    protected void incrementCounter() {
        haventseenmyenemycounter++;
        //System.out.println("Haven'tSeen: " + haventseenmyenemycounter);
        if (haventseenmyenemycounter > 1) {
            isSearchingForMeat = true;
            haventseenmyenemycounter = 0;
        }
    }

    public double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        if (isTeammate(e.getName()) && Math.abs(e.getBearing()) < 90) {
            setTurnLeft(40);
            back(20);
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        String name = e.getName();
        if (!isTeammate(name)) {
            numEnemies--;
            enemies.remove(name);
            if (!enemies.isEmpty()) {
                if (id % 2 == 0) enemyToStalk = enemies.get(0);
                else enemyToStalk = enemies.get(numEnemies - 1);
            }
            isSearchingForMeat = true;
            //System.out.println("New EnemyToStalk: " + enemyToStalk);

        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        if (isTeammate(event.getName()))
            hit_by_friendly_fire++;
    }

    @Override
    public void onDeath(DeathEvent event) {
        //System.out.println(hit_by_friendly_fire);
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        //System.out.println(hit_by_friendly_fire);
    }

    @Override
    public void onWin(WinEvent event) {
        while (true) {
            setColors(new Color((int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255)),
                    new Color((int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255)),
                    new Color((int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255), (int) getRandomNumber(0, 255)));
            setTurnRight(30);
            turnGunLeft(30);
        }
    }


}
