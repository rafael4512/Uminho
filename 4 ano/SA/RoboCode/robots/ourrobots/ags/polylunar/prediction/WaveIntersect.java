package ourrobots.ags.polylunar.prediction;

import ourrobots.ags.util.Range;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import java.util.ArrayList;
import java.util.List;

public class WaveIntersect {
    // Get the intersecting relative angle range of a bot centered at a given point
    public static Range getHitRange(AbsolutePoint origin, double radius, double speed, double referenceAngle, AbsolutePoint p) {
        // Check if it's too far away for any intersection
        if (Math.abs(origin.distance(p) - radius) > Math.sqrt(2*18*18)+speed)
            return null;
                
        // Store a list of possible extreme points 
        List<AbsolutePoint> possiblepoints = new ArrayList<AbsolutePoint>();
        
        // Get the sides of the bot
        double bottom = p.y - 18;
        double top = p.y + 18;
        double left = p.x - 18;
        double right = p.x + 18;
        
        // Check if there is actually intersections with any sides
        double lastradius = radius-speed;
        possiblepoints.addAll(XCircleIntersect(origin, lastradius, left,   bottom, top));
        possiblepoints.addAll(XCircleIntersect(origin, radius,     left,   bottom, top));
        possiblepoints.addAll(XCircleIntersect(origin, lastradius, right,  bottom, top));
        possiblepoints.addAll(XCircleIntersect(origin, radius,     right,  bottom, top));
        
        possiblepoints.addAll(YCircleIntersect(origin, lastradius, bottom, left,   right));
        possiblepoints.addAll(YCircleIntersect(origin, radius,     bottom, left,   right));
        possiblepoints.addAll(YCircleIntersect(origin, lastradius, top,    left,   right));
        possiblepoints.addAll(YCircleIntersect(origin, radius,     top,    left,   right));
        
        // Get the corners of the bot
        AbsolutePoint c1 = AbsolutePoint.fromXY(left,  bottom);
        AbsolutePoint c2 = AbsolutePoint.fromXY(left,  top);
        AbsolutePoint c3 = AbsolutePoint.fromXY(right, top);
        AbsolutePoint c4 = AbsolutePoint.fromXY(right, bottom);
        
        // Add corners too if they're within what we want
        if (intersects(origin, radius, speed, c1)) possiblepoints.add(c1);
        if (intersects(origin, radius, speed, c2)) possiblepoints.add(c2);
        if (intersects(origin, radius, speed, c3)) possiblepoints.add(c3);
        if (intersects(origin, radius, speed, c4)) possiblepoints.add(c4);
        
        // Find the lowest and highest guessfactors
        Range r = null;
        for (AbsolutePoint point : possiblepoints) {
            double relAngle = RelativePoint.fromPP(origin, point).getDirDist(referenceAngle);
            if (r != null)
                r = r.grow(relAngle);
            else
                r = new Range(relAngle);
        }
        
        return r;
    }
    
    private static boolean intersects(AbsolutePoint origin, double radius, double speed, AbsolutePoint p) {
        double d = origin.distance(p);
        return (d <= radius && d >= (radius-speed));
    }
    
    private static List<AbsolutePoint> XCircleIntersect(AbsolutePoint origin, double r, double x, double miny, double maxy) {
        List<AbsolutePoint> intersects = new ArrayList<AbsolutePoint>();
        double dx = x - origin.x;
        double D = r*r-dx*dx;
        
        // Negative discriminant, no intersection
        if (D < 0)
            return intersects;
        
        // Get an intersect
        double d = Math.sqrt(D);
        double y1 = origin.y + d;
        if (y1 >= miny && y1 <= maxy)
            intersects.add(AbsolutePoint.fromXY(x, y1));
        
        // Zero discriminant, no need for more
        if (D == 0)
            return intersects;
        
        // Get another intersect
        double y2 = origin.y - d;
        if (y2 >= miny && y2 <= maxy)
            intersects.add(AbsolutePoint.fromXY(x, y2));
        
        return intersects;
    }

    private static List<AbsolutePoint> YCircleIntersect(AbsolutePoint origin, double r, double y, double minx, double maxx) {
        List<AbsolutePoint> intersects = new ArrayList<AbsolutePoint>();
        double dy = y - origin.y;
        double D = r*r-dy*dy;
        
        // Negative discriminant, no intersection
        if (D < 0)
            return intersects;
        
        // Get an intersect
        double d = Math.sqrt(D);
        double x1 = origin.x + d;
        if (x1 >= minx && x1 <= maxx)
            intersects.add(AbsolutePoint.fromXY(x1, y));
        
        // Zero discriminant, no need for more
        if (D == 0)
            return intersects;
        
        // Get another intersect
        double x2 = origin.x - d;
        if (x2 >= minx && x2 <= maxx)
            intersects.add(AbsolutePoint.fromXY(x2, y));
        
        return intersects;
    }
}
