package Exceptions;

/**
 * Custom exception class to handle command validation errors.
 * <p>
 * This exception is thrown when a command fails validation due to incorrect format,
 * invalid parameters, or other issues.
 * </p>
 *
 * @author Yash Koladiya
 */
public class CommandValidationException extends Exception  {

    /**
     * Constructs a new CommandValidationException with the specified detail message.
     *
     * @param message the detail message that provides information about the exception cause.
     */
    public  CommandValidationException(String message) {
        super(message);
    }
}
