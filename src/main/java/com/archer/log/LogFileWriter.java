package com.archer.log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class LogFileWriter {

	private static final int MAX_KEEP_DAYS = 365 * 10;
	private static final String FILE_TAIL = ".log";
	
	private static final String LOG_PATH = "logs";
	private static final String FILE_NAME = "log";
	private static final int KEEP_DAYS = 7;


	private boolean appendFile;
	private int keepDays;
	private Path logPath;
	private String fileName;

    public LogFileWriter(boolean appendFile, int keepDays, String logPath, String fileName) {
    	this.appendFile = appendFile;
    	if(keepDays > MAX_KEEP_DAYS) {
    		this.keepDays = KEEP_DAYS;
    	} else {
    		this.keepDays = keepDays;
    	} 
		this.logPath = getLogPath(logPath);
    	if(fileName == null) {
    		this.fileName = FILE_NAME;
    	} else {
    		this.fileName = fileName;
    	}
    }

    public void record(String msg) {
    	byte[] content = (msg+"\n").getBytes(StandardCharsets.UTF_8);
    	LogRecoder.doRecord(this, content);
    }

    private Path getLogPath(String path) {
    	if(path == null) {
    		path = LOG_PATH;
    	}
    	Path dst = Paths.get(path);
    	if(!dst.isAbsolute()) {
    		dst = Paths.get(LogUtil.getCurrentWorkDir(), path);
    	}
    	if(appendFile) {
        	if(!Files.exists(dst)) {
        		try {
    				Files.createDirectories(dst);
    			} catch (IOException e) {
    				throw new RuntimeException("can not mkdir " + dst.toString());
    			}
        	}
    	}
		return dst;
    }

    private String getLogFileName() {
    	return fileName + "-" +
    			LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) +
    			FILE_TAIL;
    }

    protected File getLogFile() {
    	File logFile = new File(logPath.toString(), getLogFileName());
    	if(!logFile.exists()) {
    		try {
    			Files.createFile(logFile.toPath());
    		} catch (IOException ex) {
    			System.err.println("can not create file '" + logFile.toString() +
    					"', due to " + ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage());
    			return null;
    		}
    		removeOldLogs();
    	}
    	return logFile;
    }

    protected void removeOldLogs() {
    	if(keepDays <= 0) {
    		return ;
    	}
    	File[] allLogFiles = logPath.toFile().listFiles();
    	long ft = System.currentTimeMillis() - ((long)keepDays * 24L * 3600L * 1000L);
    	for(File log: allLogFiles) {
    		try {
				BasicFileAttributes attr = Files.readAttributes(log.toPath(), BasicFileAttributes.class);
				if(attr.creationTime().toMillis() < ft) {
					Files.delete(log.toPath());
				}
			} catch (IOException ignore) {}
    	}
    }
}
