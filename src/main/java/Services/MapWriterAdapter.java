package Services;

import Model.*;

import java.io.FileWriter;
import java.io.IOException;

/**
 * The {@code MapWriterAdapter} class acts as an adapter to allow the use of
 * {@link ConquestMapFileWriter} for writing map files in a given format.
 * This follows the Adapter Design Pattern to ensure compatibility between
 * different map writer implementations.
 *
 * @author Yash Koladiya
 */
public class MapWriterAdapter {

    /**
     * An instance of {@link ConquestMapFileWriter} used to write the map data.
     */
    private ConquestMapFileWriter d_conquestMapFileWriter;

    /**
     * Constructs a new {@code MapWriterAdapter} with the specified conquest map file writer.
     *
     * @param p_conquestMapFileWriter the conquest map file writer to be adapted
     */
    public MapWriterAdapter(ConquestMapFileWriter p_conquestMapFileWriter) {
        d_conquestMapFileWriter = p_conquestMapFileWriter;
    }

    /**
     * Parses the current map state and writes it to a file using the specified format.
     *
     * @param p_currentState the current state of the game containing map data
     * @param p_writer       the {@link FileWriter} used to write the output file
     * @param p_mapFormat    the format in which the map should be written (e.g., "conquest", "domination")
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public void parseMapToFile(CurrentState p_currentState, FileWriter p_writer, String p_mapFormat) throws IOException {
        d_conquestMapFileWriter.parseMapToFile(p_currentState, p_writer, p_mapFormat);
    }
}