package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * The type Current state test.
 */
public class CurrentStateTest {
    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private String d_mapName;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setup() {
        d_mapController = new MapController();
        d_currentState = new CurrentState();
        d_map = d_mapController.loadMap(d_currentState, "test.map");

        // Ensure the map is loaded successfully
        assertNotNull("Map should be loaded successfully", d_map);
    }

    /**
     * Test adding and removing game players.
     *
     * @throws IOException if input stream fails
     */
    @Test
    public void addOrRemoveGamePlayers() throws IOException {
        InputStream originalSystemIn = System.in;
        try {
            d_currentState.setD_players(new ArrayList<>());

            // Simulate user input for player strategy selection
            ByteArrayInputStream in = new ByteArrayInputStream("Aggressive\n".getBytes());
            System.setIn(in);

            // Add player
            d_currentState.addOrRemoveGamePlayers("add", "Player1");
            assertEquals("Player1 should be added", 1, d_currentState.getD_players().size());

            // Remove player
            d_currentState.addOrRemoveGamePlayers("remove", "Player1");
            assertEquals("Player1 should be removed", 0, d_currentState.getD_players().size());
        } finally {
            // Ensure System.in is restored
            System.setIn(originalSystemIn);
        }
    }
}
