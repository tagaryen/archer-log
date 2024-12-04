package com.archer.log;

public class LogProperties {

	private static final String  DEFAULT_LEVEL = "INFO";
	private static final String  DEFAULT_TIME = "yyyy-MM-dd HH:mm:ss.SSS";
	private static final String  DEFAULT_CLASS = "class.method.line";
	private static final int     DEFAULT_STACK = 6;
	private static final int     DEFAULT_DAYS = 7;
	private static final boolean DEFAULT_WRITE_FILE = false;
	private static final String  DEFAULT_PATH = "logs";
	private static final String  DEFAULT_FILE = "log";
	
	private String level;
	
	private String timePattern;
	
	private String classPattern;
	
	private int stackDepth;
	
	private int keepDays;
	
	private boolean appendFile;
	
	private String logPath;
	
	private String fileName;

	public LogProperties() {
		super();
	}

	public String getLevel() {
		if(level == null) {
			return DEFAULT_LEVEL;
		}
		return level;
	}

	public String getTimePattern() {
		if(timePattern == null) {
			return DEFAULT_TIME;
		}
		return timePattern;
	}

	public String getClassPattern() {
		if(classPattern == null) {
			return DEFAULT_CLASS;
		}
		return classPattern;
	}

	public int getStackDepth() {
		if(stackDepth <= 0) {
			return DEFAULT_STACK;
		}
		return stackDepth;
	}

	public int getKeepDays() {
		if(keepDays <= 0) {
			return DEFAULT_DAYS;
		}
		return keepDays;
	}

	public boolean isAppendFile() {
		return appendFile;
	}

	public String getLogPath() {
		if(logPath == null) {
			return DEFAULT_PATH;
		}
		return logPath;
	}

	public String getFileName() {
		if(fileName == null) {
			return DEFAULT_FILE;
		}
		return fileName;
	}



	public LogProperties level(String level) {
		this.level = level;
		return this;
	}
	
	public LogProperties appendFile(boolean appendFile) {
		this.appendFile = appendFile;
		return this;
	}

	public LogProperties logPath(String logPath) {
		this.logPath = logPath;
		return this;
	}

	public LogProperties fileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public LogProperties timePattern(String timePattern) {
		this.timePattern = timePattern;
		return this;
	}

	public LogProperties classPattern(String classPattern) {
		this.classPattern = classPattern;
		return this;
	}

	public LogProperties stackDepth(int stackDepth) {
		this.stackDepth = stackDepth;
		return this;
	}

	public LogProperties keepDays(int keepDays) {
		this.keepDays = keepDays;
		return this;
	}
	
	public static LogProperties getDefault() {
		return new LogProperties().level(DEFAULT_LEVEL)
				.timePattern(DEFAULT_TIME)
				.classPattern(DEFAULT_CLASS)
				.stackDepth(DEFAULT_STACK)
				.keepDays(DEFAULT_DAYS)
				.appendFile(DEFAULT_WRITE_FILE)
				.logPath(DEFAULT_PATH)
				.fileName(DEFAULT_FILE);
	}
}
