package graphics.demo.game;

import graphics.demo.framework.Pixmap;

/**
 * Created by Ben on 23/12/2014.
 */

public class BouncingShape extends MovingShape
{
    private double x;
    private double y;
    private double xVector;
    private double yVector;
    private double mass;
    private Pixmap image;
    private double bounceCoefficient;

    public BouncingShape(double x, double y, double xVector, double yVector, double mass, Pixmap image, double bounceCoefficient)
    {
        super(x, y, xVector, yVector, mass, image);
        this.x = x;
        this.y = y;
        this.xVector = xVector;
        this.yVector = yVector;
        this.mass = mass;
        this.image = image;
        this.bounceCoefficient = bounceCoefficient;

    }
}
