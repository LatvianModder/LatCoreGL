package com.latmod.latcoregl.rendering;

import com.latmod.latcoregl.EventHandler;
import com.latmod.latcoregl.IWindow;
import com.latmod.latcoregl.LatCoreGL;
import com.latmod.latcoregl.Resource;
import com.latmod.lib.PixelBuffer;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TextureManager
{
    public static final Logger logger = Logger.getLogger("TextureManager");

    static
    {
        logger.setParent(LatCoreGL.logger);
    }

    public final IWindow window;
    private final Map<Resource, Texture> textureMap;
    private final List<Texture> tickingTextures;
    private final PixelBuffer unknownTexture;
    public boolean loadTexturesBlured = false;
    public Texture currentTexture = null;

    public TextureManager(IWindow w)
    {
        window = w;
        textureMap = new HashMap<>();
        tickingTextures = new ArrayList<>();
        unknownTexture = new PixelBuffer(4, 4);
        unknownTexture.setPixels(new int[] {0xFF000000, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFFFF00FF, 0xFFFF0000, 0xFFFFD800, 0xFF000000, 0xFF000000, 0xFF0094FF, 0xFF00FF21, 0xFFFF00FF, 0xFFFF00FF, 0xFF000000, 0xFFFF00FF, 0xFF000000});
    }

    public void bind(Texture tex)
    {
        if(currentTexture == tex || tex == null)
        {
            return;
        }

        if(textureMap.put(tex.res, tex) == null)
        {
            GLHelper.texture.enable();
            tex.textureID = GL11.glGenTextures();

            if(tex.getFlag(Texture.FLAG_CUSTOM))
            {
                tex.setFlag(Texture.FLAG_UNKNOWN, false);

                tex.onLoaded(this);

                if(tex.getFlag(Texture.FLAG_TICK))
                {
                    tickingTextures.add(tex);
                }
            }
            else
            {
                PixelBuffer buffer = null;
                tex.setFlag(Texture.FLAG_UNKNOWN, true);

                try
                {
                    buffer = new PixelBuffer(ImageIO.read(window.getData(tex.res).stream));
                    tex.setFlag(Texture.FLAG_UNKNOWN, false);
                }
                catch(Exception ex)
                {
                }

                if(tex.getFlag(Texture.FLAG_UNKNOWN))
                {
                    buffer = unknownTexture.copy();
                }

                if(tex.getFlag(Texture.FLAG_STORE_PIXELS))
                {
                    tex.pixelBuffer = buffer;
                }

                tex.setFlag(Texture.FLAG_BLUR, loadTexturesBlured);
                tex.uploadPixelBuffer(buffer);
                GLHelper.bound_texture.set(0);

                if(tex.getFlag(Texture.FLAG_UNKNOWN))
                {
                    logger.warning("Unknown texture " + tex.res + " added with id " + tex.textureID);
                }
                else
                {
                    logger.info("Added texture " + tex.res + " with id " + tex.textureID);
                }

                tex.onLoaded(this);
                tex.onUpdate(this);
            }

            EventHandler.MAIN.send(new EventTextureLoaded(window, tex));
        }

        GLHelper.bound_texture.set(tex.textureID);
        currentTexture = tex;
    }

    /**
     * Sends an update for all custom textures<br>
     * (They won't be able to move without calling this
     * <br> method before drawing them)
     */
    public void tickTextures()
    {
        if(!tickingTextures.isEmpty())
        {
            GLHelper.texture.enable();

            for(Texture t : tickingTextures)
            {
                t.onUpdate(this);
            }
        }
    }

    public void onDestroyed()
    {
        for(Texture t : textureMap.values())
        {
            t.delete();
        }

        textureMap.clear();
        tickingTextures.clear();
    }
}