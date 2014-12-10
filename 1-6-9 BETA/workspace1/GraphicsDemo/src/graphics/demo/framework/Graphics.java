package graphics.demo.framework;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public interface Graphics 
{
	public static enum PixmapFormat 
	{
		ARGB8888, ARGB4444, RGB565
	}
	
	public Pixmap newPixmap(String fileName, PixmapFormat format);
	public void clear(int color);
	public void drawPixel(int x, int y, int color);
	public void drawLine(int x, int y, int x2, int y2, int color);
	public void drawRect(int x, int y, int width, int height, int color);
	public void drawPixmap(Pixmap pixmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight);
	public void drawPixmap(Pixmap pixmap, int x, int y);
	public void drawText(String text, int x, int y, int size, int color);
	public void drawChar(char[] letters, int x, int y, int index, int color);
	public int getWidth();
	public int getHeight();
}