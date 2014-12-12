package graphics.demo.game;

import graphics.demo.framework.Graphics;
import graphics.demo.framework.Pixmap;
import android.graphics.Color;

public class Square {
	
	public double x = 0; //X-Coordinate of the square
	public double y = 0; //Y-Coordinate of the square
	public double xVector = 0; //X-Vector of the sqyare
	public double yVector = 0; //Y-Vector of the square
	public double mass = 1; //The mass of the square
	int name = 0; //Each square is numbered with a name
	public static Pixmap image; //The image of the square, NB can be changed in /res folder
	public double BOUNCE_COEFFICIENT = BouncingSquares.restitution();; //The multiplier when a square hits the edge of the screen
	public double COLLIDE_COEFFICIENT = BouncingSquares.restitution(); //The coefficient of restitution between two squares
	public int glitch = 0; //This counter is used as follows
	/* NB(1) An initial problem found with the collision logic, was that the squares got stuck
	 * (Continually reversed their vectors as they would overlap by too much)
	 * In order to fix this, each square was assigned a counter "glitch", whereby when it
	 * collided with another square, the counter was increased. The squares are only allowed
	 * to collide if their counter is set to 0. Hence, squares can possibly "ghost" through
	 * eachother, but they no longer get stuck
	 */
	public int overEdge = 0; //A counter used to determine the number of times the square is off the screen
	public double initMagXDif = 0; //These counters are used for collision logic with magnets
//	initMagDif is the initial difference in distance to a nearby magnet
	public double initMagYDif = 0;
	public double magnetXCount = 0; //This is the number of times it collides with a magnet
	public double magnetYCount = 0;

	public Square(double x, double y, double xVector, double yVector, int name, Pixmap image, int glitch, double bounceCoefficient)
	{
		this.x = x;
		this.y = y;
		this.xVector = xVector;
		this.yVector = yVector;
		this.name = name;
		this.image = image;
		this.glitch = 0;
		this.overEdge = overEdge;
		this.initMagXDif = 0;
		this.initMagYDif = 0;
		this.magnetXCount = 0;
		this.magnetYCount = 0;
		this.BOUNCE_COEFFICIENT = bounceCoefficient;
		this.mass = 1;
	}
	
	public void freeze() //This method freezes the squares' velocities NB Used for OBS Teleporting
	{
		setSquare(x, y, 0, 0);
	}
	
	public void reverseVector() //This method reverses the vectors of the squares
	{
		reverseXVector(); //Calls the reverse X Method,
		reverseYVector(); //Calls the reverse Y Method
	}
	
	public void reverseYVector() //Reverses the Y vector of the square
	{
		yVector = yVector * -1 * BOUNCE_COEFFICIENT;
	}
	
	public void reverseXVector() //Reverses the X vector of the square
	{
		xVector = xVector * -1 * BOUNCE_COEFFICIENT;
	}
	
	public void collide(Square targetSquare) //This collides the squares together
	{
		double squareXVector = xVector;		
		double squareXVectorTargetSquare = targetSquare.xVector;
		xVector = 0.5 * (COLLIDE_COEFFICIENT * (squareXVectorTargetSquare - squareXVector) + squareXVector + squareXVectorTargetSquare);
		targetSquare.xVector = 0.5 * (squareXVector + squareXVectorTargetSquare - COLLIDE_COEFFICIENT * (squareXVectorTargetSquare - squareXVector));

		double squareYVector = yVector;
		double squareYVectorTargetSquare = targetSquare.yVector;
		yVector = 0.5 * (COLLIDE_COEFFICIENT * (squareYVectorTargetSquare - squareYVector) + squareYVector + squareYVectorTargetSquare);
		targetSquare.yVector = 0.5 * (squareYVector + squareYVectorTargetSquare - COLLIDE_COEFFICIENT * (squareYVectorTargetSquare - squareYVector));

		glitch = glitch + 1; //The counter of the square is increased by one
		targetSquare.glitch = targetSquare.glitch + 1;
	}
	
	public void collideMagnet(Magnet magnetJ) //This will collide a magnet with a square
	{
		reverseVector();
	}
	
