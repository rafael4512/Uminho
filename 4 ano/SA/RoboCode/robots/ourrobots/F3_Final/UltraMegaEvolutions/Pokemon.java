package ourrobots.F3_Final.UltraMegaEvolutions;

import ourrobots.F3_Final.Utils.Message;
import ourrobots.F3_Final.Utils.TwoArrayLists;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;

import java.awt.*;

public class Pokemon extends PokeFunctions {

    public void run() {
        broadcastName();
        setColors(new Color(255, 210, 0), Color.BLACK, Color.WHITE);
        setBulletColor(Color.YELLOW);
        setAdjustRadarForGunTurn(true);
        setAdjustGunForRobotTurn(true);

        numEnemies = getOthers() - 4;

        while (getTeam().size() < 4) execute();

        id = getId();

        while (enemyToStalk == null) execute();

        while (true)
            turnRadarRight(Double.POSITIVE_INFINITY);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        if (e.getName().equals(enemyToStalk)) stalkEnemy(e);
    }

    @Override
    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Message) {
            Message m = (Message) e.getMessage();
            if (m.getAction().equals("Inform.Name"))
                this.addTeammate(e.getSender());
        }
        else if (e.getMessage() instanceof TwoArrayLists) {
            TwoArrayLists two = (TwoArrayLists) e.getMessage();
            enemies.addAll(two.getArraylist1());
            enemyDroids.addAll(two.getArraylist2());

            if (id % 2 == 0) enemyToStalk = enemies.get(0);
            else enemyToStalk = enemies.get(enemies.size() - 1);
        }
    }
}