package com.archer.log;

import java.time.LocalDateTime;

final class LogMessage {
	
	private static final int BASE_LEN = 2 + 5 + 2 + 24 + 6;
	
	private LogLevel level;
	
	private String lv;
	
	private LocalDateTime time;
	
	private LogClass clazz;
	
	private String msg;
	
	public LogMessage(LogLevel level, LocalDateTime time, LogClass clazz) {
		super();
		this.level = level;
		this.lv = formatLv(level);
		this.time = time;
		this.clazz = clazz;
		this.msg = "";
	}

	public String getLv() {
		return lv;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public LogClass getClazz() {
		return clazz;
	}

	public String getMsg() {
		return msg;
	}

	public LogMessage msg(String msg) {
		this.msg = msg;
		return this;
	}
	
	public int length() {
		return BASE_LEN + clazz.length() + msg.length();
	}
	
	public String toFileString(TimeFormatter dtf, ClassFormatter cf) {
		return FileAppender.foramt(level, lv, dtf.format(time), cf.format(clazz), msg);

	}
	
	public String toConsoleString(TimeFormatter dtf, ClassFormatter cf) {
		return ConsoleAppender.foramt(level, lv, dtf.format(time), cf.format(clazz), msg);
	}
	
	private String formatLv(LogLevel level) {
		switch(level) {
		case TRACE:
		case DEBUG:
		case ERROR: {
			return "["+level.getValue()+"]";
		}
		case INFO:
		case WARN: {
			return "["+level.getValue()+" ]";
		}
		default: {
			return "[UNKNOWN]";
		}
		}
	}
}
