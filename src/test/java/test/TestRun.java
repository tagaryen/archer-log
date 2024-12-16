package test;

import java.time.LocalDateTime;

import com.archer.log.Logger;

public class TestRun {
	public static void main(String args[]) {
		Logger log = Logger.getLogger();
		
		TestObj obj = new TestObj();
		obj.a = 7123;
		obj.id = "tagaryen";
		obj.time = LocalDateTime.now();
		
		log.warn("now is {}, obj = {}", LocalDateTime.now(), obj);
	}
}
