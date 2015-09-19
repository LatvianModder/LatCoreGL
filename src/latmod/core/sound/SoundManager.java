package latmod.core.sound;
import java.io.InputStream;
import java.util.logging.Logger;

import org.lwjgl.openal.*;
import org.lwjgl.util.WaveData;

import latmod.core.LatCoreGL;
import latmod.core.res.*;
import latmod.core.util.*;

/** Made by LatvianModder */
public final class SoundManager implements Runnable
{
	public static final Logger logger = Logger.getLogger("SoundManager");
	static { logger.setParent(LatCoreGL.logger); }
	
	public final ResourceManager resManager;
	private Thread thread;
	public double masterVolume = 1D;
	public boolean muted = false;
	
	private final FastMap<Resource, SoundContainer> soundContainers = new FastMap<Resource, SoundContainer>();
	private final FastList<Sound> soundSources = new FastList<Sound>();
	
	public SoundManager(ResourceManager rm)
	{
		thread = new Thread(this, "SoundManager");
		thread.setDaemon(true);
		resManager = rm;
		
		logger.info("Starting OpenAL...");
		
		try
		{
			AL.create();
			thread.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			thread = null;
			logger.warning("Failed to start OpenAL!");
		}
	}
	
	public void run()
	{
		while(thread != null)
		{
			try
			{
				for(int i = 0; i < soundSources.size(); i++)
				{
					Sound s = soundSources.get(i);
					
					if(s.isDestroyed())
					{
						s.stop();
						AL10.alDeleteSources(s.sourceID);
						soundSources.remove(i);
					}
				}
				
				Thread.sleep(5);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				logger.info("Sound manager has crashed! Stopping...");
				thread = null;
			}
		}
	}
	
	public SoundContainer addSound(Resource r)
	{
		try
		{
			InputStream is = resManager.getInputStream(r);
			if(is == null) throw new RuntimeException("Sound '" + r.path + "' not found!");
			else return addSound(r, WaveData.create(is));
		}
		catch(Exception e)
		{ e.printStackTrace(); }
		return null;
	}
	
	private SoundContainer addSound(Resource r, WaveData wd)
	{
		int bufferID = AL10.alGenBuffers();
		
		try
		{
			AL10.alBufferData(bufferID, wd.format, wd.data, wd.samplerate);
			wd.dispose();
		}
		catch(Exception e) { e.printStackTrace(); return null; }
		
		logger.info("Adding new sound '" + r.path + "' with buffer ID " + bufferID);
		SoundContainer sc = new SoundContainer(this, r, bufferID);
		soundContainers.put(r, sc); return sc;
	}
	
	public void onDestroyed()
	{
		thread = null;
		logger.info("Stopping OpenAL...");
		
		for(int i = 0; i < soundContainers.size(); i++)
			AL10.alDeleteBuffers(soundContainers.values.get(i).bufferID);
		
		for(int i = 0; i < soundSources.size(); i++)
			AL10.alDeleteSources(soundSources.get(i).sourceID);
		
		AL.destroy();
	}
	
	public Sound playSound(Resource r)
	{ return playSound(r, 1D, 1D); }
	
	public Sound playSound(Resource r, double g, double p)
	{
		if(muted || masterVolume <= 0D) return Sound.nullSound;
		SoundContainer sc = getSoundContainer(r);
		if(sc == null) return Sound.nullSound;
		int sourceID = AL10.alGenSources();
		Sound s = new Sound(sc, sourceID, g, p);
		updateAndPlay(s);
		return s;
	}
	
	private void updateAndPlay(Sound s)
	{
		s.update();
		s.play();
		soundSources.add(s);
	}
	
	public SoundContainer getSoundContainer(Resource r)
	{
		SoundContainer sc = soundContainers.get(r);
		if(sc == null) sc = addSound(r); return sc;
	}
	
	/** Inverses muted boolean */
	public void toggleMuted()
	{ muted = !muted; }
	
	public boolean hasSoundContainer(Resource r)
	{ return soundContainers.keys.contains(r); }
}