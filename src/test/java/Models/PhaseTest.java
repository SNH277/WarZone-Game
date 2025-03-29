package Models;

import Controller.MainGameEngine;
import Controller.MapController;
import Exceptions.CommandValidationException;
import Model.CurrentState;
import Model.Map;
import Model.Player;
import Model.StartupPhase;
import Utils.CommandHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * The type Phase test.
 * <p>
 * This class contains unit tests for the initialization and validation of the startup phase of the game.
 * It ensures that the game starts correctly and transitions into the expected phase.
 */
public class PhaseTest {

    /**
     * The D current state.
     * <p>
     * This field represents the current state of the game, including players, map, and other game data.
     */
    CurrentState d_currentState;

    /**
     * The D main game engine.
     * <p>
     * This field is responsible for managing the overall game flow, including the phases of the game.
     */
    MainGameEngine d_mainGameEngine;

    /**
     * The D startup phase.
     * <p>
     * This field manages the startup phase of the game, responsible for initializing the game state and players.
     */
    StartupPhase d_startupPhase;

    /**
     * The D map.
     * <p>
     * This field holds the game map, which is used to manage territories and their relationships.
     */
    Map d_map;

    /**
     * The D player.
     * <p>
     * This field represents the player participating in the game.
     */
    Player d_player;

    /**
     * The D map controller.
     * <p>
     * This field is responsible for loading and managing the game map.
     */
    MapController d_mapController;

    /**
     * Setup the test environment.
     * <p>
     * This method initializes the necessary components, including the game engine, state, map, player,
     * and assigns the map to the game state before each test case.
     */
    @Before
    public void setup(){
        d_mainGameEngine = new MainGameEngine();
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_startupPhase = new StartupPhase(d_mainGameEngine,d_currentState);
        d_map = d_mapController.loadMap(d_currentState, "test.map");
        d_player = new Player("Player1");
        d_currentState.setD_map(d_map);
        List<Player> l_players = new ArrayList<>();
        l_players.add(d_player);
        d_currentState.setD_players(l_players);
    }

    /**
     * Test for correct initialization of the startup phase.
     * <p>
     * This test checks if the correct startup phase is set in the game engine and ensures that the
     * game phase transitions correctly to the startup phase.
     *
     * @throws CommandValidationException if there is an error with command validation
     * @throws IOException if there is an error with loading the map
     */
    @Test
    public void correctStartupPhase() throws CommandValidationException, IOException {
        // Verifies that the current phase is the startup phase
        assertEquals(d_mainGameEngine.getD_currentPhase().getClass(), d_startupPhase.getClass());

        // Uncommented line for further validation of map loading can be used if needed
        // d_mainGameEngine.getD_currentPhase().loadMap(new CommandHandler("loadmap test.map"));

        // Verifies again that the current phase is still the startup phase
        assertEquals(d_mainGameEngine.getD_currentPhase().getClass(), d_startupPhase.getClass());
    }
}
