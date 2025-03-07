package Models;


import Controller.MainGameEngine;
import Exceptions.CommandValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * The type Tournament test.
 */
public class TournamentTest {

    private Player d_player1;
    private Player d_player2;
    private CurrentState d_currentState;
    private Tournament d_tournament;
    private MainGameEngine d_mainGameEngine;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        d_mainGameEngine = new MainGameEngine();
        d_currentState = new CurrentState();

        d_player1 = new Player("Player1");
        d_player1.setD_playerBehaviourStrategy(new RandomPlayer());

        d_player2 = new Player("Player2");
        d_player2.setD_playerBehaviourStrategy(new CheaterPlayer());

        d_currentState.setD_players(Arrays.asList(d_player1, d_player2));
        d_mainGameEngine.setD_stateOfGame(d_currentState);
        d_tournament = new Tournament();
    }

    /**
     * Test invalid map arguments.
     */
    @Test
    public void testInvalidMapArgs() {
        try {
            assertFalse("Should fail for more than 5 maps", 
                d_tournament.parseTournamentCommand(d_currentState, "M", "test1.map test2.map test3.map test4.map test5.map test6.map", d_mainGameEngine));
            
            assertTrue("Should pass for exactly 4 maps", 
                d_tournament.parseTournamentCommand(d_currentState, "M", "test1.map test2.map test3.map test4.map", d_mainGameEngine));

            assertFalse("Should fail for zero maps",
                d_tournament.parseTournamentCommand(d_currentState, "M", "", d_mainGameEngine));

            assertTrue("Should pass for exactly 5 maps",
                d_tournament.parseTournamentCommand(d_currentState, "M", "test1.map test2.map test3.map test4.map test5.map", d_mainGameEngine));

        } catch (CommandValidationException e) {
            fail("Exception should not be thrown in testInvalidMapArgs.");
        }
    }

    /**
     * Test invalid player strategies.
     */
    @Test
    public void testInvalidPlayerStrategiesArgs() {
        try {
            assertTrue("Valid strategies: Random and Cheater", 
                d_tournament.parseTournamentCommand(d_currentState, "P", "Random Cheater", d_mainGameEngine));
            
            assertFalse("Invalid strategy: Aggressive not supported", 
                d_tournament.parseTournamentCommand(d_currentState, "P", "Random Aggressive", d_mainGameEngine));

            assertFalse("Invalid strategy: Empty strategy list",
                d_tournament.parseTournamentCommand(d_currentState, "P", "", d_mainGameEngine));

            assertFalse("Invalid strategy: Unknown strategy",
                d_tournament.parseTournamentCommand(d_currentState, "P", "UnknownStrategy", d_mainGameEngine));

        } catch (CommandValidationException e) {
            fail("Exception should not be thrown in testInvalidPlayerStrategiesArgs.");
        }
    }

    /**
     * Test invalid number of games.
     */
    @Test
    public void testInvalidNoOfGameArgs() {
        try {
            assertFalse("Too many games should be rejected",
                d_tournament.parseTournamentCommand(d_currentState, "G", "10", d_mainGameEngine));

            assertTrue("Valid number of games (3) should be accepted",
                d_tournament.parseTournamentCommand(d_currentState, "G", "3", d_mainGameEngine));

            assertFalse("Negative number of games should be rejected",
                d_tournament.parseTournamentCommand(d_currentState, "G", "-2", d_mainGameEngine));

            assertFalse("Zero games should be rejected",
                d_tournament.parseTournamentCommand(d_currentState, "G", "0", d_mainGameEngine));

        } catch (CommandValidationException e) {
            fail("Exception should not be thrown in testInvalidNoOfGameArgs.");
        }
    }

    /**
     * Test invalid number of turns.
     */
    @Test
    public void testInvalidNoOfTurns() {
        try {
            assertFalse("100 turns should be rejected", 
                d_tournament.parseTournamentCommand(d_currentState, "D", "100", d_mainGameEngine));

            assertTrue("40 turns should be accepted", 
                d_tournament.parseTournamentCommand(d_currentState, "D", "40", d_mainGameEngine));

            assertFalse("Negative turns should be rejected",
                d_tournament.parseTournamentCommand(d_currentState, "D", "-5", d_mainGameEngine));

            assertFalse("Zero turns should be rejected",
                d_tournament.parseTournamentCommand(d_currentState, "D", "0", d_mainGameEngine));

        } catch (CommandValidationException e) {
            fail("Exception should not be thrown in testInvalidNoOfTurns.");
        }
    }
}
