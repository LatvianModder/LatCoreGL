package latmod.core.gui;

import latmod.core.input.LMKeyboard;
import latmod.core.input.keys.*;
import latmod.core.input.mouse.*;
import latmod.core.rendering.*;
import latmod.lib.LMColorUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;

import java.util.Arrays;

/** Made by LatvianModder */
public class TextBox extends Widget implements IMousePressed, IKeyPressed
{
	public static boolean textBoxCentrX = true;
	public static boolean textBoxCentrY = false;
	
	public static void setCentred(boolean x, boolean y)
	{ textBoxCentrX = x; textBoxCentrY = y; }
	
	public boolean password = false;
	public int charLimit = 20;
	public boolean canEdit = true;
	public String label = null;
	public boolean leftAlign = false;
	
	public TextBox(Gui s, double x, double y, double w, double h)
	{
		super(s, x - (textBoxCentrX ? (w / 2D) : 0D), y - (textBoxCentrY ? (h / 2D) : 0D), w, h);
		color = 60;
		txt = "";
	}
	
	public TextBox setLabel(String s)
	{ label = s; return this; }
	
	public void onRender()
	{
		GLHelper.texture.disable();

		int col = color;
		if(mouseOver()) col = LMColorUtils.lerp(col, 0xFFFFFFFF, 0.3D);
		if(selected) col = LMColorUtils.lerp(col, 0xFFFFFFFF, 0.5D);

		GLHelper.color.setI(col);
		Renderer.rect(posX, posY, width, height);
	}
	
	public void onPostRender()
	{
		GLHelper.color.setDefault();
		GLHelper.texture.enable();

		if(txt != null && txt.length() > 0)
		{
			String txt1 = txt;
			
			if(password)
			{
				char[] c = new char[txt.length()];
				Arrays.fill(c, '*');
				txt1 = new String(c);
			}
			
			if(leftAlign) gui.parent.font.drawText(posX + 8D, posY + height / 2D - 8D, txt1);
			else textCentred(posX + width / 2D, posY + height / 2D, txt1);
		}
		
		if(label != null && label.length() > 0)
		{
			if(leftAlign) gui.parent.font.drawText(posX + 8D, posY - 12D, label);
			else textCentred(posX + width / 2D, posY - 12D, label);
		}
	}
	
	public void onKeyPressed(EventKeyPressed e)
	{
		if(selected && canEdit)
		{
			if(e.isBackspace())
			{
				if(txt.length() > 0)
				{
					if(LMKeyboard.isCtrlDown())
					{
						String txt0 = txt + "";
						txt = "";
						onCleared(txt0);
					}
					else txt = txt.substring(0, txt.length() - 1);
				}
			}
			
			else if(e.isEnter())
			{
				selected = false;
				onEnter();
				e.cancel(); return;
			}
			
			else if(e.isTab())
			{
				if(selected)
				{
					onTab();
					e.cancel(); return;
				}
			}
			
			else if(LMKeyboard.isCtrlDown())
			{
				if(e.key == Keyboard.KEY_V)
				{
					String s1 = Sys.getClipboard();
					if(s1 != null) txt += s1;
				}
			}
			
			else
			{
				if(e.isASCIIChar())
				{
					txt += e.keyChar;
					playClickSound();
				}
			}
			
			if(charLimit > 0 && txt.length() > charLimit) txt = txt.substring(0, charLimit);
			
			onChanged();
			e.cancel();
		}
	}
	
	public void onMousePressed(EventMousePressed e)
	{
		selected = false;
		
		if(canEdit && mouseOver())
		{
			if(e.button == 1)
			{
				String txt0 = txt + "";
				txt = "";
				onCleared(txt0);
				onChanged();
			}
			
			selected = true;
			e.cancel();
		}
	}
	
	public void onChanged()
	{
	}
	
	public void onTab()
	{
	}
	
	public void onEnter()
	{
	}
	
	public void onCleared(String prev)
	{
	}
}