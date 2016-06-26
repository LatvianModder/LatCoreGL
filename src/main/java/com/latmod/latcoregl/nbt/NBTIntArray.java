package com.latmod.latcoregl.nbt;

import com.latmod.lib.util.LMStringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTIntArray extends NBTBase
{
    public int[] data;

    public NBTIntArray(int[] p)
    {
        data = (p == null) ? new int[0] : p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.INT_ARRAY;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        data = new int[in.readInt()];

        for(int i = 0; i < data.length; i++)
        {
            data[i] = in.readInt();
        }
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeInt(data.length);

        for(int i = 0; i < data.length; i++)
        {
            out.writeInt(data[i]);
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
        return "[ " + LMStringUtils.stripI(data) + " ]";
    }

    @Override
    public NBTBase copy()
    {
        return new NBTIntArray(data);
    }
}