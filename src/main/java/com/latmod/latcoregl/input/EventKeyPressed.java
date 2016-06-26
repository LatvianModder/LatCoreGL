package com.latmod.latcoregl.input;

import com.latmod.latcoregl.IWindow;
import com.latmod.lib.util.LMStringUtils;
import org.lwjgl.glfw.GLFW;

public class EventKeyPressed extends EventKey
{
    public final char keyChar;
    public final boolean repeat;

    public EventKeyPressed(IWindow w, int k, char c, boolean r)
    {
        super(w, true, k);
        keyChar = c;
        repeat = r;
    }

    public boolean isASCIIChar()
    {
        return LMStringUtils.isASCIIChar(keyChar);
    }

    public boolean isEnter()
    {
        return key == GLFW.GLFW_KEY_ENTER;
    }

    public boolean isBackspace()
    {
        return key == GLFW.GLFW_KEY_BACKSPACE;
    }

    public boolean isTab()
    {
        return key == GLFW.GLFW_KEY_TAB;
    }
}