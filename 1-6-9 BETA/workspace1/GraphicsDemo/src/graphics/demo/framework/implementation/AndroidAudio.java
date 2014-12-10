package graphics.demo.framework.implementation;


import graphics.demo.framework.Audio;
import graphics.demo.framework.Music;
import graphics.demo.framework.Sound;

import java.io.IOException;


import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

/*
 * With author's permission this framework was taken from Mario Zechner's 
 * book "Beginning Android Games"
 */

public class AndroidAudio implements Audio 
{
	AssetManager assets;
	SoundPool soundPool;
	
	public AndroidAudio(Activity activity) 
	{
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	@Override
	public Music newMusic(String filename) 
	{
		try 
		{
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			return new AndroidMusic(assetDescriptor);
		} catch (IOException e) 
		{
			throw new RuntimeException("Couldn't load music '" + filename + "'");
		}
	}
	
	@Override
	public Sound newSound(String filename) 
	{
		try 
		{
			AssetFileDescriptor assetDescriptor = assets.openFd(filename);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch (IOException e) 
		{
			throw new RuntimeException("Couldn't load sound '" + filename + "'");
		}
	}
}