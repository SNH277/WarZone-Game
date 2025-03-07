package Models;

import Controller.MapController;
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
        d_map.addContinent("Asia", 15);
        d_map.addContinent("Africa", 10);

        // Add countries and assign continents
        d_map.addCountry("India", "Asia");
        d_map.addCountry("China", "Asia");
        d_map.addCountry("Morocco", "Africa");
        d_map.addCountry("Nigeria", "Africa");

        // Set borders for countries (using their country IDs)
        d_map.addBorder("India", "China");
        d_map.addBorder("China", "Morocco");
        d_map.addBorder("Morocco", "Nigeria");
        d_map.addBorder("Nigeria", "India");
    }

    /**
     * Remove country neighbour if present.
     */
    @Test
    public void removeCountryNeighbourIfPresent() {
        // Assume the Country object for "India"
        Country india = d_map.getCountryByName("India");
        
        // Verify that the neighbours for India are China (ID 2) and Nigeria (ID 4)
        assertEquals("[2, 4]", india.getD_neighbouringCountriesId().toString());
        
        // Remove China (ID 2) as a neighbour of India
        india.removeCountryNeighbourIfPresent(2);
        
        // Verify that China has been removed from the neighbour list of India
        assertEquals("[4]", india.getD_neighbouringCountriesId().toString());
    }
}
