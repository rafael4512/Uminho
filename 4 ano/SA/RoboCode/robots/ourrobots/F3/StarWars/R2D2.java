package ourrobots.F3.StarWars;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.Point;
import ourrobots.F3.Utils.RobotInfo;
import ourrobots.F3.Utils.TeamFunctions;
import robocode.*;

import java.awt.*;

public class R2D2 extends TeamFunctions {
    private int hit_by_friendly_fire = 0;
    private int id;

    public void run() {
        broadcastName();
        setColors(Color.WHITE, Color.BLUE, Color.RED); // body,gun,radar
        setBulletColor(Color.RED);
        addCustomEvent(ownPositionRefresher);

        setAdjustRadarForGunTurn(true);
        turnRadarRight(Double.POSITIVE_INFINITY);

        id = getId();

        while (enemyToStalk == null) execute();

        System.out.println(enemyToStalk);

        while (true) {
            if (isSearchingForMeat)
                turnRadarRight(Double.POSITIVE_INFINITY);

            scan();

            //System.out.println(enemyLeader);

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
        if (name.equals(enemyToStalk)) {
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
            else if (a.getAction().equals("Inform.EnemyToStalk"))
                this.enemyToStalk = a.getName();
            else if (a.getAction().equals("Inform.EnemyInfo"))
                this.addEnemyInfo(a.getRobotInfo());
            else if (a.getAction().equals("Inform.EnemyDroid"))
                this.addEnemyDroid(a.getName());
            else if (a.getAction().equals("Inform.EnemyLeader"))
                this.enemyLeader = a.getName();
            else if (a.getAction().equals("Inform.StalkPair"))
                this.addStalkPair(a.getName(), a.getRobotInfo().getName());

        }
    }

    public void onHitRobot(HitRobotEvent e) {

        if (!isTeammate(e.getName())) {
            double absoluteHitAngle = (this.getHeading() + e.getBearing() + 360) % 360;
            turnGunToFast(absoluteHitAngle);

            //Taking into account the turn angle to calculate fire power
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
