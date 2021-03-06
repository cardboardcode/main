//@@author A0139750B
package model;

import static org.junit.Assert.*;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Before;
import org.junit.Test;

import main.model.task.PriorityType;
import main.model.task.Task;

public class EventTaskTest {

	private Task eventTask;
	
	@Before
	public void setUpEventTask(){
		final String message = "test";
		final Date startTime = new Date();
		final Date endTime = DateUtils.addDays(startTime, 5);
		
		final PriorityType priority = PriorityType.NORMAL; //default priority
		
		eventTask = new Task(message, startTime, endTime, priority);
	}
	
	@Test
	public void testMessage() throws Exception{
		
		final String expected = "test";
		final String actual = eventTask.getMessage();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testendTime() throws Exception{
		final Date expected = DateUtils.addDays(new Date(), 5);
		final Date actual = eventTask.getEndTime();
		assertEquals(expected, actual);
	}
	
	
	@Test
	public void testPriority() throws Exception{
		final PriorityType expected = PriorityType.NORMAL;
		final PriorityType actual = eventTask.getPriority();
		assertEquals(expected, actual);
	}
	
	/*@Test
	public void testStartTime() throws Exception{
		final Date expected = new Date();
		final Date actual = eventTask.getStartTime();
		assertEquals(expected, actual);
	}*/
	
	
	@Test
	public void eventTaskEquals_returnTrue() throws Exception{
		final Task other = new Task("test", new Date(), DateUtils.addDays(eventTask.getStartTime(), 5),
				PriorityType.NORMAL);
		assertTrue(eventTask.equals(other));
	}
	
	
	@Test
	public void eventTaskEquals_returnFalse() throws Exception{
		final Task other = new Task(" ", new Date(), PriorityType.NORMAL);
		assertFalse(eventTask.equals(other));
	}
	
	@Test
	public void toString_correctFormat(){
		final String expected = "test" + " from " + eventTask.getStartTimeString() + " to "
				+ eventTask.getEndTimeString();
		final String actual = eventTask.toString();
		assertEquals(expected, actual);
	}

}


