package graphics.demo.framework;

import graphics.demo.framework.Graphics.PixmapFormat;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public interface Pixmap 
{
	public int getWidth();
	public int getHeight();
	public PixmapFormat getFormat();
	public void dispose();
}