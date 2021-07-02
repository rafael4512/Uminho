package ourrobots.F2;


import java.awt.*;


/**
 * RobotColors - A serializable class to send Colors to teammates.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class TeamColors implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public Color bodyColor;
    public Color gunColor;
    public Color radarColor;
    public Color scanColor;
    public Color bulletColor;
}
