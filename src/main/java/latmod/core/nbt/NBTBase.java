package latmod.core.nbt;

import latmod.lib.*;

/**
 * Made by Mojang, rewritten by LatvianModder
 */
public abstract class NBTBase
{
	public static final ByteCount ARRAY_BYTE_COUNT = ByteCount.SHORT;
	
	public final NBTID ID;
	public String name;
	protected INBTParent parent = null;
	
	protected NBTBase(NBTID id)
	{ ID = id; }
	
	public INBTParent getParent()
	{ return parent; }
	
	public NBTBase init(String s, INBTParent p)
	{
		parent = p;
		return this;
	}
	
	public abstract void read(ByteIOStream dios);
	public abstract void write(ByteIOStream dios);
	public abstract int getByteCount();
	public abstract String toString();
	
	public final Object getData() { return null; }
}