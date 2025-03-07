package Services;

import Controller.MainGameEngine;
import Controller.MapController;
import Exceptions.CommandValidationException;
import Models.*;
import Utils.CommandHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameServiceTest {

    private Player d_player1, d_player2, d_player3;
    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private MainGameEngine d_mainGameEngine;
    private String d_saveFileName;

    @Before
    public void setup() {
        d_mapController = new MapController();
        d_currentState = new CurrentState();
        d_mainGameEngine = new MainGameEngine();
        d_map = new Map();

        // Sample Map Name
        String d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);

        // Initializing Players
        d_player1 = new Player("Player-1");
        d_player2 = new Player("Player-2");
        d_player3 = new Player("Player-3");

        // Set a common filename for saving/loading
        d_saveFileName = "testGameSave.txt";
    }

    /**
     * Tests the saveGame() functionality.
     */
    @Test
    public void testSaveGame() throws CommandValidationException, IOException, ClassNotFoundException {
        StartupPhase d_currentPhase = new StartupPhase(d_currentState, d_mainGameEngine);

        List<Player> d_playerList = new ArrayList<>();
        d_playerList.add(d_player1);
        d_playerList.add(d_player2);
        d_currentState.setD_players(d_playerList);

        // Save Game
        d_currentPhase.saveGame(new CommandHandler("savegame " + d_saveFileName), d_player1);

        // Load Game and Compare
        Phase d_loadedPhase = GameService.loadGame(d_saveFileName);
        assertEquals(d_currentPhase.getD_currentState().getD_players().size(),
                     d_loadedPhase.getD_currentState().getD_players().size());

        // Additional Assertions (Check Player Names)
        assertEquals(d_player1.getD_playerName(), d_loadedPhase.getD_currentState().getD_players().get(0).getD_playerName());
        assertEquals(d_player2.getD_playerName(), d_loadedPhase.getD_currentState().getD_players().get(1).getD_playerName());
    }

    /**
     * Tests the loadGame() functionality.
     */
    @Test
    public void testLoadGame() throws CommandValidationException, IOException, ClassNotFoundException {
        StartupPhase d_currentPhase = new StartupPhase(d_currentState, d_mainGameEngine);

        List<Player> d_playerList = new ArrayList<>();
        d_playerList.add(d_player1);
        d_playerList.add(d_player2);
        d_playerList.add(d_player3);
        d_currentState.setD_players(d_playerList);

        // Save Game
        d_currentPhase.saveGame(new CommandHandler("savegame " + d_saveFileName), d_player1);

        // Load Game
        Phase d_loadedPhase = GameService.loadGame(d_saveFileName);

        // Verify players were loaded correctly
        assertEquals(d_currentPhase.getD_currentState().getD_players().size(),
                     d_loadedPhase.getD_currentState().getD_players().size());

        // Ensure all player names match
        for (int i = 0; i < d_playerList.size(); i++) {
            assertEquals(d_playerList.get(i).getD_playerName(), 
                         d_loadedPhase.getD_currentState().getD_players().get(i).getD_playerName());
        }
    }

    /**
     * Cleanup - Deletes test save files after execution.
     */
    @After
    public void cleanup() {
        File file = new File(d_saveFileName);
        if (file.exists()) {
            file.delete();
        }
    }
}
