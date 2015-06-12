package graphics.demo.game;

import android.graphics.Color;
import android.graphics.Typeface;

import graphics.demo.framework.Graphics;

/**
 * Created by Ben on 12/06/2015.
 */
public class DrawableString implements IDrawable
{
    private int x;
    private int y;
    private Typeface font;
    private String fixedText;
    private String varText;
    private int fontSize;
    private int strokeWidth;
    private int red;
    private int green;
    private int blue;

    public DrawableString(int x, int y, Typeface font, String text, int fontSize, int strokeWidth, int red, int green, int blue)
    {
        this.x = x;
        this.y = y;
        this.font = font;
        this.fixedText = text;
        this.varText = "";
        this.fontSize = fontSize;
        this.strokeWidth = strokeWidth;
        this.red = red;
        this.blue = blue;
        this.green = green;
    }

    public DrawableString(int x, int y, Typeface font, int fontSize, int strokeWidth, int red, int green, int blue)
    {
        this(x, y, font, "VOID", fontSize, strokeWidth, red, green, blue);
    }
    
    public void draw(Graphics g)
    {
        g.drawText(fixedText + varText, font, x, y, fontSize, strokeWidth, Color.rgb(red, green, blue));
    }

    public void setCoordinates(int setX, int setY)
    {
        this.x = setX;
        this.y = setY;
    }

    public void setFixedText(String textToSet)
    {
        this.fixedText = textToSet;
    }

    public void setVarText(String textToSet)
    {
        this.varText = textToSet;
    }

    public void setFontSize(int fontSizeToSet)
    {
        this.fontSize = fontSizeToSet;
    }
    
    public void invert() {}
}
