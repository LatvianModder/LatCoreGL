package com.latmod.latcoregl.nbt;

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
    {
        return (id > 0 && id < VALUES.length) ? VALUES[id] : NONE;
    }

    public NBTBase newBaseFromID()
    {
        switch(this)
        {
            case NONE:
                return null;
            case BYTE:
                return new NBTByte((byte) 0);
            case SHORT:
                return new NBTShort((short) 0);
            case INT:
                return new NBTInt(0);
            case LONG:
                return new NBTLong(0L);
            case FLOAT:
                return new NBTFloat(0F);
            case DOUBLE:
                return new NBTDouble(0D);
            case BYTE_ARRAY:
                return new NBTByteArray(null);
            case STRING:
                return new NBTString(null);
            case LIST:
                return new NBTList();
            case MAP:
                return new NBTMap();
            case INT_ARRAY:
                return new NBTIntArray(null);
            case DOUBLE_ARRAY:
                return new NBTDoubleArray(null);
            default:
                return null;
        }
    }
}