package latmod.core.res;
import java.io.InputStream;

public class StreamResManager extends ResourceManager
{
	public InputStream getInputStream(Resource r) throws Exception
	{ return Resource.class.getResourceAsStream(r.path); }
}