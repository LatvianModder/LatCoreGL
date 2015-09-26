package latmod.core.nbt;
import latmod.core.util.*;

public class NBTDoubleArray extends NBTBase
{
	public double[] data = null;
	
	public NBTDoubleArray(double[] p)
	{ super(NBTID.DOUBLE_ARRAY); data = p; }

	public void read(DataIOStream dios) throws Exception
	{
		int s = dios.readUShort();
		data = new double[s];
		for(int i = 0; i < s; i++)
		data[i] = dios.readDouble();
	}

	public void write(DataIOStream dios) throws Exception
	{
		dios.writeUShort(data.length);
		for(int i = 0; i < data.length; i++)
		dios.writeDouble(data[i]);
	}
	
	public Object getData()
	{ return data; }
	
	public String toString()
	{ return FastList.asList(data).toString(); }
	
	public int getByteCount()
	{ return 2 + ((data == null) ? 0 : data.length * 8); }
}