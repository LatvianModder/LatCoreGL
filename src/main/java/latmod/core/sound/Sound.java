package latmod.core.sound;

import org.lwjgl.openal.AL10;

public class Sound
{
	public static final SoundContainer nullContainer = new SoundContainer(null, null, 0);
	public static final Sound nullSound = new Sound(nullContainer, -1, 0D, 0D);
	
	public final SoundContainer soundContainer;
	public final int sourceID;
	
	public double gain;
	public double pitch;
	
	public boolean isPlaying = false;
	public boolean isDestroyed = false;
	
	public Sound(SoundContainer c, int s, double g, double p)
	{
		soundContainer = c;
		sourceID = s;
		
		gain = g;
		pitch = p;
	}
	
	public void togglePaused()
	{ if(isPlaying) pause(); else play(); }
	
	public void play()
	{
		isPlaying = true;
		AL10.alSourcePlay(sourceID);
	}
	
	public void pause()
	{
		isPlaying = false;
		AL10.alSourcePause(sourceID);
	}
	
	public void stop()
	{
		isDestroyed = true;
		isPlaying = false;
		AL10.alSourceStop(sourceID);
	}
	
	public boolean isDestroyed()
	{ return isDestroyed || AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_STOPPED; }
	
	public void update()
	{
		AL10.alSourcei(sourceID, AL10.AL_BUFFER, soundContainer.bufferID);
		AL10.alSourcef(sourceID, AL10.AL_PITCH, (float)pitch);
		AL10.alSourcef(sourceID, AL10.AL_GAIN, (float)(gain * soundContainer.manager.masterVolume));
	}
}