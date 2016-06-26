package com.latmod.latcoregl;

import com.latmod.latcoregl.gui.Gui;
import com.latmod.latcoregl.gui.GuiInit;
import com.latmod.latcoregl.gui.Widget;
import com.latmod.latcoregl.input.EventKeyPressed;
import com.latmod.latcoregl.input.EventKeyReleased;
import com.latmod.latcoregl.input.EventMousePressed;
import com.latmod.latcoregl.input.EventMouseReleased;
import com.latmod.latcoregl.input.EventMouseScrolled;
import com.latmod.latcoregl.input.LMInput;
import com.latmod.latcoregl.rendering.Font;
import com.latmod.latcoregl.rendering.GLHelper;
import com.latmod.latcoregl.rendering.Renderer;
import com.latmod.latcoregl.rendering.Texture;
import com.latmod.latcoregl.rendering.TextureManager;
import com.latmod.latcoregl.sound.SoundManager;
import com.latmod.lib.io.Response;
import com.latmod.lib.math.Pos2I;
import com.latmod.lib.util.LMColorUtils;
import com.latmod.lib.util.LMUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Made by LatvianModder
 */
public class LMFrame implements IWindow
{
    public final MainArgs mainArgs;
    public int width, height;
    public int FPS;
    public long renderTick;
    private int rawFPS = 0;
    private long lastFPS;
    private GLFWErrorCallback errorCallback;
    private long windowID;

    private SoundManager soundManager;
    private TextureManager textureManager;
    private Font font;
    private Gui gui;

    public LMFrame(String[] args, int w, int h) throws Exception
    {
        LatCoreGL.init();
        mainArgs = new MainArgs(args);
        width = mainArgs.getI("width", w, 200, Short.MAX_VALUE);
        height = mainArgs.getI("height", h, 150, Short.MAX_VALUE);
        run();
    }

    public void run()
    {
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        GLFW.glfwSetErrorCallback(errorCallback);

        if(!GLFW.glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        windowID = GLFW.glfwCreateWindow(width, height, "Hello World!", MemoryUtil.NULL, MemoryUtil.NULL);

        if(windowID == MemoryUtil.NULL)
        {
            GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(windowID, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        GLFW.glfwMakeContextCurrent(windowID);
        GL.createCapabilities();
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowID);

        onLoaded();
        GLHelper.background.setF(0.2F, 0.2F, 0.2F, 1F);

        IntBuffer bufferW = BufferUtils.createIntBuffer(1);
        IntBuffer bufferH = BufferUtils.createIntBuffer(1);

        while(!GLFW.glfwWindowShouldClose(windowID))
        {
            int prevW = width;
            int prevH = height;

            GLFW.glfwGetWindowSize(windowID, bufferW, bufferH);
            width = bufferW.get();
            height = bufferH.get();
            bufferW.flip();
            bufferH.flip();

            GLHelper.clear();
            GLHelper.background.setI(LMColorUtils.DARK_GRAY);
            GLHelper.color.setDefault();
            onRender();
            textureManager.tickTextures();
            renderTick++;

            long millis = Time.millis();

            while(millis - lastFPS > 1000L)
            {
                FPS = rawFPS;
                rawFPS = 0;
                //lastFPS += 1000L;
                lastFPS = millis;

                onFPSUpdate();
            }

            rawFPS++;
            GLFW.glfwSwapBuffers(windowID);
            GLFW.glfwPollEvents();
            onUpdate();

            if(prevW != width || prevH != height)
            {
                EventHandler.MAIN.send(new EventResized(this, prevW, prevH));
                gui.init();
            }
        }

        LatCoreGL.logger.info("Stopping Frame...");

        Widget.playSound = false;

        try
        {
            EventHandler.MAIN.send(new EventDestroy(this));
            onDestroyed();
            LMInput.release();
            GLFW.glfwDestroyWindow(windowID);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public void onUpdate()
    {
    }

    public void onLoaded()
    {
        LatCoreGL.window = this;
        LMInput.init();

        openGui(new GuiInit(this));

        Renderer.enter2D();
        GLHelper.blending.enable();
        GLHelper.blendFunc.setDefault();

        soundManager = new SoundManager(this);
        textureManager = new TextureManager(this);

        lastFPS = Time.millis();

        GLHelper.texture.enable();
        setIcon(new Resource("core", "textures/logo_16.png"), new Resource("core", "textures/logo_32.png"), new Resource("core", "textures/logo_128.png"));

        font = new Font(textureManager, new Resource("core", "textures/font.png"));
    }

    protected boolean isResizable()
    {
        return true;
    }

    public void setIcon(Resource... iconPaths)
    {
        ByteBuffer[] list = new ByteBuffer[iconPaths.length];
        for(int i = 0; i < iconPaths.length; i++)
        {
            Texture t = new Texture(iconPaths[i]).setFlag(Texture.FLAG_STORE_PIXELS, true);
            textureManager.bind(t);
            list[i] = LatCoreGL.toByteBuffer(t.pixelBuffer.pixels, true);
        }
        //Display.setIcon(list);
    }

    protected Pos2I startFullscreen()
    {
        return null;
    }

    public void onRender()
    {
    }

    public void onFPSUpdate()
    {
    }

    public void onDestroyed()
    {
        soundManager.onDestroyed();
        textureManager.onDestroyed();
    }

    public void setTitle(String s)
    {
        if(s != null)
        {
            GLFW.glfwSetWindowTitle(windowID, s);
        }
    }

    @Override
    public final long getWindowID()
    {
        return windowID;
    }

    @Override
    public final int getWidth()
    {
        return width;
    }

    @Override
    public final int getHeight()
    {
        return height;
    }

    @Override
    public Response getData(Resource r) throws Exception
    {
        return new Response(LMUtils.class.getResourceAsStream("/assets/" + r.getBase() + '/' + r.getPath()));
    }

    @Override
    public final SoundManager getSoundManager()
    {
        return soundManager;
    }

    @Override
    public final TextureManager getTextureManager()
    {
        return textureManager;
    }

    @Override
    public final Font getFont()
    {
        return font;
    }

    @Override
    public final Gui getGui()
    {
        return gui;
    }

    @Override
    public final Gui openGui(Gui g)
    {
        Gui prevGui = gui;
        gui = g;
        gui.init();
        return prevGui;
    }

    @Override
    public final void destroy()
    {
        GLFW.glfwSetWindowShouldClose(windowID, true);
    }

    @Override
    public void onKeyPressed(EventKeyPressed e)
    {
        gui.onKeyPressed(e);
    }

    @Override
    public void onKeyReleased(EventKeyReleased e)
    {
        gui.onKeyReleased(e);
    }

    @Override
    public void onMousePressed(EventMousePressed e)
    {
        gui.onMousePressed(e);
    }

    @Override
    public void onMouseReleased(EventMouseReleased e)
    {
        gui.onMouseReleased(e);
    }

    @Override
    public void onMouseScrolled(EventMouseScrolled e)
    {
        gui.onMouseScrolled(e);
    }
}