	public void collideGeneric(Magnet targetMagnet) //This is used to collide the square with a magnet
	{
		double squareXVector = xVector;	//The initial vectors are stored	
		double magnetXVectorTargetMagnet = targetMagnet.xVector;
		xVector = magnetXVectorTargetMagnet * COLLIDE_COEFFICIENT; //The vectors are swapped
		targetMagnet.xVector = squareXVector * COLLIDE_COEFFICIENT; //And decreased by the scale factor
	
		double squareYVector = yVector;	//The same applies here
		double magnetYVectorTargetMagnet = targetMagnet.yVector;
		yVector = magnetYVectorTargetMagnet * COLLIDE_COEFFICIENT;
		targetMagnet.yVector = squareYVector * COLLIDE_COEFFICIENT;
			
		glitch = glitch + 1; //The glitch counter of the square is increased (see note)
		targetMagnet.glitch = targetMagnet.glitch + 1; //The magnets' glitch counter is increased
	}
	
	public void recalculate() //This method recalculates the positions of the squares
	{
		x = x + xVector; //The vectors are added to the current square positions
		y = y + yVector;
		boolean glitchEdge = false;
		
		if(overEdge == 0  //NOTE OBS FIX Remove this variable ASAP
				|| onscreen() == false)  //If the square is off the edge of the screen..
		{
			if(x <= 0)
			{
				x = 1; //Put it back on the screen
				reverseXVector(); //Reverse the vector
				glitchEdge = true;
			}
			if( x >= UtilConstants.SCREEN_WIDTH - image.getWidth()) //ETC with these methods
			{
				x = UtilConstants.SCREEN_WIDTH - image.getWidth();
				reverseXVector();
				glitchEdge = true;
			}
			if(y <= 0)
			{
				y = 1;
				reverseYVector();
				glitchEdge = true;
			}
			if( y >= UtilConstants.SCREEN_HEIGHT - image.getHeight())
			{
				y = UtilConstants.SCREEN_HEIGHT - image.getHeight() - 1;
				reverseYVector();
				glitchEdge = true;
			}
			if(glitchEdge == true)
			{
				overEdge = overEdge + 1;
			}
		}
		else if(overEdge >= 1 && onscreen() == true) //If however, the squares are on the screen, set their counter to 0
		{
			overEdge = 0;
		}
	}
	
	public boolean overlaps(Square targetSquare) //This is used to see whether the squares overlap
	{
		double distX;
		double distY;
		boolean collision = false;
		
		if(x>=targetSquare.x) //It works by taking the coordinates of the squares, and
//			measuring the distance between them, and then seeing whether it is less than
//			the dimensions of the image of the squares :)
		{
			distX = x - targetSquare.x;
		}
		else
		{
			distX = targetSquare.x - x;
		}
		
		if(y>=targetSquare.y)
		{
			distY = y - targetSquare.y;
		}
		else
		{
			distY = targetSquare.y - y;
		}
		
		if((distX <= image.getWidth()+1) && (distY <= image.getHeight()+1))
		{
			collision = true;
		}
		else
		{
			collision = false;
		}
		return collision;
	}
	
	public boolean overlapsMagnet(Magnet magnetJ)
	{
		boolean overlapsMagnet = false;
		if(x >= magnetJ.x - image.getWidth()
		&& x <= magnetJ.x + magnetJ.magnetImage.getWidth()
		&& y >= magnetJ.y - image.getHeight()
		&& y <= magnetJ.x + magnetJ.magnetImage.getHeight())
		{
			overlapsMagnet = true;
		}
		return overlapsMagnet;
	}
	
	
	
	public void drawSquare(Graphics g) //This draws the squares
	{
		g.drawPixmap(image, (int)x, (int)y);
		g.drawText("" + name, (int)x, (int)y, 10, Color.BLACK);
	}
	
	public void setSquare(double X, double Y, int XVector, int YVector) //This sets the positions of the squares
	{
		x = X;
		y = Y;
		xVector = XVector;
		yVector = YVector;
	}
	
	public boolean onscreen() //This checks to see whether the squares are on the screen
	{
		boolean onscreen = true;
		if(x >= UtilConstants.SCREEN_WIDTH
				|| x <= 0
				|| y >= UtilConstants.SCREEN_HEIGHT
				|| y <= 0)
		{
			onscreen = false;
		}
		return onscreen;
	}
	
}
