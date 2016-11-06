package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.model.task.PriorityType;
import main.model.task.Task;

public class FloatingTaskTest {
	
	private Task floatingTask;
	
	@Before
	public void setUpEventTask(){
		final String message = "test";		
		final PriorityType priority = PriorityType.NORMAL; //default priority
		
		 floatingTask = new Task(message, priority);
	}
	@Test
	public void testMessage() throws Exception{
		
		final String expected = "test";
		final String actual = floatingTask.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void floatingTaskEquals_returnTrue() throws Exception{
		final Task other = new Task("test", PriorityType.NORMAL);
		assertTrue(floatingTask.equals(other));
	}
	
	
	@Test
	public void floatingTaskEquals_returnFalse() throws Exception{
		final Task other = new Task(" ",  PriorityType.NORMAL);
		assertFalse(floatingTask.equals(other));
	}
	
	@Test
	public void toString_correctFormat(){
		final String expected = "test" ;
		final String actual = floatingTask.toString();
		assertEquals(expected, actual);
	}
}
