package com.latmod.latcoregl.nbt;

import com.latmod.lib.util.LMListUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Made by LatvianModder
 */
public class NBTList extends NBTBase implements Iterable<NBTBase>
{
    public final List<NBTBase> list;
    public NBTID listID = NBTID.NONE;

    public NBTList()
    {
        list = new ArrayList<>();
    }

    @Override
    public NBTID getID()
    {
        return NBTID.LIST;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        list.clear();

        int size = in.readUnsignedShort();

        if(size > 0)
        {
            listID = NBTID.get(in.readByte());

            for(int i = 0; i < size; i++)
            {
                NBTBase b = listID.newBaseFromID();
                b.read(in);
                add(b);
            }
        }
    }

    @Override
    public void write(DataOutput out) throws IOException
    {
        int s = size();

        out.writeShort(s);

        if(s > 0)
        {
            for(int i = 0; i < size(); i++)
            {
                if(i == 0)
                {
                    out.writeByte(listID.ID);
                }

                get(i).write(out);
            }
        }
    }

    @Override
    public String toString()
    {
        return LMListUtils.toString(list);
    }

    @Override
    public int getByteCount()
    {
        int bcount = 2;

        if(size() > 0)
        {
            for(int i = 0; i < size(); i++)
            {
                if(i == 0)
                {
                    bcount += 1;
                }
                bcount += get(i).getByteCount();
            }
        }

        return bcount;
    }

    public int size()
    {
        return list.size();
    }

    public void add(NBTBase b)
    {
        if(b != null && (size() == 0 || listID == NBTID.NONE || b.getID() == listID))
        {
            listID = b.getID();
            list.add(b);
        }
    }

    public NBTBase get(int i)
    {
        return list.get(i);
    }

    public void clear()
    {
        list.clear();
    }

    public void remove(int i)
    {
        list.remove(i);
    }

    @Override
    public Iterator<NBTBase> iterator()
    {
        return list.iterator();
    }

    @Override
    public NBTBase copy()
    {
        NBTList list1 = new NBTList();
        for(NBTBase b : list)
        {
            list1.list.add(b.copy());
        }
        return list1;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        return (o instanceof NBTList) && ((NBTList) o).list.equals(list);
    }
}