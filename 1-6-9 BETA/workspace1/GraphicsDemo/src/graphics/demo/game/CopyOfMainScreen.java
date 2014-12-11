package graphics.demo.game;

import graphics.demo.framework.Game;
import graphics.demo.framework.Graphics;
import graphics.demo.framework.Graphics.PixmapFormat;
import graphics.demo.framework.Input;
import graphics.demo.framework.Input.TouchEvent;
import graphics.demo.framework.Pixmap;
import graphics.demo.framework.Screen;


import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.Typeface;

/* Name: Intro.java
 * Date: 23/03/2013
 * Author: Aleksandrs Baklanovs
 * Purpose: shows two introductory logos before main menu - Pollux and Squarez
 */



public class CopyOfMainScreen extends Screen { 

	static int NUM_BALLS = ballsInputMethod();
	String[] ballNameNumber = ballNameNumberMethod();
	static int ArrayLength = 400;
	
	//private Logo ball;
	double scrW = UtilConstants.SCREEN_WIDTH;
	double scrH = UtilConstants.SCREEN_HEIGHT;
	
	double[] ballsX = new double[ArrayLength];
	double[] ballsY = new double[ArrayLength];
	
	double[] ballsVectorX = new double[ArrayLength];
	double[] ballsVectorY = new double[ArrayLength];
	
	double accelerationX = 0.05;
	double accelerationY = 0.05;
	
	float accelX = 0;
	float accelY = 0;
	float accelZ = 0;
	
	double bounce = 0.9;
	
	boolean collision = false;
	
	public static Pixmap ball;
			
