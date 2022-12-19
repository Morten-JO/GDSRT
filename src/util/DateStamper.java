package util;

import java.time.LocalTime;

public class DateStamper {

	public static String getStampedDate() {
		return LocalTime.now().toString();
	}
	
	public static LocalTime returnStampedDate(String date) {
		return LocalTime.parse(date);
	}
	
}
