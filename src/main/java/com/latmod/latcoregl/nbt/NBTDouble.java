package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTDouble extends NBTBase
{
    public double data;

    public NBTDouble(double p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.DOUBLE;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readDouble();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeDouble(data);
    }

    @Override
    public int getByteCount()
    {
        return 8;
    }

    @Override
    public String toString()
    {
        return Double.toString(data);
    }

    @Override
    public NBTBase copy()
    {
        return new NBTDouble(data);
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
        return (float) data;
    }

    @Override
    public String getAsString()
    {
        return Double.toString(data);
    }
}