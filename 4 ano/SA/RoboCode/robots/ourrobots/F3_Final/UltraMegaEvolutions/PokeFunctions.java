package ourrobots.F3_Final.UltraMegaEvolutions;

import ourrobots.F3_Final.Utils.Point;
import ourrobots.F3_Final.Utils.TeamFunctions;
import robocode.*;

import java.awt.*;

public abstract class PokeFunctions extends TeamFunctions {
    protected int id;
    protected int numEnemies;
    protected Point enemyToStalkPoint = null;

    /*
    protected Point getMyGoTo(int id, Point enemy, int no_Robots, double distance, boolean isDividing) {
        double angle_from_center = (Math.toDegrees(Math.atan2(enemy.getX() * 2 - getBattleFieldWidth(), enemy.getY() * 2 - getBattleFieldHeight())) + 360) % 360;
        int k = (int) angle_from_center / 90;

        double angle;
        double step = 90.0 / (no_Robots - 1);

        double stepByAngle = isDividing ? (id / 2) * step : id * step;

        if (k % 2 == 1) angle = -stepByAngle + (270 + k * 90.0) % 360;
        else angle = stepByAngle + (180 + k * 90.0) % 360;

        Point p = calculateFinalPoint(enemy, distance, angle, 0);

        if(p.getX() <= 16.1) p.setX(16.2);
        else if(p.getX() >= getBattleFieldWidth()-16.1) p.setX(getBattleFieldWidth()-16.2);
        if(p.getY() <= 16.1) p.setY(16.2);
        else if(p.getY() >= getBattleFieldHeight()-16.1) p.setY(getBattleFieldHeight()-16.2);

        return p;
    }*/


    protected Point getMyGoTo(int id, Point enemy, int no_Robots, double distance, boolean isDividing) {

        double angle_from_center = (Math.toDegrees(Math.atan2(enemy.getX() * 2 - getBattleFieldWidth(), enemy.getY() * 2 - getBattleFieldHeight())) + 360) % 360;
        double centerRobotAngle = (angle_from_center + 180) % 360;

        Point p;
        if(id == 1 && no_Robots == 2)
            p = calculateFinalPoint(enemy, distance, centerRobotAngle - 45, 0);
        else if(id == 3 && no_Robots == 2)
            p = calculateFinalPoint(enemy, distance, centerRobotAngle + 45, 0);
        else
            p = calculateFinalPoint(enemy, distance, centerRobotAngle - 45 + (22.5*id), 0);

        return p;
    }

    protected void stalkEnemy(ScannedRobotEvent e) {

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
        if (e_velocity == 0) power = 3;
        else
            power = Math.min(0.15 + (2.92523654) / (1 + Math.pow(((distance - 36) / 168.6142), 3.88031)), getEnergy() / 2);

        enemyToStalkPoint = predictFuturePosition(e_position, e_heading, e_velocity, Rules.getBulletSpeed(power), this.getTime());

        if (enemyToStalkPoint != null) {
            Point goTo;
            if (id % 2 == 0)
                goTo = getMyGoTo(id, enemyToStalkPoint, 3, 125, true);
            else if (enemies.contains(enemyToStalk) && enemies.size() == 1)
                goTo = getMyGoTo(id, enemyToStalkPoint, 5, 125, false);
            else if (enemyDroids.contains(enemyToStalk) && enemyDroids.size() == 1)
                goTo = getMyGoTo(id, enemyToStalkPoint, 5, 125, false);
            else
                goTo = getMyGoTo(id, enemyToStalkPoint, 2, 125, true);

            specialMove(goTo);

            turnGunTo(enemyToStalkPoint);
        }

        if (this.getGunHeat() <= 0 && power > 0)
            fire(Math.min(power, this.getEnergy() / 2));

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
        else if (isTeammate(e.getName()) && Math.abs(e.getBearing()) >= 90) {
            setTurnLeft(40);
            ahead(20);
        }
        else if (getGunHeat() <= 0) {
            double absoluteHitAngle = (this.getHeading() + e.getBearing() + 360) % 360;
            double turnAngle = (this.getGunHeading() - absoluteHitAngle + 360) % 360;

            if (turnAngle <= 180) {
                turnGunRight(-turnAngle);
                fire(Math.min(3.1 - turnAngle / 180, getEnergy() / 2));
            }
            else {
                turnGunRight(360 - turnAngle);
                fire(Math.min(3.1 - (360 - turnAngle) / 180, getEnergy() / 2));
            }
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        String name = e.getName();
        if (!isTeammate(name)) {
            numEnemies--;
            enemies.remove(name);
            enemyDroids.remove(name);
            if (!enemies.isEmpty()) {
                if (id % 2 == 0) enemyToStalk = enemies.get(0);
                else enemyToStalk = enemies.get(enemies.size() - 1);
            }
            else if (!enemyDroids.isEmpty()) {
                if (id % 2 == 0) enemyToStalk = enemyDroids.get(0);
                else enemyToStalk = enemyDroids.get(enemyDroids.size() - 1);
            }
        }
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
