package ourrobots.ags.polylunar.prediction;

import ourrobots.ags.polylunar.base.Rules;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

public class PhysicsEngine {
    private static final int ROBOT_SIZE=18;
    /**
     * Simulates an imaginary bot moving with an intent of a certain velocity and certain turn.
     * Respects all physics limitations of the Robocode world with perfect accuracy so far as I
     * can tell.
     */
    public static void simulateTick(Rules rules, RobotSim sim, double intendedVelocity, double intendedTurn) {
        /* Get intended acceleration */
        double acceleration = intendedVelocity - sim.velocity.magnitude;
        /* Check if we're accelerating or deccelerating, and use the appropriate acceleration limit */
        if (sim.velocity.magnitude == 0 || (Math.abs(acceleration) > Math.abs(sim.velocity.magnitude) && (Math.signum(acceleration) == Math.signum(sim.velocity.magnitude))))
            acceleration = Math.max(Math.min(acceleration, rules.ACCELERATION), -rules.ACCELERATION);
        else
            acceleration = Math.max(Math.min(acceleration, rules.DECELERATION), -rules.DECELERATION);
        
        /* Calculate angular velocity */
        double maxav = rules.getTurnRate(Math.abs(sim.velocity.magnitude));
        double angularvelocity = Math.max(Math.min(intendedTurn, maxav), -maxav);
        
        /* Rotate and accelerate the velocity */
        sim.velocity = (RelativePoint)sim.velocity.clone();
        sim.velocity.setDirectionMagnitude(sim.velocity.getDirection()+angularvelocity, Math.max(Math.min(sim.velocity.magnitude+acceleration,rules.MAX_VELOCITY),-rules.MAX_VELOCITY));
        
        /* Increment the location */
        sim.location = sim.location.addRelativePoint(sim.velocity);
        
        // Rectify the situation if we're outside of the map bounds
        processWallCollision(rules, sim);
    }
    
    private static boolean processWallCollision(Rules rules, RobotSim sim) {
        boolean hitWall = false;
        double fixx = 0, fixy = 0;
        AbsolutePoint location = sim.location;
        RelativePoint velocity = sim.velocity;
    
        if (Math.round(location.getX()) > rules.BATTLEFIELD_WIDTH - ROBOT_SIZE) {
            hitWall = true;
            fixx = rules.BATTLEFIELD_WIDTH - ROBOT_SIZE - location.getX();
        }
        if (Math.round(location.getX()) < ROBOT_SIZE) {
            hitWall = true;
            fixx = ROBOT_SIZE - location.getX();
        }
        if (Math.round(location.getY()) > rules.BATTLEFIELD_HEIGHT - ROBOT_SIZE) {
            hitWall = true;
            fixy = rules.BATTLEFIELD_HEIGHT - ROBOT_SIZE - location.getY();
        }
        if (Math.round(location.getY()) < ROBOT_SIZE) {
            hitWall = true;
            fixy = ROBOT_SIZE - location.getY();
        }
        
        double tanHeading = Math.tan(velocity.direction);
        double fixxtanHeading = fixx / tanHeading;
        double fixytanHeading = fixy * tanHeading;
        
        // if it hits bottom or top wall
        if (fixx == 0) {
            fixx = fixytanHeading;
        } // if it hits a side wall
        else if (fixy == 0) {
            fixy = fixxtanHeading;
        } // if the robot hits 2 walls at the same time (rare, but just in case)
        else if (Math.abs(fixxtanHeading) > Math.abs(fixy)) {
            fixy = fixxtanHeading;
        } else if (Math.abs(fixytanHeading) > Math.abs(fixx)) {
            fixx = fixytanHeading;
        }
        
        location.setLocation(location.x+fixx, location.y+fixy);
        
        if (hitWall)
            velocity.setDirectionMagnitude(velocity.direction, 0);
        
        sim.hitWallFlag = hitWall;
        
        return hitWall;
    }
}
