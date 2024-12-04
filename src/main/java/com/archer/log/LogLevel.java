package com.archer.log;

public enum LogLevel {
	/**
	 * level
	 * */
	TRACE(5, "TRACE"),
	
	DEBUG(4, "DEBUG"),
	
	INFO(3, "INFO"),
	
	WARN(2, "WARN"),
	
	ERROR(1, "ERROR");
	
	private int level;
	
	private String value;
	
	LogLevel(int level, String value) {
		this.level = level;
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static LogLevel of(String lv) {
		for(LogLevel level: values()) {
			if(level.getValue().equals(lv)) {
				return level;
			}
		}
		throw new IllegalArgumentException("invalid log level " + lv);
	}

}
