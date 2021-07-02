package ourrobots.ags.polylunar.robotdata;

import ourrobots.ags.polylunar.communication.BeconData;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;
import robocode.Event;
import robocode.MessageEvent;
import robocode.RobotDeathEvent;

import java.util.List;

public class FriendlyBot {
    private final String name;
    private AbsolutePoint location;
    private RelativePoint velocity;
    private double energy;
    private double radarHeading;
    private boolean alive;
    private boolean fresh;
    
    public FriendlyBot(String name) {
        this.name = name;
        alive = true;
    }
    
    public void updateData(List<Event> events) {
        fresh = false;
        if (!alive)
            return;
        for (Event e : events) {
            if (e instanceof RobotDeathEvent) {
                if (((RobotDeathEvent)e).getName().equals(name)) {
                    alive = false;
                    return;
                }
            } else if (e instanceof MessageEvent) {
                MessageEvent m = (MessageEvent)e;
                if (m.getMessage() instanceof BeconData) {
                    BeconData becon = (BeconData)m.getMessage();
                    if (m.getSender().equals(name)) {
                        fresh = true;
                        location = becon.location;
                        velocity = becon.velocity;
                        energy = becon.energy;
                        radarHeading = becon.radarHeading;
                    }
                }
            }
        }
    }
    
    public String getName() {
        return name;
    }
    
    public AbsolutePoint getLocation() {
        return location;
    }
    
    public RelativePoint getVelocity() {
        return velocity;
    }
    
    public double getEnergy() {
        return energy;
    }
    
    public double getRadarHeading() {
        return radarHeading;
    }
    
    public boolean isAlive() {
        return alive;
    }
    
    // For friendly bot like this, it being alive but not fresh is a BAD sign.
    // It means the ally has either skipped a turn or has locked up.
    public boolean isFresh() {
        return fresh;
    }
}
