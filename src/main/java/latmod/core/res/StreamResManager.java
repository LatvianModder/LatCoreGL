package latmod.core.res;
import java.io.InputStream;

public class StreamResManager extends ResourceManager
{
	public StreamResManager()
	{ super(Type.STREAM); }
	
	public InputStream getInputStream(Resource r) throws Exception
	{ return Resource.class.getResourceAsStream(r.path); }

	public String getPath(Resource r)
	{ return ("/" + r.path).replace("//", "/"); }
}