package Model;
import View.GameLogger;

import java.io.Serializable;
import java.util.Observable;

/**
 * The type Model logger.
 * @author Akhilesh Kanbarkar
 */
public class ModelLogger extends Observable implements Serializable {
    /**
     * The message being logged.
     */
    private String d_message;


    /**
     * Instantiates a new Model logger.
     */
    public ModelLogger() {
        GameLogger l_logger = new GameLogger();
        this.addObserver(l_logger);
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
        if ("command".equals(p_messageType)) {
            d_message = System.lineSeparator() + "Command Entered: " + p_message + System.lineSeparator();
        } else if ("order".equals(p_messageType)) {
            d_message = System.lineSeparator() + "Order Issued: " + p_message + System.lineSeparator();
        } else if ("phase".equals(p_messageType)) {
            d_message = System.lineSeparator() + "=========" + p_message + "=========" +  System.lineSeparator() + System.lineSeparator();
        } else if ("effect".equals(p_messageType)) {
            d_message = "Log: " + p_message + System.lineSeparator();
        } else if ("start".equals(p_messageType) || ("end".equals(p_messageType))) {
            d_message = p_message + System.lineSeparator();
        }
        setChanged();
        notifyObservers();
    }
}