package ourrobots.F2;

import robocode.AdvancedRobot;
import robocode.HitRobotEvent;

import java.awt.*;

public class Grounded extends AdvancedRobot {

    double x;
    double y;

    public void run() {
        setColors(Color.yellow,Color.yellow,Color.yellow); // body,gun,radar

        x = this.getBattleFieldWidth()-18;
        y = this.getBattleFieldHeight()-18;

        while(Math.abs(this.getX() - x) > 0.001 || Math.abs(this.getY() - y) > 0.001)
            move(x,y);
    }

    public void move(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading();
        while (turnAngle < 0) turnAngle += 360;

        if(turnAngle <= 180) turnRight(turnAngle);
        else turnLeft(360-turnAngle);

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    public void onHitRobot(HitRobotEvent event) {
        turnLeft(45);
        back(20);
        move(x,y);
    }

}
