package com.sae.gandhi.spring.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class SaeDateUtils {

	public static Calendar getCalendarFromDate(Date date){
		if(date==null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
		
	}
	
	public static Date localDateToDate(LocalDate localDate){
		if(localDate==null) return null;
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static Calendar localDateToCalendar(LocalDate localDate){
		if(localDate==null) return null;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(localDateToDate(localDate));
		return cal;
	}
	
	public static LocalDate calendarToLocalDate(Calendar calendar){
		if(calendar==null) return null;
		
		return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	public static LocalDate dateToLocalDate(Date date){
		if(date==null) return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
}
