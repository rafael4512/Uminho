package ourrobots.F3.StarWars;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.Point;
import ourrobots.F3.Utils.RobotInfo;
import ourrobots.F3.Utils.TeamFunctions;
import robocode.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Skywalker extends TeamFunctions {

    private final ArrayList<String> enemies = new ArrayList<>();
    private final Color[] sabers = new Color[]{new Color(0, 255, 128), Color.RED, Color.BLUE};
    private int saberIterator = 0;
    private int hit_by_friendly_fire = 0;
    private int id;

    public void run() {
        broadcastName();
        setColors(Color.BLACK, Color.RED, new Color(0, 255, 128)); // body,gun,radar
        setAdjustRadarForGunTurn(true);

        addCustomEvent(ownPositionRefresher);

        while (enemies.size() < 5)
            turnRadarRight(45);

        while (getTeam().size() < 4) execute();

        id = getId();

        enemyToStalk = enemies.get(0);

        this.addStalkPair(this.getName(), enemyToStalk);
        broadcastStalkPair(this.getName(), enemyToStalk);

        System.out.println(enemyToStalk);

        int i = 1;
        for (String teammate : getTeam()) {
            String newEnemy = enemies.get(i++);

            Message a = new Message("Inform.EnemyToStalk");
            a.setName(newEnemy);
            try {
                sendMessage(teammate, a);
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.addStalkPair(teammate, newEnemy);
            this.broadcastStalkPair(teammate, newEnemy);
        }

        turnRadarRight(Double.POSITIVE_INFINITY);

        while (true) {
            if (isSearchingForMeat)
                turnRadarRight(Double.POSITIVE_INFINITY);

            scan();

            // Código para perseguir um inimigo
            /*
            RobotInfo ri = getEnemyToKill();
            Point position = getMyPosition(id, ri);
            move(position);
            System.out.println(ri.getName());
            */


            if (this.getGunHeat() <= 0) {
                double power = 2;
                //turnGunTo(predictFuturePosition(ri, Rules.getBulletSpeed(power)));
                //fire(power);

                Point my_position = new Point(getX(), getY());


                RobotInfo bestRI = this.getEnemiesInfo().values().iterator().next();
                Point position_of_best = predictFuturePosition(bestRI, 20 - (3 * power));
                double best_distance = position_of_best.distance(my_position);

                for (RobotInfo enemy : this.getEnemiesInfo().values()) {
                    Point enemy_position = predictFuturePosition(enemy, 20 - (3 * power));
                    double distance_to_enemy = enemy_position.distance(my_position);

                    if (distance_to_enemy < best_distance) {
                        boolean flag = true;
                        for (RobotInfo friend : getTeamInfo().values()) {
                            double threshold = Math.atan(26 / friend.getPosition().distance(my_position));

                            if ((Math.toDegrees(Math.atan((friend.getPosition().getY() - getY()) / (friend.getPosition().getX() - getX()))) + (-enemy.getHeading() + 450) + threshold) % 360 < threshold * 2) {
                                System.out.println("Saved " + friend.getName());
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            bestRI = enemy;
                            best_distance = distance_to_enemy;
                        }
                    }
                }
                turnGunTo(predictFuturePosition(bestRI, Rules.getBulletSpeed(power)));
                fire(power);
            }
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        String name = e.getName();

        if (!isTeammate(name) && !enemies.contains(name) && enemies.size() < 5) {
            // isSearchingForMeat = false;
            enemies.add(name);
            if ((e.getEnergy() > 105 && e.getEnergy() <= 140) || (e.getEnergy() > 205)) {
                this.addEnemyDroid(name);
                broadcastEnemyDroid(name);
            }
            if (e.getEnergy() > 160) {
                this.enemyLeader = name;
                broadcastEnemyLeader(name);
            }
        }
        else if (name.equals(enemyToStalk)) {
            isSearchingForMeat = false;

            double absoluteBearing = (this.getHeading() + e.getBearing() + 360) % 360;
            turnRadarTo(absoluteBearing);

            RobotInfo ri = new RobotInfo(e.getName());
            ri.setPosition(calculateFinalPoint(new Point(this.getX(), this.getY()), e.getDistance(), e.getBearing(), this.getHeading()));
            ri.setHeading(e.getHeading());
            ri.setVelocity(e.getVelocity());
            ri.setEnergy(e.getEnergy());
            ri.setTime(this.getTime());

            broadcastEnemyInfo(ri);
            addEnemyInfo(ri);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message) {
            Message a = (Message) e.getMessage();
            if (a.getAction().equals("Inform.Name"))
                this.addTeammate(e.getSender());
            else if (a.getAction().equals("Inform.TeamInfo"))
                this.addTeamInfo(a.getRobotInfo());
            else if (a.getAction().equals("Inform.EnemyInfo"))
                this.addEnemyInfo(a.getRobotInfo());
            else if (a.getAction().equals("Inform.StalkPair"))
                this.addStalkPair(a.getName(), a.getRobotInfo().getName());
        }
    }

    public void onHitRobot(HitRobotEvent e) {

        if (!isTeammate(e.getName())) {
            double absoluteHitAngle = (this.getHeading() + e.getBearing() + 360) % 360;
            turnGunToFast(absoluteHitAngle);

            setBulletColor(sabers[saberIterator++ % 3]);

            //Taking into account the turn angle to calculate fire power or if turn at all
            fire(Math.min(getEnergy() / 2, 1));
        }
        else {
            setTurnLeft(30);
            back(10);
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
