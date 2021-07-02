package ourrobots.F3.Evolutions;

import ourrobots.F3.Utils.Message;
import ourrobots.F3.Utils.Point;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;

public class PokeTrainer extends PokeFunctions {

    private final int haventseenmyenemycounter = 0;

    public void run() {
        broadcastName();
        setColors(Color.BLUE, Color.WHITE, Color.RED); // body,gun,radar
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        addCustomEvent(ownPositionRefresher);

        numEnemies = getOthers() - 4;
        //System.out.println("NumEnemies: " + numEnemies);

        while (enemies.size() < numEnemies)
            turnRadarRight(40);

        //System.out.println("Enemies: " + enemies);
        if(enemyLeader!=null) {
            enemies.remove(enemyLeader);
            enemies.add(0, enemyLeader);
        }

        broadcastEnemies();

        while (getTeam().size() < 4) execute();

        id = getId();
        //System.out.println("ID: " + id);
        //Point goTo = new Point(getBattleFieldWidth()/2,getBattleFieldHeight()/2);

        enemyToStalk = enemies.get(0);
        //System.out.println("EnemyToStalk: " + enemyToStalk);

        while (true) {
            if (isSearchingForMeat)
                turnRadarRight(Double.POSITIVE_INFINITY);

            if (getRadarTurnRemaining() == 0.0)
                incrementCounter();

            if (enemyToStalkPoint != null) {
                Point goTo = getMyGoTo(id, enemyToStalkPoint, 3, 150, true);
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
        String name = e.getName();
        if (!isTeammate(name) && !enemies.contains(name) && enemies.size() < numEnemies) {
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
        }
    }
}
