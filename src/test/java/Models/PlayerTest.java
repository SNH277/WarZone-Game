package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Player test.
 */
public class PlayerTest {
    private Player d_player;
    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private String d_mapName;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_player = new Player("Player 1");
        d_mapName = "test.map";

        // Load map and validate
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        assertNotNull("Map should be loaded successfully", d_map);
    }

    /**
     * Test setting a continent to a player.
     */
    @Test
    public void setContinent() {
        assertFalse("Map should have at least one continent", d_map.getD_mapContinents().isEmpty());

        d_player.setContinent(d_map.getD_mapContinents().get(0));
        assertEquals("Asia", d_player.getD_currentContinents().get(0).getD_continentName());
        assertNotEquals("Europe", d_player.getD_currentContinents().get(0).getD_continentName());
    }

    /**
     * Test the nextOrder method.
     */
    @Test
    public void nextOrder() {
        Orders l_order1 = new Deploy(d_player, "India", 3);
        Orders l_order2 = new Deploy(d_player, "China", 4);
        List<Orders> l_orderList = new ArrayList<>();
        l_orderList.add(l_order1);
        l_orderList.add(l_order2);

        d_player.setD_orders(l_orderList);

        assertEquals("First order should be returned", l_order1, d_player.nextOrder());
        assertEquals("Second order should be returned", l_order2, d_player.nextOrder());
        assertNull("No more orders should be left", d_player.nextOrder());
    }
}
