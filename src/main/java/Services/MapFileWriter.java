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

    /**
     * Writes the country and border metadata sections to the map file.
     *
     * <p>This method serializes all countries in the current map, including each country's:
     * <ul>
     *   <li>Unique country ID</li>
     *   <li>Name</li>
     *   <li>Associated continent ID</li>
     * </ul>
     * </p>
     *
     * <p>After listing the countries, it writes the borders between countries under the [Borders] section.
     * Each line under [Borders] starts with the country ID followed by the IDs of its neighboring countries.</p>
     *
     * <p>Example format:</p>
     * <pre>
     * [Countries]
     * 1 Canada 2
     * 2 USA 2
     * 3 Mexico 2
     *
     * [Borders]
     * 1 2
     * 2 1 3
     * 3 2
     * </pre>
     *
     * @param p_currentState the current game state containing the map data
     * @param p_writer the FileWriter object used to write to the map file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    private void writeCountryAndBorderMetaData(CurrentState p_currentState, FileWriter p_writer) throws IOException {
        String l_countryMetadata = "";
        String l_borderMetadata = "";
        List<String> l_borderList = new ArrayList<>();

        p_writer.write(System.lineSeparator() + "[Countries]" + System.lineSeparator());
        for (Country l_eachCountry : p_currentState.getD_map().getD_mapCountries()) {
            l_countryMetadata = "";
            l_countryMetadata = l_eachCountry.getD_countryID().toString() + " " + l_eachCountry.getD_countryName() + " " + l_eachCountry.getD_continentID().toString();
            p_writer.write(l_countryMetadata + System.lineSeparator());

            if (l_eachCountry.getD_neighbouringCountriesId() != null && !l_eachCountry.getD_neighbouringCountriesId().isEmpty()) {
                l_borderMetadata = "";
                l_borderMetadata = l_eachCountry.getD_countryID().toString();
                for (Integer l_eachBorder : l_eachCountry.getD_neighbouringCountriesId()) {
                    l_borderMetadata = l_borderMetadata + " " + l_eachBorder.toString();
                }
                l_borderList.add(l_borderMetadata);
            }
        }
        if (l_borderList != null && !l_borderList.isEmpty()) {
            p_writer.write(System.lineSeparator() + "[Borders]" + System.lineSeparator());
            for (String l_eachBorder : l_borderList) {
                p_writer.write(l_eachBorder + System.lineSeparator());
            }
        }
    }

    /**
     * Writes the continent metadata section to the map file.
     *
     * <p>This method serializes all continents from the current map into the [Continents] section of the map file.</p>
     *
     * <p>Each line contains the continent's name followed by its control value, which determines
     * the bonus armies a player receives when controlling the entire continent.</p>
     *
     * <p>Example format:</p>
     * <pre>
     * [Continents]
     * NorthAmerica 5
     * Asia 7
     * Europe 3
     * </pre>
     *
     * @param p_currentState the current game state containing the map data
     * @param p_writer the FileWriter object used to write to the map file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    private void writeContinentMetaData(CurrentState p_currentState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + "[Continents]" + System.lineSeparator());
        for (Continent l_eachContinent : p_currentState.getD_map().getD_mapContinents()) {
            p_writer.write(l_eachContinent.getD_continentName() + " " + l_eachContinent.getD_continentValue().toString() + System.lineSeparator());
        }
    }
}
