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
    /**
     * Links countries to their respective continents.
     *
     * @param p_continentList list of parsed continents
     * @param p_updatedCountryList list of countries with neighbors
     * @return updated list of continents with country references
     */
    private List<Continent> linkCountryToContinent(List<Continent> p_continentList, List<Country> p_updatedCountryList) {
        for (Country l_eachCountry : p_updatedCountryList) {
            for (Continent l_eachContinent : p_continentList) {
                if (l_eachContinent.getD_continentID().equals(l_eachCountry.getD_continentID())) {
                    l_eachContinent.addCountry(l_eachCountry);
                }
            }
        }
        return p_continentList;
    }
    /**
     * Parses and adds neighboring countries for each country.
     *
     * @param p_countryList the list of countries to update
     * @param p_countryData the raw country data from the file
     * @return list of countries with neighboring country IDs set
     */
    private List<Country> parseNeighboursMetaData(List<Country> p_countryList, List<String> p_countryData) {
        List<Country> l_updatedCountryList = new ArrayList<>(p_countryList);
        String l_matchedCountry = null;

        for (Country l_eachCountry : l_updatedCountryList) {
            for (String l_eachCountryData : p_countryData) {
                if ((l_eachCountryData.split(",")[0]).equalsIgnoreCase(l_eachCountry.getD_countryName())) {
                    l_matchedCountry = l_eachCountryData;
                    break;
                }
            }

            if (l_matchedCountry.split(",").length > 4) {
                for (int i = 4; i < l_matchedCountry.split(",").length; i++) {
                    Country l_country = this.getCountryByName(p_countryList, l_matchedCountry.split(",")[i]);
                    l_eachCountry.addCountryNeighbour(l_country.getD_countryID());
                }
            }
        }

        return l_updatedCountryList;
    }
    /**
     * Finds and returns a country object from the list by name.
     *
     * @param p_countryList list of all countries
     * @param p_countryName name to search
     * @return matched Country object, or null if not found
     */
    private Country getCountryByName(List<Country> p_countryList, String p_countryName) {
        for (Country l_eachCountry : p_countryList) {
            if (l_eachCountry.getD_countryName().equals(p_countryName)) {
                return l_eachCountry;
            }
        }
        return null;
    }
    /**
     * Parses the country section of the file into Country objects.
     *
     * @param p_countryData raw lines of country data
     * @param p_continentList parsed list of continents (used for mapping)
     * @return list of parsed Country objects
     */
    private List<Country> parseCountryMetaData(List<String> p_countryData, List<Continent> p_continentList) {
        List<Country> l_countryList = new ArrayList<>();
        int l_countryId = 1;

        for (String l_eachCountry : p_countryData) {
            String[] l_countryData = l_eachCountry.split(",");
            Continent l_continent = this.getContinentByName(p_continentList, l_countryData[3]);
            Country l_country = new Country(l_countryId, l_countryData[0], l_continent.getD_continentID());
            l_countryList.add(l_country);
            l_countryId++;
        }

        return l_countryList;
    }
}
