package test.model;
import static  org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.model.task.*;
//import java.util.Date;

public class FloatingTaskTest{
	String message ;
	//Date startTime, endTime, deadline ;
	boolean isFloating ;
	//boolean isEvent; 
	
	@Before
	public void setUp(){
		Task task = new Task("hi");
		String expected = "hi";
		assertEquals(expected , task.getMessage());
		assertTrue(task.getIsFloating());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void message_null() throws IllegalArgumentException{
		Task task = new Task(message);
		task.getMessage().equals(null) ;
	}
	
}
