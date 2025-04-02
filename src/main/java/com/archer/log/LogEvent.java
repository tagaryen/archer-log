package com.archer.log;

import java.time.LocalDateTime;

class LogEvent {

	private static final char[] formatPattern = {'{', '}'};
	
	private Logger logger;
	private StackTraceElement sourceInvoke;
	private LogLevel level; 
	private String txt;
	private Object[] args;
	
	private LocalDateTime time;
	private LogClass logCls;
	private String msg;
	
	public LogEvent(Logger logger, StackTraceElement sourceInvoke, LogLevel level, String txt, Object[] args) {
		this.logger = logger;
		this.sourceInvoke = sourceInvoke;
		this.level = level;
		this.txt = txt;
		this.args = args;
		this.time = LocalDateTime.now();
	}
	
	protected void genLogMessage() {
		String clazz = sourceInvoke.getClassName();
		String method = sourceInvoke.getMethodName();
		String line = String.valueOf(sourceInvoke.getLineNumber());
		logCls = new LogClass(clazz, method, line);
		
		String[] texts = new String[args.length];
		for(int i = 0; i < args.length; i++) {
			texts[i] = LogObjectFormatter.formatObject(args[i], logger.getStackDepth());
		}
		char[] txtChars = txt.toCharArray();
		StringBuilder finalTxtSb = new StringBuilder(txt.length() * args.length * 2);
		int count = 0;
		for(int i = 0; i < txtChars.length; i++) {
			if(txtChars[i] == formatPattern[0] && txtChars[i+1] == formatPattern[1]) {
				if(count < texts.length) {
					finalTxtSb.append(texts[count++]);
				} else {
					count++;
				}
				i++;
				continue;
			}
			finalTxtSb.append(txtChars[i]);
		}
		if(count < texts.length) {
			for(; count < texts.length; count++) {
				finalTxtSb.append(LogObjectFormatter.COMMA)
				.append(LogObjectFormatter.SPACE).append(texts[count]);
			}
		}
		this.msg = finalTxtSb.toString();
	}
	
	public boolean isAppendFile() {
		return logger.isAppendFile();
	}
	
	public Logger logger() {
		return logger;
	}
	
	public String toFileString(TimeFormatter dtf, ClassFormatter cf) {
		return FileAppender.foramt(level, formatLv(level), dtf.format(time), cf.format(logCls), msg);

	}
	
	public String toConsoleString(TimeFormatter dtf, ClassFormatter cf) {
		return ConsoleAppender.foramt(level, formatLv(level), dtf.format(time), cf.format(logCls), msg);
	}
	

	private String formatLv(LogLevel level) {
		switch(level) {
		case TRACE:
		case DEBUG:
		case ERROR:
		case INFO:
		case WARN: {
			return "["+level.getValue()+"]";
		}
		default: {
			return "[UNKNOWN]";
		}
		}
	}
}
