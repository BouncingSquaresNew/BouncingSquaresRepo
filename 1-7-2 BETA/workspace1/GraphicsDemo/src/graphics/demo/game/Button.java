package graphics.demo.game;

import android.graphics.Color;

import graphics.demo.framework.Graphics;

/**
 * Created by Ben on 11/06/2015.
 */
public class Button implements IDrawable, IClickable
{
    public int x;
    public int y;
    public int width;
    public int height;
    public int red;
    public int green;
    public int blue;
    public boolean invert;

    public Button (int x, int y, int width, int height, int red, int green, int blue)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public Button ()
    {
        this(0, 0, 0, 0, 0, 0, 0);
    }

    public void draw(Graphics g)
    {

    }

    public boolean isPressed(int touchX, int touchY)
    {
        boolean pressed = false;
        if(touchX >= x
                && touchX <= x + width
                && touchY >= y
                && touchY <= y + height)
        {
            pressed = true;
        }
        return pressed;
    }

    public void click()
    {

    }

    public void invert()
    {
        if(invert == true)
        {
            invert = false;
        }
        else if(invert == false)
        {
            invert = true;
        }
    }

    public void setColor(int red, int green, int blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

}
