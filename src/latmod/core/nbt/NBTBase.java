package latmod.core.nbt;

import latmod.core.util.DataIOStream;

/** Made by Mojang, rewritten by LatvianModder */
public abstract class NBTBase
{
	public final NBTID ID;
	public String name;
	protected INBTParent parent = null;
	
	protected NBTBase(NBTID id)
	{ ID = id; }
	
	public INBTParent getParent()
	{ return parent; }
	
	public NBTBase init(String s, INBTParent p)
	{ parent = p; return this; }
	
	public abstract void read(DataIOStream dios) throws Exception;
	public abstract void write(DataIOStream dios) throws Exception;
	public abstract Object getData();
	public abstract int getByteCount();
	
	public String toString()
	{ return String.valueOf(getData()); }
}