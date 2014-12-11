package graphics.demo.framework;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public interface Audio 
{
	public Music newMusic(String filename);
	public Sound newSound(String filename);
}