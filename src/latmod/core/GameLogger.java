package latmod.core;
import java.io.*;

public abstract class GameLogger
{
	public GameLogger()
	{
		EventGroup.DEFAULT.addListener(this);
	}
	
	public abstract int getLogCount();
	
	private static void copyTo(File from, File to)
	{
		try
		{
			if(!to.exists()) to.createNewFile();
			
			FileInputStream fis = new FileInputStream(from);
			FileOutputStream fos = new FileOutputStream(to);
			
			byte[] b = new byte[fis.available()];
			fis.read(b);
			fos.write(b);
			
			fis.close();
			fos.close();
		}
		catch(Exception e)
		{ e.printStackTrace(); }
	}
	
	@EventHandler
	public void saveLog(EventSaveLog e)
	{
		int logCount = getLogCount();
		
		File logs[] = new File[logCount];
		
		for(int i = 0; i < logs.length; i++)
		logs[i] = LatCoreGL.file("logs/log_" + (i + 1) + ".txt");
		
		if(logCount > 0)
		{
			for(int i = logs.length - 2; i >= 0; i--)
			if(logs[i].exists()) copyTo(logs[i], logs[i + 1]);
		}
		else return;
		
		if(!logs[0].exists()) logs[0] = LatCoreGL.newFile("logs/log_1.txt");
		
		try
		{
			FileWriter fw = new FileWriter(logs[0]);
			
			fw.append(">> Log saved in " + Time.get().getDateString() + " :: " + Time.get().getTimeString() + " :: " + Time.millis() + " <<");
			fw.append("\n\n");
			
			for(int i = 0; i < e.log.size(); i++)
			{
				fw.append(e.log.get(i));
				fw.append('\n');
			}
			
			fw.close();
		}
		catch(Exception ex)
		{ ex.printStackTrace(); }
	}
}