package ourrobots.F3.Utils;

import robocode.RobotDeathEvent;
import robocode.Rules;
import robocode.TeamRobot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class TeamFunctions extends TeamRobot {

    protected final RefreshOwnPosition ownPositionRefresher = new RefreshOwnPosition("teamRefresher", this);
    protected final ArrayList<String> enemies = new ArrayList<>();
    protected final ArrayList<String> enemyDroids = new ArrayList<>();
    private final HashSet<String> teammates = new HashSet<>();
    private final HashSet<String> enemiesToStalk = new HashSet<>();
    private final HashMap<String, RobotInfo> teamInfo = new HashMap<>();
    private final HashMap<String, String> stalkPair = new HashMap<>();
    private final HashMap<String, RobotInfo> enemiesInfo = new HashMap<>();
    protected String enemyLeader = null;
    protected String enemyToStalk = null;
    protected boolean isSearchingForMeat = true;


    protected void move(Point p) {
        this.move(p.getX(), p.getY());
    }

    protected void move(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        turnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    protected void setMove(Point p) {
        this.setMove(p.getX(), p.getY());
    }

    protected void setMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        setTurnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.setAhead(distance);
    }

    protected void specialMove(Point p) {
        this.specialMove(p.getX(), p.getY());
    }

    protected void specialMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double required_angle = (Math.toDegrees(Math.atan2(dx, dy)) - getHeading() + 360) % 360;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (required_angle <= 90)
        {
            this.setTurnRight(required_angle);
            this.setAhead(distance);
        }
        else if(required_angle > 90 && required_angle <= 180)
        {
            this.setTurnRight(-(180-required_angle));
            this.setAhead(-distance);
        }
        else if(required_angle > 180 && required_angle <= 270)
        {
            this.setTurnRight(required_angle-180);
            this.setAhead(-distance);
        }
        else
        {
            this.setTurnRight(-(360 - required_angle));
            this.setAhead(distance);
        }
    }

    protected void turnSetMove(Point p) {
        this.turnSetMove(p.getX(), p.getY());
    }

    protected void turnSetMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        turnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.setAhead(distance);
    }

    protected void setTurnMove(Point p) {
        this.setTurnMove(p.getX(), p.getY());
    }

    protected void setTurnMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        setTurnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    protected void setAheadMove(Point p) {
        this.setAheadMove(p.getX(), p.getY());
    }

    protected void setAheadMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        turnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.setAhead(distance);
    }

    protected void turnTo(Point p) {
        this.turnTo(p.getX(), p.getY());
    }

    protected void turnTo(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading()) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if (turnAngle <= 180) turnRight(turnAngle);
        else turnLeft(360 - turnAngle);
    }

    protected void turnTo(double angle) {
        double required_angle = (angle - getHeading()) % 360;
        if (required_angle < 0) required_angle += 360;

        if (required_angle <= 180) turnRight(required_angle);
        else turnLeft(360 - required_angle);
    }

    protected void setTurnTo(double angle) {
        double required_angle = (angle - getHeading() + 360) % 360;

        if (required_angle <= 180) setTurnRight(required_angle);
        else setTurnLeft(360 - required_angle);
    }

    protected void setTurnTo(Point p) {
        this.setTurnTo(p.getX(), p.getY());
    }

    protected void setTurnTo(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading()) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if (turnAngle <= 180) setTurnRight(turnAngle);
        else setTurnLeft(360 - turnAngle);
    }

    protected void turnGunToFast(double angle) {
        double angleToShoot = (this.getGunHeading() - angle + 360) % 360;

        if (angleToShoot <= 180) {
            setTurnGunLeft(angleToShoot * 2 / 3);
            turnLeft(angleToShoot / 3);
        }
        else {
            setTurnGunRight((360 - angleToShoot) * 2 / 3);
            turnRight((360 - angleToShoot) / 3);
        }
    }

    protected void turnGunTo(Point target) {
        this.turnGunTo(target.getX(), target.getY());
    }

    protected void turnGunTo(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getGunHeading()) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if (turnAngle <= 180) turnGunRight(turnAngle);
        else turnGunLeft(360 - turnAngle);
    }

    protected void turnGunTo(double angle) {
        double turnAngle = (this.getGunHeading() - angle + 360) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if (turnAngle <= 180) turnGunLeft(turnAngle);
        else turnGunRight(360 - turnAngle);
    }

    protected void turnRadarTo(double angle) {
        double angleToScan = (this.getRadarHeading() - angle + 360) % 360;

        if (angleToScan <= 180) setTurnRadarLeft(angleToScan);
        else setTurnRadarRight(360 - angleToScan);
    }

    protected void turnRadarTo(Point p) {
        turnRadarTo(p.getX(), p.getY());
    }

    protected void turnRadarTo(double x, double y) {
        double dy = y - this.getY();
        double dx = x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dy, dx)) - (-this.getRadarHeading() + 450) % 360 + 360) % 360;

        if (turnAngle <= 180) setTurnRadarRight(turnAngle);
        else setTurnRadarLeft(360 - turnAngle);
    }

    protected Point calculateFinalPoint(Point initPoint, double distance, double angle, double heading) {
        double radian = Math.toRadians(heading + angle % 360);

        double newX = (initPoint.getX() + Math.sin(radian) * distance);
        double newY = (initPoint.getY() + Math.cos(radian) * distance);

        return new Point(newX, newY);
    }

    protected Point predictFuturePosition(RobotInfo ri, double bullet_Velocity) {
        return predictFuturePosition(ri.getPosition(), ri.getHeading(), ri.getVelocity(), bullet_Velocity, ri.getTime());
    }

    protected Point predictFuturePosition(Point enemy_position, double heading, double enemy_velocity, double bullet_Velocity, long time_seen) {
        return predictFuturePosition(enemy_position.getX(), enemy_position.getY(), heading, enemy_velocity, bullet_Velocity, time_seen);
    }

    protected Point predictFuturePosition(double x, double y, double heading, double velocity, double bulletVelocity, long time_seen) {
        double angle = ((-heading + 450) % 360) * Math.PI / 180;
        Point p = new Point(getX(), getY());
        double distance = new Point(x, y).distance(p);
        double time = distance / bulletVelocity + getTime() - time_seen;

        for (int i = 0; i < 3; i++) {
            Point approx = new Point(x + velocity * Math.cos(angle) * time, y + velocity * Math.sin(angle) * time);
            distance = approx.distance(p);
            time = distance / bulletVelocity + getTime() - time_seen;
        }

        return new Point(x + velocity * Math.cos(angle) * time, y + velocity * Math.sin(angle) * time);
    }

    protected Point predictFuturePosition2(double x, double y, double heading, double velocity, double bulletVelocity, long time_seen, double gunAngleBefore) {
        heading = ((-heading + 450) % 360);
        gunAngleBefore = ((-gunAngleBefore + 450) % 360);
        Point p = new Point(getX(), getY());

        double differenceGunAngle = gunAngleBefore - heading;
        if (differenceGunAngle < -180) differenceGunAngle += 360;
        else if (differenceGunAngle > 180) differenceGunAngle -= 360;
        differenceGunAngle = Math.abs(differenceGunAngle);
        double timeGunAngle = differenceGunAngle / Rules.GUN_TURN_RATE;

        heading *= Math.PI / 180;

        double distance = new Point(x, y).distance(p);
        double time = distance / bulletVelocity + getTime() - time_seen + timeGunAngle - 1;

        Point predictedPoint = new Point(x + velocity * Math.cos(heading) * time, y + velocity * Math.sin(heading) * time);

        double predictedX = predictedPoint.getX();
        double predictedY = predictedPoint.getY();

        if (predictedX < 18 || predictedX > getBattleFieldWidth() - 18 || predictedY < 18 || predictedY > getBattleFieldHeight() - 18) {
            double distanceSideWalls = Math.min(predictedX - 18, getBattleFieldWidth() - 18 - predictedX);
            double distanceUpperWalls = Math.min(predictedY - 18, getBattleFieldHeight() - 18 - predictedY);
            double distanceToWall = Math.min(distanceSideWalls, distanceUpperWalls) * velocity / Math.abs(velocity);

            predictedPoint = new Point(x + Math.cos(heading) * distanceToWall, y + Math.sin(heading) * distanceToWall);
        }

        return predictedPoint;
    }

    protected HashSet<String> getTeam() {
        return this.teammates;
    }

    public HashMap<String, RobotInfo> getTeamInfo() {
        return this.teamInfo;
    }

    public HashMap<String, RobotInfo> getEnemiesInfo() {
        return this.enemiesInfo;
    }

    public HashMap<String, String> getStalkPair() {
        return this.stalkPair;
    }

    protected void addTeammate(String name) {
        this.teammates.add(name);
    }

    protected void addTeamInfo(RobotInfo r) {
        this.teamInfo.put(r.getName(), r);
    }

    protected void addEnemyInfo(RobotInfo r) {
        this.enemiesInfo.put(r.getName(), r);
    }

    protected void addEnemyDroid(String name) {
        this.enemyDroids.add(name);
    }

    protected void addStalkPair(String teammate, String enemy) {
        this.stalkPair.put(teammate, enemy);
    }

    @Override
    public boolean isTeammate(String name) {
        return this.teammates.contains(name);
    }

    protected void broadcastName() {
        Message a = new Message("Inform.Name");
        try {
            broadcastMessage(a);
        } catch (IOException ignored) {
        }
    }

    protected void broadcastInfo() {
        Message a = new Message("Inform.TeamInfo");
        RobotInfo ri = new RobotInfo(this.getName());
        ri.setPosition(new Point(this.getX(), this.getY()));
        ri.setHeading(this.getHeading());
        ri.setVelocity(this.getVelocity());
        ri.setEnergy(this.getEnergy());
        ri.setTime(this.getTime());
        a.setRobotInfo(ri);
        try {
            broadcastMessage(a);
        } catch (IOException ignored) {
        }
    }

    protected void broadcastEnemyInfo(RobotInfo ri) {
        Message a = new Message("Inform.EnemyInfo");
        a.setRobotInfo(ri);
        try {
            broadcastMessage(a);
        } catch (IOException ignored) {
        }
    }

    protected void broadcastEnemies() {
        try {
            broadcastMessage(enemies);
        } catch (IOException ignored) {
        }
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

    protected void broadcastEnemyDroid(String name) {
        Message a = new Message("Inform.EnemyDroid");
        a.setName(name);
        try {
            broadcastMessage(a);
        } catch (IOException ignored) {
        }
    }

    protected void broadcastEnemyLeader(String name) {
        Message a = new Message("Inform.EnemyLeader");
        a.setName(name);
        try {
            broadcastMessage(a);
        } catch (IOException ignored) {
        }
    }

    protected void broadcastStalkPair(String name, String enemyToStalk) {
        Message m = new Message("Inform.StalkPair");
        m.setName(name);
        m.setRobotInfo(new RobotInfo(enemyToStalk));
        try {
            broadcastMessage(m);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected int getId() {
        String myName = getName();
        int id = 0;
        for (String s : this.getTeam()) {
            if (myName.compareTo(s) > 0) id++;
        }

        return id;
    }

    @Override
    public void onRobotDeath(RobotDeathEvent e) {
        String name = e.getName();
        if (isTeammate(name)) {
            // remove Teammate
            teammates.remove(name);
            String stalked = stalkPair.remove(name);

            // Try to find new enemyToStalk in enemiesToStalk if doesn't have one
            if (enemyToStalk == null && stalked != null && enemiesInfo.containsKey(stalked)) {
                // remove Enemy that he is stalking from enemiesInfo
                enemiesInfo.remove(stalked);
                // add enemy to enemiesToStalk
                enemiesToStalk.add(stalked);

                enemyToStalk = enemiesToStalk.iterator().next();
                System.out.println(enemyToStalk);
                this.stalkPair.put(this.getName(), stalked);
                broadcastStalkPair(this.getName(), stalked);
                isSearchingForMeat = true;
            }
        }
        else {
            // Remove from enemiesInfo
            enemiesInfo.remove(name);
            enemyDroids.remove(name);
            // Remove Enemy from enemiesToStalk
            enemiesToStalk.remove(name);

            HashMap<String, String> copy = (HashMap<String, String>) stalkPair.clone();
            for (Map.Entry<String, String> entry : copy.entrySet())
                if (entry.getValue().equals(name))
                    stalkPair.remove(entry.getKey());

            // Try to find new enemyToStalk in enemiesToStalk
            if (name.equals(enemyToStalk)) {
                if (!enemiesToStalk.isEmpty()) {
                    enemyToStalk = enemiesToStalk.iterator().next();
                    System.out.println(enemyToStalk);
                    enemiesToStalk.remove(enemyToStalk);
                    stalkPair.put(this.getName(), enemyToStalk);
                    broadcastStalkPair(this.getName(), enemyToStalk);
                    isSearchingForMeat = true;
                }
                else
                    enemyToStalk = null;
            }
        }
    }
}
