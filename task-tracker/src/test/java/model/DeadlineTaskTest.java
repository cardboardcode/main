//@@author A0139750B
package model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import main.model.task.PriorityType;
import main.model.task.Task;
import main.model.task.TaskType;

public class DeadlineTaskTest {
	
	private Task deadlineTask;
	
	@Before
	public void setUpDeadlineTask(){
		final String message = "test";
		final Date deadline = new Date();
		final PriorityType priority = PriorityType.NORMAL; //default priority
		
		deadlineTask = new Task(message, deadline, priority);
	}
	@Test
	public void testMessage() throws Exception{
		
		final String expected = "test";
		final String actual = deadlineTask.getMessage();
		assertEquals(expected, actual);
	}
	@Test
	public void testDeadline() throws Exception{
		final Date expected = new Date();
		final Date actual = deadlineTask.getDeadline();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPriority() throws Exception{
		final PriorityType expected = PriorityType.NORMAL;
		final PriorityType actual = deadlineTask.getPriority();
		assertEquals(expected, actual);
	}
	
	@Test
	public void floatingTaskEquals_returnTrue() throws Exception{
		final Task other = new Task("test", new Date(), PriorityType.NORMAL);
		assertTrue(deadlineTask.equals(other));
	}
	
	
	@Test
	public void floatingTaskEquals_returnFalse() throws Exception{
		final Task other = new Task(" ", new Date(), PriorityType.NORMAL);
		assertFalse(deadlineTask.equals(other));
	}
	
	@Test
	public void toString_correctFormat(){
		final String expected = "test" + " due " + deadlineTask.getDeadlineString();
		final String actual = deadlineTask.toString();
		assertEquals(expected, actual);
	}

}
