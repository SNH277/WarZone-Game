package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Map test.
 */
public class MapTest {
    private CurrentState d_currentState;
    private MapController d_mapController;
    private String d_mapName;
    private Map d_map;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);

        // Ensure map is loaded successfully
        assertNotNull("Map should be loaded successfully", d_map);
    }

    /**
     * Gets country by name.
     */
    @Test
    public void getCountryByName() {
        Country l_morocco = d_map.getCountryByName("Morocco");
        Country l_india = d_map.getCountryByName("India");

        assertNotNull("Morocco should exist in the map", l_morocco);
        assertNotNull("India should exist in the map", l_india);

        assertEquals(3, (int) l_morocco.getD_countryID());
        assertNotEquals(2, (int) l_india.getD_countryID());
    }

    /**
     * Gets continent by name.
     */
    @Test
    public void getContinentByName() {
        Continent l_africa = d_map.getContinentByName("Africa");
        Continent l_asia = d_map.getContinentByName("Asia");

        assertNotNull("Africa should exist", l_africa);
        assertNotNull("Asia should exist", l_asia);

        assertEquals(10, (int) l_africa.getD_continentValue());
        assertNotEquals(10, (int) l_asia.getD_continentValue());
    }

    /**
     * Validate map.
     */
    @Test
    public void validateMap() {
        d_map = d_mapController.loadMap(d_currentState, "testconquest.map");
        assertNotNull("Test conquest map should load successfully", d_map);
        assertTrue("Valid map should pass validation", d_map.validateMap());

        d_map = d_mapController.loadMap(d_currentState, "testInvalid.map");
        assertNotNull("Test invalid map should load successfully", d_map);
        assertFalse("Invalid map should fail validation", d_map.validateMap());
    }

    /**
     * Validate countries and continents.
     */
    @Test
    public void validateCountriesAndContinents() {
        assertTrue("Countries and continents should be valid", d_map.validateCountriesAndContinents());

        d_map.setD_mapContinents(new ArrayList<>()); // Remove all continents
        assertFalse("Validation should fail when no continents exist", d_map.validateCountriesAndContinents());
    }

    /**
     * Add continent.
     */
    @Test
    public void addContinent() {
        d_map.addContinent("Europe", 35);
        Continent l_continent = d_map.getContinentByName("Europe");

        assertNotNull("Europe should exist in the map", l_continent);
        assertEquals("Europe", l_continent.getD_continentName());
    }

    /**
     * Remove continent.
     */
    @Test
    public void removeContinent() {
        Continent l_asia = d_map.getContinentByName("Asia");
        assertNotNull("Asia should exist before removal", l_asia);

        d_map.removeContinent("Asia");

        assertNull("Asia should be removed", d_map.getContinentByName("Asia"));
    }

    /**
     * Add country.
     */
    @Test
    public void addCountry() {
        d_map.addContinent("Asia", 10);
        d_map.addCountry("Sri Lanka", "Asia");

        assertNotNull("Sri Lanka should exist", d_map.getCountryByName("Sri Lanka"));
    }

    /**
     * Add neighbour.
     */
    @Test
    public void addNeighbour() {
        d_map.addNeighbour(1, 3);

        List<Integer> l_neighboursIndia = d_map.getCountryByName("India").getD_neighbouringCountriesId();
        List<Integer> l_neighboursMorocco = d_map.getCountryByName("Morocco").getD_neighbouringCountriesId();

        assertTrue("India should have Morocco as a neighbor", l_neighboursIndia.contains(3));
        assertTrue("Morocco should have India as a neighbor", l_neighboursMorocco.contains(1));
    }

    /**
     * Remove neighbour.
     */
    @Test
    public void removeNeighbour() {
        d_map.removeNeighbour(1, 4);

        List<Integer> l_neighboursIndia = d_map.getCountryByName("India").getD_neighbouringCountriesId();
        List<Integer> l_neighboursNigeria = d_map.getCountryByName("Nigeria").getD_neighbouringCountriesId();

        assertFalse("India should no longer have Nigeria as a neighbor", l_neighboursIndia.contains(4));
        assertFalse("Nigeria should no longer have India as a neighbor", l_neighboursNigeria.contains(1));
    }

    /**
     * Remove country.
     */
    @Test
    public void removeCountry() {
        assertNotNull("India should exist before removal", d_map.getCountryByName("India"));
        d_map.removeCountry("India");
        assertNull("India should be removed", d_map.getCountryByName("India"));
    }

    /**
     * Validate continent subgraph connectivity.
     */
    @Test
    public void validateContinentSubgraphConnectivity() {
        assertTrue("Continent subgraph should be connected", d_map.validateContinentSubgraph());

        d_map.removeNeighbour(1, 2);
        assertFalse("Subgraph should be disconnected after removing a connection", d_map.validateContinentSubgraph());
    }

    /**
     * Validate map is a connected graph of countries.
     */
    @Test
    public void validateMapIsAConnectedGraphOfCountries() {
        assertTrue("Map should be a connected graph", d_map.validateCountryConnections());

        d_map.removeNeighbour(3, 2);
        d_map.removeNeighbour(3, 4);
        assertFalse("Map should be disconnected after removing connections", d_map.validateCountryConnections());
    }
}
