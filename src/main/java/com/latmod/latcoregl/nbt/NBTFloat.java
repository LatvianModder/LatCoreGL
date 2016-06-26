package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTFloat extends NBTBase
{
    public float data;

    public NBTFloat(float p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.FLOAT;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readFloat();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeFloat(data);
    }

    @Override
    public int getByteCount()
    {
        return 4;
    }

    @Override
    public String toString()
    {
        return Float.toString(data);
    }

    @Override
    public NBTBase copy()
    {
        return new NBTFloat(data);
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
        return (long) data;
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
        return Float.toString(data);
    }
}