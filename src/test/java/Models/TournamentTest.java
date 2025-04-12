package Models;

import Controller.MainGameEngine;
import Exceptions.CommandValidationException;
import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test class for the {@link Tournament} mode command validation.
 * <p>
 * This class tests different parts of the tournament command:
 * - Map file count
 * - Player strategy validation
 * - Number of games
 * - Number of turns
 * </p>
 */
public class TournamentTest {

    /** First test player. */
    Player d_player1;

    /** Second test player. */
    Player d_player2;

    /** Holds the current state of the game during testing. */
    CurrentState d_currentState;

    /** Tournament instance being tested. */
    Tournament d_tournament;

    /** Game engine used to manage the tournament state and logs. */
    MainGameEngine d_mainGameEngine = new MainGameEngine();

    /**
     * Initializes test setup with two players and a game state before each test case.
     * This method sets default strategies and prepares the tournament instance.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_player1 = new Player("Player1");
        d_player1.setD_playerBehaviourStrategy(new RandomPlayer());
        d_player2 = new Player("Player2");
        d_player2.setD_playerBehaviourStrategy(new CheaterPlayer());

        d_currentState.setD_players(Arrays.asList(d_player1, d_player2));
        d_mainGameEngine.setD_currentGameState(d_currentState);
        d_tournament = new Tournament();
    }

    /**
     * Tests validation of map argument count in the tournament command.
     * <p>
     * Verifies that more than 5 maps fails and 4 maps succeeds.
     * </p>
     *
     * @throws CommandValidationException if validation fails internally
     */
    @Test
    public void testInvalidMapArgs() throws CommandValidationException {
        assertFalse(d_tournament.parseTournamentCommand(d_currentState, "M", "test.map test.map test.map test.map test.map test.map", d_mainGameEngine));
        assertTrue(d_tournament.parseTournamentCommand(d_currentState, "M", "test.map test.map test.map test.map", d_mainGameEngine));
    }

    /**
     * Tests validation of player strategies in the tournament command.
     * <p>
     * Valid strategies are allowed; invalid or misspelled ones are rejected.
     * </p>
     *
     * @throws CommandValidationException if validation fails internally
     */
    @Test
    public void testInvalidPlayerStrategiesArgs() throws CommandValidationException {
        assertTrue(d_tournament.parseTournamentCommand(d_currentState, "P", "Random Cheater", d_mainGameEngine));
        assertFalse(d_tournament.parseTournamentCommand(d_currentState, "P", "Random Aggessive", d_mainGameEngine)); // typo in Aggressive
    }

    /**
     * Tests validation of number of games to be played in the tournament.
     * <p>
     * Accepts only values between 1 and 5 (inclusive).
     * </p>
     *
     * @throws CommandValidationException if validation fails internally
     */
    @Test
    public void testInvalidNoOfGameArgs() throws CommandValidationException {
        assertFalse(d_tournament.parseTournamentCommand(d_currentState, "G", "10", d_mainGameEngine)); // Invalid, greater than 5
        assertTrue(d_tournament.parseTournamentCommand(d_currentState, "G", "3", d_mainGameEngine));   // Valid
    }

    /**
     * Tests validation of maximum number of turns in the tournament.
     * <p>
     * Only values between 10 and 50 (inclusive) are allowed.
     * </p>
     *
     * @throws CommandValidationException if validation fails internally
     */
    @Test
    public void testInvalidNoOfTurns() throws CommandValidationException {
        assertFalse(d_tournament.parseTournamentCommand(d_currentState, "D", "100", d_mainGameEngine)); // Invalid, greater than 50
        assertTrue(d_tournament.parseTournamentCommand(d_currentState, "D", "40", d_mainGameEngine));   // Valid
    }
}

