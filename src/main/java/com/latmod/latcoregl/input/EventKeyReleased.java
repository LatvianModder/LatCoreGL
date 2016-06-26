package com.latmod.latcoregl.input;

import com.latmod.latcoregl.IWindow;

public class EventKeyReleased extends EventKey
{
    public final long millis;

    public EventKeyReleased(IWindow w, int k, long m)
    {
        super(w, false, k);
        millis = m;
    }
}