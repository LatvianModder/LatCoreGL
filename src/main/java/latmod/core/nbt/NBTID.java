package latmod.core.nbt;

import latmod.lib.Converter;

public enum NBTID
{
	NONE,
	BYTE,
	SHORT,
	INT,
	LONG,
	FLOAT,
	DOUBLE,
	BYTE_ARRAY,
	STRING,
	LIST,
	MAP,
	INT_ARRAY,
	DOUBLE_ARRAY;
	
	private static final NBTID[] VALUES = values();
	
	public final byte ID;
	public final String name;
	
	NBTID()
	{
		ID = (byte) ordinal();
		name = toString();
	}
	
	public static NBTID get(byte id)
	{ return (id > 0 && id < VALUES.length) ? VALUES[id] : NONE; }
	
	public NBTBase newBaseFromID()
	{
		if(this == NONE) return null;
		else if(this == BYTE) return new NBTByte((byte) 0);
		else if(this == SHORT) return new NBTShort((short) 0);
		else if(this == INT) return new NBTInt(0);
		else if(this == LONG) return new NBTLong(0L);
		else if(this == FLOAT) return new NBTFloat(0F);
		else if(this == DOUBLE) return new NBTDouble(0D);
		else if(this == BYTE_ARRAY) return new NBTByteArray(null);
		else if(this == STRING) return new NBTString(null);
		else if(this == LIST) return new NBTList();
		else if(this == MAP) return new NBTMap();
		else if(this == INT_ARRAY) return new NBTIntArray(null);
		else if(this == DOUBLE_ARRAY) return new NBTDoubleArray(null);
		return null;
	}
	
	public static NBTBase newBaseFromObject(Object o)
	{
		if(o == null) return null;
		else if(o instanceof Boolean) return new NBTByte(((Boolean) o).booleanValue() ? (byte) 1 : (byte) 0);
		else if(o instanceof Byte) return new NBTByte(((Byte) o).byteValue());
		else if(o instanceof Short) return new NBTShort(((Short) o).shortValue());
		else if(o instanceof Integer) return new NBTInt(((Integer) o).intValue());
		else if(o instanceof Long) return new NBTLong(((Long) o).longValue());
		else if(o instanceof Float) return new NBTFloat(((Float) o).floatValue());
		else if(o instanceof Double) return new NBTDouble(((Double) o).doubleValue());
		else if(o instanceof byte[]) return new NBTByteArray((byte[]) o);
		else if(o instanceof Byte[]) return new NBTByteArray(Converter.toBytes((Byte[]) o));
		else if(o instanceof String) return new NBTString(o.toString());
		else if(o instanceof NBTMap) return (NBTMap) o;
		else if(o instanceof NBTList) return (NBTList) o;
		else if(o instanceof int[]) return new NBTIntArray((int[]) o);
		else if(o instanceof Integer[]) return new NBTIntArray(Converter.toInts((Integer[]) o));
		else if(o instanceof double[]) return new NBTDoubleArray((double[]) o);
		else if(o instanceof Double[]) return new NBTDoubleArray(Converter.toDoubles((Double[]) o));
		else return null;
	}
}