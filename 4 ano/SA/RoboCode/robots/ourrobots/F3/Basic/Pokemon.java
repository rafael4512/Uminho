package ourrobots.F3.Basic;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.Point;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.ArrayList;

public class Pokemon extends PokeFunctions {

    ArrayList<Point> positions = new ArrayList<>();


    public void run() {
        broadcastName();
        setColors(new Color(255, 210, 0), Color.BLACK, Color.WHITE);
        setBulletColor(Color.YELLOW);
        //addCustomEvent(ownPositionRefresher);

        getPositions();

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        while (getTeam().size() < 4) execute();

        id = getId();
        System.out.println("ID: " + id);
        //Point goTo = positions.get(id-1);
        //System.out.println(goTo);

        while (enemyToStalk == null) execute();

        turnRadarRight(Double.POSITIVE_INFINITY);

        while (true) {
            if (isSearchingForMeat)
                turnRadarRight(Double.POSITIVE_INFINITY);

            if (getRadarTurnRemaining() == 0.0)
                incrementCounter();

            if (enemyToStalkPoint != null) {
                Point goTo = getMyGoTo(id, enemyToStalkPoint, 5, 200, false);
                if (goTo.distance(new Point(this.getX(), this.getY())) > 0.001) {
                    System.out.println("Run: " + goTo);
                    move(goTo);
                }
            }
            execute();
        }
    }

    private void getPositions() {
        double x = getBattleFieldWidth();
        double y = getBattleFieldHeight();

        positions.add(new Point(x / 4, y / 4));
        positions.add(new Point(x / 4, 3 * y / 4));
        positions.add(new Point(3 * x / 4, y / 4));
        positions.add(new Point(3 * x / 4, 3 * y / 4));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        String name = e.getName();

        if (name.equals(enemyToStalk))
            stalkEnemy(e);

    }


    @Override
    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message) {
            Message a = (Message) e.getMessage();
            if (a.getAction().equals("Inform.Name"))
                this.addTeammate(e.getSender());
            else if (a.getAction().equals("Inform.TeamInfo"))
                this.addTeamInfo(a.getRobotInfo());
            else if (a.getAction().equals("Inform.EnemyInfo"))
                this.addEnemyInfo(a.getRobotInfo());
            else if (a.getAction().equals("Inform.EnemyDroid"))
                this.addEnemyDroid(a.getName());
            else if (a.getAction().equals("Inform.EnemyLeader"))
                this.enemyLeader = a.getName();
        }
        else if (e.getMessage() instanceof ArrayList) {
            ArrayList<String> l = (ArrayList<String>) e.getMessage();
            enemies.addAll(l);
            System.out.println("Enemies: " + enemies);
            enemyToStalk = l.get(0);
            System.out.println("EnemyToStalk: " + enemyToStalk);
        }
    }
}
