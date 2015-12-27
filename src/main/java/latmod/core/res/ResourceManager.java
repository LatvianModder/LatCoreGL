package latmod.core.res;

import java.io.InputStream;

public abstract class ResourceManager
{
	public abstract InputStream getInputStream(Resource r) throws Exception;
}