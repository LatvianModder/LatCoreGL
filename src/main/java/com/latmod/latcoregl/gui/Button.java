package com.latmod.latcoregl.gui;

import com.latmod.latcoregl.input.EventMousePressed;
import com.latmod.latcoregl.rendering.GLHelper;
import com.latmod.latcoregl.rendering.Renderer;
import com.latmod.lib.util.LMColorUtils;

import java.util.List;

/**
 * Made by LatvianModder
 */
public abstract class Button extends Widget
{
    public boolean leftAlign = false;
    public int color;
    public String title;

    public Button(String id, double x, double y, double w, double h, String s1)
    {
        super(id, x, y, w, h);
        title = s1;
        color = LMColorUtils.WIDGETS;
    }

    @Override
    public void renderWidget()
    {
        GLHelper.texture.disable();
        GLHelper.color.setI(mouseOver() ? LMColorUtils.lerp(color, 0xFF000000, 0.5F) : color);
        Renderer.rect(posX, posY, width, height);
    }

    @Override
    public void getMouseOverText(List<String> list)
    {
        if(title != null && !title.isEmpty())
        {
            double x = leftAlign ? posX + 8D : posX + (width - getGui().font.textWidth(title)) / 2D + 8D;
            getGui().font.drawText(x, posY + height / 2F - 8F, title);
        }
    }

    @Override
    public void onMousePressed(EventMousePressed e)
    {
        if(mouseOver())
        {
            playClickSound();
            onPressed(e);
        }
    }

    public abstract void onPressed(EventMousePressed e);
}