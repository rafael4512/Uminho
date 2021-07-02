package ourrobots.F2;

import robocode.TeamRobot;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public abstract class TeamFunctions extends TeamRobot {
    private final ArrayList<String> teammates = new ArrayList<>();

    protected void move(Point p) {
        this.move(p.getX(), p.getY());
    }

    protected void move(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        turnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    protected void setMove(Point p) {
        this.setMove(p.getX(), p.getY());
    }

    protected void setMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        setTurnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.setAhead(distance);
    }

    protected void setTurnMove(Point p)  {
        this.setTurnMove(p.getX(), p.getY());
    }

    protected void setTurnMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        setTurnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.ahead(distance);
    }

    protected void setAheadMove(Point p)  {
        this.setAheadMove(p.getX(), p.getY());
    }

    protected void setAheadMove(double target_x, double target_y) {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        turnTo(Math.toDegrees(Math.atan2(dx, dy)));

        double distance = Math.sqrt(dx * dx + dy * dy);
        this.setAhead(distance);
    }

    protected void turnTo(Point p) { this.turnTo(p.getX(),p.getY()); }

    protected void turnTo(double target_x, double target_y)
    {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading()) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if(turnAngle <= 180) turnRight(turnAngle);
        else turnLeft(360-turnAngle);
    }

    protected void turnTo(double angle) {
        double required_angle = (angle - getHeading()) % 360;
        if (required_angle < 0) required_angle += 360;

        if (required_angle <= 180) turnRight(required_angle);
        else turnLeft(360 - required_angle);
    }

    protected void setTurnTo(double angle) {
        double required_angle = (angle - getHeading()) % 360;
        if (required_angle < 0) required_angle += 360;

        if (required_angle <= 180) setTurnRight(required_angle);
        else setTurnLeft(360 - required_angle);
    }

    protected void setTurnTo(Point p) { this.setTurnTo(p.getX(),p.getY()); }

    protected void setTurnTo(double target_x, double target_y)
    {
        double dy = target_y - this.getY();
        double dx = target_x - this.getX();

        double turnAngle = (Math.toDegrees(Math.atan2(dx, dy)) - this.getHeading()) % 360;
        while (turnAngle < 0) turnAngle += 360;

        if(turnAngle <= 180) setTurnRight(turnAngle);
        else setTurnLeft(360-turnAngle);
    }

    public double getRandomNumber(double min, double max) {
        return (Math.random() * (max - min)) + min;
    }

    protected double calculateAngle(Point a, Point b, Point c) {
        double ba_x = a.getX() - b.getX();
        double ba_y = a.getY() - b.getY();

        double bc_x = c.getX() - b.getX();
        double bc_y = c.getY() - b.getY();

        return Math.acos(((ba_x * bc_x) + (ba_y * bc_y)) / (Math.sqrt(Math.pow(ba_x, 2) + Math.pow(ba_y, 2)) * Math.sqrt(Math.pow(bc_x, 2) + Math.pow(bc_y, 2))));
    }

    protected Point calculateFinalPoint(Point initPoint, double distance, double angle, double heading)
    {
        double radian = Math.toRadians(heading + angle % 360);

        double newX = (initPoint.getX() + Math.sin(radian) * distance);
        double newY = (initPoint.getY() + Math.cos(radian) * distance);

        return new Point(newX, newY);
    }

    protected TeamColors getColorsTeam() {
        TeamColors c = new TeamColors();

        c.bodyColor = Color.red;
        c.gunColor = Color.red;
        c.radarColor = Color.red;
        c.scanColor = Color.yellow;
        c.bulletColor = Color.yellow;

        return c;
    }

    protected void setColorsTeam(TeamColors c) {
        setBodyColor(c.bodyColor);
        setGunColor(c.gunColor);
        setRadarColor(c.radarColor);
        setScanColor(c.scanColor);
        setBulletColor(c.bulletColor);
    }

    protected void broadcastName() {
        try {
            // Send RobotColors object to our entire team
            broadcastMessage(getName());
        } catch (IOException ignored) {}
    }

    protected void addTeammate(String name) {
        this.teammates.add(name);
    }

    protected ArrayList<String> getTeam() {
        return this.teammates;
    }
}
