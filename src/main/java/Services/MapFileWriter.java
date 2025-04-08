package Services;

import Constants.ProjectConstants;
import Model.*;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code MapFileWriter} class is responsible for serializing the current map state
 * into a file format. It writes continent and country data along with their relationships
 * (e.g., borders) into a valid map file format for use in the game.
 *
 * This class implements {@link Serializable} for potential future use cases involving serialization.
 * It uses helper methods to structure and write the data in appropriate sections.
 *
 * Typical usage:
 * <pre>{@code
 *     MapFileWriter writer = new MapFileWriter();
 *     writer.parseMapToFile(currentState, fileWriter, "Domination");
 * }</pre>
 *
 * @author Yash Koladiya
 */
public class MapFileWriter implements Serializable {

    /**
     * Default constructor for MapFileWriter.
     */
    public MapFileWriter() {
    }

    /**
     * Writes the current map state from {@link CurrentState} into the specified file using {@link FileWriter}.
     * The output includes metadata for continents, countries, and their borders.
     *
     * @param p_currentState the current state of the game containing the map data
     * @param p_writer the file writer used to write the map data into a file
     * @param p_mapFormat the format of the map (e.g., "Domination" or other formats if extended)
     * @throws IOException if an I/O error occurs during writing to the file
     */
    public void parseMapToFile(CurrentState p_currentState, FileWriter p_writer, String p_mapFormat) throws IOException {
        if(p_currentState.getD_map().getD_mapContinents() != null && !p_currentState.getD_map().getD_mapContinents().isEmpty()){
            writeContinentMetaData(p_currentState, p_writer);
        }
        if(p_currentState.getD_map().getD_mapCountries() != null && !p_currentState.getD_map().getD_mapCountries().isEmpty()){
            writeCountryAndBorderMetaData(p_currentState, p_writer);
        }
    }

}
