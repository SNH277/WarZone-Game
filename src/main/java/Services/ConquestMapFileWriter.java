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
    /**
     * Parses the current game state and writes map data to a file in the specified map format.
     *
     * @param p_currentState The current game state containing map data.
     * @param p_writer The FileWriter object used to write to the file.
     * @param p_mapFormat The format of the map (currently unused, reserved for future use).
     * @throws IOException if an I/O error occurs.
     */
    public void parseMapToFile(CurrentState p_currentState, FileWriter p_writer, String p_mapFormat) throws IOException {
        if (p_currentState.getD_map().getD_mapContinents() != null && !p_currentState.getD_map().getD_mapContinents().isEmpty()) {
            writeContinentMetaData(p_currentState, p_writer);
        }
        if (p_currentState.getD_map().getD_mapCountries() != null && !p_currentState.getD_map().getD_mapCountries().isEmpty()) {
            writeCountryAndBorderMetaData(p_currentState, p_writer);
        }
    }
    /**
     * Writes continent metadata to the file under the [Continents] section.
     *
     * @param p_currentState The current state containing continent data.
     * @param p_writer The FileWriter used to write to the map file.
     * @throws IOException if an I/O error occurs.
     */
    private void writeContinentMetaData(CurrentState p_currentState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + "[Continents]" + System.lineSeparator());
        for (Continent l_eachContinent : p_currentState.getD_map().getD_mapContinents()) {
            p_writer.write(l_eachContinent.getD_continentName() + "=" + l_eachContinent.getD_continentValue().toString() + System.lineSeparator());
        }
    }
}