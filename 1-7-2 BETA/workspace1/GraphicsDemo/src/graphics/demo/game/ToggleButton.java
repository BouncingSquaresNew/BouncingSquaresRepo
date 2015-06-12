package graphics.demo.game;

import android.graphics.Color;
import android.graphics.Typeface;

import graphics.demo.framework.Graphics;

/**
 * Created by Ben on 11/06/2015.
 */
public class ToggleButton extends Button
{
    public String label;

    public ToggleButton (int x, int y, int width, int height, int red, int green, int blue)
    {
        super(x, y, width, height, red, green, blue);
        this.label = "A";
    }

    public void drawToggle(Graphics g)
    {
        if(invert == false)
        {
            g.drawRect(x, y, width, height, Color.rgb(red, green, blue));
        }
        else
        {
            g.drawRect(x, y, width, height, Color.rgb(220, 220, 220));
        }
    }
    public void drawText(Graphics g, Typeface font)
    {
        if(invert == false)
        {
            g.drawText(label, font, x + 12, y + height - 12, width, (int)(width * 0.1), Color.rgb(220, 220, 220));
        }
        else
        {
            g.drawText(label, font, x + 12, y + height - 12, width, (int)(width * 0.1), Color.rgb(red, blue, green));
        }
    }


}
