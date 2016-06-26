package com.latmod.latcoregl.nbt;

import com.latmod.lib.util.LMStringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTDoubleArray extends NBTBase
{
    public double[] data = null;

    public NBTDoubleArray(double[] p)
    {
        data = p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.DOUBLE_ARRAY;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = new double[in.readInt()];

        for(int i = 0; i < data.length; i++)
        {
            data[i] = in.readDouble();
        }
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeInt(data.length);

        for(int i = 0; i < data.length; i++)
        {
            out.writeDouble(data[i]);
        }
    }

    @Override
    public int getByteCount()
    {
        return 4 + data.length * 4;
    }

    @Override
    public String toString()
    {
        return "[ " + LMStringUtils.stripD(data) + " ]";
    }

    @Override
    public NBTBase copy()
    {
        return new NBTDoubleArray(data);
    }
}