package main.model.task;



/**
 * A read-only immutable interface for a Person in the addressbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {
	int getTaskID();
    Message getMessage();
    Time getTime();
    boolean getIsFloating();
    

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getMessage().equals(this.getMessage()) // state checks here onwards
                && other.getTime().equals(this.getTime()));
                //&& other.getIsFloating().equals(this.getIsFloating()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTaskID())
        		.append(getMessage())
                .append(" Phone: ")
                .append(getTime());
                
                
        return builder.toString();
    }

    /**
     * Returns a string representation of this Person's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
