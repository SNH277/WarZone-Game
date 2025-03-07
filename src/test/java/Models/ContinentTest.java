package Models;

import Controller.MapController;
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
        d_map.addContinent("Asia", 15);
        d_map.addContinent("Africa", 10);

        // Add countries and assign continents
        d_map.addCountry("India", "Asia");
        d_map.addCountry("China", "Asia");
        d_map.addCountry("Morocco", "Africa");
        d_map.addCountry("Nigeria", "Africa");

        // Set borders for countries
        d_map.addBorder("India", "China");
        d_map.addBorder("China", "Morocco");
        d_map.addBorder("Morocco", "Nigeria");
        d_map.addBorder("Nigeria", "India");
    }

    /**
     * Test adding countries.
     */
    @Test
    public void addCountry() {
        d_map.addCountry("Pakistan", "Asia");
        d_map.addCountry("Nepal", "Asia");
        
        assertEquals("Pakistan", d_map.getCountryByName("Pakistan").getD_countryName());
        assertEquals("Nepal", d_map.getCountryByName("Nepal").getD_countryName());
    }

    /**
     * Test removing countries.
     */
    @Test
    public void removeCountry() {
        d_map.addCountry("Pakistan", "Asia");
        d_map.addCountry("Nepal", "Asia");

        d_map.removeCountry("Pakistan");
        assertNull(d_map.getCountryByName("Pakistan"));

        d_map.removeCountry("Nepal");
        assertNull(d_map.getCountryByName("Nepal"));
    }

    /**
     * Test continent ownership by country.
     */
    @Test
    public void testContinentOwnership() {
        // Assert that countries belong to their respective continents
        assertEquals("Asia", d_map.getCountryByName("India").getD_continent().getD_continentName());
        assertEquals("Asia", d_map.getCountryByName("China").getD_continent().getD_continentName());
        assertEquals("Africa", d_map.getCountryByName("Morocco").getD_continent().getD_continentName());
        assertEquals("Africa", d_map.getCountryByName("Nigeria").getD_continent().getD_continentName());
    }

    /**
     * Test borders between countries.
     */
    @Test
    public void testBorders() {
        // Assert that borders are correctly established
        assertTrue(d_map.getCountryByName("India").getBorders().contains(d_map.getCountryByName("China")));
        assertTrue(d_map.getCountryByName("China").getBorders().contains(d_map.getCountryByName("Morocco")));
        assertTrue(d_map.getCountryByName("Morocco").getBorders().contains(d_map.getCountryByName("Nigeria")));
        assertTrue(d_map.getCountryByName("Nigeria").getBorders().contains(d_map.getCountryByName("India")));
    }
}
