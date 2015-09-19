package latmod.core.nbt;

import latmod.core.util.DataIOStream;

public class NBTString extends NBTBase
{
	public String data;
	
	public NBTString(String p)
	{ super(NBTID.STRING); data = p; }

	public void read(DataIOStream dios) throws Exception
	{ data = dios.readString(); }

	public void write(DataIOStream dios) throws Exception
	{ dios.writeString(data); }
	
	public Object getData()
	{ return data; }
	
	public int getByteCount()
	{ return 2 + ((data == null) ? 0 : data.getBytes().length); }
}