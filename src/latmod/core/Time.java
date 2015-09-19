package latmod.core;
import java.util.Date;

/** Made by LatvianModder */
public final class Time
{
	public static enum Month
	{
		January,
		February,
		March,
		April,
		May,
		June,
		July,
		August,
		September,
		October,
		November,
		December,
	};
	
	public static enum Day
	{
		Sunday,
		Monday,
		Tuesday,
		Wednesday,
		Thursday,
		Friday,
		Saturday,
	};
	
	public final long millis;
	public final Date date;
	public final int hours;
	public final int minutes;
	public final int seconds;
	
	public final int day;
	public final Month month;
	public final int year;
	public final Day dayOfWeek;
	
	@SuppressWarnings("deprecation")
	private Time(long m)
	{
		millis = m;
		date = new Date(millis);
		
		seconds = date.getSeconds();
		minutes = date.getMinutes();
		hours = date.getHours();
		
		day = date.getDate();
		month = Month.values()[date.getMonth()];
		year = date.getYear();
		dayOfWeek = Day.values()[(date.getDay() + 6) % 7];
	}
	
	//public static long millisGL()
	//{ return (Sys.getTime() * 1000) / Sys.getTimerResolution(); }
	
	public static long millis()
	{ return System.currentTimeMillis(); }
	
	public static float since(long l)
	{ return (millis() - l) / 1000F; }
	
	public static Time get()
	{ return get(System.currentTimeMillis()); }
	
	public static Time get(long m)
	{ return new Time(m); }
	
	public String getDateString()
	{
		String d = "" + (day + 1);
		if(d.length() == 1) d = "0" + d;
		String m = "" + (month.ordinal() + 1);
		if(m.length() == 1) m = "0" + m;
		String y = "" + year;
		return d + "." + m + "." + y;
	}
	
	public String getDateStringInv()
	{
		String d = "" + (day + 1);
		if(d.length() == 1) d = "0" + d;
		String m = "" + (month.ordinal() + 1);
		if(m.length() == 1) m = "0" + m;
		String y = "" + year;
		return y + "." + m + "." + d;
	}
	
	public String getTimeString()
	{
		String h = "" + hours;
		if(h.length() == 1) h = "0" + h;
		String m = "" + minutes;
		if(m.length() == 1) m = "0" + m;
		String s = "" + seconds;
		if(s.length() == 1) s = "0" + s;
		return h + ":" + m + ":" + s;
	}
}