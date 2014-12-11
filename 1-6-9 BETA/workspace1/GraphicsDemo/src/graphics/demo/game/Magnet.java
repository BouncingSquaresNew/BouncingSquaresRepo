package graphics.demo.game;

import graphics.demo.framework.Graphics;
import graphics.demo.framework.Pixmap;

import java.util.Random;

import android.graphics.Color;

public class Magnet {

	public double x; //X value of the magnet
	public double y; //Y value of the magnet
	public double xVector; //X-Velocity of the magnet
	public double yVector; //Y-Velocity of the magnet
	public int name; //Each magnet is assigned a name
	public Pixmap magnetImage; //The magnet image is selected
	public double BOUNCE_COEFFICIENT = 1; //The bounce coefficient is the value applied
//	when the magnets hit the edge of the screen
	int glitch = 0; //Whether the magnets are stuck(glitching) see NB(1) in Square.java
	int overEdge = 0; //Whether the magnets are stuck off the edge of the screen
	
	public Magnet(int x, int y, int xVector, int yVector, int name, Pixmap magnetImage)
	{
//		Assigns initial values for the magnets when called
		this.x = x;
		this.y = y;
		this.xVector = xVector;
		this.yVector = yVector;
		this.name = name;
		this.magnetImage = magnetImage;
		this.glitch = 0;
	}
	
	public void recalculate(int imageSquareWidth, int imageSquareHeight)
	{
		x = x + xVector;
		y = y + yVector;
		boolean glitchEdge = false;
/* 		x = UtilConstants.SCREEN_WIDTH * 3;
		y = UtilConstants.SCREEN_HEIGHT * 3;
*/				
		if(overEdge == 0
				|| onScreen() == false) //If the magnet is off the screen
		{
			if((x <= imageSquareWidth) ) //If its lower the 0
			{
				x = 1 + imageSquareWidth; //Set to 1 + the default
				reverseXVector(); //Reverse the vector
				glitchEdge = true; //Set the glitch ocounter + 1
			}
//			ETC for the rest of this statement
			if( x >= UtilConstants.SCREEN_WIDTH - magnetImage.getWidth() - imageSquareWidth)
			{
				x = UtilConstants.SCREEN_WIDTH - magnetImage.getWidth() - 1 - imageSquareWidth;
				reverseXVector(); 
				glitchEdge = true;
			}
			if( y >= UtilConstants.SCREEN_HEIGHT - magnetImage.getHeight() - imageSquareHeight)
			{
				y = UtilConstants.SCREEN_HEIGHT - magnetImage.getHeight() - 1 - imageSquareHeight;
				reverseYVector();
				glitchEdge = true;
			}
			if(y <= imageSquareHeight)
			{
				y = imageSquareHeight + 1;
				reverseYVector();
				glitchEdge = true;
			}
			if(glitchEdge == true)
			{
				overEdge = overEdge + 1;
			}
		}	
		else if(glitchEdge == false 
				&& onScreen() == true)
		{
			overEdge = 0;
		}

	}
	
	public void freeze() //Freezes the magnets at their current position
	{
		xVector = 0;
		yVector = 0;
	}
	
	public void reverseVector()
	{
		reverseXVector();
		reverseYVector();
	}
	
	public void reverseXVector()
	{
		xVector = xVector * -BOUNCE_COEFFICIENT;
	}
	
	public void reverseYVector()
	{
		yVector = yVector * -BOUNCE_COEFFICIENT;
	}
	
	public boolean overlaps(Magnet targetMagnet)
	{
		boolean overlapsMagnet = overlapsGeneric(x, y, targetMagnet.x, targetMagnet.y, magnetImage);
		return overlapsMagnet;
	}
	
	public void collide(Magnet targetMagnet)
	{
			double magnetXVector = xVector;		
			double magnetXVectorTargetMagnet = targetMagnet.xVector;
			xVector = magnetXVectorTargetMagnet;
			targetMagnet.xVector = magnetXVector;
	
			double magnetYVector = yVector;		
			double magnetYVectorTargetMagnet = targetMagnet.yVector;
			yVector = magnetYVectorTargetMagnet;
			targetMagnet.yVector = magnetYVector;
			
			glitch = glitch + 1;
			targetMagnet.glitch = targetMagnet.glitch + 1;
	}
	
	public void collideGeneric(Square targetSquare)
	{
			double magnetXVector = xVector;		
			double squareXVectorTargetSquare = targetSquare.xVector;
			xVector = squareXVectorTargetSquare;
			targetSquare.xVector = magnetXVector;
	
			double magnetYVector = yVector;		
			double squareYVectorTargetSquare = targetSquare.yVector;
			yVector = squareYVectorTargetSquare;
			targetSquare.yVector = magnetYVector;
			
			glitch = glitch + 1;
			targetSquare.glitch = targetSquare.glitch + 1;
	}
	
	public void stickSquare(Square squareJ)
	{
		squareJ.x = x + squareJ.initMagXDif;
		squareJ.y = y + squareJ.initMagYDif;
		squareJ.xVector = xVector;
		squareJ.yVector = yVector;
		squareJ.magnetXCount ++;
		squareJ.magnetYCount ++;
	}
	
	public void drawMagnet(Graphics g)
	{
		g.drawPixmap(magnetImage, (int)x, (int)y);
	}

	public boolean nomNomNom(Square squareJ) 
	{
		boolean nomNomNom = overlapsGeneric(x, y, squareJ.x, squareJ.y, squareJ.image);
		return nomNomNom;
	}
	
	public boolean overlapsGeneric(double X, double Y, double targetX, double targetY, Pixmap targetImage)
	{
		double distX;
		double distY;
		boolean collision = false;
		boolean collisionY = false;
		
		if(X>=targetX)
		{
			distX = X - targetX;
		}
		else
		{
			distX = targetX - X;
		}
		
		if(Y >= targetY)
		{
			distY = Y - targetY;
			if(distY <= targetImage.getWidth())
			{
				collisionY = true;
			}
		}
		else
		{
			distY = targetY - Y;
			if(distY <= magnetImage.getWidth())
			{
				collisionY = true;
			}
		}
		
		if((distX <= magnetImage.getWidth()+1)
				&& collisionY == true)
		{
			collision = true;
		}
		else
		{
			collision = false;
		}
		return collision;
	}

	public void dif(Square squareJ)
	{
		squareJ.initMagXDif = squareJ.x - x;
		squareJ.initMagYDif = squareJ.y - y;
	}
	
	public boolean onScreen()
	{
		boolean onScreen = true;
		if(x >= UtilConstants.SCREEN_WIDTH
				|| x <= 0
				|| y >= UtilConstants.SCREEN_HEIGHT
				|| y <= 0)
		{
			onScreen = false;
			
		}
		return onScreen;
	}

	public void setMagnet(double X, double Y, int XVector, int YVector) 
	{
		x = X;
		y = Y;
		xVector = XVector;
		yVector = YVector;
	}
	
}
