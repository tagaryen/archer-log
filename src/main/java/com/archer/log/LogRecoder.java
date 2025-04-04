package com.archer.log;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author xuyi
 */
final class LogRecoder extends Thread {
	
	private static volatile LogRecoder recoder;
	
	private static Buffer buf = new Buffer();
	
	private static ConcurrentLinkedQueue<LogEvent> queue = new ConcurrentLinkedQueue<>();

	public static void doRecord(LogEvent event) {
		queue.add(event);
		if(recoder == null || !recoder.isAlive()) {
			recoder = new LogRecoder();
			recoder.start();
		}
	}

	@Deprecated
	public static void doRecord(LogFileWriter writer, byte[] content) {
		buf.write(content);
		if(recoder == null || !recoder.isAlive()) {
			recoder = new LogRecoder(writer);
			recoder.start();
		}
	}

	@SuppressWarnings("unused")
	@Deprecated
	private LogFileWriter writer;
	
	@Deprecated
	public LogRecoder(LogFileWriter writer) {
		this.writer = writer;
	}

	public LogRecoder() {}
	
	@Override
	public void run() {
		int tryCount = 3;
		while(tryCount > 0) {
			LogEvent event = queue.poll();
			if(event == null) {
				tryCount--;
				if(tryCount == 0) {
					break;
				}
				continue;
			}
			tryCount = 3;
			event.genLogMessage();
			Logger.jlog(event.toConsoleString(event.logger().getTimeFormatter(), event.logger().getClassFormatter()));
			if(event.isAppendFile()) {
		    	byte[] content = event.toFileString(event.logger().getTimeFormatter(), event.logger().getClassFormatter()).getBytes(StandardCharsets.UTF_8);
				File log = event.logger().getWriter().getLogFile();
				if(log == null) {
					return ;
				}
				try {
					Files.write(log.toPath(), content, StandardOpenOption.APPEND);
				} catch (IOException e) {
					System.err.println("can not write logs to file '" +
							log.toString() + "', due to " + e.getClass().getSimpleName() + ": " + e.getLocalizedMessage());
				}	
		    	
			}
		}
		recoder = null;
	}
	
	@Deprecated
	static class Buffer {
		byte[] buf;
		int read;
		int write;
		Object lock = new Object();
		
		public Buffer() {
			this.buf = new byte[1024 * 1024];
			this.read = 0;
			this.write = 0;
		}
		
		public byte[] read() {
			synchronized(lock) {
				int len = write - read;
				byte[] ret = Arrays.copyOfRange(buf, read, read + len);
				read = read + len;
				return ret;
			}
		}
		
		public void write(byte[] in) {
			synchronized(lock) { 
				int len = in.length;
				if(buf.length - write + read < len) {
					int newLen = buf.length << 1;
					while(newLen - write + read < len) {
						newLen <<= 1;
					}
					byte[] tmp = new byte[newLen];
					System.arraycopy(buf, read, tmp, 0, write - read);
					buf = tmp;
					write = write - read;
					read = 0;
				} else if(buf.length - write < len) {
					System.arraycopy(buf, read, buf, 0, write - read);
					write = write - read;
					read = 0;
				}
				System.arraycopy(in, 0, buf, write, len);
				write += len;
			}
		}
		
		public int available() {
			return write - read;
		}
	}
}
