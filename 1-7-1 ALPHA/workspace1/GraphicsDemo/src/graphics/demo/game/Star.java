package graphics.demo.game;

import graphics.demo.framework.Graphics;
import graphics.demo.framework.Graphics.PixmapFormat;
import graphics.demo.framework.Pixmap;

/* A star is effectively a motionless supermassive object, that will exist in the game
 * A star's mass can be specified by an input paramater, and so effectively can be increased
 * up to any size required :)
 */

public class Star extends Shape{
	
	public static Pixmap star;
	public double x = 0;
	public double y = 0;
	public double gravityX = 0;
	public double gravityY = 0;
	public double mass = 5;
	
	public Star(Pixmap star, double mass, int width, int height)
	{
        super((width - star.getWidth()) / 2, (height - star.getHeight()) / 2, mass);
        this.gravityX = 0.5 * width;
		this.gravityY = 0.5 * height;
	}

}
