package com.archer.log;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

final class TimeFormatter {
	DateTimeFormatter dtf;
	
	public TimeFormatter(String pattern) {
		dtf = DateTimeFormatter.ofPattern(pattern);
	}
	
	public String format(LocalDateTime time) {
		return "[" + dtf.format(time) + "]";
	}
}
