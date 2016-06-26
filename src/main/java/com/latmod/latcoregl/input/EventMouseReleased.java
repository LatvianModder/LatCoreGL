package com.latmod.latcoregl.input;

import com.latmod.latcoregl.IWindow;

public class EventMouseReleased extends EventMouse
{
    public final int button;
    public final long millis;

    public EventMouseReleased(IWindow w, int b, long m)
    {
        super(w, false);
        button = b;
        millis = m;
    }
}