package com.latmod.latcoregl.nbt;

import com.latmod.lib.io.ByteIOStream;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Made by LatvianModder
 */
public class NBTMap extends NBTBase implements Iterable<NBTBase>
{
    public final Map<String, NBTBase> map;

    public NBTMap()
    {
        map = new HashMap<>();
    }

    @Override
    public NBTID getID()
    {
        return NBTID.MAP;
    }

    @Override
    public void read(DataInput in) throws IOException
    {
        map.clear();
        int size = in.readUnsignedShort();
        if(size > 0)
        {
            for(int i = 0; i < size; i++)
            {
                byte typeID = in.readByte();
                String s = in.readUTF();
                NBTBase b = NBTID.get(typeID).newBaseFromID();

                if(b != null)
                {
                    b.read(in);
                    map.put(s, b);
                }
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
            for(Map.Entry<String, NBTBase> e : map.entrySet())
            {
                NBTBase b = e.getValue();
                out.writeByte(b.getID().ID);
                out.writeUTF(e.getKey());
                b.write(out);
            }
        }
    }

    @Override
    public int getByteCount()
    {
        int bcount = 2;

        for(Map.Entry<String, NBTBase> e : map.entrySet())
        {
            bcount += 1;
            bcount += ByteIOStream.getUTFLength(e.getKey());
            bcount += e.getValue().getByteCount();
        }

        return bcount;
    }

    @Override
    public String toString()
    {
        return map.toString();
    }

    @Override
    public NBTBase copy()
    {
        NBTMap map1 = new NBTMap();

        for(Map.Entry<String, NBTBase> entry : map.entrySet())
        {
            map1.map.put(entry.getKey(), entry.getValue().copy());
        }

        return map1;
    }

    // 'Get' methods //

    public NBTBase getBase(String key)
    {
        return map.get(key);
    }

    public String getString(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? "" : ((NBTString) b).data;
    }

    public boolean getBoolean(String s)
    {
        return getByte(s) == 1;
    }

    public int getByte(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? 0 : b.getAsByte();
    }

    public int getShort(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? 0 : b.getAsShort();
    }

    public int getInt(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? 0 : b.getAsInt();
    }

    public long getLong(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? 0 : b.getAsLong();
    }

    public float getFloat(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? 0 : b.getAsFloat();
    }

    public double getDouble(String s)
    {
        NBTBase b = getBase(s);
        return (b == null) ? 0 : b.getAsDouble();
    }

    public NBTList getList(String s)
    {
        NBTBase p = getBase(s);
        return (p == null) ? null : (NBTList) p;
    }

    public NBTMap getMap(String s)
    {
        NBTBase p = getBase(s);
        return (p == null) ? null : (NBTMap) p;
    }

    public byte[] getByteArray(String s)
    {
        NBTBase p = getBase(s);
        return (p == null) ? new byte[0] : ((NBTByteArray) p).data;
    }

    public int[] getIntArray(String s)
    {
        NBTBase p = getBase(s);
        return (p == null) ? new int[0] : ((NBTIntArray) p).data;
    }

    public double[] getDoubleArray(String s)
    {
        NBTBase p = getBase(s);
        return (p == null) ? new double[0] : ((NBTDoubleArray) p).data;
    }

    public int size()
    {
        return map.size();
    }

    // 'Set' methods //

    public void setTag(String key, NBTBase base)
    {
        if(key == null || base == null || key.isEmpty())
        {
            throw new NullPointerException();
        }

        map.put(key, base);
    }

    public void setString(String key, String p)
    {
        setTag(key, new NBTString(p));
    }

    public void setBoolean(String s, boolean p)
    {
        setTag(s, new NBTByte(p ? (byte) 1 : (byte) 0));
    }

    public void setByte(String s, int p)
    {
        setTag(s, new NBTByte((byte) p));
    }

    public void setShort(String s, int p)
    {
        setTag(s, new NBTShort((short) p));
    }

    public void setInt(String s, int p)
    {
        setTag(s, new NBTInt(p));
    }

    public void setLong(String s, long p)
    {
        setTag(s, new NBTLong(p));
    }

    public void setFloat(String s, float p)
    {
        setTag(s, new NBTFloat(p));
    }

    public void setDouble(String s, double p)
    {
        setTag(s, new NBTDouble(p));
    }

    public void setByteArray(String s, byte... ai)
    {
        setTag(s, new NBTByteArray(ai));
    }

    public void setIntArray(String s, int... ai)
    {
        setTag(s, new NBTIntArray(ai));
    }

    public void setDoubleArray(String s, double... ai)
    {
        setTag(s, new NBTDoubleArray(ai));
    }

    public void remove(String s)
    {
        map.remove(s);
    }

    public void clear()
    {
        map.clear();
    }

    public boolean hasKey(String s)
    {
        return map.containsKey(s);
    }

    @Override
    public NBTMap clone()
    {
        NBTMap map1 = new NBTMap();
        map1.map.putAll(map);
        return map1;
    }

    @Override
    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }

        return (o instanceof NBTMap) && ((NBTMap) o).map.equals(map);
    }

    public boolean hasValues()
    {
        return !map.isEmpty();
    }

    @Override
    public Iterator<NBTBase> iterator()
    {
        return map.values().iterator();
    }
}