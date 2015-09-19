package latmod.core.nbt;

public abstract class NBTNumber extends NBTBase
{
	protected NBTNumber(NBTID id)
	{ super(id); }
	
	public abstract Number getNumber();
	
	public final Object getData()
	{ return getNumber(); }
}