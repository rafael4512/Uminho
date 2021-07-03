package ourrobots.F3_Final.UltraMegaEvo2;

import ourrobots.F3_Final.Utils.Point;
import ourrobots.F3_Final.Utils.TeamFunctions;
import robocode.*;

import java.awt.*;

public abstract class PokeFunctions extends TeamFunctions {
    protected int id;
    protected int numEnemies;
    protected Point enemyToStalkPoint = null;

    protected Point getMyGoTo(int id, Point enemy, int no_Robots, double distance) {
        double angle_from_center = (Math.toDegrees(Math.atan2(enemy.getX() * 2 - getBattleFieldWidth(), enemy.getY() * 2 - getBattleFieldHeight())) + 360) % 360;
        double angle_from_center_inner = (angle_from_center + 180) % 360;

        double convert_360_to_quadrant = angle_from_center % 90;
        double max_angle_for_opening = Math.abs(45 - convert_360_to_quadrant); // returns [0, 45] -> 45º - corner -> 90º formation; 0º -> 180º; 20º

        Point p;
        double a;
        if(id == 1 && no_Robots == 2) {
            a = angle_from_center_inner - 45 - max_angle_for_opening;
            p = calculateFinalPoint(enemy, distance, a, 0);
        }
        else if(id == 3 && no_Robots == 2) {
            a = angle_from_center_inner + 45 + max_angle_for_opening;
            p = calculateFinalPoint(enemy, distance, a, 0);
        }
        else {
            a = angle_from_center_inner - 45 + (22.5 * id) - max_angle_for_opening * (2 - id) / 2;
            p = calculateFinalPoint(enemy, distance, a, 0);
        }
        // System.out.println("Angulo: " + a + ", id: " + id + ", no_robots: " + no_Robots);

        //p.oscillate(angle_from_center);

        return p;
    }

    protected void stalkEnemy(ScannedRobotEvent e) {
        long time = getTime();

        double e_bearing = e.getBearing();
        double heading = this.getHeading();

        double absoluteBearing = (this.getHeading() + e_bearing + 360) % 360;
        turnRadarTo(absoluteBearing);

        Point my_position = new Point(getX(), getY());
        double e_velocity = e.getVelocity();

        Point e_position = calculateFinalPoint(my_position, e.getDistance(), e_bearing, heading);

        double distance = my_position.distance(e_position);
        double power;
        if (e_velocity == 0) power = 3;
        else
            power = Math.min(0.15 + 3 / (1 + Math.pow(((distance - 36) / 169), 3.88)), getEnergy() / 2);

        power = Math.min(power, this.getEnergy() / 2);
        power = Math.min(3, power);

        Point myPosition = new Point(this.getX(), this.getY());
        enemyToStalkPoint = predictEnemyFuturePosition(myPosition, e_position, e, power, time, true);

        if (enemyToStalkPoint != null) {
            Point goTo;
            if (id % 2 == 0)
                goTo = getMyGoTo(id, enemyToStalkPoint, 3, 125);
            else if (enemies.contains(enemyToStalk) && enemies.size() == 1)
                goTo = getMyGoTo(id, enemyToStalkPoint, 5, 125);
            else if (enemyDroids.contains(enemyToStalk) && enemyDroids.size() == 1)
                goTo = getMyGoTo(id, enemyToStalkPoint, 5, 125);
            else
                goTo = getMyGoTo(id, enemyToStalkPoint, 2, 125);

            specialMove(goTo);

            double distanceToGoTo = myPosition.distance(goTo);
            Point myFuturePosition = predictMyFuturePosition(myPosition, goTo, Math.min(1, Rules.MAX_VELOCITY/distanceToGoTo)*0.2);
            enemyToStalkPoint = predictEnemyFuturePosition(myFuturePosition, e_position, e, power, time, false);

            turnGunTo(enemyToStalkPoint);

            if (this.getGunHeat() <= 0 && power > 0)
                fire(power);
        }
    }

    private Point predictMyFuturePosition(Point p1, Point p2, double alpha) {
        return new Point((1 - alpha) * p1.getX() + alpha * p2.getX(), (1 - alpha) * p1.getY() + alpha * p2.getY());
    }

    public double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    @Override
    public void onHitRobot(HitRobotEvent e) {
        if (isTeammate(e.getName())) {
            setTurnLeft(40);
            if(Math.abs(e.getBearing()) < 90)
                back(40);
            else
                ahead(40);
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
