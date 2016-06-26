package com.latmod.latcoregl.nbt;

import com.latmod.lib.io.ByteIOStream;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTString extends NBTBase
{
    public String data;

    public NBTString(String p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.STRING;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = in.readUTF();
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeUTF(data);
    }

    @Override
    public int getByteCount()
    {
        return 2 + ByteIOStream.getUTFLength(data);
    }

    @Override
    public String toString()
    {
        return data;
    }

    @Override
    public NBTBase copy()
    {
        return new NBTString(data);
    }

    @Override
    public byte getAsByte()
    {
        return Byte.parseByte(data);
    }

    @Override
    public short getAsShort()
    {
        return Short.parseShort(data);
    }

    @Override
    public int getAsInt()
    {
        return Integer.parseInt(data);
    }

    @Override
    public long getAsLong()
    {
        return Long.parseLong(data);
    }

    @Override
    public double getAsDouble()
    {
        return Double.parseDouble(data);
    }

    @Override
    public float getAsFloat()
    {
        return Float.parseFloat(data);
    }

    @Override
    public String getAsString()
    {
        return data;
    }
}