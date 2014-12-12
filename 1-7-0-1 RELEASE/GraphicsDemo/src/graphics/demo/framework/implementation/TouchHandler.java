package graphics.demo.framework.implementation;


import graphics.demo.framework.Input.TouchEvent;

import java.util.List;


import android.view.View.OnTouchListener;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public interface TouchHandler extends OnTouchListener 
{
	public boolean isTouchDown(int pointer);
	public int getTouchX(int pointer);
	public int getTouchY(int pointer);
	public List<TouchEvent> getTouchEvents();
}