package graphics.demo.game;

import android.graphics.Color;
import android.graphics.Typeface;

import graphics.demo.framework.Graphics;

/**
 * Created by Ben on 11/06/2015.
 */
public class MenuButton extends Button
{
    public String text;

    public MenuButton (int x, int y, int width, int height, int red, int green, int blue, String text)
    {
        super(x, y, width, height, red, green, blue);
        this.text = text;
    }

    public void drawButton(Graphics g)
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

    public void drawText(Graphics g, Typeface font, int fontSize, int strokeWidth)
    {
        if(invert == false)
        {
            g.drawText(text, font, x + 12, y + height - 12, fontSize, strokeWidth, Color.rgb(220, 220, 220));
        }
        else
        {
            g.drawText(text, font, x + 12, y + height - 12, fontSize, strokeWidth, Color.rgb(red, blue, green));
        }
    }
}
