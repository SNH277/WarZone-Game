package Services;

import Model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The {@code MapFileReader} class serves as a base class for reading map files.
 * It implements {@link Serializable} to allow its instances to be serialized.
 * This class can be extended or used as a utility for different map reading implementations.
 *
 * @author Yash Koladiya
 */
public class MapFileReader implements Serializable {
    public MapFileReader() {
    }

    /**
     * Parses the contents of a map file and updates the current game state and map object.
     * This method extracts continent, country, and border data from the file lines,
     * processes and links them accordingly, and updates the map and game state.
     *
     * @param p_currentState the current state of the game that will be updated with the parsed map
     * @param p_map          the map object to populate with parsed continents and countries
     * @param p_fileLines    the list of strings representing the lines of the map file
     * @param p_fileName     the name of the map file, used to set the map's name
     *
     */
    public void parseMapFile(CurrentState p_currentState, Map p_map, List<String> p_fileLines, String p_fileName) {
        List<String> l_continentData = getContinentData(p_fileLines);
        List<String> l_countryData = getCountryData(p_fileLines);
        List<String> l_borderData = getBorderData(p_fileLines);

        List<Continent> l_continents = modifyContinentData(l_continentData);
        List<Country> l_countries = modifyCountryData(l_countryData);

        l_countries = updateCountryBorders(l_borderData, l_countries);
        l_continents = updateContinentCountries(l_continents, l_countries);

        p_map.setD_mapContinents(l_continents);
        p_map.setD_mapCountries(l_countries);
        p_map.setD_mapName(p_fileName);

        p_currentState.setD_map(p_map);
    }

    /**
     * Extracts the country data section from the file lines.
     *
     * @param p_fileLines the list of all lines in the file
     * @return the list of country data strings
     */
    private List<String> getCountryData(List<String> p_fileLines) {
        int l_startIndex=p_fileLines.indexOf("[Countries]")+1;
        int l_endIndex=p_fileLines.indexOf("[Borders]")-1;

        return (l_startIndex>0 && l_endIndex>l_startIndex) ? p_fileLines.subList(l_startIndex,l_endIndex) : new ArrayList<>();
    }

    /**
     * Extracts the continent data section from the file lines.
     *
     * @param p_fileLines the list of all lines in the file
     * @return the list of continent data strings
     */
    private List<String> getContinentData(List<String> p_fileLines) {
        int l_startIndex=p_fileLines.indexOf("[Continents]")+1;
        int l_endIndex=p_fileLines.indexOf("[Countries]")-1;

        return (l_startIndex>0 && l_endIndex>l_startIndex) ? p_fileLines.subList(l_startIndex,l_endIndex) : new ArrayList<>();
    }

    /**
     * Extracts the border data section from the file lines.
     *
     * @param p_fileLines the list of all lines in the file
     * @return the list of border data strings
     */
    private List<String> getBorderData(List<String> p_fileLines) {
        int l_startIndex=p_fileLines.indexOf("[Borders]")+1;
        int l_endIndex=p_fileLines.size();

        return (l_startIndex>0 && l_endIndex>l_startIndex) ? p_fileLines.subList(l_startIndex,l_endIndex) : new ArrayList<>();
    }

    /**
     * Parses and modifies the continent data from file lines.
     *
     * @param p_continentData the list of strings representing continent data
     * @return the list of Continent objects created from the data
     */
    private List<Continent> modifyContinentData(List<String> p_continentData) {
        List<Continent> l_continents =new ArrayList<>();
        int l_continentIds=1;

        for(String l_continent:p_continentData){
            String[] l_splitContinentData=l_continent.split(" ");
            if(l_splitContinentData.length <2)
                continue;

            String l_continentName=l_splitContinentData[0];
            int l_continentValue;
            try{
                l_continentValue=Integer.parseInt(l_splitContinentData[1]);
            }
            catch (NumberFormatException l_e){
                continue;
            }

            l_continents.add(new Continent(l_continentIds,l_continentName,l_continentValue));
            l_continentIds++;
        }
        return l_continents;
    }

    /**
     * Parses and modifies the country data from file lines.
     *
     * @param p_countryData the list of strings representing country data
     * @return the list of Country objects created from the data
     */
    private List<Country> modifyCountryData(List<String> p_countryData) {
        List<Country> l_countries =new ArrayList<>();

        for(String l_country:p_countryData){
            String[] l_splitCountryData= l_country.split(" ");
            if(l_splitCountryData.length < 3)
                continue;

            String l_countryName = l_splitCountryData[1];
            int l_countryId,l_continentId;

            try{
                l_countryId =Integer.parseInt(l_splitCountryData[0]);
                l_continentId=Integer.parseInt(l_splitCountryData[2]);
            } catch (NumberFormatException l_e) {
                continue;
            }
            l_countries.add(new Country(l_countryId,l_countryName,l_continentId));
        }
        return l_countries;
    }

    /**
     * Updates the continents by adding the corresponding countries to each continent.
     *
     * @param p_continents the list of continents to update
     * @param p_countries  the list of countries to be assigned to continents
     * @return the updated list of continents with countries added
     */
    private List<Continent> updateContinentCountries(List<Continent> p_continents, List<Country> p_countries) {
        for(Country l_country : p_countries){
            for(Continent l_continent:p_continents){
                if(l_country.getD_continentID().equals(l_continent.getD_continentID())){
                    l_continent.setCountry(l_country);
                }
            }
        }
        return p_continents;
    }

    /**
     * Updates the border information for each country based on the provided border data.
     *
     * @param p_borderData the list of border data strings
     * @param p_countries  the list of countries to update with neighbouring country IDs
     * @return the updated list of countries with border information set
     */
    private List<Country> updateCountryBorders(List<String> p_borderData, List<Country> p_countries) {
        LinkedHashMap<Integer,List<Integer>> l_borderDataMap =new LinkedHashMap<>();

        for (String l_eachCountryNeighbour : p_borderData) {
            if (l_eachCountryNeighbour == null || l_eachCountryNeighbour.isEmpty())
                continue;

            String[] l_borderDataSplit = l_eachCountryNeighbour.split(" ");
            if (l_borderDataSplit.length < 2)
                continue;

            int l_countryId;
            List<Integer> l_neighbourCountries = new ArrayList<>();

            try {
                l_countryId = Integer.parseInt(l_borderDataSplit[0]);
                for (int i = 1; i < l_borderDataSplit.length; i++) {
                    l_neighbourCountries.add(Integer.parseInt(l_borderDataSplit[i]));
                }
            } catch (NumberFormatException e) {
                continue;
            }

            l_borderDataMap.put(l_countryId, l_neighbourCountries);
        }

        for (Country l_eachCountry : p_countries) {
            l_eachCountry.setD_neighbouringCountriesId(l_borderDataMap.getOrDefault(l_eachCountry.getD_countryID(), new ArrayList<>()));
        }

        return p_countries;
    }
}
