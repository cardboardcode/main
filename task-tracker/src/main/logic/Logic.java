package main.logic;

import main.logic.command.CommandResult;

public interface Logic {
    public CommandResult execute(String input);
}
