package com.latmod.latcoregl.nbt;

import com.latmod.lib.util.LMStringUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTByteArray extends NBTBase
{
    public byte[] data;

    public NBTByteArray(byte[] p)
    {
        data = (p == null) ? new byte[0] : p;
    }

    @Override
    public NBTID getID()
    {
        return NBTID.BYTE_ARRAY;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        int s = in.readInt();

        data = new byte[s];

        if(s > 0)
        {
            in.readFully(data, 0, s);
        }
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        out.writeInt(data.length);
        out.write(data);
    }

    @Override
    public int getByteCount()
    {
        return 4 + data.length;
    }

    @Override
    public String toString()
    {
        return "[ " + LMStringUtils.stripB(data) + " ]";
    }

    @Override
    public NBTBase copy()
    {
        return new NBTByteArray(data.clone());
    }
}