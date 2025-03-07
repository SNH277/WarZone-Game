package Models;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.Test;

import Controller.MapController;

/**
 * The type Player test.
 */
public class PlayerTest {
    /**
     * The D player.
     */
    Player d_player;
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
     * Setup method to initialize test variables.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_player = new Player("Player 1");
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
    }

    /**
     * Test setting continent for the player.
     */
    @Test
    public void setContinent() {
        // Set the continent for the player
        d_player.setContinent(d_map.getD_mapContinents().get(0));  // Asia

        // Check that the player's continent is correctly set
        assertEquals("Asia", d_player.getD_currentContinents().iterator().next().getD_continentName());
        assertNotEquals("Europe", d_player.getD_currentContinents().iterator().next().getD_continentName());
    }

    /**
     * Test processing orders for the player.
     */
    @Test
    public void nextOrder() {
        // Create orders for deployment
        Orders l_order1 = new Orders("deploy", "India", 3);
        Orders l_order2 = new Orders("deploy", "China", 4);
        
        // Create a list of orders and set them for the player
        List<Orders> l_orderlist = new ArrayList<>();
        l_orderlist.add(l_order1);
        l_orderlist.add(l_order2);
        d_player.setD_orders(l_orderlist);

        // Test that the player's next order is processed correctly
        assertEquals(l_order1, d_player.nextOrder());
        assertEquals(l_order2, d_player.nextOrder());
    }
}
