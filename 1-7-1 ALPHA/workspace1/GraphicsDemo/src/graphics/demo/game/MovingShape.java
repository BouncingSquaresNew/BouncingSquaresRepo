package graphics.demo.game;

/**
 * Created by Ben on 10/06/2015.
 */

import graphics.demo.framework.Pixmap;

public class MovingShape extends Shape implements IMoving
{
    double x;
    double y;
    double xVector;
    double yVector;
    Pixmap image;

    public MovingShape(double x, double y, double xVector, double yVector, double mass, Pixmap image)
    {
        super(x, y, mass);
        this.xVector = xVector;
        this.yVector = yVector;
        this.image = image;
    }

    public MovingShape(double x, double y, double mass, Pixmap image)
    {
        super(x, y, mass);
        this.xVector = 0;
        this.yVector = 0;
        this.image = image;
    }

    public void move()
    {
        x = x + xVector;
        y = y + yVector;
    }
}
