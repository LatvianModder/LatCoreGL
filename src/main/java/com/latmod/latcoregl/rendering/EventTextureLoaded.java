package com.latmod.latcoregl.rendering;

import com.latmod.latcoregl.Event;
import com.latmod.latcoregl.IWindow;

public class EventTextureLoaded extends Event
{
    public final Texture texture;

    public EventTextureLoaded(IWindow w, Texture t)
    {
        super(w, false);
        texture = t;
    }
}