package latmod.core.nbt;
import latmod.lib.*;

public class NBTByteArray extends NBTBase
{
	public byte[] data = null;
	
	public NBTByteArray(byte[] p)
	{ super(NBTID.BYTE_ARRAY); data = p; }
	
	public void read(ByteIOStream dios)
	{ data = dios.readByteArray(ARRAY_BYTE_COUNT); }
	
	public void write(ByteIOStream dios)
	{ dios.writeByteArray(data, ARRAY_BYTE_COUNT); }
	
	public int getByteCount()
	{ return ARRAY_BYTE_COUNT.bytes + ((data == null) ? 0 : data.length); }
	
	public String toString()
	{ return "[ " + LMStringUtils.stripB(data) + " ]"; }
}