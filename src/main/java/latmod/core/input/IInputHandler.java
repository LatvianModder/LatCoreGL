package latmod.core.input;

/**
 * Created by LatvianModder on 10.04.2016.
 */
public interface IInputHandler
{
	void onKeyPressed(EventKeyPressed e);
	void onKeyReleased(EventKeyReleased e);
	void onMousePressed(EventMousePressed e);
	void onMouseReleased(EventMouseReleased e);
	void onMouseScrolled(EventMouseScrolled e);
}