    public CopyOfMainScreen(Game game) 
    {    	
        super(game);
        
        Random random = new Random();
        
    	Graphics g = game.getGraphics();    	
		ball = g.newPixmap("ball.png", PixmapFormat.ARGB4444);
		Assets.font = Typeface.createFromAsset(game.getAssets(), "fonts/electrolize.ttf");  

       
        for (int b=0;b<NUM_BALLS;b++) {

        	ballsX[b] = random.nextInt(UtilConstants.SCREEN_WIDTH-ball.getWidth())+1;
        	ballsY[b] = random.nextInt(UtilConstants.SCREEN_HEIGHT-ball.getHeight())+1;
        
        	ballsVectorX[b] = random.nextInt(4)+1;
        	ballsVectorY[b] = random.nextInt(4)+1;
        }
        
        for(int i = 0; i < NUM_BALLS;i++)
        {
        	double ballOtherX = 0;
        	double ballOtherY = 0;
        	for(int j = 0 + i + 1; j < NUM_BALLS; j++)
        	{
        		ballOtherX = ballsX[j];
        		ballOtherY = ballsY[j];
        		
	        	if(
	        			((ballsX[i]>=ballOtherX-ball.getWidth())&&ballsX[i]<=ballOtherX+ball.getWidth())
	        			&&
	        			((ballsY[i]>=ballOtherY-ball.getHeight())&&ballsY[i]<=ballOtherY+ball.getHeight())	)
	        	{
	        		while((ballsX[i]>=ballOtherX-ball.getWidth())&&ballsX[i]<=ballOtherX+ball.getWidth())
	        		{
	        			ballsX[i] = ballsX [i] + ball.getWidth() + 1;
	        		}
	        	}
        	}
        	
//        	if(ballsX[i]>=UtilConstants.SCREEN_WIDTH - ball.getWidth() - 1)
//        	{
//        		while((ballsX[i]>=UtilConstants.SCREEN_WIDTH - ball.getWidth() - 1))
//        		{
//        			ballsX[i] = random.nextInt(UtilConstants)
//        		}
//        	}
        	
        }
        
        
        
    }


    
    //The intro part only takes a couple of seconds and isn't skippable 
    @Override

    
    public void update(float deltaTime) 
    {
        Random random = new Random();
        
    	for (int b=0;b<NUM_BALLS;b++) {
        	ballsX[b]+=ballsVectorX[b];
        	ballsY[b]+=ballsVectorY[b];
        }
        
        for (int b=0;b<NUM_BALLS;b++) {
        	
        	int ballWidth = ball.getWidth();
        	int ballHeight = ball.getHeight();
        	
        	if ((ballsX[b] >= (UtilConstants.SCREEN_WIDTH-ballWidth)) || (ballsX[b] <= 0)) 
        	{
        		ballsVectorX[b] = ballsVectorX[b] * -1;
        		ballsVectorX[b] = ballsVectorX[b] * (bounce);
            }
            	
            	
        	if ((ballsY[b] >= UtilConstants.SCREEN_HEIGHT-ballHeight) || (ballsY[b] <= 0)) 
        	{
        		ballsVectorY[b] = ballsVectorY[b] * -1;
        		ballsVectorY[b] = ballsVectorY[b] * (bounce);
        	}
        	
        	//ballsVectorX[b] = ballsVectorX[b] * (1+accelerationX);
        	
        	String X = "X";
        	String Y = "Y";
        	ballsVectorX = gravityMethod(ballsVectorX, X);
        	ballsVectorY = gravityMethod(ballsVectorY, Y);
        }

        double distX = 0;
        double distY = 0;
        for(int c=0;c<NUM_BALLS;c++)
        {
        	for(int d = c + 1; d<NUM_BALLS;d++)
        	{
        		if(ballsX[c]>=ballsX[d])
        		{
        			distX = ballsX[c] - ballsX[d];
        		}
        		else
        		{
        			distX = ballsX[d] - ballsX[c];
        		}
        		
        		if(ballsY[c]>=ballsY[d])
        		{
        			distY = ballsY[c] - ballsY[d];
        		}
        		else
        		{
        			distY = ballsY[d] - ballsY[c];
        		}
        		
        		if((distX <= ball.getWidth()+1) && (distY <= ball.getHeight()+1))
        		{
        			double ballVectorXC = ballsVectorX[c];
        			double ballVectorXD = ballsVectorX[d];
        			ballsVectorX[c] = ballVectorXD;
        			ballsVectorX[d] = ballVectorXC;

        			double ballVectorYC = ballsVectorY[c];
        			double ballVectorYD = ballsVectorY[d];
        			ballsVectorY[c] = ballVectorYD;
        			ballsVectorY[d] = ballVectorYC;
        		}
        	}
        }
        
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int len = touchEvents.size();

        for(int i = 0; i < len; i++) 
        {
                    TouchEvent event = touchEvents.get(i);
                    if(event.type == TouchEvent.TOUCH_UP) 
                    {

                        ballsX[NUM_BALLS] = event.x-(ball.getWidth()/2);
                        ballsY[NUM_BALLS] = event.y-(ball.getHeight()/2);
                        
                        ballsVectorX[NUM_BALLS] = random.nextInt(4)+1;
                        ballsVectorY[NUM_BALLS] = random.nextInt(4)+1;
                        
                        NUM_BALLS = NUM_BALLS + 1;
                        
                    }
        }
        
        for(int i = 0;i<NUM_BALLS; i++)
        {
        		ballsVectorY[i] = gravityCalcMethodY(ballsVectorY[i]);
            	ballsVectorX[i] = gravityCalcMethodX(ballsVectorX[i]);
        }

        
        for(int i = 0; i < NUM_BALLS; i++)
        {
        	if(ballsX[i]<0)
        	{
        		ballsX[i] = 0;
        		ballsVectorX[i] = Math.abs(ballsVectorX[i]);
        	}
        	if(ballsX[i]>UtilConstants.SCREEN_WIDTH - ball.getWidth())
        	{
        		ballsX[i] = UtilConstants.SCREEN_WIDTH - ball.getWidth() - 1;
        		ballsVectorX[i] = -1 * (Math.abs(ballsVectorX[i]));
        	}
//        	
        	if(ballsY[i]<0)
        	{
        		ballsY[i] = 0;
        		ballsVectorY[i] = Math.abs(ballsVectorY[i]);
        	}
        	if(ballsY[i]>UtilConstants.SCREEN_HEIGHT - ball.getHeight())
        	{
        		ballsY[i] = UtilConstants.SCREEN_HEIGHT - ball.getHeight() - 1;
        		ballsVectorY[i] = -1 * (Math.abs(ballsVectorY[i]));
        	}
        }
        
        
//        game.getInput().
        	
        


    }

