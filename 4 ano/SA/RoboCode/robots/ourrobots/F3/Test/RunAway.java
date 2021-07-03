package ourrobots.F3.Test;

import ourrobots.F3.Utils.TeamFunctions;
import robocode.Bullet;
import robocode.HitByBulletEvent;
import robocode.MessageEvent;

public class RunAway extends TeamFunctions {
    int hits_suffered = 0;
    int bullets_received = 0;

    @Override
    public void run() {
        addCustomEvent(ownPositionRefresher);

        move(400, 100);
        turnTo(0);

        int i = 0;
        while (i < 10) {
            setMaxVelocity(8);
            move(400, 300);
            move(400, 100);
            i++;
        }

        System.out.println(hits_suffered);
        System.out.println(bullets_received);
    }

    @Override
    public void onMessageReceived(MessageEvent event) {
        if (event.getMessage() instanceof Bullet) {
            bullets_received++;
            Bullet b = (Bullet) event.getMessage();

            double heading = ((-getHeading() + 450) % 360) * Math.PI / 180;
            boolean willGetHitContinue = Math.abs(Math.toDegrees(Math.atan((getY() + getVelocity() * Math.sin(heading) - b.getY()) / (getX() + getVelocity() * Math.cos(heading) - b.getX()))) + (-b.getHeading() + 460)) % 360 < 20;
            boolean willGetHitStopped = Math.abs(Math.toDegrees(Math.atan((getY() - b.getY()) / (getX() - b.getX()))) + (-b.getHeading() + 455)) % 360 < 10;

            // Se continuar sou atingido, mas se ficar parado, não
            if (willGetHitContinue && !willGetHitStopped) {
                setMaxVelocity(0);
            }
            // Se ficar parado sou atingido, mas se continuar não
            else if (!willGetHitContinue && willGetHitStopped) {
                setAhead(100);
            }
            // Se continuar sou atingido e se ficar parado também
            else if (willGetHitContinue) {
                setBack(100);
            }
        }
        setMaxVelocity(8);
    }

    @Override
    public void onHitByBullet(HitByBulletEvent event) {
        hits_suffered++;
    }
}
