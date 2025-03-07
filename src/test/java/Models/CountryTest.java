package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Country test.
 */
public class CountryTest {
    private Map d_map;
    private CurrentState d_currentState;
    private MapController d_mapController;
    private String d_mapName;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);

        // Ensure the map is loaded successfully
        assertNotNull("Map should be loaded successfully", d_map);

        // Ensure "India" exists in the map
        if (d_map.getCountryByName("India") == null) {
            Country l_india = new Country(1, "India", 1);
            l_india.addCountryNeighbour(2);
            l_india.addCountryNeighbour(4);
            d_map.addCountry("India", "Asia");  // Assuming Asia exists
            d_map.getCountryByName("India").setD_neighbouringCountriesId(List.of(2, 4));
        }
    }

    /**
     * Test removing a neighboring country.
     */
    @Test
    public void removeCountryNeighbourIfPresent() {
        Country l_country = d_map.getCountryByName("India");
        assertNotNull("India should exist in the map", l_country);

        // Ensure initial neighbors
        assertTrue("India should initially have neighbor 2", l_country.getD_neighbouringCountriesId().contains(2));
        assertTrue("India should initially have neighbor 4", l_country.getD_neighbouringCountriesId().contains(4));

        // Remove neighbor and test again
        l_country.removeCountryNeighbourIfPresent(2);
        assertFalse("India should no longer have neighbor 2", l_country.getD_neighbouringCountriesId().contains(2));
        assertTrue("India should still have neighbor 4", l_country.getD_neighbouringCountriesId().contains(4));
    }
}
