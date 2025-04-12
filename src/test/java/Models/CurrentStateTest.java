package Models;

import Controller.MapController;
import Model.Country;
import Model.CurrentState;
import Model.Map;
import org.junit.Before;
import org.junit.Test;

import javax.imageio.IIOException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        d_map.addCountry("USA", "Asia");
        d_map.addCountry("Canada", "Asia");
        d_map.addCountry("Morocco", "Africa");
        d_map.addCountry("Nigeria", "Africa");

    }

    /**
     * Add or remove game players.
     */
    @Test
    public void addOrRemoveGamePlayers() throws IOException {
        InputStream originalSystemIn = System.in;
        d_currentState.setD_players(new ArrayList<>());
        ByteArrayInputStream in = new ByteArrayInputStream("Aggressive\n".getBytes());
        System.setIn(in);
        // Add "Player1" to the game
        d_currentState.addOrRemovePlayer("add", "Player1");
        assertEquals(1, d_currentState.getD_players().size());  // Verify one player has been added

        // Remove "Player1" from the game
        d_currentState.addOrRemovePlayer("remove", "Player1");
        assertEquals(0, d_currentState.getD_players().size());  // Verify no players are present
        System.setIn(originalSystemIn);
    }

    /**
     * Verify the countries and their neighboring countries
     */
    @Test
    public void verifyCountryBorders() {
        // Test the neighboring countries for "USA"
        Country USA = d_map.getCountryByName("USA");
        assertNotNull(USA);
        assertEquals("[2, 4, 6]", USA.getD_neighbouringCountriesId().toString());  // Verify borders for USA

        // Test the neighboring countries for "Canada"
        Country Canada = d_map.getCountryByName("Canada");
        assertNotNull(Canada);
        assertEquals("[1, 3, 5]", Canada.getD_neighbouringCountriesId().toString());  // Verify borders for Canada
    }
}
