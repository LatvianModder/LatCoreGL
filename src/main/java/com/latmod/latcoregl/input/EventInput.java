package com.latmod.latcoregl.input;

import com.latmod.latcoregl.Event;
import com.latmod.latcoregl.IWindow;

public abstract class EventInput extends Event
{
    public EventInput(IWindow w, boolean b)
    {
        super(w, b);
    }
}