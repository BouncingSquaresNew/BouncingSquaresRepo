package graphics.demo.game;

/* Name: BouncingSquares.java
 * Date: 07/03/2014
 * Author: Benedict Winchester
 * Purpose: Holds all of the code for the main method :)
 */

import graphics.demo.framework.Game; //Framework for the game
import graphics.demo.framework.Graphics; //Framework for drawing objects
import graphics.demo.framework.Graphics.PixmapFormat; //Framework for drawing the pixmap images
import graphics.demo.framework.Input.TouchEvent; //Framework for touch events arrayList
import graphics.demo.framework.Pixmap; //Framework for interprating images
import graphics.demo.framework.Screen; //Framework for the screen size

import java.util.ArrayList; //Imports the ArrayList framework
import java.util.List; //Imports the list framework
import java.util.Random; //Imports the framework for generating random variables

import android.graphics.Color; //A standard list of colors is imported
import android.graphics.Typeface; //The type face fonts are imported from the res folder
/* NB These can be altered by placing the .ttf file in the /res folder and changing NB(1)
 * Notes: NOTE denotes code to be corrected,
 * FIX denotes code which requires immediate attention
 * NB denotes important comments for code readers
 * OBS denotes obsolete code or variables which were either removed, or will be :) */

public class BouncingSquares extends Screen { 

//	Variable decleration
	private static int MAX_NUM_SQUARES = 100; //The max score before you win
	private static String versionNumber = "1.7.2A"; //The current app version number
	private int developer = 0; //Whether the nightly toggle is enabled
	private int nightly = 0; //Whether nightly mode is enabled
	private boolean magnetEnable = false; //Whether the magnets are enabled
	private boolean christmas = false; //Whether to enable the Christmas theme
    private static int DEFAULT_NUM_SQUARES = 0; //The initial number of squares to begin with
	private int difLevel = 0; //The current level (0 = endless, 1 = times)
	private static Typeface font;
	
	private static int MAX_INITIAL_SPEED = 4; //The maximum starting speed of the squares
    private static int NUM_MENU_BUTTONS = 5;
    private static int NUM_TOGGLE_BUTTONS = 2;
    private static int NUM_STRINGS = 9;
    private int height = 960;
    private int width = 640;
    private int FONT_SIZE = 40;
    private int menuWidth = width / 3; //The width of the menu is initialised
    private int menuHeight = 60; //The height of the menu is initialised NOTE to self, why not set?
    private int score  = 0; //The initial score
    private int highScore = 0; //The highscore value is initalised
    private int highTime = 0; //The fastest time they've completed it in is initialised
    private int currentNumSquares = DEFAULT_NUM_SQUARES; //The current number of squares that exist
    private int maxNumSquares = DEFAULT_NUM_SQUARES; //The maximum ever number of squares
    private static int maxNumMagnets = 3; //This is used to determine the number and position of the magnets
	/* This can be changed in order to easily alter the number of magnet objects */
    private int toggleWidth = 80; //The dimensions of the nightly toggles
    private int localCheckCurrent = 0; //The current location for the touch Event
    private int selectedElasticityValue = 0; //The current value to be taken from the Elasticity Array, and to be set to the squares
    private int keplerMode = 0; //Whether to enable planetary motion: 0 = Earth, 1 = Space, 2 = Solar
    private static int STROKE_WIDTH = 5;
    private double timerDown = 30; //The time limit allocated for the user to reach the MaxScore (MAX_NUM_SQUARES)
    private double gravityConstant = 0.1; //The screen's gravity constant
    private double keplerFreeSquareGravityConstant = 10; //The Newton's G constant
    private double solarMass = 50;
    private static double e = 0.95; //The coefficient of restitution between the squares
    private boolean touchTrueAlpha = false; //A variable is initialised, which will determine whether a square was pressed
    private boolean touchTrueBeta = false; //The same as for Alpha, but for determining whether a magnet was pressed
    private boolean errorA; //Used for debugging, NOTE Currently obsolete
    private boolean invert = false; //Whether to invert the colors
    private boolean pro1 = false; //The following 4 "pro" toggles are used for allowing developer mode
    private boolean pro2 = false;
    private boolean pro3 = false;
    private boolean pro4 = false;
    private boolean win = false; //Whether the user has won (Initialised)
    private boolean fail = false; //Whether the user has lost (Initialised)

//  These are switches to determine the current mode
    private int switchInt = 0;
	
//	This generates a new ArrayList for the values of the square objects
    private ArrayList<Object> shapes = new ArrayList<Object>(); //An array list containing squares, and eventually, all free moving objects
	private ArrayList<Object> stars = new ArrayList<Object>(); //An array list containing all of the stars which are fixed
	private ArrayList<Integer> removers = new ArrayList<Integer>(); //The arraylist for removing objects
    private ArrayList<MenuButton> menus = new ArrayList<MenuButton>();
    private ArrayList<ToggleButton> toggles = new ArrayList<ToggleButton>();
    private ArrayList<DrawableString> developerstrings = new ArrayList<DrawableString>();
    private ArrayList<DrawableString> userstrings = new ArrayList<DrawableString>();
	private double[] Elasticity = new double[3]; //The array list containing the values for the elasticity
	
//	This generates the images for the objects
	private Graphics g = game.getGraphics();
    private static Pixmap image; //The image of the square
    private static Pixmap smallimage; //The image of the smaller keperian squares
    private static Pixmap magnetImage; //The image of the magnet (obsolete, NOTE as the magnets are rectangles)
    private static Pixmap backdrop;	// The image for the background
    private static Pixmap sun; //The image for the sun in "solar system" mode
	