    //just consists of a help screen image
    @Override
    public void present(float deltaTime) 
    {
        accelX = game.getInput().getAccelX();
        accelY = game.getInput().getAccelY();
        accelZ = game.getInput().getAccelZ();
    	
    	Graphics g = game.getGraphics();      
        g.drawRect(0,0, UtilConstants.SCREEN_WIDTH+1, UtilConstants.SCREEN_HEIGHT+1, Color.WHITE);
        
        for (int b=0;b<NUM_BALLS;b++) {
        	String ballName = ballNameNumber[b];
        	int ballCurrentX = (int) Math.round(ballsX[b]);
        	int ballCurrentY = (int) Math.round(ballsY[b]);
        	g.drawPixmap(ball, (int)ballsX[b], (int)ballsY[b]);
        	g.drawText(ballName, ballCurrentX, ballCurrentY, 10, Color.BLACK);
        }

//        g.drawText("Accel X: " + game.getInput().getAccelX(), 20, 100, 20, Color.BLACK);
//        g.drawText("Accel Y: " + game.getInput().getAccelY(), 20, 120, 20, Color.BLACK);
//        g.drawText("Accel Z: " + game.getInput().getAccelZ(), 20, 140, 20, Color.BLACK);
//        g.drawText("Screenwidth: " + UtilConstants.SCREEN_WIDTH, 20, 180, 20, Color.RED);
//        g.drawText("Screenheight: " + UtilConstants.SCREEN_HEIGHT, 20, 200, 20, Color.RED);

        double angleY = angleMethodY();
        g.drawText("AngleY: " + angleY, 20, 40, 20, Color.BLUE);
        double angleX = angleMethodX();
        g.drawText("AngleX: " + angleX, 20, 60, 20, Color.BLUE);
//        g.drawRect(40, 40, 40, 40, Color.GRAY);
        

    }

    public static double[] gravityMethod(double[] ballsVector, String XY)
    {
    	int Acceleration = 0;
    	if(XY.equals("X"))
    	{
    		//X Acceleration
    		Acceleration = 0;
    	}
    	else if(XY.equals("Y"))
    	{
    		//Y Acceleration
    		Acceleration =  0;
    	}
    	for(int i = 0;i<NUM_BALLS;i++)
    	{
    		if(ballsVector[i]>0)
    		{
    			ballsVector[i] = ballsVector[i] * (1+(Acceleration/100));
	    	}
	    	else if(ballsVector[i]<0)
    		{
    			ballsVector[i] = ballsVector[i] / (1+(Acceleration/100));
    		}
    	}
    	
    	
    	return ballsVector;
    }
    
    public double gravityCalcMethodY(double originalYVector)
    {
    	double gravityVectorY = 0;
    	double angle = 0;
    	angle = angleMethodY();
    	gravityVectorY = gravityCalcGeneric(angle, originalYVector);
    	return gravityVectorY;
    }
    
    public double gravityCalcMethodX(double originalXVector)
    {
    	double gravityVectorX = 0;
    	double angle = 0;
    	angle = angleMethodX();
    	gravityVectorX = gravityCalcGeneric(angle, originalXVector);
    	return gravityVectorX;
    }
    
    public double gravityCalcGeneric(double angle, double originalVectorGeneric)
    {
    	double gravity = 0.1;
    	double returnVectorGeneric = 0;
    	returnVectorGeneric = originalVectorGeneric + ((gravity/10) * angle);
    	return returnVectorGeneric;
    }
    
