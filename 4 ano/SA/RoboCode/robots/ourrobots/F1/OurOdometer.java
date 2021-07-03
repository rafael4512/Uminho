package ourrobots.F1;

import robocode.Condition;
import robocode.Robot;

import java.util.ArrayList;


public class OurOdometer extends Condition
{
    private boolean isRacing;
    private boolean finished;
    private boolean isReady;

    static final double initCoords = 18.0;

    private final ArrayList<Point> history;
    private final Robot robot;

    public OurOdometer(String name, Robot r)
    {
        super(name);

        this.isReady = false;
        this.isRacing = false;
        this.finished = false;

        this.history = new ArrayList<>();

        this.robot = r;
    }

    public boolean test()
    {
        if(!finished)
        {
            double x = this.robot.getX();
            double y = this.robot.getY();
            Point position = new Point(x, y);

            if(position.distance(initCoords, initCoords) < 0.0001 && !this.isReady)
                this.isReady = true;

            else if(position.distance(initCoords, initCoords) > 0.0001 && this.isReady && !this.isRacing)
            {
                this.isRacing = true;
                this.history.add(position);
            }

            else if(position.distance(initCoords, initCoords) > 0.0001 && this.isRacing)
                this.history.add(position);

            else if(position.distance(initCoords, initCoords) < 0.0001 && this.isRacing)
            {
                this.finished = true;
                this.history.add(position);
            }
        }

        return finished;
    }

    public double getRaceDistance()
    {
        double distance = 0.0;

        for(int i = 0; i < this.history.size() - 1; ++i)
            distance += this.history.get(i).distance(this.history.get(i+1));

        return distance;
    }

}
