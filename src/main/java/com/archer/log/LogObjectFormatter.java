package com.archer.log;

import com.archer.xjson.XJSON;

class LogObjectFormatter {

	public static final char COMMA = ',';
	public static final char COLON = ':';
	public static final char DOT = '.';
	public static final char SPACE = ' ';
	public static final char ENTER = '\n';
	public static final char QUOTE = '"';
	public static final char SEM = ';';
	
	static final XJSON json = new XJSON();
	
	public static String formatObject(Object data, int stackDepth) {
        if(data instanceof Throwable) {
        	return formatThrowable(data, stackDepth);
        }
        return json.stringify(data);
	}
	
	
	private static String formatThrowable(Object data, int stackDepth) {
    	Throwable ex = (Throwable)data;
    	StackTraceElement[] stackTrace = ex.getStackTrace();
    	int depth = stackDepth > stackTrace.length ? stackTrace.length : stackDepth;
    	StringBuilder sb = new StringBuilder(ex.getClass().getName());
    	sb.append(COLON).append(ex.getLocalizedMessage());
    	for(int i= 0; i < depth; i++) {
    		StackTraceElement el = stackTrace[i];
    		sb.append(SEM).append(SPACE).append(el.getClassName())
    		.append(DOT).append(el.getMethodName()).append(COLON)
    		.append(el.getLineNumber());
    	}
    	return sb.toString();
	}
}
