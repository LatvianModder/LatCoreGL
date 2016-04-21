package latmod.core.sound;

import latmod.core.*;
import latmod.lib.net.Response;
import org.lwjgl.openal.*;
import org.lwjgl.util.WaveData;

import java.util.*;
import java.util.logging.Logger;

/**
 * Made by LatvianModder
 */
public final class SoundManager implements Runnable
{
	public static final Logger logger = Logger.getLogger("SoundManager");
	
	static { logger.setParent(LatCoreGL.logger); }
	
	public final IWindow window;
	private Thread thread;
	public double masterVolume = 1D;
	public boolean muted = false;
	
	private final Map<Resource, SoundContainer> soundContainers = new HashMap<Resource, SoundContainer>();
	private final List<Sound> soundSources = new ArrayList<Sound>();
	
	public SoundManager(IWindow w)
	{
		thread = new Thread(this, "SoundManager");
		thread.setDaemon(true);
		window = w;
		
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
			Response is = window.getData(r);
			if(is == null) throw new RuntimeException("Sound '" + r + "' not found!");
			else return addSound(r, WaveData.create(is.stream));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
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
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
		logger.info("Adding new sound '" + r + "' with buffer ID " + bufferID);
		SoundContainer sc = new SoundContainer(this, r, bufferID);
		soundContainers.put(r, sc);
		return sc;
	}
	
	public void onDestroyed()
	{
		thread = null;
		logger.info("Stopping OpenAL...");
		
		for(SoundContainer c : soundContainers.values())
			AL10.alDeleteBuffers(c.bufferID);
		
		soundContainers.clear();
		
		for(Sound s : soundSources)
			AL10.alDeleteSources(s.sourceID);
		
		soundSources.clear();
		
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
		if(sc == null) sc = addSound(r);
		return sc;
	}
	
	/**
	 * Inverses muted boolean
	 */
	public void toggleMuted()
	{ muted = !muted; }
	
	public boolean hasSoundContainer(Resource r)
	{ return soundContainers.containsKey(r); }
}