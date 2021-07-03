package ourrobots.F3_Final.Utils;

import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public abstract class TeamFunctions extends TeamRobot {
    protected final ArrayList<String> enemies = new ArrayList<>();
    protected final ArrayList<String> enemyDroids = new ArrayList<>();
    private final HashSet<String> teammates = new HashSet<>();
    protected String enemyToStalk = null;
    protected double oldEnemyHeading = -360;
    protected double oldEnemyVelocity = -360;

    protected void specialMove(Point p) {
        this.specialMove(p.getX(), p.getY());
    }

    protected void specialMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double required_angle = (Math.toDegrees(Math.atan2(dx, dy)) - getHeading() + 360) % 360;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (required_angle <= 90) {
            this.setTurnRight(required_angle);
            this.setAhead(distance);
        }
        else if (required_angle > 90 && required_angle <= 180) {
            this.setTurnRight(-(180 - required_angle));
            this.setAhead(-distance);
        }
        else if (required_angle > 180 && required_angle <= 270) {
            this.setTurnRight(required_angle - 180);
            this.setAhead(-distance);
        }
        else {
            this.setTurnRight(-(360 - required_angle));
            this.setAhead(distance);
        }
    }

    protected void turnGunTo(Point target) {
        this.turnGunTo(target.getX(), target.getY());
    }

    protected void turnGunTo(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getGunHeading() + 360) % 360;

        if (turnAngle <= 180) turnGunRight(turnAngle);
        else turnGunLeft(360 - turnAngle);
    }

    protected void turnRadarTo(double angle) {
        double angleToScan = (this.getRadarHeading() - angle + 360) % 360;

        if (angleToScan <= 180) setTurnRadarLeft(angleToScan);
        else setTurnRadarRight(360 - angleToScan);
    }

    protected Point calculateFinalPoint(Point initPoint, double distance, double angle, double heading) {
        double radian = Math.toRadians(heading + angle % 360);

        double newX = (initPoint.getX() + Math.sin(radian) * distance);
        double newY = (initPoint.getY() + Math.cos(radian) * distance);

        return new Point(newX, newY);
    }

    protected Point predictFuturePosition(Point enemy_position, double heading, double enemy_velocity, double bullet_Velocity, long time_seen) {
        return predictFuturePosition(enemy_position.getX(), enemy_position.getY(), heading, enemy_velocity, bullet_Velocity, time_seen);
    }

    protected Point predictFuturePosition(double x, double y, double heading, double velocity, double bulletVelocity, long time_seen) {
        double angle = ((-heading + 450) % 360) * Math.PI / 180;
        Point p = new Point(getX(), getY());
        double distance = new Point(x, y).distance(p);
        double time = distance / bulletVelocity + getTime() - time_seen;

        Point approx = new Point(x + velocity * Math.cos(angle) * time, y + velocity * Math.sin(angle) * time);
        distance = approx.distance(p);
        time = distance / bulletVelocity + getTime() - time_seen;

        return new Point(x + velocity * Math.cos(angle) * time, y + velocity * Math.sin(angle) * time);
    }

    protected Point predictEnemyFuturePosition(Point myPosition, Point enemyPosition, ScannedRobotEvent e, double bulletPower, long time, boolean firstTime) {
        double enemyX = enemyPosition.getX();
        double enemyY = enemyPosition.getY();
        double enemyHeading = e.getHeadingRadians();
        double enemyVelocity = e.getVelocity();


        if (oldEnemyHeading == -360) oldEnemyHeading = enemyHeading;
        if (oldEnemyVelocity == -360) oldEnemyVelocity = enemyVelocity;


        double enemyHeadingChange = enemyHeading - oldEnemyHeading;
        double enemyAccelerationChange = enemyVelocity - oldEnemyVelocity;

        if (enemyAccelerationChange > 0.5) enemyAccelerationChange = 1;
        else if (enemyAccelerationChange < -0.5) enemyAccelerationChange = -1;
        else enemyAccelerationChange = 0;

        if (Math.abs(enemyVelocity) > 7.5) enemyAccelerationChange = 0;

        if(!firstTime)
        {
            oldEnemyHeading = enemyHeading;
            oldEnemyVelocity = enemyVelocity;
        }

        double deltaTime = 0;
        double battleFieldHeight = getBattleFieldHeight(), battleFieldWidth = getBattleFieldWidth();
        double predictedX = enemyX, predictedY = enemyY;

        while((++deltaTime + time - getTime()) * (20.0 - 3.0 * bulletPower) < myPosition.distance(predictedX, predictedY)){
            predictedX += Math.sin(enemyHeading) * (enemyVelocity + enemyAccelerationChange);
            predictedY += Math.cos(enemyHeading) * (enemyVelocity + enemyAccelerationChange);
            enemyHeading += enemyHeadingChange;
            if(predictedX < 18.0 || predictedY < 18.0 || predictedX > battleFieldWidth - 18.0 || predictedY > battleFieldHeight - 18.0){
                predictedX = Math.min(Math.max(18.0, predictedX), battleFieldWidth - 18.0);
                predictedY = Math.min(Math.max(18.0, predictedY), battleFieldHeight - 18.0);
                break;
            }
        }

        return new Point(predictedX, predictedY);
    }



    protected HashSet<String> getTeam() {
        return this.teammates;
    }

    protected void addTeammate(String name) {
        this.teammates.add(name);
    }

    @Override
    public boolean isTeammate(String name) {
        return this.teammates.contains(name);
    }

    protected void broadcastName() {
        Message a = new Message("Inform.Name");
        try {
            broadcastMessage(a);
        } catch (IOException ignored) {}
    }

    protected void broadcastEnemiesWithDroids() {
        try {
            TwoArrayLists two = new TwoArrayLists();
            two.setArraylist1(enemies);
            two.setArraylist2(enemyDroids);
            broadcastMessage(two);
        } catch (IOException ignored) {
        }
    }

    protected int getId() {
        String myName = getName();
        int id = 0;
        for (String s : this.getTeam())
            if (myName.compareTo(s) > 0) id++;

        return id;
    }
}
