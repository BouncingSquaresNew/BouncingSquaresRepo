package graphics.demo.game;

import graphics.demo.framework.Pixmap;

/**
 * Created by Ben on 23/12/2014.
 */
public class Shape
{
    public double x;
    public double y;
    public double mass;

    public Shape(double x, double y, double mass)
    {
        this.x = x;
        this.y = y;
        this.mass = mass;
    }

    public boolean onscreen(int width, int height)
    {
        boolean onscreen = true;
        if(x <= 0
                || x >= width
                || y <= 0
                || y >= height)
        {
            onscreen = false;
        }
        return onscreen;
    }

}
