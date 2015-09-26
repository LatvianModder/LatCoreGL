package latmod.core.gui;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import latmod.core.LatCoreGL;
import latmod.core.input.LMKeyboard;
import latmod.core.input.keys.*;
import latmod.core.input.mouse.*;
import latmod.core.rendering.*;
import latmod.core.util.LMColorUtils;

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
	public TextPart label = null;
	public boolean leftAlign = false;
	
	public TextBox(Gui s, double x, double y, double w, double h)
	{
		super(s, x - (textBoxCentrX ? (w / 2D) : 0D), y - (textBoxCentrY ? (h / 2D) : 0D), w, h);
		color = 60;
		txt = TextPart.get("");
	}
	
	public TextBox setLabel(TextPart s)
	{ label = s; return this; }
	
	public void onRender()
	{
		Renderer.disableTexture();
		
		int col = color;
		if(mouseOver()) col = LMColorUtils.lerp(col, 0xFFFFFFFF, 0.3D);
		if(selected) col = LMColorUtils.lerp(col, 0xFFFFFFFF, 0.5D);
		
		LatCoreGL.setColor(col);
		Renderer.rect(posX, posY, width, height);
	}
	
	public void onPostRender()
	{
		GL11.glColor4f(1F, 1F, 1F, 1F);
		Renderer.enableTexture();
		
		if(txt != null && txt.length() > 0)
		{
			TextPart txt1 = txt.copy();
			
			if(password)
			{
				txt1 = TextPart.get("");
				
				for(int j = 0; j < txt.text.length(); j++)
					txt1.text += '*';
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
						txt.text = "";
						onCleared(txt0);
					}
					else txt.text = txt.text.substring(0, txt.text.length() - 1);
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
					if(s1 != null) txt.text += s1;
				}
			}
			
			else
			{
				if(e.isASCIIChar())
				{
					txt.text += e.keyChar;
					playClickSound();
				}
			}
			
			if(charLimit > 0 && txt.text.length() > charLimit) txt.text = txt.text.substring(0, charLimit);
			
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
				txt.text = "";
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