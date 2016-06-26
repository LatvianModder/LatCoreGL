package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Made by Mojang, rewritten by LatvianModder
 */
public abstract class NBTBase
{
    protected NBTBase()
    {
    }

    public abstract NBTID getID();

    public abstract void read(DataInput in) throws IOException;

    public abstract void write(DataOutput out) throws IOException;

    public abstract int getByteCount();

    @Override
    public abstract String toString();

    public abstract NBTBase copy();

    public byte getAsByte()
    {
        return 0;
    }

    public short getAsShort()
    {
        return 0;
    }

    public int getAsInt()
    {
        return 0;
    }

    public long getAsLong()
    {
        return 0L;
    }

    public double getAsDouble()
    {
        return 0D;
    }

    public float getAsFloat()
    {
        return 0F;
    }

    public String getAsString()
    {
        return "";
    }
}