package ourrobots.F3.Evolutions;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.Point;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.util.ArrayList;

public class Pokemon extends PokeFunctions {

    public void run() {
        broadcastName();
        setColors(new Color(255, 210, 0), Color.BLACK, Color.WHITE);
        setBulletColor(Color.YELLOW);
        addCustomEvent(ownPositionRefresher);

        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        numEnemies = getOthers() - 4;

        while (getTeam().size() < 4) execute();

        id = getId();
        //System.out.println("ID: " + id);

        while (enemyToStalk == null) execute();

        turnRadarRight(Double.POSITIVE_INFINITY);

        while (true) {
            if (isSearchingForMeat)
                turnRadarRight(Double.POSITIVE_INFINITY);

            if (getRadarTurnRemaining() == 0.0)
                incrementCounter();

            if (enemyToStalkPoint != null) {
                Point goTo;
                if (id % 2 == 0) goTo = getMyGoTo(id, enemyToStalkPoint, 3, 150, true);
                else if(enemies.size()<=1) goTo = getMyGoTo(id, enemyToStalkPoint, 5, 150, false);
                else goTo = getMyGoTo(id, enemyToStalkPoint, 2, 150, true);

                if (goTo.distance(new Point(this.getX(), this.getY())) > 0.001) {
                    //System.out.println("Run: " + goTo);
                    setMove(goTo);
                }
            }
            scan();
            execute();
        }
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        if (e.getName().equals(enemyToStalk)) stalkEnemy(e);
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
            //System.out.println("Enemies: " + enemies);
            if (id % 2 == 0) enemyToStalk = l.get(0);
            else enemyToStalk = l.get(numEnemies - 1);
            //System.out.println("EnemyToStalk: " + enemyToStalk);
        }
    }
}
