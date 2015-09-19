package latmod.core.nbt;
import latmod.core.util.*;

public class NBTIntArray extends NBTBase
{
	public int[] data;
	
	public NBTIntArray(int[] p)
	{ super(NBTID.INT_ARRAY); data = p; }

	public void read(DataIOStream dios) throws Exception
	{
		int s = dios.readShort();
		data = new int[s];
		for(int i = 0; i < s; i++)
		data[i] = dios.readInt();
	}

	public void write(DataIOStream dios) throws Exception
	{
		dios.writeShort(data.length);
		for(int i = 0; i < data.length; i++)
		dios.writeInt(data[i]);
	}
	
	public Object getData()
	{ return data; }
	
	public String toString()
	{ return FastList.asList(data).toString(); }
	
	public int getByteCount()
	{ return 2 + ((data == null) ? 0 : data.length * 4); }
}