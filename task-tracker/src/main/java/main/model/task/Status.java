package main.model.task;

public class Status {
	public static enum State{
		DONE, UNDONE
	}
	private static final String MESSAGE_TASK_DONE = "DONE";
	private static final String MESSAGE_TASK_UNDONE = "UNDONE";
	private State status;
	
	public Status(State status){
		assert status !=null;
		this.status = status;
	}
	
	public Status(String statusString){
		assert statusString != null;
		this.status = getStatusString(statusString);
	}
	
	
	
	private State getStatusString(String statusString){
		
		if(statusString.equals(MESSAGE_TASK_DONE)){
			return State.DONE;
		}
		else{
			return State.UNDONE;
		}
	}
	
	@Override
	public boolean equals(Object other){
		return other == this || //same object return true
				 (other instanceof Status// null
				&& this.status.equals(((Status) other).status));
	}
	@Override
	public String toString(){
		
		if(status == State.DONE){
			return "Done";
		}
		else{
			return "Undone";
		}
	}
	
}
