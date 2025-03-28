package Controller;

import Model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for MainGameEngine class.
 */
public class MainGameEngineTest {
    private MainGameEngine d_mainGameEngine;

    /**
     * Setup before each test.
     */
    @Before
    public void setUp() {
        d_mainGameEngine = new MainGameEngine();
    }

    /**
     * Test if game state is initialized correctly.
     */
    @Test
    public void testGameStateInitialization() {
        assertNotNull("Game state should not be null", d_mainGameEngine.getD_currentGameState());
    }

    /**
     * Test if the initial phase is StartupPhase.
     */
    @Test
    public void testInitialPhase() {
        assertTrue("Initial phase should be StartupPhase",
                d_mainGameEngine.getD_currentPhase() instanceof StartupPhase);
    }

}
