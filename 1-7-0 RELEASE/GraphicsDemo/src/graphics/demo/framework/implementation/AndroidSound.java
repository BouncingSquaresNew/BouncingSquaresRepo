package graphics.demo.framework.implementation;

import graphics.demo.framework.Sound;
import android.media.SoundPool;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public class AndroidSound implements Sound 
{
	int soundId;
	SoundPool soundPool;

	public AndroidSound(SoundPool soundPool, int soundId) 
	{
		this.soundId = soundId;
		this.soundPool = soundPool;
	}

	@Override
	public void play(float volume) 
	{
		soundPool.play(soundId, volume, volume, 0, 0, 1);
	}
	
	public void playForever(float volume)
	{
		soundPool.play(soundId, volume, volume, 0, -1, 1);
	}

	@Override
	public void dispose() 
	{
		soundPool.unload(soundId);
	}
}