package com.latmod.latcoregl.input;

import com.latmod.latcoregl.IWindow;

public abstract class EventKey extends EventInput
{
    public final int key;

    public EventKey(IWindow w, boolean b, int k)
    {
        super(w, b);
        key = k;
    }
}