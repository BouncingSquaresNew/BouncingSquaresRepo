package graphics.demo.game;

import graphics.demo.framework.Graphics;
import graphics.demo.framework.Graphics.PixmapFormat;
import graphics.demo.framework.Pixmap;

/* A star is effectively a motionless supermassive object, that will exist in the game
 * A star's mass can be specified by an input paramater, and so effectively can be increased
 * up to any size required :)
 */

public class Star {
	
	public static Pixmap star;
	public double x = 0;
	public double y = 0;
	public double gravityX = 0;
	public double gravityY = 0;
	public double mass = 5;
	
	public Star(Pixmap star, double mass)
	{
		this.x = (UtilConstants.SCREEN_WIDTH - star.getWidth()) / 2;
		this.y = (UtilConstants.SCREEN_HEIGHT - star.getHeight()) / 2;
		this.gravityX = 0.5 * UtilConstants.SCREEN_WIDTH;
		this.gravityY = 0.5 * UtilConstants.SCREEN_HEIGHT;
		this.mass = mass;
	}

}
