package Models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import Model.*;
import org.junit.Before;
import org.junit.Test;

import Controller.MapController;

/**
 * The type Orders test.
 */
public class OrdersTest {
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
     * Sets .
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_player = new Player("Player 1");
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState,d_mapName);
    }

    /**
     * Execute.
     */
    @Test
    public void execute() {
        d_player.setD_currentCountries(d_map.getD_mapCountries());
        Orders l_order1 = new Deploy(d_player,"USA",3);
        Orders l_order2 = new Deploy(d_player,"UK",4);
        l_order1.execute(d_currentState);
        assertEquals("3", d_map.getCountryByName("USA").getD_armies().toString());
        l_order2.execute(d_currentState);
        assertEquals("4", d_map.getCountryByName("UK").getD_armies().toString());
        assertNotEquals("3", d_map.getCountryByName("Chile").getD_armies().toString());
    }
}