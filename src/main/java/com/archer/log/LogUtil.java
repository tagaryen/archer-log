package com.archer.log;

import java.io.File;
import java.io.IOException;

class LogUtil {

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
}
