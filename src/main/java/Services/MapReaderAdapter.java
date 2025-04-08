package Services;

import Model.*;
import java.util.List;

/**
 * The {@code MapReaderAdapter} class acts as an adapter for reading map files
 * using the {@link ConquestMapFileReader}. It facilitates reading maps
 * in the Conquest format and updating the current game state accordingly.
 * This follows the Adapter Design Pattern for format compatibility.
 *
 * @author Yash Koladiya
 */
public class MapReaderAdapter {

    /**
     * An instance of {@link ConquestMapFileReader} used to read conquest map files.
     */
    private ConquestMapFileReader d_conquestMapFileReader;

    /**
     * Constructs a new {@code MapReaderAdapter} with the specified conquest map file reader.
     *
     * @param p_conquestMapFileReader the conquest map file reader to be adapted
     */
    public MapReaderAdapter(ConquestMapFileReader p_conquestMapFileReader) {
        this.d_conquestMapFileReader = p_conquestMapFileReader;
    }

    /**
     * Parses the map file content and updates the given map and game state.
     *
     * @param p_currentState the current game state to be updated
     * @param p_map          the map object to populate with parsed data
     * @param p_fileLines    the content of the map file as a list of strings
     * @param p_fileName     the name of the map file being read
     */
    public void parseMapFile(CurrentState p_currentState, Map p_map, List<String> p_fileLines, String p_fileName) {
        d_conquestMapFileReader.readConquestFile(p_currentState, p_map, p_fileLines, p_fileName);
    }
}