    public BouncingSquares(Game game) 
    {    	
        super(game); //Initialises the game
        setupDefaultElasticity();
        
        // This assigns the values for the images from the assets file
    	image = g.newPixmap("ball.png", PixmapFormat.ARGB4444);
    	smallimage = g.newPixmap("smallball.png", PixmapFormat.ARGB4444);
		magnetImage = g.newPixmap("ball3.png", PixmapFormat.ARGB4444);
		backdrop = g.newPixmap("backdrop.png", PixmapFormat.ARGB4444);
		sun = g.newPixmap("sun.png", PixmapFormat.ARGB4444);

        // Grabs the typeface from the assets file
		font = Typeface.createFromAsset(game.getAssets(), "fonts/roboto-light.ttf");

        // This assigns the initial values for the squares coordinates and vectors.
        for (int b=0;b<currentNumSquares;b++) //So long as there are few squares than the maximum that can be initialised
        {
        	initialSquares(b);
        }
        Star starSol = new Star (sun, solarMass, width, height);
        stars.add(starSol);

        createButtons();
        createStringsNightly();
        createStringsNormal();

//	This makes sure that none of the squares are initially colliding, and then adjusts their positioins to compensate.
        initialOverlapPrevention();
    }

    private void createStringsNightly()
    {
        for(int i = 0; i < NUM_STRINGS; i++)
        {
            DrawableString newDrawableString = new DrawableString(FONT_SIZE, (i + 2) * FONT_SIZE, font, FONT_SIZE, STROKE_WIDTH, 255, 87, 34);
            developerstrings.add(newDrawableString);
        }
        DrawableString drawableString0 = developerstrings.get(0);
        drawableString0.setFixedText("AngleY: ");

        DrawableString drawableString1 = developerstrings.get(1);
        drawableString1.setFixedText("AngleX: ");

        DrawableString drawableString2 = developerstrings.get(2);
        drawableString2.setFixedText("Shapes: ");

        DrawableString drawableString3 = developerstrings.get(3);
        drawableString3.setFixedText("Success: ");

        DrawableString drawableString4 = developerstrings.get(4);
        drawableString4.setFixedText("Score: ");

        DrawableString drawableString5 = developerstrings.get(5);
        drawableString5.setFixedText("[MAX]: ");

        DrawableString drawableString6 = developerstrings.get(6);
        drawableString6.setFixedText("LocalCheck: ");

        DrawableString drawableString7 = developerstrings.get(7);
        drawableString7.setFixedText("Elasticity: ");

        DrawableString drawableString8 = developerstrings.get(8);
        drawableString8.setCoordinates(FONT_SIZE, height - FONT_SIZE);
        drawableString8.setFixedText("v");
        drawableString8.setVarText(versionNumber + " :)");
        drawableString8.setFontSize((int)1.5 * FONT_SIZE);
    }

    private void createStringsNormal()
    {
        DrawableString drawableStringUser = new DrawableString (FONT_SIZE, height / 2, font, "SQUARE", 3 * FONT_SIZE, 3 * STROKE_WIDTH, 0, 105, 92);
        userstrings.add(drawableStringUser);

        DrawableString drawableStringNumSquares = new DrawableString (FONT_SIZE, (int)(FONT_SIZE * 1.5), font, "", (int)(1.5 * FONT_SIZE), 3 * STROKE_WIDTH, 0, 105, 92);
        userstrings.add(drawableStringNumSquares);

        DrawableString drawableStringMaxNumSquares = new DrawableString (FONT_SIZE, (int)(FONT_SIZE * 2.5), font, "[MAX] ", FONT_SIZE, 3 * STROKE_WIDTH, 0, 105, 92);
        userstrings.add(drawableStringMaxNumSquares);

        DrawableString drawableVersionNumberString = new DrawableString(FONT_SIZE, height - FONT_SIZE, font, "v", (int)(1.5 * FONT_SIZE), STROKE_WIDTH, 0, 105, 92);
        drawableVersionNumberString.setVarText(versionNumber + " :)");
        userstrings.add(drawableVersionNumberString);
    }

    private void createButtons()
    {
        for (int i = 0; i < NUM_MENU_BUTTONS; i++)
        {
            MenuButton menuButtonNew = new MenuButton (width - menuWidth, height - menuHeight * (i + 1), menuWidth, menuHeight, 0, 105, 92, "VOID");
            menus.add(menuButtonNew);
        }
        for (int i = 0; i < NUM_TOGGLE_BUTTONS; i++)
        {
            ToggleButton toggleButtonNew = new ToggleButton (width - (i + 1) * toggleWidth, 0, toggleWidth, toggleWidth, 0, 105, 92);
            toggles.add(toggleButtonNew);
        }
        MenuButton menuButton0 = menus.get(0);
        menuButton0.text = "B WINCH";

        MenuButton menuButton1 = menus.get(1);
        menuButton1.text = "CLEAR";

        MenuButton menuButton2 = menus.get(2);
        menuButton2.text = "TIME MODE";

        MenuButton menuButton3 = menus.get(3);
        menuButton3.text = "FREEZE";

        MenuButton menuButton4 = menus.get(4);
        menuButton4.text = "LOCATION";

        ToggleButton toggleButton0 = toggles.get(0);
        toggleButton0.label = "N";

        ToggleButton toggleButton1 = toggles.get(1);
        toggleButton1.label = "e";
    }

//	The intro part only takes a couple of seconds and isn't skippable 

