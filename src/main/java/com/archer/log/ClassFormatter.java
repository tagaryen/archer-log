package com.archer.log;

final class ClassFormatter {

	private static final String CLS = "classSimple";
	private static final String CLASS = "class";
	private static final String METHOD = "method";
	private static final String LINE = "line";
	
	private boolean hasClass = false;
	private boolean isSimpleClass = false;
	private boolean hasMethod = false;
	private boolean hasLine = false;
	
	
	public ClassFormatter(String pattern) {
		parse(pattern);
	}
	
	public String format(LogClass classMsg) {
		StringBuilder sb = new StringBuilder(classMsg.length());
		if(hasClass) {
			if(isSimpleClass) {
				sb.append(classMsg.getSimpleClassName());
			} else {
				sb.append(classMsg.getClassName());
			}
		}
		if(hasMethod) {
			sb.append('.').append(classMsg.getMethodName());
		}
		if(hasLine) {
			sb.append(':').append(classMsg.getLine());
		}
		return "[" + sb.toString() + "]";
	}
	
	private void parse(String pattern) {
		if(pattern == null) {
			return ;
		}
		if(pattern.contains(CLS)) {
			hasClass = true;
			isSimpleClass = true;
		} else if (pattern.contains(CLASS)) {
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
