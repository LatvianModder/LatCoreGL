package com.latmod.latcoregl.rendering;

import com.latmod.latcoregl.Event;
import com.latmod.latcoregl.IWindow;
import com.latmod.latcoregl.Time;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Made by LatvianModder
 */
public class EventScreenshot extends Event
{
    public final ThreadScreenshot thread;
    public final Time time;
    public BufferedImage image;
    public File file;

    public EventScreenshot(IWindow w, ThreadScreenshot t, Time ti, BufferedImage img, File f)
    {
        super(w, true);
        thread = t;
        time = ti;
        image = img;
        file = f;
    }
}