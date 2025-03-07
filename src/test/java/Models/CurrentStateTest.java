package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * The type Current state test.
 */
public class CurrentStateTest {
    /**
     * The D map.
     */
    Map d_map;
    /**
     * The D map controller.
     */
    MapController d_mapController;
    /**
     * The D current state.
     */
    CurrentState d_currentState;
    /**
     * The D map name.
     */
    String d_mapName;


    /**
     * Sets up the test environment.
     */
    @Before
    public void setup() {
        d_mapController = new MapController();
        d_currentState = new CurrentState();
        d_map = new Map();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);

        // Add continents with respective bonus values
        d_map.addContinent("Asia", 15);
        d_map.addContinent("Africa", 10);

        // Add countries and assign them to continents
        d_map.addCountry("India", "Asia");
        d_map.addCountry("China", "Asia");
        d_map.addCountry("Morocco", "Africa");
        d_map.addCountry("Nigeria", "Africa");

        // Set borders (using country IDs)
        d_map.addBorder("India", "China");
        d_map.addBorder("China", "Morocco");
        d_map.addBorder("Morocco", "Nigeria");
        d_map.addBorder("Nigeria", "India");
    }

    /**
     * Add or remove game players.
     */
    @Test
    public void addOrRemoveGamePlayers() {
        // Initialize the players list in the current state
        d_currentState.setD_players(new ArrayList<>());
        
        // Add "Player1" to the game
        d_currentState.addOrRemoveGamePlayers("add", "Player1");
        assertEquals(1, d_currentState.getD_players().size());  // Verify one player has been added
        
        // Remove "Player1" from the game
        d_currentState.addOrRemoveGamePlayers("remove", "Player1");
        assertEquals(0, d_currentState.getD_players().size());  // Verify no players are present
    }

    /**
     * Verify the countries and their neighboring countries
     */
    @Test
    public void verifyCountryBorders() {
        // Test the neighboring countries for "India"
        Country india = d_map.getCountryByName("India");
        assertNotNull(india);
        assertEquals("[2, 4]", india.getD_neighbouringCountriesId().toString());  // Verify borders for India

        // Test the neighboring countries for "China"
        Country china = d_map.getCountryByName("China");
        assertNotNull(china);
        assertEquals("[1, 3, 4]", china.getD_neighbouringCountriesId().toString());  // Verify borders for China
    }
}
