package com.archer.log;

final class ClassFormatter {

	private static final String CLS = "cls";
	private static final String CLASS = "class";
	private static final String METHOD = "method";
	private static final String LINE = "line";
	
	private String pattern;
	
	private boolean hasClass = false;
	private boolean isSimpleClass = false;
	private boolean hasMethod = false;
	private boolean hasLine = false;
	
	
	public ClassFormatter(String pattern) {
		parse(pattern);
		this.pattern = pattern;
	}
	
	public String format(LogClass classMsg) {
		String formatMsg = pattern;
		if(hasClass) {
			if(isSimpleClass) {
				formatMsg = formatMsg.replace(CLS, classMsg.getSimpleClassName());
			} else {
				formatMsg = formatMsg.replace(CLASS, classMsg.getClassName());
			}
		}
		if(hasMethod) {
			formatMsg = formatMsg.replace(METHOD, classMsg.getMethodName());
		}
		if(hasLine) {
			formatMsg = formatMsg.replace(LINE, classMsg.getLine());
		}
		return "[" + formatMsg + "]";
	}
	
	private void parse(String pattern) {
		if(pattern == null) {
			return ;
		}
		if(pattern.contains(CLS)) {
			hasClass = true;
			isSimpleClass = true;
		}
		if(pattern.contains(CLASS)) {
			if(isSimpleClass) {
				throw new IllegalArgumentException("invalid pattern " + pattern);
			}
			hasClass = true;
			isSimpleClass = false;
		}
		if(pattern.contains(METHOD)) {
			hasMethod = true;
		}
		if(pattern.contains(LINE)) {
			hasLine = true;
		}
	}
}
