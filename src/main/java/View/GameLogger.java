package View;

import Model.ModelLogger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * The GameLogger class listens for changes to the message in ModelLogger
 * and writes the updated message to a log file.
 * @author Akhilesh Kanbarkar
 */
public class GameLogger implements Observer {

    /**
     * The default file path for storing game log output.
     * Used by the logging system to write execution details, game events, and errors.
     */
    private static final String LOG_FILE_PATH = "src/main/resources/GameLogs.txt";

    /**
     * The D model logger.
     */
    ModelLogger d_modelLogger;

    /**
     * Instantiates a new Game logger.
     */
    public GameLogger() {
    }

    /**
     * Update.
     *
     * @param p_observable the p observable
     * @param p_arg        the p arg
     */
    @Override
    public void update(Observable p_observable, Object p_arg) {
        d_modelLogger = (ModelLogger) p_observable;
        File l_loggerFile = new File(LOG_FILE_PATH);
        String l_logMessage = d_modelLogger.getD_message();

        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(l_loggerFile, true));
            if(l_logMessage!=null) {
                writer.write(l_logMessage);
            }
            writer.newLine();
            writer.close();
        }
        catch (IOException p_e) {
            p_e.printStackTrace();
        }
    }
}