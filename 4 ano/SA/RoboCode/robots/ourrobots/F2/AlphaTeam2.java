package ourrobots.F2;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.RobocodeFileOutputStream;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class AlphaTeam2 extends TeamFunctions {

    private ReadyCondition readyCondition;
    private final Color[] colors = new Color[]{Color.gray, Color.black, Color.white, Color.darkGray, Color.lightGray};
    private int id = -1;
    private ArrayList<Point> queuePath;
    private double goToX = 0;
    private double goToY = 0;

    public void run() {

        readyCondition = new ReadyCondition("nextStep", this, 4);
        addCustomEvent(readyCondition);

        calculateId();
        System.out.printf("Robot id: %d!\n", id);
        setColors(colors[id],colors[(id+1)%5],colors[(id+2)%5]); // body,gun,radar

        double height = getBattleFieldHeight();
        double width = getBattleFieldWidth();

        int sizePath = calculateQueuePath(height,width);
        System.out.println("SizePath: "+sizePath);

        if(id == 0) //
        {
            int iteration = 25;
            while(true)
            {
                readyCondition.setAllNotReady();

                Point goToPoint = queuePath.get(iteration % (queuePath.size()-1));
                goToX = goToPoint.getX();
                goToY = goToPoint.getY();
                this.move(goToX,goToY);

                Action move = new Action("Move", null, id, 1);
                move.setIndex(iteration-1);

                try {
                    this.broadcastMessage(move);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                waitFor(readyCondition);

                iteration++;
            }
        }
    }

    private int calculateQueuePath(double height, double width) {
        queuePath = new ArrayList<>();
        double danceDistance = 50;
        double x = width/2;
        double y = height/2;

        queuePath.add(new Point(x,y));
        while(y < height - danceDistance)
            queuePath.add(new Point(x += danceDistance, y += danceDistance));
        queuePath.add(new Point(x += danceDistance, y));
        while(y > danceDistance)
            queuePath.add(new Point(x, y -= danceDistance));
        queuePath.add(new Point(x -= danceDistance, y));
        while(y < height - danceDistance)
            queuePath.add(new Point(x -= danceDistance,y += danceDistance));
        queuePath.add(new Point(x -= danceDistance, y));
        while(y > danceDistance)
            queuePath.add(new Point(x,y -= danceDistance));
        queuePath.add(new Point(x += danceDistance, y));
        while(y < height/2)
            queuePath.add(new Point(x += danceDistance,y += danceDistance));

        return queuePath.size();
    }

    private void calculateId() {
        try {
            try (BufferedReader br = new BufferedReader(new FileReader(this.getDataFile("ids.txt")))) {
                id = Integer.parseInt(br.readLine());
            }
        } catch (IOException | NumberFormatException e) {
            id = 0;
        }

        try (PrintStream ps = new PrintStream(new RobocodeFileOutputStream(this.getDataFile("ids.txt")))) {
            ps.println((id + 1) % 5);
            if (ps.checkError())
                this.out.println("Enable to write id!");

        } catch (IOException e) {
            e.printStackTrace(this.out);
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Action) {
            Action a = (Action) e.getMessage();
            //System.out.println(a);

            if(a.getAction().equals("Move") && a.getTargetTeamID() == id)
            {
                int index = a.getIndex();

                Point target = queuePath.get(index % (queuePath.size()-1));
                goToX = target.getX();
                goToY = target.getY();

                this.move(target);

                if(id < 4) {
                    int newIndex = index - 1;

                    Action move = new Action("Move", null, id, id + 1);
                    move.setIndex(newIndex);

                    try {
                        this.broadcastMessage(move);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

                Action inform = new Action("Inform.ReadyToDance", null, id, 0);
                try {
                    this.broadcastMessage(inform);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            else if(id == 0 && a.getAction().equals("Inform.ReadyToDance"))
                readyCondition.oneMoreReady();

        }
    }

    public void onHitRobot(HitRobotEvent event) {
        turnLeft(30);
        back(40);
        move(goToX,goToY);
    }
}
