package graphics.demo.game;

import graphics.demo.framework.Pixmap;

/**
 * Created by Ben on 23/12/2014.
 */

public class BouncingShape extends MovingShape
{
    public double bounceCoefficient;

    public BouncingShape(double x, double y, double xVector, double yVector, double mass, Pixmap image, double bounceCoefficient)
    {
        super(x, y, xVector, yVector, mass, image);
        this.bounceCoefficient = bounceCoefficient;
    }
}
