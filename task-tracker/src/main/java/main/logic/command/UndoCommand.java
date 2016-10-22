package main.logic.command;

/**
 * Stub command for now. Should take in the last known input. Last known input
 * should be a part of a list called History. Undo command reads in last input
 * and dose its own parsing. Whenever an UndoCommand is executed, the elements
 * in the list should be reduced like a stack.
 * 
 * @author bey
 *
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the last known command input.\n" + "eg. "
            + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Reverted last command: ";
    public static final String MESSAGE_EMPTY_HISTORY = "There are no more inputs before this.";

    public static final int ADD = 1;
    public static final int DEL = 2;
    public static final int EDIT = 3;
    public static final int DONE = 4;

    public UndoCommand() {

    }

    @Override
    public CommandResult execute() {
        return null;
//        if (lastInput != null)
//            // Process
//            /**
//             * Add - Delete, Delete - Add, Edit - Delete & Add, Clear - AddAll
//             * 
//             */
//            return new CommandResult(MESSAGE_SUCCESS);
//        else
//            return new CommandResult("never gonna give you up, never gonna let you down,"
//                    + "never gonna run around and desert you. Never gonna make you "
//                    + "cry, never gonna say goodbye. Never gonna tell a lie and hurt you.");
    }

}
