package Services;

import Controller.MainGameEngine;
import Controller.MapController;
import Exceptions.CommandValidationException;
import Model.*;
import Utils.CommandHandler;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit test class for the {@link GameService} responsible for saving and loading game states.
 * <p>
 * Tests the ability to:
 * - Save the current game state to a file.
 * - Load the game state from a file.
 * </p>
 */
public class GameServiceTest {

    /** First player used for testing. */
    Player d_player1;

    /** Second player used for testing. */
    Player d_player2;

    /** Third player used for testing. */
    Player d_player3;

    /** Map instance used in test setup. */
    Map d_map;

    /** Controller used to load and manage maps. */
    MapController d_mapController;

    /** Holds the current game state. */
    CurrentState d_currentState;

    /** Main game engine instance. */
    MainGameEngine d_mainGameEngine;

    /** Name of the map file used for testing. */
    String d_mapName;

    /**
     * Setup method executed before each test case.
     * Initializes map, players, and game state for testing save/load functionality.
     */
    @Before
    public void setup() {
        d_mapController = new MapController();
        d_currentState = new CurrentState();
        d_map = new Map();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        d_player3 = new Player("Player3");
    }

    /**
     * Tests the functionality of saving the game state and then loading it back.
     * <p>
     * Verifies that the number of players in the saved state matches the loaded one.
     * </p>
     *
     * @throws CommandValidationException if command syntax is invalid
     * @throws IOException if there is an error during file operations
     * @throws ClassNotFoundException if the saved object class is not found during deserialization
     */
    @Test
    public void testSaveGame() throws CommandValidationException, IOException, ClassNotFoundException {
        StartupPhase d_currentPhase = new StartupPhase(d_mainGameEngine, d_currentState);

        List<Player> d_player_list = new ArrayList<>();
        d_player_list.add(d_player1);
        d_player_list.add(d_player2);
        d_currentState.setD_players(d_player_list);

        d_currentPhase.saveGame(new CommandHandler("savegame savedGame.txt"), d_player1);

        Phase d_phase = new StartupPhase(new MainGameEngine(), new CurrentState());
        d_phase = GameService.loadGame("savedGame.txt");

        assertEquals(d_currentPhase.getD_currentState().getD_players().size(),
                d_phase.getD_currentState().getD_players().size());
    }

    /**
     * Tests the ability to load a previously saved game state.
     * <p>
     * Saves a game with three players and then loads it to verify player list consistency.
     * </p>
     *
     * @throws CommandValidationException if command syntax is invalid
     * @throws IOException if there is an error during file operations
     * @throws ClassNotFoundException if the saved object class is not found during deserialization
     */
    @Test
    public void loadGameTest() throws CommandValidationException, IOException, ClassNotFoundException {
        StartupPhase d_currentPhase = new StartupPhase(d_mainGameEngine, d_currentState);

        List<Player> d_player_list = new ArrayList<>();
        d_player_list.add(d_player1);
        d_player_list.add(d_player2);
        d_player_list.add(d_player3);
        d_currentState.setD_players(d_player_list);
        d_currentPhase.getD_currentState().setD_players(d_player_list);

        d_currentPhase.saveGame(new CommandHandler("savegame loadGame.txt"), d_player1);

        Phase d_phase = new StartupPhase(new MainGameEngine(), new CurrentState());
        d_phase = GameService.loadGame("loadGame.txt");

        assertEquals(d_currentPhase.getD_currentState().getD_players().size(),
                d_phase.getD_currentState().getD_players().size());
    }
}

