package com.latmod.latcoregl.input;

import com.latmod.latcoregl.IWindow;

public abstract class EventMouse extends EventInput
{
    public EventMouse(IWindow w, boolean b)
    {
        super(w, b);
    }
}