package Models;

import Controller.MainGameEngine;
import Controller.MapController;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Phase test.
 */
public class PhaseTest {

    private CurrentState d_currentState;
    private MainGameEngine d_mainGameEngine;
    private StartupPhase d_startupPhase;
    private Map d_map;
    private Player d_player;
    private MapController d_mapController;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        d_mainGameEngine = new MainGameEngine();
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_startupPhase = new StartupPhase(d_currentState, d_mainGameEngine);

        // Load map and validate
        d_map = d_mapController.loadMap(d_currentState, "test.map");
        assertNotNull("Map should be loaded successfully", d_map);

        // Set game state
        d_player = new Player("Mehak");
        d_currentState.setD_map(d_map);

        // Add player to game state
        List<Player> l_players = new ArrayList<>();
        l_players.add(d_player);
        d_currentState.setD_players(l_players);
    }

    /**
     * Correct startup phase.
     *
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    @Test
    public void correctStartupPhase() throws CommandValidationException, IOException {
        // Ensure initial phase is StartupPhase
        assertEquals("Game should start in StartupPhase", d_startupPhase.getClass(), d_mainGameEngine.getD_currentPhase().getClass());

        // Load map and check phase remains StartupPhase
        d_mainGameEngine.getD_currentPhase().loadMap(new CommandHandler("loadmap test.map"));
        assertEquals("After loading map, phase should still be StartupPhase", d_startupPhase.getClass(), d_mainGameEngine.getD_currentPhase().getClass());
    }
}
