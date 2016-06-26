package com.latmod.latcoregl.input;

import com.latmod.latcoregl.IWindow;

public class EventMousePressed extends EventMouse
{
    public final int button;

    public EventMousePressed(IWindow w, int b)
    {
        super(w, true);
        button = b;
    }
}