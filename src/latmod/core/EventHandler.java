package latmod.core;
import java.lang.annotation.*;

/** Made by LatvianModder
 * @see Event
 * @see EventGroup */
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler
{
}