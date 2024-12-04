package com.archer.log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;

class LogUtil {


	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	
	public static final char BRACES_L = '[';
	public static final char BRACES_R = ']';
	public static final char B_BRACES_L = '{';
	public static final char B_BRACES_R = '}';
	public static final char COMMA = ',';
	public static final char COLON = ':';
	public static final char DOT = '.';
	public static final char SPACE = ' ';
	public static final char ENTER = '\n';
	public static final char QUOTE = '"';
	public static final char SEM = ';';
	
	private static final String DECIMAL_PATTERN = "#.00";
	/**
	 * inner class to super
	 * */
	private static final String INNER_CLASS_FIELD = "this$";
	

	public static String formatObject(Object data, int stackDepth) {
		if(data == null) {
			return null;
		}
		if(data.getClass().isPrimitive()) {
			return String.valueOf(data);
		}
		if(data instanceof Boolean) {
			return (Boolean) data?"true":"false";
		}
		if(data instanceof Byte) {
			return ((Byte) data).toString();
		}
		if(data instanceof Character) {
			return ((Character) data).toString();
		}
		if(data instanceof Boolean) {
			return (Boolean) data?"true":"false";
		}
        if(data instanceof String) {
            return (String) data;
        }
        if(data instanceof Short) {
            return String.valueOf((Short)data);
        }
        if(data instanceof Integer) {
            return String.valueOf((Integer)data);
        }
        if(data instanceof Long) {
            return String.valueOf((Long)data);
        }
        if(data instanceof Number) {
            DecimalFormat df = new DecimalFormat(DECIMAL_PATTERN);
            return df.format(data);
        }
        if(data instanceof LocalDateTime) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(TIME_PATTERN);
            try {
                return ((LocalDateTime)data).format(dtf);
            } catch(Exception ignored) {}
            throw new RuntimeException("cannot format localDateTime "+data);
        }
        if(data instanceof Date) {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
            try {
                return sdf.format((Date)data);
            } catch(Exception ignored) {}
            throw new RuntimeException("cannot format date "+data);
        }
        if(data.getClass().isArray()) {
        	return formatArray(data, stackDepth);
        }
        if(data instanceof Collection) {
        	return formatCollection(data, stackDepth);
        }
        if(data instanceof Throwable) {
        	return formatThrowable(data, stackDepth);
        }
        try {
			return formatClass(data, stackDepth);
		} catch (IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("cannot format data "+data);
		}
	}
	
	private static String formatClass(Object data, int stackDepth) 
			throws IllegalArgumentException, IllegalAccessException {
		Class<?> clazz = data.getClass();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sb = new StringBuilder(clazz.getSimpleName());
		sb.append(B_BRACES_L);
		for(Field f: fields) {
			if(f.getName().startsWith(INNER_CLASS_FIELD)) {
				continue;
			}
			if(Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			if(Modifier.isTransient(f.getModifiers())) {
				continue;
			}
			sb.append(f.getName()).append(COLON);
			f.setAccessible(true);
			Object fv = f.get(data);
			if(fv instanceof String) {
				sb.append(QUOTE).append((String)fv)
						.append(QUOTE).append(COMMA);
				continue;
			}
			sb.append(formatObject(fv, stackDepth))
			.append(COMMA);
		}
		if(sb.charAt(sb.length()-1) == COMMA) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(B_BRACES_R);
		return sb.toString();
	}



	public static String formatThrowable(Object data, int stackDepth) {
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

	public static String formatCollection(Object data, int stackDepth) {
		StringBuilder sb = new StringBuilder();
		sb.append(BRACES_L);
		for(Object o: (Collection<?>)data) {
			if(o instanceof String) {
	    		sb.append(QUOTE).append((String)o).append(QUOTE).append(COMMA);
			} else {
				sb.append(formatObject(o, stackDepth)).append(COMMA);
			}
		}
		if(sb.charAt(sb.length()-1) == COMMA) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(BRACES_R);
    	
    	return sb.toString();
	}
	
	public static String formatArray(Object data, int stackDepth) {
		StringBuilder sb = new StringBuilder();
		sb.append(BRACES_L);
		if(data instanceof String[]) {
	    	for(String b: (String[])data) {
	    		sb.append(QUOTE).append(b).append(QUOTE).append(COMMA);
	    	}
		} else {
	    	for(Object b: (Object[])data) {
	    		sb.append(formatObject(b, stackDepth)).append(COMMA);
	    	}
		}
    	if(sb.charAt(sb.length()-1) == COMMA) {
    		sb.deleteCharAt(sb.length() - 1);
    	}
    	sb.append(BRACES_R);
    	
    	return sb.toString();
	}
	

	public static String getCurrentWorkDir() {
		try {
			return (new File("")).getCanonicalPath()+File.separator;
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static void mkdirs(String path) {
		File f = new File(path);
		if(!f.exists()) {
			if(!f.mkdirs()) {
				throw new RuntimeException("Mkdirs error,"+path);
			}
		}
	}

	public static void loadLib(String resourceName, String relativeLibPath) {
		String libPath = getCurrentWorkDir() + relativeLibPath;
		mkdirs(libPath);
		File libFile = new File(new File(libPath), resourceName);
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
			if(in == null) {
				throw new RuntimeException("Can not found resource: " + resourceName);
			}
			byte[] libBytes = new byte[in.available()];
			in.read(libBytes);
			if(!libFile.exists()) {
				libFile.createNewFile();
				Files.write(libFile.toPath(), libBytes, StandardOpenOption.WRITE);
			} else {
				byte[] existsBytes = Files.readAllBytes(libFile.toPath());
				if(existsBytes.length != libBytes.length) {
					Files.write(libFile.toPath(), libBytes, StandardOpenOption.WRITE);
				}
			}

			in.close();
			System.load(libFile.getAbsolutePath());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
