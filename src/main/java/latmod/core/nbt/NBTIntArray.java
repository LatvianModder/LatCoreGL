package latmod.core.nbt;

import latmod.lib.ByteIOStream;
import latmod.lib.LMStringUtils;

public class NBTIntArray extends NBTBase
{
	public int[] data;
	
	public NBTIntArray(int[] p)
	{
		super(NBTID.INT_ARRAY);
		data = p;
	}
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readIntArray(ARRAY_BYTE_COUNT); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeIntArray(data, ARRAY_BYTE_COUNT); }
	
	@Override
	public int getByteCount()
	{ return ARRAY_BYTE_COUNT.bytes + ((data == null) ? 0 : data.length * 4); }
	
	@Override
	public String toString()
	{ return "[ " + LMStringUtils.stripI(data) + " ]"; }
}