package com.latmod.latcoregl.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTByte extends NBTBase
{
    public byte data;

    public NBTByte(byte p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.BYTE;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readByte();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeByte(data);
    }

    @Override
    public int getByteCount()
    {
        return 1;
    }

    @Override
    public String toString()
    {
        return Byte.toString(data);
    }

    @Override
    public NBTBase copy()
    {
        return new NBTByte(data);
    }

    @Override
    public byte getAsByte()
    {
        return data;
    }

    @Override
    public short getAsShort()
    {
        return data;
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
        return Byte.toString(data);
    }
}