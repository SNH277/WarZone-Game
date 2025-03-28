package View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The GameLogger class listens for changes to the message in ModelLogger
 * and writes the updated message to a log file.
 * @author Akhilesh Kanbarkar
 */
public class GameLogger implements PropertyChangeListener {

    /**
     * The default file path for storing game log output.
     * Used by the logging system to write execution details, game events, and errors.
     */
    private static final String LOG_FILE_PATH = "GameLogs.txt";

    /**
     * This method is called when a property change occurs in the observed object (ModelLogger).
     * It writes the new log message to the log file if the message is valid (non-null and non-empty).
     *
     * @param event the PropertyChangeEvent containing details of the property change
     */
    @Override
    public void propertyChange(PropertyChangeEvent event) {
        // Check if the property name is "d_message", which signifies a log message change
        if ("d_message".equals(event.getPropertyName())) {
            String logMessage = (String) event.getNewValue();

            // If the message is valid (non-null and non-empty), write it to the log file
            if (logMessage != null && !logMessage.trim().isEmpty()) {
                File logFile = new File(LOG_FILE_PATH);

                // Try-with-resources to ensure proper resource management of the BufferedWriter
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                    writer.write(logMessage);
                    writer.newLine(); // Write each log message on a new line
                } catch (IOException e) {
                    // Handle file I/O errors
                    System.err.println("Error writing to log file: " + e.getMessage());
                }
            } else {
                // Log a warning if the message is empty or null
                System.err.println("Warning: Received an empty or null log message.");
            }
        }
    }
}
