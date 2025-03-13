package Models;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import Model.Continent;
import Model.CurrentState;
import Model.Map;
import org.junit.Before;
import org.junit.Test;

import Controller.MapController;

/**
 * The type Map test.
 */
public class MapTest {
    /**
     * The D current state.
     */
    CurrentState d_currentState;
    /**
     * The D map controller.
     */
    MapController d_mapController;
    /**
     * The D map name.
     */
    String d_mapName;
    /**
     * The D map.
     */
    Map d_map;

    /**
     * Setup.
     */
    @Before
    public void setup(){
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
    }

    /**
     * Gets country by name.
     */
    @Test
    public void getCountryByName() {
        assertEquals(1, (int) d_map.getCountryByName("USA").getD_countryID());
        assertNotEquals(2, (int) d_map.getCountryByName("UK").getD_countryID());
    }

    /**
     * Gets continent by name.
     */
    @Test
    public void getContinentByName() {
        assertEquals(15, (int) d_map.getContinentByName("Europe").getD_continentValue());
        assertNotEquals(15, (int) d_map.getContinentByName("SouthAmerica").getD_continentValue());
    }

    /**
     * Validate map.
     */
    @Test
    public void validateMap() {
        assertTrue(d_mapController.loadMap(d_currentState, "canada.map").validateMap());
        assertFalse(d_mapController.loadMap(d_currentState, "testInvalid.map").validateMap());
    }

    /**
     * Validate countries and continents.
     */
    @Test
    public void validateCountriesAndContinents() {
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        assertTrue(d_map.validateCountriesAndContinents());

        List<Continent> l_continents = new ArrayList<>();
        d_map.setD_mapContinents(l_continents);
        assertFalse(d_map.validateCountriesAndContinents());
    }

    /**
     * Add continent.
     */
    @Test
    public void addContinent() {
        d_map = new Map();
        d_map.addContinent("Europe", 35);
        assertEquals("Europe", d_map.getD_mapContinents().getFirst().getD_continentName());
    }

    /**
     * Remove continent.
     */
    @Test
    public void removeContinent() {
        assertEquals("NorthAmerica", d_map.getD_mapContinents().getFirst().getD_continentName());
        d_map.removeContinent("NorthAmerica");
        assertEquals("Europe", d_map.getD_mapContinents().getFirst().getD_continentName());
    }

    /**
     * Add country.
     */
    @Test
    public void addCountry() {
        d_map = new Map();
        d_map.addContinent("NorthAmerica", 15);
        d_map.addCountry("Cuba", "NorthAmerica");
        assertEquals("Cuba", d_map.getD_mapCountries().getFirst().getD_countryName());
    }

    /**
     * Add neighbour.
     */
    @Test
    public void addNeighbour() {
        d_map.addNeighbour(1, 2);
        d_map.addNeighbour(1, 4);

        List<Integer> l_neighbourCountryIdListUSA = d_map.getCountryByName("USA").getD_neighbouringCountriesId();
        List<Integer> l_neighbourCountryIdListUK = d_map.getCountryByName("UK").getD_neighbouringCountriesId();

        assertEquals("[2, 4, 6]", l_neighbourCountryIdListUSA.toString());
        assertEquals("[2, 4]", l_neighbourCountryIdListUK.toString());
    }

    /**
     * Remove neighbour.
     */
    @Test
    public void removeNeighbour() {
        d_map.removeNeighbour(1, 2);
        d_map.removeNeighbour(1, 4);

        List<Integer> l_neighbourCountryIdListUSA = d_map.getCountryByName("USA").getD_neighbouringCountriesId();
        List<Integer> l_neighbourCountryIdListNigeria = d_map.getCountryByName("UK").getD_neighbouringCountriesId();

        assertEquals("[6]", l_neighbourCountryIdListUSA.toString());
        assertEquals("[2, 4]", l_neighbourCountryIdListNigeria.toString());
    }

    /**
     * Remove country.
     */
    @Test
    public void removeCountry() {
        assertEquals("USA", d_map.getD_mapCountries().getFirst().getD_countryName());
        d_map.removeCountry("USA");
        assertEquals("Canada", d_map.getD_mapCountries().getFirst().getD_countryName());
    }

    /**
     * Validate continent subgraph connectivity.
     */
    @Test
    public void validateContinentSubgraphConnectivity() {
        assertTrue(d_map.validateContinentSubgraph());
        d_map.removeNeighbour(1, 2);
        assertFalse(d_map.validateContinentSubgraph());
    }

    /**
     * Validate map is a connected graph of countries.
     */
    @Test
    public void validateMapIsAConnectedGraphOfCountries() {
        assertTrue(d_map.validateCountryConnections());
        d_map.removeNeighbour(3, 2);
        d_map.removeNeighbour(3, 4);
        assertFalse(d_map.validateCountryConnections());
    }
}