	@Override 
    public void update(float deltaTime) 
    {
        update(); //The main update method
//      This is the touch event handling code
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents(); //The arraylist is defined
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) 
        {
        	TouchEvent event = touchEvents.get(i); //The touch events are cycled through
            if((event.type == TouchEvent.TOUCH_UP)) //If the event was a touch up event
            		// NB Here, touch down events didn't work, as a drag was interprated
	        {
//      If The Touch Event is definitely a Touch UP
           		localCheckCurrent = toggleCheck(event.x, event.y);
//           	If no toggles were pressed
            	if(localCheckCurrent == 0) //If no toggles were pressed
				{
            		developerToggle(event.x, event.y); //Check to see whether to toggle developer mode
//					Quite a long bit of code, NOTE to self, edit it down :)
				
//            		If there are no squares currently, then
//This code works if there are no squares (make a new square)
             		if(shapes.size() == 0)
            		{
             			shapesAddSquare(event.x, event.y); //Adds the square
            		}
//            		If there is atleast 1 shape,
//            		And clear is set to false (no squares off screen)
             		else if(shapes.size() >= 1)
	            	{
	            		touchTrueAlpha = false;
	            		touchTrueBeta = false;
//	            		This checks to see whether any squares
//	            		were clicked on
//	            		If a square was clicked on, then increase the score
	           			squaresTouched(event.x, event.y);
//	            		If no squares were clicked on, check for magnet events
//	            		If a magnet was pressed, cycle through this obsolete code
//    	            	Check to see wheter any magnets were pressed
	            	}
				}
	        }
        }
    }

    //just consists of a help screen image
    @Override
    public void present(float deltaTime) 
    {
    	int timerDownInt = (int)timerDown; //Calculates the timer value to 2 dp
		int timerDownDec = (int)((timerDown - timerDownInt) * 100);
    	updateDraw(timerDownInt, timerDownDec);        
//      This code draws the squares onto the screen
        
    }
    
    public void initialSquares(int b)
    {
//    	Generate random vectors and positions for the squares
    	Random random = new Random(); //Generates a new set list of random numbers
    	int x = random.nextInt(width-image.getWidth())+1;
    	int y = random.nextInt(height-image.getHeight())+1;
        shapesAddSquare(x, y);
    }
    
    public static double restitution()
    {
    	return e;
    }
    
    public void update() //The main updater method
    {
//    	This code recalculates the squares' positions and their bouncing coefficients
        for(int i = 0; i < shapes.size(); i++) //The squares are cycled through
        {
        	Square squareI = (Square)shapes.get(i);
            squareI.recalculate(width, height); //The squares are then re-calculated
            gravityCalcMethod(squareI); //Calculates the gravity factors acting on the squares
        }
//    	This loops through all of the squares and applies the square and kepler logic
        Star starSol = (Star)stars.get(0);
        for(int i = 0 ; i < currentNumSquares; i ++)
        {
        	squareCollideSquareLogic(i);
        	if(keplerMode == 1) //Apply kepler motion if enabled
        	{
        		keplerianFreeSquare(i);
        	}
        	if(keplerMode == 2)
        	{
        		keplerianSolarSystemSquare(i, starSol);
        	}
        }
        
//      Check whether any shapes have crashed into the sun
        for(int i = 0 ; i < removers.size() ; i ++)
        {
        	int removerID = (int)removers.get(i);
        	shapes.remove(removerID - i);
        	currentNumSquares = currentNumSquares - 1;
        }
        removers.clear();

//		Recalculates the squares gravity factors
//		This code is for the magnet collisions

//		This will enable developer mode if the combination is selected
        developer();
        
//      This is the counting down code
		countDown();

//		This determins wheter the user has won (has exceeded the maximum score limit set)
//		Or whether they've lost (failed to win in the time limit
		win();
		fail();
		highscore(); //Determines the new highscore
		hightime(); //Determines the fastest time they've won in
		shapeMax(); //Updates the maximum number of squares
        updateMenus();
        updateStrings();
    }

    private void updateStrings()
    {
        DrawableString drawableStringAngleX = developerstrings.get(0);
        String angleX = "" + angleMethodX();
        drawableStringAngleX.setVarText(angleX);

        DrawableString drawableStringAngleY = developerstrings.get(1);
        String angleY = "" + angleMethodY();
        drawableStringAngleY.setVarText(angleY);

        DrawableString drawableStringShapes = developerstrings.get(2);
        String numShapesToDraw = "" + shapes.size();
        drawableStringShapes.setVarText(numShapesToDraw);

        DrawableString drawableStringSuccess = developerstrings.get(3);
        String successToDraw = "" + "VOID";
        drawableStringSuccess.setVarText(successToDraw);

        DrawableString drawableStringScore = developerstrings.get(4);
        String scoreToDraw = "" + score;
        drawableStringScore.setVarText(scoreToDraw);

        DrawableString drawableStringMAX = developerstrings.get(5);
        String maxNumSquaresToDraw = "" + maxNumSquares;
        drawableStringMAX.setVarText(maxNumSquaresToDraw);

        DrawableString drawableStringLocalCheck = developerstrings.get(6);
        String localCheckToDraw = "" + localCheckCurrent;
        drawableStringLocalCheck.setVarText(localCheckToDraw);

        DrawableString drawableStringElasticity = developerstrings.get(7);
        String elasticityToDraw = "" + Elasticity[selectedElasticityValue];
        drawableStringElasticity.setVarText(elasticityToDraw);

        DrawableString stringUserCurrentNumSquares = userstrings.get(1);
        stringUserCurrentNumSquares.setVarText("" + currentNumSquares);
    }

    private void updateMenus()
    {
        updateDifButton(2);
        updateKeplerButton(4);
    }

    private void updateDifButton(int i)
    {
        MenuButton menuButtonDifficulty = menus.get(i);
        switch(keplerMode)
        {
            case 0 :
                menuButtonDifficulty.text = "ENDLESS";
                break;
            case 1 :
                menuButtonDifficulty.text = "TIMED";
                break;
        }
    }

    private void updateKeplerButton(int i)
    {
        MenuButton menuButtonKepler = menus.get(i);
        switch(keplerMode)
        {
            case 0 :
                menuButtonKepler.text = "EARTH";
                break;
            case 1 :
                menuButtonKepler.text = "SPACE";
                break;
            case 2 :
                menuButtonKepler.text = "SOLAR";
                break;
        }
    }

    public int toggleCheck(int eventX, int eventY)
    {
    	int localCheck = localeCheck(eventX, eventY); //The location method is called with the event coordinates, to determine the event position
/* Local Check Values for the touch Event location
 * 0 = No toggles pressed
 * 1 = Clear Squares Toggle
 * 2 = Nightly Toggle
 * 3 = Magnet Activate Toggle
 * 4 = Invert Colors Easteregg
 * 5 = Third Toggle from Right
 * 6 = DifLevel Toggle
 * 7 = Forth Toggle from Right
 * 8 = Timer Toggle
 * 9 = Fifth Toggle from Right
 */
//      Get the Menus
        MenuButton menuButtonInvert = menus.get(0);
        MenuButton menuButtonClear = menus.get(1);
        MenuButton menuButtonDifficulty = menus.get(2);
        MenuButton menuButtonFreeze = menus.get(3);
        MenuButton menuButtonKepler = menus.get(4);
        ToggleButton toggleButtonNightly = toggles.get(0);
        ToggleButton toggleButtonElasticity = toggles.get(1);

//		If the clear toggle was pressed, clear both the squares and the magnets
   		if(menuButtonClear.isPressed(eventX, eventY)) //If the clear button was pressed
   		{
			clear(true, true);
   		}
//    	If the nightly toggle was pressed, then change the mode
    	if(toggleButtonNightly.isPressed(eventX, eventY))
    	{
    		nightlyToggle();
            nightlyMenuButtonToggle(nightly);
            nightlyToggleButtonToggle(nightly);
   		}
//    	If developer mode is not on, then set the location to 0
   		else if(localCheck == 2 && developer == 0)
    	{
   			localCheck = 0;
   		}
// 		If the magnet button was pressed, generate the magnets
		else if(localCheck == 3
		&& developer == 0)
		{
			localCheck = 0; //If developer mode isn't on, then set the location to 0
		}
//    	If the easterEgg was pressed, change the color inversion
    	else if (menuButtonInvert.isPressed(eventX, eventY))
    	{
    		invert();
    	}
//    	If the gravity toggle was pressed, enable planetary gravity
    	else if(localCheck == 5
    	&& developer == 1)
    	{
    		keplerModeToggle();
    	}
    	else if(localCheck == 5
    	&& developer == 0)
    	{
    		localCheck = 0;
    	}
//    	If the kepler toggle was pressed, toggle the kepler toggle mode
    	else if(menuButtonKepler.isPressed(eventX, eventY))
    	{
    		keplerModeToggle(); //Toggle the difficulty between 0 and 2
    	}
    	else if(localCheck == 7
    	&& developer == 0)
    	{
    		localCheck = 0;
    	}
//    	If the mode toggle was pressed, the level will be increased, and the timer
//    	started, meaning that they now have 30 seconds
		if(menuButtonDifficulty.isPressed(eventX, eventY))
		{
			levelToggle(); //Toggles the level with pre-adjusted settings
		}
//		If the elasticity toggle was pressed, then alter the elasticity of the squares
		if(toggleButtonElasticity.isPressed(eventX, eventY))
		{
			elasticityCycle();
		}
		else if(localCheck == 9
		&& developer == 0)
		{
			localCheck = 0;
		}
//    	If the freeze toggle was pressed, freeze the shapes
		if(menuButtonFreeze.isPressed(eventX, eventY))
		{
			freeze(true, true);
		}
		return localCheck;
    }

    private void nightlyToggleButtonToggle(int nightly)
    {
        for(int i = 0; i < toggles.size(); i++)
        {
            ToggleButton toggleButtonI = toggles.get(i);
            if(nightly == 1)
            {
                toggleButtonI.setColor(255, 87, 34);

            }
            else if(nightly == 0)
            {
                toggleButtonI.setColor(0, 105, 92);
            }
        }
    }

    private void nightlyMenuButtonToggle(int nightly)
    {
        for(int i = 0; i < menus.size(); i++)
        {
            MenuButton menuButtonI = menus.get(i);
            if(nightly == 1)
            {
                menuButtonI.setColor(255, 87, 34);

            }
            else
            {
                menuButtonI.setColor(0, 105, 92);
            }
        }
    }

    public void shapesAddSquare(int eventX, int eventY)
    {
    	Random random = new Random(); //Generates a new set list of random numbers
    	double Ax = eventX - (image.getWidth()/2);
		double Ay = eventY - (image.getHeight()/2);
		double AxVector = random.nextInt(2 * MAX_INITIAL_SPEED) - MAX_INITIAL_SPEED;
	 	double AyVector = random.nextInt(2 * MAX_INITIAL_SPEED) - MAX_INITIAL_SPEED;
		int name = shapes.size() + 1;
        double elasticity = Elasticity[selectedElasticityValue];
		if(keplerMode == 0)

		{
			Square squareA = new Square(Ax, Ay, AxVector, AyVector, name, image, 0, elasticity, elasticity);
	       	shapes.add(squareA);
            shapes.size();
		}
		else if(keplerMode == 1
				|| keplerMode == 2)
		{
			Square squareB = new Square(Ax, Ay, AxVector, AyVector, name, smallimage, 0, elasticity, elasticity);
	       	shapes.add(squareB);
		}
		currentNumSquares = currentNumSquares + 1;
		
    }
    
    public boolean touchShape (double squareX, double squareY, int width, int height, int touchX, int touchY)
    {
    	boolean touchShape = false;
    	if(touchX >= squareX 
    			&& touchX <= squareX + width
    			&& touchY >= squareY
    			&& touchY <= squareY + height)
    	{
    		touchShape = true;
    	}
    	return touchShape;
    }
    
    public void squaresTouched(int eventX, int eventY)
    {
    	touchTrueAlpha = false;
		for(int k = 0; k <= shapes.size() - 1; k++)
		{
			touchTrueAlpha = touchedShape(k, eventX, eventY);
		}
//		If a square was clicked on, then increase the score
		if(touchTrueAlpha == true)
		{
			score = score + 1;
		}
		if(touchTrueAlpha == false)
		{
			touchTrueBeta = false;
			if(touchTrueBeta == false)
			{
				shapesAddSquare(eventX, eventY);
				errorA = false;
	            score = score -1;
			}
		}
    }
    
    public int localeCheck(int eventX, int eventY)
    {
    	int localCheck = 0;
    	if(eventX >= width - menuWidth
    			&& eventY >= height - menuHeight * 2
    			&& eventY <= height - menuHeight)
    	{
    		localCheck = 1; // The clear squares menu
    	}
    	if(eventX >= width - toggleWidth
    			&& eventY <= toggleWidth)
    	{
    		localCheck = 2; //The nightly toggle
    	}
    	if(eventX >= width - menuWidth
    			&& eventY <= height - menuHeight * 5
    			&& eventY >= height - menuHeight * 6)
    	{
    		localCheck = 3; //The Magnet Activate Toggle
    	}
    	if(eventX >= width - menuWidth
    			&& eventY >= height - menuHeight)
    	{
    		localCheck = 4; //The Color Inversion EasterEgg Toggle
    	}
    	if(eventX >= width - 3 * toggleWidth
    			&& eventX <= width - 2 * toggleWidth
    			&& eventY <= toggleWidth)
    	{
    		localCheck = 5; //Third Toggle in from Right
    	}
    	if(eventX >= width - menuWidth
    			&& eventY >= height - 5 * menuHeight
    			&& eventY <= height - 4 * menuHeight)
    	{
    		localCheck = 6; // The DifLevel Toggle
    	}
//    	if(eventX >= width - (toggleWidth * 4)
//    			&& eventX <= width - (toggleWidth * 3)
//    			&& eventY <= toggleWidth)
//    	{
//    		localCheck = 7; //Forth Toggle in pressed
//    	}
		if(eventX >= width - menuWidth
				&& eventY >= height - menuHeight * 3
				&& eventY <= height - menuHeight * 2)
		{
			localCheck = 8; //Timer toggle
		}
//		if(eventX >= width - 5 * toggleWidth
//				&& eventX <= width - 4 * toggleWidth
//				&& eventY <= toggleWidth)
//		{
//			localCheck = 9; //Fifth Toggle from the Right
//		}
		if(eventY >= height - 4 * menuHeight
		&& eventY <= height - 3 * menuHeight
		&& eventX >= width - menuWidth)
		{
			localCheck = 10; //Freeze menu toggle
		}
    	return localCheck;
    }
    
    public static Square teleport(Square squareTeleport, int width, int height)
    {
    	squareTeleport.x = width * 3;
    	squareTeleport.y = height * 3;
    	squareTeleport.xVector = 0;
    	squareTeleport.yVector = 0;
    	return squareTeleport;
    }
    
    public void highscore()
    {
    	if(score >= highScore)
        {
        	highScore = score;
        }
    }
    
    public void freeze(boolean shapeFreeze, boolean magnetFreeze)//Used to freeze the objects
    {
    	for(int i = 0; i < currentNumSquares; i++) //Cycles through the squares
    	{
    		Square squareI = (Square)shapes.get(i);
    		squareI.freeze();
    	}
    }
    
    public void hightime()
    {
    	if(win == true
    	&& timerDown >= highTime)
    	{
    		highTime = (int)timerDown;
    	}
    }
    
    public void shapeMax()
    {
    	if (currentNumSquares > maxNumSquares)
    	{
    		maxNumSquares = currentNumSquares;
    	}
    }
    
    public void gravityCalcMethod(Square squareI)
    {
        squareI.xVector = gravityCalcMethodX(squareI.xVector);
       	squareI.yVector = gravityCalcMethodY(squareI.yVector);
    }
    
    public double gravityCalcMethodY(double originalYVector)
    {    	
    	double gravityVectorY = 0;
    	double angle = 0;
    	angle = angleMethodY();
    	gravityVectorY = gravityCalcGeneric(angle, originalYVector);
    	return gravityVectorY;
    }
    
	public void squareCollideSquareLogic(int i)
	{
		Square squareI = (Square)shapes.get(i);
    	boolean glitching = false;
    	for(int j = i + 1 ; j < currentNumSquares; j++)
       	{
       		Square squareJ = (Square)shapes.get(j);
//	Now, this will test to see whether the squares overlap
       		if(squareI.overlaps(squareJ) == true
       		&& squareI.glitch == 0
    		&& squareJ.glitch == 0)
       		{
//If they overlap and their counter is zero,
//	Then the collision logic applies and 1 is added to the counter
        		squareI.collide(squareJ);
       			glitching = true;
       		}
//1 is then added to the counter
       		else if(squareI.overlaps(squareJ) == true)
       		{
        		glitching = true;
       		}
    	}
//If the square collides with no other squares, its counter
//is reset to 0
    	if(glitching == false)
    	{
    		squareI.glitch = 0;
    	}
    
	}
	
	public void initialOverlapPrevention()
	{
		for(int i = 0; i < currentNumSquares;i++) //SquareI is cycled
        {
    		Square squareI = (Square)shapes.get(i);
        	for(int j = 0 + i + 1; j < currentNumSquares; j++) //SquareJ is cycled through the other squares
        	{
        		Square squareJ = (Square)shapes.get(j);
//	        	NOTE change to the .overlap method so as to reduce code
        		while(squareI.overlaps(squareJ) == true)
	        	{
	        		squareI.x = squareI.x + image.getWidth() + 1; //Hence, if the squares overlap, they are moved until they no longer do so
	        		// NOTE Errors could arrise here due to the squares being re-positioned offscreen, FIX
	        	}
        	}
        }
	}

	
