package graphics.demo.game;

import graphics.demo.framework.Screen;
import graphics.demo.framework.implementation.AndroidGame;

/* Name: BouncingSquares.java
 * Date: 23/03/2013
 * Author: Aleksandrs Baklanovs
 * Purpose: main class that launches the loading class that launches the Loading class, which Launches Intro
 * which launches the Main Menu
 */

public class GraphicsDemo extends AndroidGame {
	
	@Override
	public Screen getStartScreen() 
	{
		return new BouncingSquares(this);
	}
}
