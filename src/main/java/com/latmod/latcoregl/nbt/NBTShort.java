package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTShort extends NBTBase
{
    public short data;

    public NBTShort(short p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.SHORT;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readShort();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeShort(data);
    }

    @Override
    public int getByteCount()
    {
        return 2;
    }

    @Override
    public String toString()
    {
        return Short.toString(data);
    }

    @Override
    public NBTBase copy()
    {
        return new NBTShort(data);
    }
}