package ourrobots.F3.MegaEvolutions;

import ourrobots.F3.Utils.Message;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;

public class PokeTrainer extends PokeFunctions {

    public void run() {
        broadcastName();
        setColors(Color.BLUE, Color.WHITE, Color.RED); // body,gun,radar
        setBulletColor(Color.RED);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        numEnemies = getOthers() - 4;

        while ((enemies.size() + enemyDroids.size()) < numEnemies)
            turnRadarRight(40);

        broadcastEnemiesWithDroids();

        while (getTeam().size() < 4) execute();

        id = getId();
        //System.out.println("ID: " + id);

        enemyToStalk = enemies.get(0);
        //System.out.println("EnemyToStalk: " + enemyToStalk);

        while (true)
            turnRadarRight(Double.POSITIVE_INFINITY);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        String name = e.getName();
        if (!isTeammate(name) && !enemies.contains(name) && !enemyDroids.contains(name) && (enemies.size()+enemyDroids.size()) < numEnemies) {
            if ((e.getEnergy() > 105 && e.getEnergy() <= 140))
                this.enemyDroids.add(name);
            else if (e.getEnergy() > 140)
                enemies.add(0,name);
            else
                enemies.add(name);
        }
        if (name.equals(enemyToStalk))
            stalkEnemy(e);
    }

    @Override
    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message)
        {
            Message m = (Message) e.getMessage();

            if (m.getAction().equals("Inform.Name"))
                this.addTeammate(e.getSender());
        }
    }
}