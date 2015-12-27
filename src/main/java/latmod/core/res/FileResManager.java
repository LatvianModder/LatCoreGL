package latmod.core.res;
import java.io.*;

public class FileResManager extends ResourceManager
{
	public final File baseDirectory;
	
	public FileResManager(File f)
	{ baseDirectory = f; }
	
	public FileResManager(FileResManager rm, String sub)
	{ this(new File(rm.baseDirectory, sub)); }
	
	public FileResManager(FileResManager rm, File sub)
	{ this(rm, sub.getPath()); }
	
	public InputStream getInputStream(Resource r) throws Exception
	{ return new FileInputStream(new File(baseDirectory, r.path)); }
}