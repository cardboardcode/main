package main.logic.command;


/**
 * Edits an existing task in task-tracker
 * @author 
 *
 */
public class EditCommand extends Command {
public static final String COMMAND_WORD = "edit";
	
	public static final String MESSAGE_USAGE = COMMAND_WORD + ":/n" + "Edits a task in the Task-Tracker."
												+ "Parameters: [task] [date] [time]\n\t"
												+ "Example: " + COMMAND_WORD
												+ " 2 tk/wash dishes t/0900 d/081016";
	
	public static final String MESSAGE_SUCCESS = "Task edited: %1$s";
	public static final String MESSAGE_NO_SUCH_TASK = "This task does not exist in the Task-Tracker";
	
	private Task toEdit;
	private final int editNum;
	private String message;
	private int time;
	private int date;
	
	public EditCommand(int targetVisibleIndex, String message, int time, int date) throws IllegalValueException{
		super(targetVisibleIndex);
		editNum = targetVisibleIndex;
		
		this.message = message;
		this.time = time;
		this.date = date;
	}
	
	@Override
	public CommandResult execute() {
		try {
			toEdit = new Task(
					new Message(),
					new Date(email),
					new Time(address)
					);
			DeleteCommand deleted = new DeleteCommand(editNum);
			deleted.execute();

			AddCommand added = new AddCommand(toEdit);
			added.execute();
			return new CommandResult(String.format(MESSAGE_SUCCESS, toEdit));
		}catch (IndexOutOfBoundsException ie) {
			return new CommandResult("The task index provided is invalid");
		}catch (TaskNotFoundException e) {
			return new CommandResult("Task does not exist in task-tracker");
		}catch (IllegalValueException e) {
			return new CommandResult(e.getMessage());
		}
	}
	}
	
}
