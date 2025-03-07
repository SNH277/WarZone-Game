package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The type Continent test.
 */
public class ContinentTest {
    private CurrentState d_currentState;
    private MapController d_mapController;
    private String d_mapName;
    private Map d_map;

    /**
     * Sets up test environment.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);

        // Ensure the map is loaded correctly
        assertNotNull("Map should be loaded successfully", d_map);

        // Ensure "Asia" continent exists before adding countries
        if (d_map.getContinentByName("Asia") == null) {
            d_map.addContinent("Asia", 5); // Assuming continent bonus is 5
        }
    }

    /**
     * Tests adding a country to the map.
     */
    @Test
    public void addCountry() {
        d_map.addCountry("Pakistan", "Asia");
        d_map.addCountry("Nepal", "Asia");

        assertNotNull("Pakistan should exist in the map", d_map.getCountryByName("Pakistan"));
        assertNotNull("Nepal should exist in the map", d_map.getCountryByName("Nepal"));

        assertEquals("Pakistan", d_map.getCountryByName("Pakistan").getD_countryName());
        assertEquals("Nepal", d_map.getCountryByName("Nepal").getD_countryName());
    }

    /**
     * Tests removing a country from the map.
     */
    @Test
    public void removeCountry() {
        d_map.addCountry("Pakistan", "Asia");
        d_map.addCountry("Nepal", "Asia");

        d_map.removeCountry("Pakistan");
        assertNull("Pakistan should be removed from the map", d_map.getCountryByName("Pakistan"));

        d_map.removeCountry("Nepal");
        assertNull("Nepal should be removed from the map", d_map.getCountryByName("Nepal"));
    }
}
