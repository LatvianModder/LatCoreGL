package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTInt extends NBTBase
{
    public int data;

    public NBTInt(int p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.INT;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readInt();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeInt(data);
    }

    @Override
    public int getByteCount()
    {
        return 4;
    }

    @Override
    public String toString()
    {
        return Integer.toString(data);
    }

    @Override
    public NBTBase copy()
    {
        return new NBTInt(data);
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
        return data;
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
        return Integer.toString(data);
    }
}