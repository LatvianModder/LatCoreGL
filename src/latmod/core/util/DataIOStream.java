package latmod.core.util;
import java.io.*;
import java.util.UUID;

/** Made by LatvianModder <br>
 * DataInputStream + DataOutputStream */
public class DataIOStream
{
	private static final byte B_TRUE = 1;
	private static final byte B_FALSE = 0;
	
	private boolean closed = false;
	
	/** Input stream */
	public final DataInputStream input;
	
	/** Output stream */
	public final DataOutputStream output;
	
	/** Instance for reading and writing */
	public DataIOStream(InputStream is, OutputStream os)
	{
		input = getInput(is);
		output = getOutput(os);
	}
	
	/** Instance for reading */
	public DataIOStream(InputStream is)
	{ this(is, null); }
	
	/** Instance for writing */
	public DataIOStream(OutputStream os)
	{ this(null, os); }
	
	private DataInputStream getInput(InputStream is)
	{
		if(is == null) return null;
		else if(is instanceof DataInputStream) return (DataInputStream)is;
		return new DataInputStream(is);
	}
	
	private DataOutputStream getOutput(OutputStream os)
	{
		if(os == null) return null;
		else if(os instanceof DataOutputStream) return (DataOutputStream)os;
		return new DataOutputStream(new BufferedOutputStream(os));
	}
	
	/** Flushes output stream */
	public void flush() throws IOException
	{ if(output != null) output.flush(); }
	
	/** Stops streams */
	public void close() throws IOException
	{
		if(input != null) input.close();
		if(output != null) output.close();
		closed = true;
	}
	
	public boolean isClosed()
	{ return closed; }
	
	/** @return Bytes available to read */
	public int available() throws Exception
	{ return input.available(); }
	
	public void clear() throws Exception
	{ while(available() > 0) readByte(); }
	
	// Write functions //
	
	/** Byte count: 1 */
	public void writeByte(byte i) throws IOException
	{ output.write(i); }
	
	/** Byte count: 1 */
	public void writeBoolean(boolean b) throws Exception
	{ writeByte(b ? B_TRUE : B_FALSE); }
	
	/** Byte count: 2 + s.getBytes().length */
	public void writeString(String s) throws Exception
	{ writeBytes(s.getBytes()); }
	
	/** Byte count: 2 */
	public void writeShort(int i) throws Exception
	{ output.writeShort((short)i); }
	
	/** Byte count: 4 */
	public void writeInt(int i) throws Exception
	{ output.writeInt(i); }
	
	/** Byte count: 8 */
	public void writeLong(long l) throws Exception
	{ output.writeLong(l); }
	
	/** Byte count: 4 */
	public void writeFloat(float f) throws Exception
	{ int i = Float.floatToIntBits(f); writeInt(i); }
	
	/** Byte count: 8 */
	public void writeDouble(double d) throws Exception
	{ long l = Double.doubleToLongBits(d); writeLong(l); }
	
	/** Byte count: 2 + b.length */
	public void writeBytes(byte[] b) throws Exception
	{ if(b == null) { writeShort(-1); return; }
	writeShort(b.length); output.write(b); }
	
	public void writeAllBytes(byte[] b) throws Exception
	{ output.write(b); }
	
	public void writeUUID(UUID id) throws Exception
	{ writeLong(id.getMostSignificantBits()); writeLong(id.getLeastSignificantBits()); }
	
	// Read functions //
	
	public byte readByte() throws Exception
	{ return (byte)input.read(); }
	
	public boolean readBoolean() throws Exception
	{ return readByte() == 1; }
	
	public String readString()  throws Exception
	{ byte[] b = readBytes(); return new String(b); }
	
	public short readShort() throws Exception
	{ return input.readShort(); }
	
	public int readInt() throws Exception
	{ return input.readInt(); }
	
	public long readLong() throws Exception
	{ return input.readLong(); }
	
	public float readFloat() throws Exception
	{ int i = readInt(); return Float.intBitsToFloat(i); }
	
	public double readDouble() throws Exception
	{ long l = readLong(); return Double.longBitsToDouble(l); }
	
	public byte[] readBytes() throws Exception
	{ int s = readShort(); if(s == -1) return null;
	byte[] b = new byte[s]; input.read(b); return b; }
	
	public void read(byte[] b) throws Exception
	{ input.read(b); }
	
	public UUID readUUID() throws Exception
	{ return new UUID(readLong(), readLong()); }
}