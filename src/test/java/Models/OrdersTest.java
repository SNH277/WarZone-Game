package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Orders test.
 */
public class OrdersTest {
    private Player d_player;
    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private String d_mapName;

    /**
     * Sets up.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_player = new Player("Player 1");
        d_mapName = "test.map";

        // Load map and validate
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        assertNotNull("Map should be loaded", d_map);

        // Assign map to game state
        d_currentState.setD_map(d_map);
    }

    /**
     * Execute deploy orders.
     */
    @Test
    public void execute() {
        // Ensure player owns the countries
        d_player.setD_currentCountries(d_map.getD_mapCountries());

        // Fetch countries and validate they exist
        Country l_india = d_map.getCountryByName("India");
        Country l_china = d_map.getCountryByName("China");
        Country l_morocco = d_map.getCountryByName("Morocco");

        assertNotNull("India should exist in map", l_india);
        assertNotNull("China should exist in map", l_china);
        assertNotNull("Morocco should exist in map", l_morocco);

        // Deploy Orders
        Orders l_order1 = new Deploy(d_player, "India", 3);
        Orders l_order2 = new Deploy(d_player, "China", 4);

        // Execute orders and verify army count
        l_order1.execute(d_currentState);
        assertEquals("India should have 3 armies", 3, l_india.getD_armies().intValue());

        l_order2.execute(d_currentState);
        assertEquals("China should have 4 armies", 4, l_china.getD_armies().intValue());

        // Ensure Morocco remains unchanged
        assertNotEquals("Morocco should not have 3 armies", 3, l_morocco.getD_armies().intValue());
    }
}
