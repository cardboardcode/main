package main.model.task;

import main.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidMessage(String)}
 */
public class Message {

    public static final String MESSAGE_Message_CONSTRAINTS = "Messages should be spaces or alphanumeric characters";
    public static final String MESSAGE_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String fullMessage;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Message(String message) throws IllegalValueException {
        assert message != null;
        message = message.trim();
        if (!isValidMessage(message)) {
            throw new IllegalValueException(MESSAGE_Message_CONSTRAINTS);
        }
        this.fullMessage = message;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidMessage(String test) {
        return test.matches(MESSAGE_VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullMessage;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Message // instanceof handles nulls
                && this.fullMessage.equals(((Message) other).fullMessage)); // state check
    }

    @Override
    public int hashCode() {
        return fullMessage.hashCode();
    }

}
