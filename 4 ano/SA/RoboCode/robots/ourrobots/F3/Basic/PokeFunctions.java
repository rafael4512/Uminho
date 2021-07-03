package ourrobots.F3.Basic;

import ourrobots.F3.Utils.Point;
import ourrobots.F3.Utils.TeamFunctions;
import robocode.*;


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

        if (k % 2 == 1)
            angle = -stepByAngle + (270 + k * 90.0) % 360;
        else
            angle = stepByAngle + (180 + k * 90.0) % 360;


        return calculateFinalPoint(enemy, distance, angle, 0);
    }


    protected void stalkEnemy(ScannedRobotEvent e) {
        isSearchingForMeat = false;
        System.out.println("stalkEnemy Counter: " + haventseenmyenemycounter);
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

        if (this.getGunHeat() <= 0 && power > 0) {
            turnGunTo(enemyToStalkPoint);
            fire(Math.min(power, this.getEnergy() / 2));
        }

        //Point goTo = getMyGoTo(id,enemyToStalkPoint,5,200);
        //System.out.println("OnScannedGoTo: "+goTo);
        //move(goTo);
        //execute();
    }

    protected void incrementCounter() {
        haventseenmyenemycounter++;
        System.out.println("HaventSeen: " + haventseenmyenemycounter);
        if (haventseenmyenemycounter > 1) {
            isSearchingForMeat = true;
            haventseenmyenemycounter = 0;
        }
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
            enemies.remove(name);
            if (!enemies.isEmpty()) enemyToStalk = enemies.get(0);
            isSearchingForMeat = true;
            System.out.println("New EnemyToStalk: " + enemyToStalk);
            numEnemies--;
        }
    }


    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        if (isTeammate(event.getName()))
            hit_by_friendly_fire++;
    }

    @Override
    public void onDeath(DeathEvent event) {
        System.out.println(hit_by_friendly_fire);
    }

    @Override
    public void onRoundEnded(RoundEndedEvent event) {
        System.out.println(hit_by_friendly_fire);
    }
}