    public double angleMethodY()
    {
    	double angle = 0;
        accelX = game.getInput().getAccelX();
        accelY = game.getInput().getAccelY();
        accelZ = game.getInput().getAccelZ();
//	    if(accelX <= 0 + 2 && accelX >= -2)
//	    {
	    	if(accelY <= 1 && accelY >= -1)
	    	{
	    		angle = 0;
	    	}
	    	else if(accelZ >= -10 && accelZ < -10 + 1)
	    	{
	    		angle = 0;
	    	}
	    	else if(accelY > 0 + 1 && accelY <= 2) 
	    	{
	    		angle = 2;
	    	}
	    	else if(accelY > 0 + 2 && accelY <= 3) 
	    	{
	    		angle = 3;
	    	}
	    	else if(accelY > 0 + 3 && accelY <= 4) 
	    	{
	    		angle = 4;
	    	}
	    	else if(accelY > 0 + 4 && accelY <= 5) 
	    	{
	    		angle = 5;
	    	}
	    	else if(accelY > 0 + 5 && accelY <= 6) 
	    	{
	    		angle = 6;
	    	}
	    	else if(accelY > 0 + 6 && accelY <= 7) 
	    	{
	    		angle = 7;
	    	}
	    	else if(accelY > 0 + 7 && accelY <= 8) 
	    	{
	    		angle = 8;
	    	}
	    	else if(accelY > 0 + 8 && accelY <= 9) 
	    	{
	    		angle = 9;
	    	}
	    	else if(accelY > 0 + 9 && accelY <= 10) 
	    	{
	    		angle = 10;
	    	}
	    	else if(accelY >= -2 && accelY < -1)
	    	{
	    		angle = -2;
	    	}
	    	else if(accelY >= -3 && accelY < -2)
	    	{
	    		angle = -3;
	    	}
	    	else if(accelY >= -4 && accelY < -3)
	    	{
	    		angle = -4;
	    	}
	    	else if(accelY >= -5 && accelY < -4)
	    	{
	    		angle = -5;
	    	}
	    	else if(accelY >= -6 && accelY < -5)
	    	{
	    		angle = -6;
	    	}
	    	else if(accelY >= -7 && accelY < -6)
	    	{
	    		angle = -7;
	    	}
	    	else if(accelY >= -8 && accelY < -7)
	    	{
	    		angle = -8;
	    	}
	    	else if(accelY >= -9 && accelY < -8)
	    	{
	    		angle = -9;
	    	}
	    	else if(accelY >= -10 && accelY < -9)
	    	{
	    		angle = -10;
	    	}
//	    }
	    return angle;
	    
    }
    
    public double angleMethodX()
    {
        double angleX = 0;
    	double accelX = game.getInput().getAccelX();
        double accelY = game.getInput().getAccelY();
        double accelZ = game.getInput().getAccelZ();
    	if(accelX < -9 && accelX >= -10)
    	{
    		angleX = 10;
    	}
    	if(accelX < -8 && accelX >= -9)
    	{
    		angleX = 9;
    	}
    	if(accelX < -7 && accelX >= -8)
    	{
    		angleX = 8;
    	}
    	if(accelX < -6 && accelX >= -7)
    	{
    		angleX = 7;
    	}
    	if(accelX < -5 && accelX >= -6)
    	{
    		angleX = 6;
    	}
    	if(accelX < -4 && accelX >= -5)
    	{
    		angleX = 5;
    	}
    	if(accelX < -3 && accelX >= -4)
    	{
    		angleX = 4;
    	}
    	if(accelX < -2 && accelX >= -3)
    	{
    		angleX = 3;
    	}
    	if(accelX < -1 && accelX >= -2)
    	{
    		angleX = 2;
    	}
    	if(accelX <= 1 && accelX >= -1)
    	{
    		angleX = 0;
    	}
    	if(accelX <= 2 && accelX > 1)
    	{
    		angleX = -2;
    	}
    	if(accelX <= 3 && accelX > 2)
    	{
    		angleX = -3;
    	}
    	if(accelX <= 4 && accelX > 3)
    	{
    		angleX = -4;
    	}
    	if(accelX <= 5 && accelX > 4)
    	{
    		angleX = -5;
    	}
    	if(accelX <= 6 && accelX > 5)
    	{
    		angleX = -6;
    	}
    	if(accelX <= 7 && accelX > 6)
    	{
    		angleX = -7;
    	}
    	if(accelX <= 8 && accelX > 7)
    	{
    		angleX = -8;
    	}
    	if(accelX <= 9 && accelX > 8)
    	{
    		angleX = -9;
    	}
    	if(accelX <= 10 && accelX > 9)
    	{
    		angleX = -10;
    	}
    	return angleX;
    }
    
    public static int ballsInputMethod()
    {
    	NUM_BALLS = 0;
    	return NUM_BALLS;
    }
    
    public static String[] ballNameNumberMethod()
    {
    	String [] ballNameNumberArray = new String [ArrayLength];
    	for(int i=0;i<ArrayLength;i++)
    	{
    		int j = i+1;
    		ballNameNumberArray[i] = ("" + j);
    	}
    	return ballNameNumberArray;
    }
    


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}