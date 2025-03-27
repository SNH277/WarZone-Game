package Model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
/**
 * The type Model logger.
 * @author Akhilesh Kanbarkar
 */
public class ModelLogger {
    /**
     * The message being logged.
     */
    private String d_message;

    /**
     * Property change support for observers.
     */
    private final PropertyChangeSupport d_support;

    /**
     * Instantiates a new Model logger.
     */
    public ModelLogger() {
        d_support = new PropertyChangeSupport(this);
    }

    /**
     * Adds an observer to the logger.
     *
     * @param p_listener the listener (observer)
     */
    public void addObserver(PropertyChangeListener p_listener) {
        d_support.addPropertyChangeListener(p_listener);
    }

    /**
     * Gets the latest message.
     *
     * @return the logged message
     */
    public String getD_message() {
        return d_message;
    }

    /**
     * Sets a log message and notifies observers.
     *
     * @param p_message     the log message
     * @param p_messageType the message type
     */
    public void setD_message(String p_message, String p_messageType) {
        if (p_message == null || p_messageType == null) {
            return; // Avoid null inputs
        }

        StringBuilder l_logBuilder = new StringBuilder();

        switch (p_messageType) {
            case "command":
                l_logBuilder.append(System.lineSeparator())
                        .append("Command Entered: ").append(p_message)
                        .append(System.lineSeparator());
                break;
            case "order":
                l_logBuilder.append(System.lineSeparator())
                        .append("Order Issued: ").append(p_message)
                        .append(System.lineSeparator());
                break;
            case "phase":
                l_logBuilder.append(System.lineSeparator())
                        .append("=========").append(p_message).append("=========")
                        .append(System.lineSeparator()).append(System.lineSeparator());
                break;
            case "effect":
                l_logBuilder.append("Log: ").append(p_message)
                        .append(System.lineSeparator());
                break;
            case "start":
            case "end":
                l_logBuilder.append(p_message).append(System.lineSeparator());
                break;
            default:
                l_logBuilder.append("Unknown Log Type: ").append(p_messageType)
                        .append(" - ").append(p_message)
                        .append(System.lineSeparator());
                break;
        }

        String l_oldMessage = d_message;
        d_message = l_logBuilder.toString();
        d_support.firePropertyChange("message", l_oldMessage, d_message);
    }
}
