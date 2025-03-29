package Models;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Controller.MapController;
import Model.Advance;
import Model.Country;
import Model.CurrentState;
import Model.Map;
import Model.Orders;
import Model.Player;

/**
 * The type Advance order test.
 * This class contains unit tests to verify the behavior of the Advance order within the game.
 * It sets up the game environment and tests if the Advance order works correctly in different scenarios.
 * @author Taksh Rana
 */
public class AdvanceOrderTest {

    /**
     * The D current state.
     * The current state of the game, which includes players, map, and other game-related information.
     */
    CurrentState d_currentState;

    /**
     * The D player 1.
     * The first player in the game.
     */
    Player d_player1;

    /**
     * The D player 2.
     * The second player in the game.
     */
    Player d_player2;

    /**
     * The D map.
     * The map that contains all the countries and territories for the game.
     */
    Map d_map;

    /**
     * The D map controller.
     * The controller responsible for loading and managing the map for the game.
     */
    MapController d_mapController = new MapController();

    /**
     * Setup.
     * This method sets up the initial game environment before each test.
     * It initializes players, the current state, and the map, and assigns countries to players.
     */
    @Before
    public void setup(){
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        d_currentState = new CurrentState();

        d_map = d_mapController.loadMap(d_currentState, "test.map");
        List<Player> l_players = new ArrayList<>();
        l_players.add(d_player1);
        l_players.add(d_player2);
        d_currentState.setD_players(l_players);
        d_currentState.setD_map(d_map);
        List<Country> l_player1Countries = new ArrayList<>();
        List<Country> l_player2Countries = new ArrayList<>();
        d_map.getCountryByName("USA").setD_armies(10);
        d_map.getCountryByName("Canada").setD_armies(0);
        l_player1Countries.add(d_map.getCountryByName("USA"));
        l_player2Countries.add(d_map.getCountryByName("Canada"));
        d_player1.setD_currentCountries(l_player1Countries);
        d_player2.setD_currentCountries(l_player2Countries);
    }

    /**
     * Execute.
     * This method tests the execution of an Advance order from the USA to Canada.
     * It verifies that the correct number of armies are moved from USA to Canada.
     */
    @Test
    public void execute(){
        Orders l_advanceOrder = new Advance("USA","Canada",9,d_player1);
        l_advanceOrder.execute(d_currentState);
        Assert.assertEquals("9",d_map.getCountryByName("Canada").getD_armies().toString());
    }
}
