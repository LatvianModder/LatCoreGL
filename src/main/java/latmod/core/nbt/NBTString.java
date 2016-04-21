package latmod.core.nbt;

import latmod.lib.ByteIOStream;

public class NBTString extends NBTBase
{
	public String data;
	
	public NBTString(String p)
	{
		super(NBTID.STRING);
		data = p;
	}
	
	public void read(ByteIOStream dios)
	{ data = dios.readUTF(); }
	
	public void write(ByteIOStream dios)
	{ dios.writeUTF(data); }
	
	public int getByteCount()
	{ return ByteIOStream.getUTFLength(data); }
	
	public String toString()
	{ return data; }
}