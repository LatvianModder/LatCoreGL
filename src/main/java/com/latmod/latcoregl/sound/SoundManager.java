package com.latmod.latcoregl.sound;

import com.latmod.latcoregl.IWindow;
import com.latmod.latcoregl.LatCoreGL;
import com.latmod.latcoregl.Resource;
import com.latmod.lib.io.Response;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.stb.STBVorbisInfo;

import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Made by LatvianModder
 */
public final class SoundManager implements Runnable
{
    public static final Logger logger = Logger.getLogger("SoundManager");

    static
    {
        logger.setParent(LatCoreGL.logger);
    }

    public final IWindow window;
    public double masterVolume = 1D;
    public boolean muted = false;
    public ALCCapabilities device;
    public long context;
    private Thread thread;
    private Map<Resource, SoundContainer> soundContainers = new HashMap<>();
    private List<Sound> soundSources = new ArrayList<>();

    public SoundManager(IWindow w)
    {
        window = w;
        logger.info("Starting OpenAL...");
        thread = new Thread(this, "SoundEngine");
        thread.setDaemon(true);
        thread.start();
    }

    public static ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info)
    {
        /*
        ByteBuffer vorbis;
		try
		{
			vorbis = ioResourceToByteBuffer(resource, bufferSize);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
		
		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		if(decoder == MemoryUtil.NULL) throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
		
		stb_vorbis_get_info(decoder, info);
		
		int channels = info.channels();
		
		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);
		
		ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);
		
		stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm);
		stb_vorbis_close(decoder);
		
		return pcm;
		*/

        return null;
    }

    @Override
    public void run()
    {
        boolean success = false;
        /*
        device = ALDevice.create();
        if(device == MemoryUtil.NULL)
        {
            throw new IllegalStateException("Failed to open the default device.");
        }

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        boolean success = false;

        if(deviceCaps.OpenALC10)
        {
            logger.info("OpenALC10: " + deviceCaps.OpenALC10 + ", OpenALC11: " + deviceCaps.OpenALC11 + ", caps.ALC_EXT_EFX: " + deviceCaps.ALC_EXT_EFX);
            String defaultDeviceSpecifier = alcGetString(MemoryUtil.NULL, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
            logger.info("Default device: " + defaultDeviceSpecifier);
            context = ALC10.alcCreateContext(device, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(context);
            
            AL.createCapabilities(device);
            logger.info("Frequency: " + ALC10.alcGetInteger(device.get, ALC10.ALC_FREQUENCY) + "Hz, Refresh: " + ALC10.alcGetInteger(device, ALC10.ALC_REFRESH) + "Hz, Sync: " + (ALC10.alcGetInteger(device, ALC10.ALC_SYNC) == ALC10.ALC_TRUE) + ", Mono Sources: " + ALC10.alcGetInteger(device, ALC11.ALC_MONO_SOURCES) + ", Stereo Sources: " + ALC10.alcGetInteger(device, ALC11.ALC_STEREO_SOURCES));
            success = true;
        }
        */

        if(success)
        {
            logger.info("OpenAL thread started");
        }
        else
        {
            logger.warning("Failed to start OpenAL!");
            thread = null;
            return;
        }

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
            if(is == null)
            {
                throw new RuntimeException("Sound '" + r + "' not found!");
            }

            int bufferID = AL10.alGenBuffers();

            try
            {
                WaveData wd = WaveData.create(window.getData(r).stream);
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
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public void onDestroyed()
    {
        thread = null;
        logger.info("Stopping OpenAL...");

        for(SoundContainer c : soundContainers.values())
        {
            AL10.alDeleteBuffers(c.bufferID);
        }

        soundContainers.clear();

        for(Sound s : soundSources)
        {
            AL10.alDeleteSources(s.sourceID);
        }

        soundSources.clear();

        ALC10.alcDestroyContext(context);
    }

    public Sound playSound(Resource r)
    {
        return playSound(r, 1D, 1D);
    }

    public Sound playSound(Resource r, double g, double p)
    {
        if(muted || masterVolume <= 0D)
        {
            return Sound.nullSound;
        }
        SoundContainer sc = getSoundContainer(r);
        if(sc == null)
        {
            return Sound.nullSound;
        }
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
        if(sc == null)
        {
            sc = addSound(r);
        }
        return sc;
    }

    /**
     * Inverses muted boolean
     */
    public void toggleMuted()
    {
        muted = !muted;
    }

    public boolean hasSoundContainer(Resource r)
    {
        return soundContainers.containsKey(r);
    }
}