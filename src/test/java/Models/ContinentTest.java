package Models;

import Controller.MapController;
import Model.CurrentState;
import Model.Map;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Continent test.
 */
public class ContinentTest {
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
     * Test adding countries.
     */
    @Test
    public void addCountry() {
        d_map.addCountry("Mexico", "NorthAmerica");
        d_map.addCountry("Chile", "SouthAmerica");
        
        assertEquals("Mexico", d_map.getCountryByName("Mexico").getD_countryName());
        assertEquals("Chile", d_map.getCountryByName("Chile").getD_countryName());
    }

    /**
     * Test removing countries.
     */
    @Test
    public void removeCountry() {
        d_map.addCountry("Mexico", "NorthAmerica");
        d_map.addCountry("Chile", "NorthAmerica");

        d_map.removeCountry("Mexico");
        assertNull(d_map.getCountryByName("Mexico"));

        d_map.removeCountry("Chile");
        assertNull(d_map.getCountryByName("Chile"));
    }

}
