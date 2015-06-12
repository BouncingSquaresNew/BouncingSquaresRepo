package graphics.demo.game;

/**
 * Created by Ben on 11/06/2015.
 */
public interface IClickable
{
    public boolean isPressed(int touchX, int touchY);
    public void click();
}
