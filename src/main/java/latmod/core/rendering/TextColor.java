package latmod.core.rendering;

import latmod.lib.LMColorUtils;

/**
 * Made by LatvianModder
 */
public enum TextColor
{
	BLACK('0', 0),
	BLUE('1', 1),
	GREEN('2', 2),
	CYAN('3', 3),
	RED('4', 4),
	PURPLE('5', 5),
	GOLD('6', 6),
	L_GRAY('7', 7),
	GRAY('8', 8),
	L_BLUE('9', 9),
	LIME('a', 10),
	AQUA('b', 11),
	L_RED('c', 12),
	MAGENTA('d', 13),
	YELLOW('e', 14),
	WHITE('f', 15),
	BOLD('o'),
	ITALIC('i'),
	UNDERLINE('u');
	
	public static final TextColor[] VALUES = values();
	public static final TextColor[] COLORS = new TextColor[16];
	
	static { System.arraycopy(VALUES, 0, COLORS, 0, 16); }
	
	public static final char CHAR = '\u00a7';//Font //LatCore
	
	public char code;
	public int color = 0xFF000000;
	private String txt;
	public boolean isColor = false;
	
	TextColor(char c, int col)
	{
		code = c;
		isColor = false;
		txt = "\u00a7" + code;
		
		if(col >= 0)
		{
			color = LMColorUtils.chatFormattingColors[col];
			isColor = true;
		}
	}
	
	TextColor(char c)
	{ this(c, -1); }
	
	public String toString()
	{ return txt; }
	
	public static TextColor getColor(char c1)
	{
		for(int i = 0; i < VALUES.length; i++)
			if(VALUES[i].code == c1) return VALUES[i];
		return null;
	}
	
	public TextPart colored(TextPart s)
	{ return s.setColor(this); }
}