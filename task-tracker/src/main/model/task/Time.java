package main.model.task;

import main.commons.exceptions.IllegalValueException;
import java.util.Date;


public class Time {
	private Date startDate;
	private Date dueDate;
	
	public Time(){
		this.startDate = new Date();
		this.dueDate = new Date();	
		
	}
   
}
