package Services;

import Model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a map file in Conquest format and parses it into the application's internal data structures.
 * Supports parsing of continent, country, and neighbor data.
 *
 * Author: Akhilesh Kanbarkar
 */
public class ConquestMapFileReader implements Serializable {

    /**
     * Default constructor.
     */
    public ConquestMapFileReader() {
    }
    /**
     * Parses a conquest-format map file and populates the map and current state objects.
     *
     * @param p_currentState the current game state to update
     * @param p_map the map object to populate
     * @param p_fileLines the lines read from the map file
     * @param p_fileName the name of the map file
     */
    public void readConquestFile(CurrentState p_currentState, Map p_map, List<String> p_fileLines, String p_fileName) {
        List<String> l_continentData = getMetaData(p_fileLines, "continent");
        List<Continent> l_continentList = parseContinentMetaData(l_continentData);

        List<String> l_countryData = getMetaData(p_fileLines, "country");
        List<Country> l_countryList = parseCountryMetaData(l_countryData, l_continentList);
        List<Country> l_updatedCountryList = parseNeighboursMetaData(l_countryList, l_countryData);

        l_continentList = linkCountryToContinent(l_continentList, l_updatedCountryList);
        p_map.setD_mapContinents(l_continentList);
        p_map.setD_mapCountries(l_updatedCountryList);
        p_map.setD_mapName(p_fileName);
        p_currentState.setD_map(p_map);
    }
}
