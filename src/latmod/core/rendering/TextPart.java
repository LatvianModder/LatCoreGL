package latmod.core.rendering;

import latmod.core.nbt.NBTMap;
import latmod.core.util.FastList;

public class TextPart
{
	public static final TextPart defaultParentTextPart = new TextPart()
	{
		public boolean isBold()
		{ return false; }
		
		public boolean isItalic()
		{ return false; }
		
		public boolean hasUnderline()
		{ return false; }
		
		public TextColor getColor()
		{ return TextColor.WHITE; }
		
		public String toString()
		{ return ""; }
		
		public int length()
		{ return 0; }
	};
	
	public String text;
	public TextPart parent;
	
	private Boolean isBold = null;
	private Boolean isItalic = null;
	private Boolean hasUnderline = null;
	private TextColor color = null;
	
	private boolean canAdd = true;
	
	public TextPart(String s, TextPart p)
	{
		text = s;
		parent = (p == null) ? defaultParentTextPart : p;
	}
	
	private TextPart()
	{
		text = "";
		parent = null;
	}
	
	public TextPart reset()
	{
		isBold = false;
		isItalic = false;
		hasUnderline = false;
		color = TextColor.WHITE;
		return this;
	}
	
	public TextPart copy()
	{
		TextPart p = new TextPart(text, parent);
		p.isBold = isBold;
		p.isItalic = isItalic;
		p.hasUnderline = hasUnderline;
		p.canAdd = canAdd;
		return p;
	}
	
	public TextPart setCantAdd()
	{ canAdd = false; return this; }
	
	public TextPart setBold(boolean b)
	{ isBold = b; return this; }
	
	public boolean isBold()
	{ return isBold == null ? parent.isBold() : isBold; }
	
	public TextPart setItalic(boolean b)
	{ isItalic = b; return this; }
	
	public boolean isItalic()
	{ return isItalic == null ? parent.isItalic() : isItalic; }
	
	public TextPart setHasUnderline(boolean b)
	{ hasUnderline = b; return this; }
	
	public boolean hasUnderline()
	{ return hasUnderline == null ? parent.hasUnderline() : hasUnderline; }
	
	public TextPart setColor(TextColor c)
	{ color = c; return this; }
	
	public TextColor getColor()
	{ return color == null ? parent.getColor() : color; }
	
	public TextPart add(TextPart p)
	{ p.parent = this; return p; }
	
	public String toString()
	{ return parent.toString() + text; }
	
	public int length()
	{ return text.length() + parent.length(); }
	
	public FastList<TextPart> toList()
	{
		FastList<TextPart> l = new FastList<TextPart>();
		addToList(l);
		return l;
	}
	
	private void addToList(FastList<TextPart> l)
	{ if(this != defaultParentTextPart) parent.addToList(l); l.add(this); }
	
	// Static //
	
	public static TextPart get(String s)
	{ return new TextPart(s, null); }
	
	public static TextPart compile(String s)
	{ return get(s); }
	
	public void readFromNBT(NBTMap m)
	{
		text = m.getString("T");
		if(m.hasKey("B")) isBold = m.getBoolean("B");
		if(m.hasKey("I")) isItalic = m.getBoolean("I");
		if(m.hasKey("U")) hasUnderline = m.getBoolean("U");
		if(m.hasKey("C")) color = TextColor.values()[m.getInt("C")];
		canAdd = !m.hasKey("A");
	}
	
	public void writeToNBT(NBTMap m)
	{
		m.setString("T", text);
		if(isBold != null) m.setBoolean("B", isBold);
		if(isItalic != null) m.setBoolean("I", isItalic);
		if(hasUnderline != null) m.setBoolean("U", hasUnderline);
		if(color != null) m.setInt("C", color.color);
		if(!canAdd) m.setBoolean("A", true);
	}
}