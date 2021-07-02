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

public class AlphaTeam extends TeamFunctions {

    private final ReadyCondition readyCondition = new ReadyCondition("nextStep", this, 4);
    private final ReadyCondition imreadyCondition = new ReadyCondition("imready", this, 1);
    private final Color[] colors = new Color[]{Color.gray, Color.black, Color.white, Color.darkGray, Color.lightGray};
    private int id = -1;
    private ArrayList<Point> queuePath;
    private double goToX = 0;
    private double goToY = 0;
    private boolean firstmove = true;
    private boolean onTheSpot = false;

    public void run() {

        calculateId();
        System.out.printf("Robot id: %d!\n", id);
        setColors(colors[id],colors[(id+1)%5],colors[(id+2)%5]); // body,gun,radar

        if(id == 0)
        {
            addCustomEvent(readyCondition);

            double height = getBattleFieldHeight();
            double width = getBattleFieldWidth();

            int sizePath = calculateQueuePath(height,width);
            System.out.println("SizePath: "+sizePath);

            int iteration = 0;
            while(iteration < sizePath-4)
            {
                readyCondition.setAllNotReady();

                Point goToPoint = queuePath.get(iteration+4);
                goToX = goToPoint.getX();
                goToY = goToPoint.getY();
                move(goToX,goToY);

                Action move1 = new Action("Move", queuePath.get(iteration+3), id, 1);
                Action move2 = new Action("Move", queuePath.get(iteration+2), id, 2);
                Action move3 = new Action("Move", queuePath.get(iteration+1), id, 3);
                Action move4 = new Action("Move", queuePath.get(iteration), id, 4);

                try {
                    this.broadcastMessage(move1);
                    this.broadcastMessage(move2);
                    this.broadcastMessage(move3);
                    this.broadcastMessage(move4);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                waitFor(readyCondition);

                iteration++;
            }
        }
        else {
            addCustomEvent(imreadyCondition);
            while(true)
            {
                waitFor(imreadyCondition);
                imreadyCondition.setAllNotReady();
                firstmove = false;
                onTheSpot = true;

                Action inform = new Action("Inform.Ready", null, id, 0);
                try {
                    this.broadcastMessage(inform);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    private int calculateQueuePath(double height, double width) {
        queuePath = new ArrayList<>();
        double danceDistance = 60;
        double x = danceDistance;
        double y = danceDistance;

        queuePath.add(new Point(x,y));
        while(y < (height / 2))
            queuePath.add(new Point(x,y += danceDistance));
        while(x < (width / 2))
            queuePath.add(new Point(x += danceDistance,y));
        while(x < (width-danceDistance) && y < (height-danceDistance))
            queuePath.add(new Point(x += danceDistance,y += danceDistance));
        while(x < (width-danceDistance))
            queuePath.add(new Point(x += danceDistance,y));
        while(y > danceDistance)
            queuePath.add(new Point(x,y -= danceDistance));
        while(x > danceDistance)
            queuePath.add(new Point(x -= danceDistance,y));
        queuePath.add(new Point(x,y += danceDistance));
        queuePath.add(new Point(x,y += danceDistance));
        queuePath.add(new Point(x,y += danceDistance));
        queuePath.add(new Point(x,y += danceDistance));

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
            if(a.getAction().equals("Move") && a.getTargetTeamID() == id)
            {
                onTheSpot = false;
                Point target = a.getTarget();
                goToX = target.getX();
                goToY = target.getY();

                while(this.getVelocity() > 0) {
                    turnRadarRight(360);
                    execute();
                }
                move(target);

                if(target.distance(new Point(getX(), getY())) < 0.001) imreadyCondition.oneMoreReady();
            }
            else if((id == 0) && a.getAction().equals("Inform.Ready"))
                readyCondition.oneMoreReady();
        }
    }

    public void onHitRobot(HitRobotEvent event) {
        if(firstmove)
        {
            turnRight(30);
            back(40);
        }
        move(goToX,goToY);
        if((new Point(goToX,goToY).distance(new Point(getX(), getY())) < 0.001) && goToX != 0 && goToY != 0 && !onTheSpot)
            imreadyCondition.oneMoreReady();
    }
}
