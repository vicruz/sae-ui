package com.sae.gandhi.spring.utils;

import java.util.Calendar;
import java.util.Date;

public class SaeDateUtils {

	public static Calendar getCalendarFromDate(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
		
	}
	
}
