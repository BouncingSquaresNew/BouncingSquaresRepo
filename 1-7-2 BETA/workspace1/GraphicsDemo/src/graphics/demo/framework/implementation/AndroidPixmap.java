package graphics.demo.framework.implementation;

import graphics.demo.framework.Pixmap;
import graphics.demo.framework.Graphics.PixmapFormat;
import android.graphics.Bitmap;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public class AndroidPixmap implements Pixmap 
{
	Bitmap bitmap;
	PixmapFormat format;

	public AndroidPixmap(Bitmap bitmap, PixmapFormat format) 
	{
		this.bitmap = bitmap;
		this.format = format;
	}
	
	@Override
	public int getWidth() 
	{
		return bitmap.getWidth();
	}
	
	@Override
	public int getHeight() 
	{
		return bitmap.getHeight();
	}
	
	@Override
	public PixmapFormat getFormat() 
	{
		return format;
	}
	
	@Override
	public void dispose() 
	{
		bitmap.recycle();
	}
}