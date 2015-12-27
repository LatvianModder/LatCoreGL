package latmod.core.gui;

import latmod.core.LatCoreGL;
import latmod.core.rendering.TextColor;

public abstract class TextButton extends Button
{
	public TextButton(Gui g, String s, double x, double y)
	{
		super(g, x, y, g.parent.font.textWidth(s) + 16D, 32D, s);
		leftAlign = true;
		setColor(0);
	}
	
	public TextButton(Gui g, String s, float y)
	{ this(g, s, LatCoreGL.getWidth() / 2D, y); }
	
	public String getText()
	{ return mouseOver() ? (TextColor.GOLD + txt) : txt; }
}