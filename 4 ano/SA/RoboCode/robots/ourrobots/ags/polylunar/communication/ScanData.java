package ourrobots.ags.polylunar.communication;

import ourrobots.ags.polylunar.robotdata.HostileBot;
import ourrobots.ags.polylunar.robotdata.log.LogSituation;
import ourrobots.ags.util.points.AbsolutePoint;
import ourrobots.ags.util.points.RelativePoint;

import java.io.Serializable;
import java.util.Map;

public class ScanData implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String name;
    public final AbsolutePoint location;
    public final RelativePoint velocity;
    public final double energy;
    public final Map<String, LogSituation> situations;
    public final double angularVelocity;
    
    public ScanData(HostileBot enemy) {
        name = enemy.getName();
        location = enemy.getNewLocation();
        velocity = enemy.getNewVelocity();
        energy = enemy.getNewEnergy();
        situations = enemy.getNewSituations();
        angularVelocity = enemy.getNewAngularVelocity();
    }
}
