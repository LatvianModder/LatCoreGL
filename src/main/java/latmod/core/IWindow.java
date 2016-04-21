package latmod.core;

import latmod.core.gui.Gui;
import latmod.core.input.IInputHandler;
import latmod.core.rendering.*;
import latmod.core.sound.SoundManager;
import latmod.lib.net.Response;

/**
 * Created by LatvianModder on 12.04.2016.
 */
public interface IWindow extends IInputHandler
{
	int getWidth();
	int getHeight();
	Response getData(Resource r) throws Exception;
	SoundManager getSoundManager();
	TextureManager getTextureManager();
	Font getFont();
	Gui getGui();
	void openGui(Gui g);
	void destroy();
}
