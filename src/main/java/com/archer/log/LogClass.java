package com.archer.log;

import java.util.Arrays;

final class LogClass {
	
	private String simpleClassName;
	
	private String className;
	
	private String methodName;
	
	private String line;
	
	public LogClass(String className, String methodName, String line) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.line = line;
		char[] chars = className.toCharArray();
		int i = chars.length - 1;
		for(; i >= 0; i--) {
			if(chars[i] == '.') {
				i++;
				break ;
			}
		}
		simpleClassName = new String(Arrays.copyOfRange(chars, i, chars.length));
	}

	public String getClassName() {
		return className;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getLine() {
		return line;
	}
	
	public String getSimpleClassName() {
		return simpleClassName;
	}

	public int length() {
		return className.length() + methodName.length() + line.length();
	}
}
