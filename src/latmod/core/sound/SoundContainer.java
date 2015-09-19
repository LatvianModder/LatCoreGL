package latmod.core.sound;
import org.lwjgl.openal.AL10;

import latmod.core.res.Resource;

/** Made by LatvianModder */
public class SoundContainer
{
	public final SoundManager manager;
	public final Resource res;
	public final int bufferID;
	
	public SoundContainer(SoundManager sm, Resource r, int i)
	{ manager = sm; res = r; bufferID = i; }
	
	public int getBufferSize()
	{ return AL10.alGetBufferi(bufferID, AL10.AL_SIZE); }
}