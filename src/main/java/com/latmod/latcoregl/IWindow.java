package com.latmod.latcoregl;

import com.latmod.latcoregl.gui.Gui;
import com.latmod.latcoregl.input.IInputHandler;
import com.latmod.latcoregl.rendering.Font;
import com.latmod.latcoregl.rendering.TextureManager;
import com.latmod.latcoregl.sound.SoundManager;
import com.latmod.lib.io.Response;

/**
 * Created by LatvianModder on 12.04.2016.
 */
public interface IWindow extends IInputHandler
{
    long getWindowID();

    int getWidth();

    int getHeight();

    Response getData(Resource r) throws Exception;

    SoundManager getSoundManager();

    TextureManager getTextureManager();

    Font getFont();

    Gui getGui();

    Gui openGui(Gui g);

    void destroy();
}
