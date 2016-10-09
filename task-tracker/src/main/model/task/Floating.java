package main.model.task;

public class Floating extends Task {
	private boolean isFloating;
	
	public Floating(int taskID, Message message){
		super(taskID, message);
		this.isFloating = true;
	}
}
