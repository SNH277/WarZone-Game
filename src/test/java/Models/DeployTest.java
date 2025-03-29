package Models;

import Controller.MapController;
import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Deploy test.
 * This class contains unit tests for the Deploy order in the game.
 * It tests the execution of the Deploy order to verify that armies are correctly deployed to a specified country.
 */
public class DeployTest {

    /**
     * The D map.
     * The map of countries in the game, including their armies and adjacencies.
     */
    Map d_map;

    /**
     * The D player.
     * The player who is involved in deploying armies to countries.
     */
    Player d_player;

    /**
     * The D current state.
     * The current state of the game, including players, map, and other game-related details.
     */
    CurrentState d_currentState;

    /**
     * The D map controller.
     * The controller that handles map-related actions, including loading the map.
     */
    MapController d_mapController;

    /**
     * Sets up the game environment for testing.
     * This method initializes the game state, player, map, and controller, and loads the map.
     * The player is assigned the list of countries from the map.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        d_map = new Map();
        d_mapController = new MapController();
        d_player = new Player("Player1");
        d_currentState = new CurrentState();
        d_map = d_mapController.loadMap(d_currentState, "test.map");
        List<Country> l_countryList = d_map.getD_mapCountries();
        d_player.setD_currentCountries(l_countryList);
    }

    /**
     * Execute deploy order.
     * This test case checks the execution of the Deploy order.
     * It verifies that the correct number of armies is deployed to the specified country.
     */
    @Test
    public void execute() {
        Deploy l_deploy = new Deploy(d_player, "USA", 5);
        l_deploy.execute(d_currentState);
        assertEquals("5", d_currentState.getD_map().getD_mapCountries().get(0).getD_armies().toString());
    }
}
