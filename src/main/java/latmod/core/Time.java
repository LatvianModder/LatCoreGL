package latmod.core;

import java.util.Date;

/**
 * Made by LatvianModder
 */
public final class Time
{
	public enum Month
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
	}
	
	public enum Day
	{
		Sunday,
		Monday,
		Tuesday,
		Wednesday,
		Thursday,
		Friday,
		Saturday,
	}
	
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
		StringBuilder sb = new StringBuilder();
		int day1 = day + 1;
		if(day1 < 10) sb.append('0');
		sb.append(day1);
		sb.append('.');
		int month1 = month.ordinal() + 1;
		if(month1 < 10) sb.append('0');
		sb.append(month1);
		sb.append('.');
		sb.append(year);
		return sb.toString();
	}
	
	public String getDateStringInv()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(year);
		sb.append('.');
		int month1 = month.ordinal() + 1;
		if(month1 < 10) sb.append('0');
		sb.append(month1);
		sb.append('.');
		int day1 = day + 1;
		if(day1 < 10) sb.append('0');
		sb.append(day1);
		return sb.toString();
	}
	
	public String getTimeString()
	{
		StringBuilder sb = new StringBuilder();
		if(hours < 10) sb.append('0');
		sb.append(hours);
		sb.append(':');
		if(minutes < 10) sb.append('0');
		sb.append(minutes);
		sb.append(':');
		if(seconds < 10) sb.append('0');
		sb.append(seconds);
		return sb.toString();
	}
}