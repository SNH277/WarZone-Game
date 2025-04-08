package Services;

import Model.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

/**
 * This class is responsible for writing a game map in Conquest format to a file.
 * It handles the serialization of continent and territory metadata from the current state of the game.
 *
 * @author Akhilesh Kanbarkar
 */
public class ConquestMapFileWriter implements Serializable {

    /**
     * Default constructor.
     */
    public ConquestMapFileWriter() {
    }

}