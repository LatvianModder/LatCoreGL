package latmod.core.nbt;

import latmod.core.util.FastList;

public interface INBTParent
{
	public FastList<NBTBase> getChildren();
}