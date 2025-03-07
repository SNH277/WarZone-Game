package Models;

import Controller.MainGameEngine;
import Controller.MapController;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Order execution phase test.
 */
public class OrderExecutionPhaseTest {
    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private MainGameEngine d_mainGameEngine;
    private Player d_player1;
    private Player d_player2;
    private OrderExecutionPhase d_orderExecutionPhase;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mainGameEngine = new MainGameEngine();

        // Load Map
        d_map = d_mapController.loadMap(d_currentState, "test.map");
        assertNotNull("Map should be loaded", d_map);
        d_currentState.setD_map(d_map);

        // Create Players
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        List<Player> l_players = new ArrayList<>(List.of(d_player1, d_player2));
        d_currentState.setD_players(l_players);

        // Assign Countries
        Country l_india = d_map.getCountryByName("India");
        Country l_china = d_map.getCountryByName("China");
        Country l_nigeria = d_map.getCountryByName("Nigeria");
        Country l_morocco = d_map.getCountryByName("Morocco");

        assertNotNull("India should exist in map", l_india);
        assertNotNull("China should exist in map", l_china);
        assertNotNull("Nigeria should exist in map", l_nigeria);
        assertNotNull("Morocco should exist in map", l_morocco);

        // Set Army Counts
        l_india.setD_armies(10);
        l_china.setD_armies(0);

        // Assign Countries to Players
        d_player1.setD_currentCountries(new ArrayList<>(List.of(l_india, l_nigeria, l_morocco)));
        d_player2.setD_currentCountries(new ArrayList<>(List.of(l_china)));

        // Initialize Order Execution Phase
        d_orderExecutionPhase = new OrderExecutionPhase(d_currentState, d_mainGameEngine);
    }

    /**
     * Check end of game.
     */
    @Test
    public void checkEndOfGame() {
        assertFalse("Game should not end initially", d_orderExecutionPhase.checkEndOfGame(d_currentState));

        Orders l_advanceOrder = new Advance("India", "China", 9, d_player1);
        assertEquals("Player should have no cards initially", 0, d_player1.getD_cardsOwnedByPlayer().size());

        l_advanceOrder.execute(d_currentState);

        // Check if player 1 gets a card
        assertEquals("Player should receive 1 card after execution", 1, d_player1.getD_cardsOwnedByPlayer().size());

        // Check if China is now owned by Player1
        assertTrue("Game should end when Player1 conquers all", d_orderExecutionPhase.checkEndOfGame(d_currentState));
    }
}
