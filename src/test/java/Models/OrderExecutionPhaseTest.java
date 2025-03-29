package Models;

import Controller.MainGameEngine;
import Controller.MapController;
import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Order execution phase test.
 * <p>
 * This class contains unit tests for the execution of orders during the game phase,
 * specifically focusing on verifying the state changes after performing actions like advancing orders
 * and checking for the end of the game.
 */
public class OrderExecutionPhaseTest {
    /**
     * The D map.
     * <p>
     * This field stores the current game map, which is used for validating game mechanics like
     * advancing orders and updating countries' armies.
     */
    Map d_map;

    /**
     * The D map controller.
     * <p>
     * This field manages the loading and setup of the game map for the current game session.
     */
    MapController d_mapController;

    /**
     * The D current state.
     * <p>
     * This field represents the current game state, including all players, territories, and the map.
     */
    CurrentState d_currentState;

    /**
     * The D main game engine.
     * <p>
     * This field is responsible for executing the main game logic and controlling the flow of the game.
     */
    MainGameEngine d_mainGameEngine;

    /**
     * The D player 1.
     * <p>
     * This field represents the first player in the game, with associated territories and actions.
     */
    Player d_player1;

    /**
     * The D player 2.
     * <p>
     * This field represents the second player in the game, with associated territories and actions.
     */
    Player d_player2;

    /**
     * The D order execution phase.
     * <p>
     * This field handles the execution of orders within the game, ensuring that actions are processed and the game state is updated accordingly.
     */
    OrderExecutionPhase d_orderExecutionPhase;

    /**
     * Sets up the test environment.
     * <p>
     * This method initializes the game state, players, and the map before each test case.
     * It also prepares the order execution phase for testing.
     */
    @Before
    public void setUp() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mainGameEngine = new MainGameEngine();

        d_map = d_mapController.loadMap(d_currentState, "test.map");
        d_currentState.setD_map(d_map);

        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        List<Player> l_players = List.of(d_player1, d_player2);
        d_currentState.setD_players(l_players);

        List<Country> l_player1Countries = new ArrayList<>();
        List<Country> l_player2Countries = new ArrayList<>();
        d_map.getCountryByName("USA").setD_armies(10);
        d_map.getCountryByName("Canada").setD_armies(0);
        l_player1Countries.add(d_map.getCountryByName("USA"));
        l_player1Countries.add(d_map.getCountryByName("Canada"));
        l_player1Countries.add(d_map.getCountryByName("France"));
        l_player1Countries.add(d_map.getCountryByName("UK"));
        l_player1Countries.add(d_map.getCountryByName("Germany"));
        l_player1Countries.add(d_map.getCountryByName("Argentina"));
        l_player1Countries.add(d_map.getCountryByName("Chile"));
        l_player2Countries.add(d_map.getCountryByName("Brazil"));
        d_player1.setD_currentCountries(l_player1Countries);
        d_player2.setD_currentCountries(l_player2Countries);

        d_orderExecutionPhase = new OrderExecutionPhase(d_currentState, d_mainGameEngine);
    }

    /**
     * Check if the game ends after executing an advance order.
     * <p>
     * This test verifies whether the end of game condition is triggered after a successful conquest
     * and the acquisition of a card by Player 1.
     */
    @Test
    public void checkEndOfGame() {
        assertFalse(d_orderExecutionPhase.checkEndOfGame(d_currentState));
        Orders l_advanceOrder = new Advance("USA", "Brazil", 9, d_player1);
        assertEquals(0,d_player1.getD_cardOwnedByPlayer().size());
        l_advanceOrder.execute(d_currentState);
        assertEquals(1,d_player1.getD_cardOwnedByPlayer().size());
        assertTrue(d_orderExecutionPhase.checkEndOfGame(d_currentState));
    }
}
