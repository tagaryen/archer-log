package com.archer.log;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

final class LogManager {
	
	private static ConcurrentMap<String, Logger> loggers = new ConcurrentSkipListMap<>();
	
	protected static void setProperties(String name, LogProperties properties) {
		Logger log = loggers.getOrDefault(name, null);
		if(log == null) {
			log = new Logger(name, properties);
			loggers.put(name, log);
		}
	}
	
	protected static Logger getLogger(String name) {
		Logger log = loggers.getOrDefault(name, null);
		if(log == null) {
			log = new Logger(name, LogProperties.getDefault());
			loggers.put(name, log);
		}
		return log;
	}
	
	protected static Logger getLogger(String name, LogProperties properties) {
		Logger log = loggers.getOrDefault(name, null);
		if(log == null) {
			log = new Logger(name, properties);
			loggers.put(name, log);
		}
		return log;
	}
}
