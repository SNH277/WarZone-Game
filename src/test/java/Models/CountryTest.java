package Models;

import Controller.MapController;
import Model.Country;
import Model.CurrentState;
import Model.Map;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Country test.
 */
public class CountryTest {

    /**
     * The D map.
     */
    Map d_map;
    /**
     * The D map name.
     */
    String d_mapName;
    /**
     * The D current state.
     */
    CurrentState d_currentState;
    /**
     * The D map controller.
     */
    MapController d_mapController;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);

        // Add continents
        d_map.addContinent("NorthAmerica", 15);
        d_map.addContinent("Europe", 10);

        // Add countries and assign continents
        d_map.addCountry("USA", "NorthAmerica");
        d_map.addCountry("Canada", "NorthAmerica");
        d_map.addCountry("UK", "Europe");
        d_map.addCountry("France", "Europe");

    }

    /**
     * Remove country neighbour if present.
     */
    @Test
    public void removeCountryNeighbourIfPresent() {
        // Assume the Country object for "USA"
        Country USA = d_map.getCountryByName("USA");
        
        // Verify that the neighbours for USA are Canada (ID 2) and France (ID 4)
        assertEquals("[2, 4, 6]", USA.getD_neighbouringCountriesId().toString());
        
        // Remove Canada (ID 2) as a neighbour of USA
        USA.removeCountryNeighbour(2);

        // Verify that Canada has been removed from the neighbour list of USA
        assertEquals("[4, 6]", USA.getD_neighbouringCountriesId().toString());
    }
}
