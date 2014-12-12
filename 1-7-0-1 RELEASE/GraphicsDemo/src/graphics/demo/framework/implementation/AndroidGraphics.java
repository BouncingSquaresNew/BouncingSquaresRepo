package graphics.demo.framework.implementation;

import graphics.demo.framework.Graphics;
import graphics.demo.framework.Pixmap;
import graphics.demo.game.Assets;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games" slightly modified by Aleksandrs Baklanovs
 */

public class AndroidGraphics implements Graphics 
{
	AssetManager assets;
	Bitmap frameBuffer;
	Canvas canvas;
	Paint paint;
	Rect srcRect = new Rect();
	Rect dstRect = new Rect();
	
	public AndroidGraphics(AssetManager assets, Bitmap frameBuffer) 
	{
		this.assets = assets;
		this.frameBuffer = frameBuffer;
		this.canvas = new Canvas(frameBuffer);
		this.paint = new Paint();
	}
	
	@Override
	public Pixmap newPixmap(String fileName, PixmapFormat format) 
	{
		Config config = null;
		if (format == PixmapFormat.RGB565)
			config = Config.RGB_565;
		else if (format == PixmapFormat.ARGB4444)
			config = Config.ARGB_4444;
		else
			config = Config.ARGB_8888;
		Options options = new Options();
		options.inPreferredConfig = config;
		InputStream in = null;
		Bitmap bitmap = null;
		try 
		{
			in = assets.open(fileName);
			bitmap = BitmapFactory.decodeStream(in);
			if (bitmap == null)
				throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
		} catch (IOException e) 
		{
			throw new RuntimeException("Couldn't load bitmap from asset '" + fileName + "'");
		} finally 
		{
			if (in != null) 
			{
				try 
				{
					in.close();
				} catch (IOException e) 
				{
				}
			}
		}
		if (bitmap.getConfig() == Config.RGB_565)
			format = PixmapFormat.RGB565;
		else if (bitmap.getConfig() == Config.ARGB_4444)
			format = PixmapFormat.ARGB4444;
		else
			format = PixmapFormat.ARGB8888;
		return new AndroidPixmap(bitmap, format);
	}
	
	@Override
	public void clear(int color) 
	{
		canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, (color & 0xff));
	}
	
	@Override
	public void drawPixel(int x, int y, int color) 
	{
		paint.setColor(color);
		canvas.drawPoint(x, y, paint);
	}
	
	@Override
	public void drawLine(int x, int y, int x2, int y2, int color) 
	{
		paint.setColor(color);
		canvas.drawLine(x, y, x2, y2, paint);
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height, int color) 
	{
		paint.setColor(color);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight) 
	{
		srcRect.left = srcX;
		srcRect.top = srcY;
		srcRect.right = srcX + srcWidth - 1;
		srcRect.bottom = srcY + srcHeight - 1;
		dstRect.left = x;
		dstRect.top = y;
		dstRect.right = x + srcWidth - 1;
		dstRect.bottom = y + srcHeight - 1;
		canvas.drawBitmap(((AndroidPixmap) pixmap).bitmap, srcRect, dstRect, null);
	}
	
	@Override
	public void drawText(String text, int x, int y, int size, int color)
	{
		paint.setColor(color);
		paint.setTextSize(size);
		paint.setTypeface(Assets.font);
		paint.setAntiAlias(true);
		paint.setPathEffect(null);
		paint.setShader(null);
		paint.setStrokeWidth(5);
		canvas.drawText(text, x, y, paint);
	}
	
	public void drawChar(char[] letters, int x, int y, int index, int color)
	{
		paint.setColor(color);
		paint.setTextSize(22);
		canvas.drawText(letters, index, 1, x, y, paint);
	}
	
	@Override
	public void drawPixmap(Pixmap pixmap, int x, int y) 
	{
		canvas.drawBitmap(((AndroidPixmap)pixmap).bitmap, x, y, null);
	}
	
	@Override
	public int getWidth() 
	{
		return frameBuffer.getWidth();
	}
	
	@Override
	public int getHeight() 
	{
		return frameBuffer.getHeight();
	}
}