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
	
	@Override
	public void read(ByteIOStream dios)
	{ data = dios.readUTF(); }
	
	@Override
	public void write(ByteIOStream dios)
	{ dios.writeUTF(data); }
	
	@Override
	public int getByteCount()
	{ return ByteIOStream.getUTFLength(data); }
	
	@Override
	public String toString()
	{ return data; }
}