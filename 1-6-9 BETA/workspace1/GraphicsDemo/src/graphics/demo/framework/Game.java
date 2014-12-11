package graphics.demo.framework;

import android.content.res.AssetManager;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public interface Game 
{
	public Input getInput();
	public FileIO getFileIO();
	public Graphics getGraphics();
	public Audio getAudio();
	public void setScreen(Screen screen);
	public Screen getCurrentScreen();
	public Screen getStartScreen();
	public AssetManager getAssets();
}