package Classes;

import Agents.AI;

import java.io.Serializable;
import java.util.Objects;

public class Position implements Serializable {

    private double x, y;

    public Position(double x, double y) {
        super();
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distancia(Position p) {
        return Math.sqrt(Math.pow(this.x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
    }

    public Position clone() {
        return new Position(x, y);
    }

    public double move(Position pf, double dist) {
        double d = this.distancia(pf);

        if (d <= dist) {
            this.x = pf.x;
            this.y = pf.y;
            return d;
        }
        else {
            double xtemp = (pf.getX() - this.x) / d;
            double ytemp = (pf.getY() - this.y) / d;

            this.x += xtemp * dist;
            this.y += ytemp * dist;
            return dist;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position p = (Position) o;
        return Double.compare(x, p.getX()) == 0 &&
                Double.compare(y, p.getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + String.format("%.3f", x).replace(",", ".") + ", " + String.format("%.3f", y).replace(",", ".") + ')';
    }

    public int drawX(int w) {
        return (int) (0.2 * w + ((this.x) / AI.X_MAX) * 0.6 * w);
    }

    public int drawY(int h) {
        return (int) (0.8 * h - ((this.y) / AI.X_MAX) * 0.6 * h);
    }
}