//	This method is for the squares floating freely, but attracted to eachother by gravitational froces
	public void keplerianFreeSquare(int i)
	{
		Square squareI = (Square)shapes.get(i); //Get square I from the ArrayList
		for (int j = i + 1 ; j < currentNumSquares ; j++)
		{
			Square squareJ = (Square)shapes.get(j); //Get squareJ from the arraylist
//			Calculate the distance between the squares
			double distX = squareI.x - squareJ.x; //Dist in X
			if(distX < 10)
			{
				distX = 10;
			}
			double distY = squareI.y - squareJ.y; //Dist in Y, where the positive value is if I is downright of J
			if(distY < 10)
			{
				distY = 10;
			}
			double distT = Math.sqrt(distX * distX + distY * distY); //Calculate the actual distance between them
			double accelG = keplerFreeSquareGravityConstant * squareI.mass * squareJ.mass / (distT * distT); //And use this to calculate the gravitational accel
			double theta = Math.atan(distY / distX); //Now calculate the angle between them
			double xAccel = accelG * Math.cos(theta); //And use this to calculate the X Acceleration
			double yAccel = accelG * Math.sin(theta); //And the Y Acceleration
			
//			If I is to the Right of J, 
			if(squareI.x > squareJ.x)
			{
				squareI.xVector = squareI.xVector - xAccel; //Then accelerate I to the left
				squareJ.xVector = squareJ.xVector + xAccel; //And J to the right
			}
			else if(squareI.x < squareJ.x) //Otherwise, if I is to the left of J
			{
				squareI.xVector = squareI.xVector + xAccel; //Accelerate I to the right
				squareJ.xVector = squareJ.xVector - xAccel; //And J to the left
			}
			
			if(squareI.y > squareJ.y) //If I is above J
			{
				squareI.yVector = squareI.yVector - yAccel; //Then I should accelerate downwards
				squareJ.yVector = squareJ.yVector + yAccel; //And J should accelerate upwards
			}
			else if(squareI.y < squareJ.y) //But if I is below J
			{
				squareI.yVector = squareI.yVector + yAccel; //Then I should accelerate upwards
				squareJ.yVector = squareJ.yVector - yAccel; //And J should acclrate downwards
			}
		}
	}
	
