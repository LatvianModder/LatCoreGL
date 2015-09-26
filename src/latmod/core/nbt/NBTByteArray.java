package latmod.core.nbt;
import latmod.core.util.*;

public class NBTByteArray extends NBTBase
{
	public byte[] data = null;
	
	public NBTByteArray(byte[] p)
	{ super(NBTID.BYTE_ARRAY); data = p; }
	
	public void read(DataIOStream dios) throws Exception
	{ data = dios.readByteArray(); }
	
	public void write(DataIOStream dios) throws Exception
	{ dios.writeByteArray(data); }
	
	public Object getData()
	{ return data; }
	
	public int getByteCount()
	{ return 2 + ((data == null) ? 0 : data.length); }
	
	public String toString()
	{ return "[ " + LMStringUtils.stripI(Converter.toInts(data)) + " ]"; }
}