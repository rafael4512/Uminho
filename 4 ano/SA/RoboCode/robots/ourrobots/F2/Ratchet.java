package ourrobots.F2;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.RobocodeFileOutputStream;
import robocode.ScannedRobotEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

public class Ratchet extends TeamFunctions {

    private int id = -1;
    private final double distanceFromWall = 56;
    private boolean ready = false;
    private double goToX = 0;
    private double goToY = 0;
    private boolean selfdestroy = false;

    public void run() {

        calculateId();
        System.out.printf("Robot id: %d!\n", id);

        if(id == 0)
        {
            setColors(Color.yellow, Color.orange, Color.lightGray); // body,gun,radar

            goToX = distanceFromWall;
            goToY = getBattleFieldHeight()/2;
        }
        else if(id == 1)
        {
            setColors(Color.yellow, Color.orange, Color.darkGray); // body,gun,radar

            goToX = getBattleFieldWidth() - distanceFromWall;
            goToY = getBattleFieldHeight()/2;
        }

        while(Math.abs(this.getX() - goToX) > 0.001 || Math.abs(this.getY() - goToY) > 0.001)
            this.move(goToX,goToY);

        if(id == 0)
        {
            if(this.getHeading() > 90 && this.getHeading() < 270)
                setTurnGunRight(-90);
            else
                setTurnGunLeft(-90);
        }
        else if(id == 1)
        {
            if(this.getHeading() > 90 && this.getHeading() < 270)
                setTurnGunRight(90);
            else
                setTurnGunLeft(90);
        }


        ready = true;
        setMaxVelocity(3);

        Action inform = new Action("Inform.Ready", null, id, -1);
        try {
            this.broadcastMessage(inform);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        while(!selfdestroy) {
            setTurnRadarLeft(360);
            execute();
        }

        while(true)
            this.move(goToX,goToY);

    }

    @Override
    public void onPaint(Graphics2D g) {
        if(ready && !selfdestroy) {
            g.setColor(Color.white);
            g.setStroke(new BasicStroke(2.0F));

            int x = (int)this.getX();
            g.drawLine(x, 0, x, (int)this.getBattleFieldHeight());
        }
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
            ps.println((id + 1) % 2);
            if (ps.checkError())
                this.out.println("Enable to write id!");

        } catch (IOException e) {
            e.printStackTrace(this.out);
        }
    }

    protected void specialMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double required_angle = (Math.toDegrees(Math.atan2(dx, dy)) - getHeading() + 360) % 360;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (required_angle <= 90) {
            this.turnRight(required_angle);
            this.setAhead(distance);
        }
        else if (required_angle > 90 && required_angle <= 180) {
            this.turnRight(-(180 - required_angle));
            this.setAhead(-distance);
        }
        else if (required_angle > 180 && required_angle <= 270) {
            this.turnRight(required_angle - 180);
            this.setAhead(-distance);
        }
        else {
            this.turnRight(-(360 - required_angle));
            this.setAhead(distance);
        }
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        if(ready && e.getName().contains("Clank") && e.getVelocity() > 0 && !selfdestroy) {
            double enemyHeading = e.getHeading();

            if((id == 0 && enemyHeading <= 180) || (id == 1 && enemyHeading >= 180))
            {
                double targetY = this.getBattleFieldHeight()/2;
                if (Math.abs(this.getY() - targetY) > 0.001)
                    specialMove(this.getX(), targetY);
                return ;
            }

            Point enemy = calculateFinalPoint(new Point(this.getX(),this.getY()),e.getDistance(),e.getBearing(),this.getHeading());

            double theta = 90 - e.getHeading();
            if (theta < 0 ) theta += 360;

            double delta_x = getX() - enemy.getX();

            if(id == 0) delta_x += 18;
            else delta_x -= 18;

            double delta_y = Math.tan(Math.toRadians(theta)) * delta_x;
            goToY = delta_y + enemy.getY();

            double height = getBattleFieldHeight();
            if (goToY > -height && goToY < 2 * height) {
                if (goToY < 0)
                    goToY = -goToY;
                else if (goToY > height)
                    goToY = 2 * height - goToY;

                specialMove(goToX, goToY);
            }
        }
    }

    public void onMessageReceived(MessageEvent e) {
        if (e.getMessage() instanceof Action) {
            Action a = (Action) e.getMessage();
            if(a.getAction().equals("KillYourself")){
                selfdestroy = true;
                goToX = this.getBattleFieldWidth()/2;
                goToY = this.getBattleFieldHeight()/2;
                this.setAheadMove(goToX,goToY);
            }
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        if(e.getName().contains("Clank") && ready && !selfdestroy) {
            Action hit = new Action();

            double angle = getRandomNumber(id * 180 + 20, id * 180 + 160);
            hit.setAction("HitRobot");
            hit.setAngle(angle);

            try {
                sendMessage(e.getName(),hit);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        else {
            setTurnLeft(30);
            back(40);
        }
    }
}
