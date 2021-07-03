package ourrobots.ags.util;

/**
 * A utility class for storing number ranges
 * 
 * @author Alexander Schultz
 */
public class Range implements Cloneable {
    private final double upper, lower;
    
    public Range(double v1, double v2) {
        this.lower = Math.min(v1, v2);
        this.upper = Math.max(v1, v2);
    }
    
    public Range(double v) {
        this(v, v);
    }
    
    public Range clone() {
        return new Range(lower, upper);
    }
    
    public boolean intersects(double n) {
        return (n >= lower && n <= upper);
    }
    
    // Returns how far outside the range, a number is
    public double distanceOutside(double n) {
        if (n > upper) 
            return (n - upper);
        else if (n < lower)
            return (lower - n);
        else
            return 0;
    }
    
    public boolean looseIntersect(double n, double a) {
        return (n >= lower-a && n <= upper+a);
    }
    
    public double getLower() { return lower; }
    public double getUpper() { return upper; }
    
    // Return a number range containing both ranges
    public Range grow(Range r) {
        return grow(r.lower, r.upper);
    }
    public Range grow(double v1, double v2) {
        return new Range(Math.min(lower, Math.min(v1, v2)), Math.max(upper, Math.max(v1, v2)));
    }
    
    // Return a range grown to contain this value too
    public Range grow(double v) {
        return new Range(Math.min(lower, v), Math.max(upper, v));
    }
    
    public double getSize() {
        return upper-lower;
    }
    
    public double getCenter() {
        return 0.5*(upper+lower);
    }
    
    public static Range getIntersection(Range r1, Range r2) {
        if (r2.lower > r1.upper || r1.lower > r2.upper)
            return null;
        return getMiddle(r1, r2);
    }
    
    public static Range getMiddle(Range r1, Range r2) {
        return new Range(Math.max(r1.lower, r2.lower), Math.min(r1.upper, r2.upper));
    }
    
    @Override
    public String toString() {
        return "Range: "+(Math.rint(lower*1000)/1000)+" to "+(Math.rint(upper*1000)/1000);
    }
}
