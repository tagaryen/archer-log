package com.archer.log;

final class ConsoleAppender {
	
	private static final int BASE_LEN = 16 * 5;

	private static final char SPACE = ' ';
	
	private static final String RESET = "\u001B[0m";
	private static final String BOLD = "\u001B[0;1m";
	private static final String BOLD_GREEN = "\u001B[1;32m";
	private static final String RED = "\u001B[31m";
	private static final String BOLD_RED = "\u001B[1;31m";
	
	public static final int TRACE = 5;
	public static final int DEBUG = 4;
	public static final int INFO = 3;
	public static final int WARN = 2;
	public static final int ERROR = 1;
	
	public static String foramt(LogLevel level, String lv, String time, String clazz, String msg) {
		StringBuilder sb = new StringBuilder(lv.length() + time.length() + 
				clazz.length() + msg.length() + BASE_LEN);

		sb.append(getLvColor(level)).append(lv).append(SPACE)
		  .append(getTimeColor(level)).append(time).append(SPACE)
		  .append(getClassColor(level)).append(clazz).append(SPACE)
		  .append(getMsgColor(level)).append(msg).append(RESET);
		
		return sb.toString();
	}
	
	
	public static String getLvColor(LogLevel level) {
		switch(level) {
		case TRACE: {
			return RESET;
		}
		case DEBUG: {
			return BOLD;
		}
		case INFO: {
			return BOLD_GREEN;
		}
		case WARN: {
			return RED;
		}
		case ERROR: {
			return BOLD_RED;
		}
		default: {
			return RESET;
		}
		}
	}
	
	public static String getTimeColor(LogLevel level) {
		switch(level) {
		case TRACE: {
			return RESET;
		}
		case DEBUG: {
			return BOLD;
		}
		case INFO: {
			return BOLD;
		}
		case WARN: {
			return RED;
		}
		case ERROR: {
			return BOLD_RED;
		}
		default: {
			return RESET;
		}
		}
	}
	

	public static String getClassColor(LogLevel level) {
		switch(level) {
		case TRACE: {
			return RESET;
		}
		case DEBUG: {
			return BOLD;
		}
		case INFO: {
			return BOLD;
		}
		case WARN: {
			return BOLD;
		}
		case ERROR: {
			return BOLD_RED;
		}
		default: {
			return RESET;
		}
		}
	}

	public static String getMsgColor(LogLevel level) {
		switch(level) {
		case TRACE: {
			return RESET;
		}
		case DEBUG: {
			return RESET;
		}
		case INFO: {
			return RESET;
		}
		case WARN: {
			return RESET;
		}
		case ERROR: {
			return BOLD_RED;
		}
		default: {
			return RESET;
		}
		}
	}
}
