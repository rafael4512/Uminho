/**
 * Copyright (c) 2001-2020 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * https://robocode.sourceforge.io/license/epl-v10.html
 */
package sampleteam;


/**
 * Point - a serializable point class
 */
public class Point implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private double x = 0.0;
	private double y = 0.0;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

	public Point(double x, double y, int decimal) {
		this.x = round(x, decimal);
		this.y = round(y, decimal);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

    public double distance(double x, double y) {
        double ac = Math.abs(this.y - y);
        double cb = Math.abs(this.x - x);
        return Math.hypot(ac, cb);
    }

	public double distance(Point other) {
		double ac = Math.abs(this.y - other.getY());
		double cb = Math.abs(this.x - other.getX());
		return Math.hypot(ac, cb);
	}

    public double round(double number, int scale) {
        int pow = 1;
        for (int i = 0; i < scale; i++)
            pow *= 10;
        double tmp = number * pow;
        return (double) Math.round(tmp) / pow;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.x, x) != 0) return false;
        return Double.compare(point.y, y) == 0;
    }

    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String toString() {
        return "Point (" +
                "x=" + x +
                ", y=" + y +
                ')';
    }
}
