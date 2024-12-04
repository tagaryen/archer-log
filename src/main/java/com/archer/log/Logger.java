package com.archer.log;

import java.time.LocalDateTime;


/**
 * @author xuyi
 */
public class Logger {

	private static final String def = "DEFAULT";

	private static final int CLASS_STACK = 4;

	private static final char[] formatPattern = {'{', '}'};
	
	public synchronized static Logger getLogger() {
		return LogManager.getLogger(def);
	}
	
	public synchronized static Logger getLoggerAndSetPropertiesIfNotExits(String name, LogProperties properties) {
		return LogManager.getLogger(name, properties);
	}

	public synchronized static void setDefaultProperties(LogProperties properties) {
		LogManager.setProperties(def, properties);
	}
	
	public synchronized static void setProperties(String name, LogProperties properties) {
		LogManager.setProperties(name, properties);
	} 

	private static void jlog(String msg) {
		System.out.println(msg);
	}
	
	
	private String name;
	
	private LogFileWriter writer;
	
	private LogProperties properties;
	
	private LogLevel logLevel;
	
	private TimeFormatter tf;
	
	private ClassFormatter cf;
	
	protected Logger(LogProperties properties) {
		this(def, properties);
	}
	
	protected Logger(String name, LogProperties properties) {
		this.name = name;
		this.properties = properties;
		logLevel = LogLevel.of(properties.getLevel());
		tf = new TimeFormatter(properties.getTimePattern());
		cf = new ClassFormatter(properties.getClassPattern());
		if(properties.isAppendFile()) {
			writer = new LogFileWriter(
					properties.isAppendFile(),
					properties.getKeepDays(),
					properties.getLogPath(),
					properties.getFileName());
		} 
	}

	public String getName() {
		return name;
	}

	public void trace(String txt, Object...texts) {
		log(LogLevel.TRACE, txt, texts);
	}

	public void debug(String txt, Object...texts) {
		log(LogLevel.DEBUG, txt, texts);
	}

	public void info(String txt, Object...texts) {
		log(LogLevel.INFO, txt, texts);
	}
	
	public void warn(String txt, Object...texts) {
		log(LogLevel.WARN, txt, texts);
	}
	
	public void error(String txt, Object...texts) {
		log(LogLevel.ERROR, txt, texts);
	}

	private void log(LogLevel level, String txt, Object...args) {
		if(level.getLevel() > logLevel.getLevel()) {
			return ;
		}
		LogMessage logMsg = getLogMessage(level);
		String[] texts = new String[args.length];
		for(int i = 0; i < args.length; i++) {
			texts[i] = LogUtil.formatObject(args[i], properties.getStackDepth());
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
				finalTxtSb.append(LogUtil.COMMA)
				.append(LogUtil.SPACE).append(texts[count]);
			}
		}
		logMsg.msg(finalTxtSb.toString());
		appendLog(logMsg);
	}
	

	private LogMessage getLogMessage(LogLevel level) {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement sourceInvoke;
		if(stackTrace.length > CLASS_STACK) {
			sourceInvoke = stackTrace[CLASS_STACK];
		} else if (stackTrace.length > 0){
			sourceInvoke = stackTrace[stackTrace.length - 1];
		} else {
			throw new RuntimeException("Error stack trace"); 
		}
		String clazz = sourceInvoke.getClassName();
		String method = sourceInvoke.getMethodName();
		String line = String.valueOf(sourceInvoke.getLineNumber());
		LogClass logCls = new LogClass(clazz, method, line);
		
		return new LogMessage(level, LocalDateTime.now(), logCls);
	}
	
	private void appendLog(LogMessage logMsg) {
		String msg = logMsg.toConsoleString(tf, cf);
		jlog(msg);
		if(properties.isAppendFile()) {
			String fileMsg = logMsg.toFileString(tf, cf);
			writer.record(fileMsg);
		}
	}
}
