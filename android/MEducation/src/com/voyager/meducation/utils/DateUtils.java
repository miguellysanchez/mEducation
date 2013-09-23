package com.voyager.meducation.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

	public static String getDateString() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		return date;
	}
}