//  This method is for the squares in a solar systemesque layout
	public void keplerianSolarSystemSquare(int i, Star starSol)
    {
//    	Do the stuff
		keplerianFreeSquare(i); //Will call the free-floating method
    	Square squareI = (Square)shapes.get(i);
    	double distX = squareI.x - starSol.gravityX;
    	double distY = squareI.y - starSol.gravityY;
    	double distT = Math.sqrt(distX * distX + distY * distY);
    	double theta = Math.atan(distY / distX);
    	double tAccel = starSol.mass * squareI.mass * keplerFreeSquareGravityConstant / (distT * distT);
    	double xAccel = Math.abs(tAccel * Math.cos(theta));
    	double yAccel = Math.abs(tAccel * Math.sin(theta));
    	if(squareI.x > starSol.gravityX)
		{
			squareI.xVector = squareI.xVector - xAccel; //Then accelerate I to the left
		}
		else if(squareI.x < starSol.gravityX) //Otherwise, if I is to the left of J
		{
			squareI.xVector = squareI.xVector + xAccel; //Accelerate I to the right
		}
		if(squareI.y > starSol.gravityY)//If I is above J
		{
			squareI.yVector = squareI.yVector - yAccel; //Then I should accelerate downwards
		}
		else if(squareI.y < starSol.gravityY) //But if I is below J
		{
			squareI.yVector = squareI.yVector + yAccel; //Then I should accelerate upwards
		}
//      But if a square crashes into the sun: DELETE IT!!!
    	if(Math.abs(distX) < sun.getWidth() / 2
    		&& Math.abs(distY) < sun.getHeight() / 2 )
    	{
    		starSol.mass = starSol.mass + squareI.mass;
    		removers.add(i);
    	}
    }
	
	public double gravityCalcMethodX(double originalXVector)
    {
    	double gravityVectorX = 0;
    	double angle = 0;
    	angle = angleMethodX();
    	gravityVectorX = gravityCalcGeneric(angle, originalXVector);
    	return gravityVectorX;
    }
    
    public void difToggle(int lower, int higher)
    {
    	if(difLevel == lower)
		{
			difLevel = higher;
		}
		else if(difLevel == higher)
		{
			difLevel = lower;
        }
    }
    
    public void keplerModeToggle()
    {
    	shapes.clear();
    	currentNumSquares = 0;
    	if(keplerMode == 2)
    	{
    		keplerMode = 0;
    	}
    	else if(keplerMode == 1)
    	{
    		keplerMode = 2;
    	}
    	else if(keplerMode == 0)
    	{
    		keplerMode = 1;
    	}
    }
    
    public void clear(boolean shapesClear, boolean magnetClear) //Clears the array lists
    {
    	if(shapesClear == true)
    	{
    		shapes.clear(); //Clear the squares
    		currentNumSquares = 0; //Set the squareCount to zero
    	}
    }
    
    public double gravityCalcGeneric(double angle, double originalVectorGeneric)
    {
    	double returnVectorGeneric = 0;
    	returnVectorGeneric = originalVectorGeneric + ((gravityConstant/10) * angle);
    	return returnVectorGeneric;
    }
    
    public double angleMethodY()
    {
    	double angle = 0;
        float accelY = game.getInput().getAccelY();
        float accelZ = game.getInput().getAccelZ();

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

	    return angle;
	    
    }
    
    public double angleMethodX()
    {
        double angleX = 0;
    	double accelX = game.getInput().getAccelX();
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
    
    public boolean touchedShape(int k, int eventX, int eventY) //Checks to see whether a square was pressed
    {
    	Square squareK = (Square)shapes.get(k);
		boolean touchShape = touchShape(squareK.x, squareK.y, image.getWidth(), image.getHeight(), eventX, eventY);
		if(touchShape == true)
		{
			touchTrueAlpha = true;
			errorA = true;
//			shapes.remove(squareK);
		}
		return touchTrueAlpha; //Returns a boolean value
    }
    
    public void developerToggle(int eventX, int eventY) //Decides whether to enable the developer mode
    {
//    	If the bottom left corner is pressed, enable pro1
    	if(eventX <= toggleWidth
    	&& eventY >= height - toggleWidth
    	&& pro1 == false)
    	{
    		pro1 = true;
    	}
//    	If the next event is in the top left, enable pro2, else, disable pro1
    	if(eventX <= toggleWidth
    	&& eventY <= toggleWidth
    	&& pro1 == true
    	&& pro3 == false)
    	{
    		pro2 = true;
    	}
//    	If the next event is in the bottome left again, enable pro3, else, disable pro2 and pro 1.
    	else if(eventX <= toggleWidth
    	&& eventY >= height - toggleWidth
    	&& pro2 == true)
    	{
    		pro3 = true;
    	}
//    	If the last event is in the top left again, enable pro4, and cycle through the developer method
    	else if(eventX <= toggleWidth
    	&& eventY <= toggleWidth
    	&& pro3 == true)
    	{
    		pro4 = true;
    	}
    }

    /**
     * This method will select a new value for coefficient of restitution to use
     * for future collisions.
     */
    public void elasticityCycle()
    {
        selectedElasticityValue++;
        if(selectedElasticityValue == Elasticity.length )
		{
			selectedElasticityValue = 0;
		}

		for(int k = 0; k < shapes.size(); k++)
		{
			Square squareK = (Square)shapes.get(k);
			squareK.setBounceCoefficient(Elasticity[selectedElasticityValue]);
		}
    }
    
    public void levelToggle() //Toggles the levels through those available (currently 0 and 1 for stable)
    {
		difSet(2, 1); //Sets the difficulty level down to 1 if above 1
		difToggle(0, 1); //Toggles the mode
		timeSet(difLevel); //Sets the timer mode according to the level
    }
    
    public void difSet(int over, int setTo) //Sets the dificulty level value down to those for the stable release toggle
    {
    	if(difLevel >= over) //If the level is over that for the timer, reduce it
    	{
    		difLevel = setTo;
    	}
    }
    
    public void timeSet(int dificulty) //This will set the timer according to the level specified
    {
    	if(difLevel == 1) //If the dificulty level is 1
    	{
    		timerDown = 30; //Reset the timer
    		win = false; //The user hasn't won
    		fail = false; //Or lost, yet.. :)
    		score = 0; //Set the score to 0 so that they can't cheat
    		clear(true, true); //Clear all squares from the screen
    	}
    	if(difLevel == 0)
    	{
    		timerDown = 30;
    		win = false;
    		fail = false;
    		score = 0;
    	}
    }
    
    public void countDown() //This code handles the count down timer
    {
    	if(timerDown > 0
    			&& win == false
    			&& difLevel == 1)
    	{
    	timerDown = timerDown - 0.02;
    	}
    }
    
    public void invert() //This method inverts the color scheme when called
    {
    	if(invert == false)
		{
			invert = true;
            for(int i = 0; i < menus.size(); i++)
            {
                MenuButton menuButtonI = menus.get(i);
                menuButtonI.invert = true;
            }
            for(int i = 0; i < toggles.size(); i++)
            {
                ToggleButton toggleButtonI = toggles.get(i);
                toggleButtonI.invert = true;
            }
		}
		else if(invert == true)
		{
			invert = false;
            for(int i = 0; i < menus.size(); i++)
            {
                MenuButton menuButtonI = menus.get(i);
                menuButtonI.invert = false;
            }
            for(int i = 0; i < toggles.size(); i++)
            {
                ToggleButton toggleButtonI = toggles.get(i);
                toggleButtonI.invert = false;
            }
		}
    }
    
    public void win() //This code decides whether the user has won the game
    {
    	if(score >= MAX_NUM_SQUARES
    	&& fail == false
    	&& difLevel == 1)
    	{
    		win = true;
    	}
    }
    
    public void fail() //This code decides whether the user has lost the game
    {
		if(timerDown <= 0
		&& win == false
		&& difLevel == 1)
		{
			fail = true;
		}
    }
    
    public void developer() //This code works out whether to enable developer mode
    {
        if(pro4 == true)
		{
			developer = 1;
		}
    }
    
    public void nightlyToggle()
    {
    	if(nightly == 1)
		{
			nightly = 0;
		}
		else if(nightly == 0)
		{
			nightly = 1;
		}
    }
    
    public boolean overlapGeneric(double x, double y, double imageX, double imageY, double targetX, double targetY, double targetImageX, double targetImageY)
    {
    	boolean overlap = false;
    	if(x <= targetX + targetImageX
    			&& targetX <= x + imageX
    			&& y <= targetY + targetImageY
    			&& targetY <= y + imageY)
    	{
    		overlap = true;
    	}
    	return overlap;			
    }

    /*
     * switchInt Explaination
     * 0 - Normal
     * 1 - Developer mode is active
     * 2 - Magnet mode is active
     * keplerMode Explaination
     * 0 - Earth
     * 1 - Space
     * 2 - Solar
     */

    public void updateDraw(int timerDownInt, int timerDownDec)
    {
        g.drawRect(0, 0, width, height, Color.rgb(220, 220, 220));
            menuDraw();
            toggleDraw();
//	    	If in solar system, draw the sun and write the kepler mdoe also
	    	
		switch(nightly)
        {
            case 1 :
                drawDeveloperStrings();
                if(score >= currentNumSquares + 1)
                {
                    g.drawText("WINNING!", font, 20, 220, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                }
                if(win == true)
                {
                    g.drawText("You Have Won The Game", font, 20, 260, FONT_SIZE, STROKE_WIDTH, Color.rgb(255, 87, 34));
                }
                if(fail == true)
                {
                    g.drawText("You Lost :(", font, 20, 260, FONT_SIZE, STROKE_WIDTH, Color.rgb(255, 87, 34));
                }
                if(keplerMode == 1)
                {
                    g.drawText("Keplerian Mode", font, 20, 280, FONT_SIZE, STROKE_WIDTH, Color.rgb(255, 87, 34));
                }
                break;
            case 0 :
                drawUserStrings();
                if(score >= currentNumSquares + 1)
                {
                    g.drawText("WINNING!", font, 20, 140, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                }
                if(win == true)
                {
                    g.drawText("You Have Won The Game", font, 20, 160, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                }
                if(fail == true)
                {
                    g.drawText("You Lose :(", font, 20, 160, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                }
                break;
        }
        

        if (invert == true) {
            switch (nightly) {
                case 1:
                    g.drawRect(0, 0, width + 1, height + 1, Color.rgb(255, 87, 34));
                    menuDraw();
                    g.drawText("NIGHTLY", font, 20, height / 2 + FONT_SIZE, 3 * FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("AngleY: " + angleMethodY(), font, 20, 2 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("AngleX: " + angleMethodX(), font, 20, 3 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("Shapes: " + currentNumSquares, font, 20, 4 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("v" + versionNumber + " :)", font, 20, height - 30, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("Success: " + errorA, font, 20, 5 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("Score: " + score, font, 20, 6 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("[MAX] " + maxNumSquares, font, 20, 7 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("LocalCheck: " + localCheckCurrent, font, 20, 9 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("Elasticity: " + Elasticity[selectedElasticityValue] * 100 + "%", font, 20, 10 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    if (score >= currentNumSquares + 1) {
                        g.drawText("WINNING!", font, 20, 220, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    }
                    if (win == true) {
                        g.drawText("You Have Won The Game", font, 20, 260, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    }
                    if (fail == true) {
                        g.drawText("You Lose :(", font, 20, 260, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    }
                    break;
                case 0:
                    g.drawRect(0, 0, width + 1, height + 1, Color.rgb(00, 105, 92));
                    menuDraw();
                    g.drawText("B WINCH", font, width - menuWidth + 5, height - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                    g.drawText("CLEAR", font, width - menuWidth + 5, height - menuHeight - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                    g.drawText("FREEZE", font, width - menuWidth + 5, height - 3 * menuHeight - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                    switch(keplerMode)
                    {
                        case 0 :
                            g.drawText("EARTH", font, width - menuWidth + 5, height - 4 * menuHeight - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                            break;
                        case 1 :
                            g.drawText("SPACE", font, width - menuWidth + 5, height - 4 * menuHeight - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                            break;
                        case 2 :
                            g.drawPixmap(sun, (width - sun.getWidth()) / 2, (height - sun.getHeight()) / 2);
                            g.drawText("SOLAR", font, width - menuWidth + 5, height - 5 - 4 * menuHeight, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                    }
                    switch(difLevel)
                    {
                        case 0 :
                            g.drawText("ENDLESS", font, width - menuWidth + 5, height - menuHeight * 2 - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                            break;
                        case 1 :
                            g.drawText("TIMED", font, width - menuWidth + 5, height - menuHeight * 2 - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                            break;
                        case 2 :
                            g.drawText("MAGNETS", font, width - menuWidth + 5, height - menuHeight * 2 - 5, FONT_SIZE, STROKE_WIDTH, Color.rgb(00, 105, 92));
                    }
                    if (developer == 1)
                    {
                        g.drawRect(width - toggleWidth, 0, toggleWidth, toggleWidth, Color.rgb(220, 220, 220));
                    }
                    g.drawText("" + currentNumSquares, font, 20, 80, 2 * FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("v" + versionNumber + " :)", font, 20, height - 30, 40, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("[MAX] " + maxNumSquares, font, 20, 3 * FONT_SIZE, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    g.drawText("SQUARE", font, 20, height / 2 + FONT_SIZE, 3 * FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    if (score >= currentNumSquares + 1) {
                        g.drawText("WINNING!", font, 20, 140, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    }
                    if (win == true) {
                        g.drawText("You Have Won The Game", font, 20, 160, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    }
                    if (fail == true) {
                        g.drawText("You Lose :(", font, 20, 160, FONT_SIZE, STROKE_WIDTH, Color.rgb(220, 220, 220));
                    }
                    break;
            }
        }
    	updateDrawShapes();
    }

    private void drawUserStrings()
    {
        for(int i = 0; i < userstrings.size(); i++)
        {
            DrawableString drawableStringI = userstrings.get(i);
            drawableStringI.draw(g);
        }
    }

    private void drawDeveloperStrings()
    {
        for(int i = 0; i < developerstrings.size(); i++)
        {
            DrawableString drawableStringI = developerstrings.get(i);
            drawableStringI.draw(g);
        }
    }

    private void toggleDraw()
    {
        for(int i = 0; i < toggles.size(); i++)
        {
            ToggleButton toggleButtonI = toggles.get(i);
            toggleButtonI.drawToggle(g);
            toggleButtonI.drawText(g, font);
        }
    }

    private void menuDraw()
    {
        for(int i = 0; i < menus.size(); i++)
        {
            MenuButton menuButtonI = menus.get(i);
            menuButtonI.drawButton(g);
        }
        for(int i = 0; i < menus.size(); i++)
        {
            MenuButton menuButtonI = menus.get(i);
            menuButtonI.drawText(g, font, FONT_SIZE, STROKE_WIDTH);
        }
    }

    public void updateDrawShapes()
    {
    	for (int b=0; b < shapes.size(); b++)
        {
        	Square squareB = (Square)shapes.get(b);
        	squareB.drawSquare(g);
        }
    }

    public void setupDefaultElasticity()
    {
        Elasticity[0] = e;
        Elasticity[1] = 1.3;
        Elasticity[2] = 0.8;
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
