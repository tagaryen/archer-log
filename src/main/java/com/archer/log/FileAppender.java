package com.archer.log;

final class FileAppender {

	private static final int BASE_LEN = 4 * 5;

	private static final char SPACE = ' ';
	
	public static String foramt(LogLevel level, String lv, String time, String clazz, String msg) {
		StringBuilder sb = new StringBuilder(lv.length() + time.length() + 
				clazz.length() + msg.length() + BASE_LEN);

		sb.append(lv).append(SPACE)
		  .append(time).append(SPACE)
		  .append(clazz).append(SPACE)
		  .append(msg);
		
		return sb.toString();
	}
}
