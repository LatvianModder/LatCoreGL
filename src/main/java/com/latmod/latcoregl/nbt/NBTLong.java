package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTLong extends NBTBase
{
    public long data;

    public NBTLong(long p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.LONG;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readLong();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeLong(data);
    }

    @Override
    public int getByteCount()
    {
        return 8;
    }

    @Override
    public String toString()
    {
        return Long.toString(data);
    }

    @Override
    public NBTBase copy()
    {
        return new NBTLong(data);
    }

    @Override
    public byte getAsByte()
    {
        return (byte) data;
    }

    @Override
    public short getAsShort()
    {
        return (short) data;
    }

    @Override
    public int getAsInt()
    {
        return (int) data;
    }

    @Override
    public long getAsLong()
    {
        return data;
    }

    @Override
    public double getAsDouble()
    {
        return data;
    }

    @Override
    public float getAsFloat()
    {
        return data;
    }

    @Override
    public String getAsString()
    {
        return Long.toString(data);
    }
}