package Models;

import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Card blockade test.
 * This class contains unit tests to verify the functionality of the CardBlockade order in the game.
 * It tests the validity of the CardBlockade order and its execution when applied to a country.
 @author Taksh Rana
 */
public class CardBlockadeTest {

    /**
     * The D current state.
     * The current state of the game, including the players, map, and other game-related information.
     */
    CurrentState d_currentState;

    /**
     * The D card blockade order 1.
     * The instance of the CardBlockade order for the valid case.
     */
    CardBlockade d_cardBlockadeOrder1;

    /**
     * The D card blockade order 2.
     * The instance of the CardBlockade order for the invalid case.
     */
    CardBlockade d_cardBlockadeOrder2;

    /**
     * The D player 1.
     * The first player involved in the game.
     */
    Player d_player1;

    /**
     * Sets up the game environment for testing.
     * This method initializes the game state, players, countries, and map, and assigns the CardBlockade orders.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_player1 = new Player("player1");

        Player d_player2 = new Player("Neutral");

        List<Player> l_playersList = new ArrayList<>();
        l_playersList.add(d_player1);
        l_playersList.add(d_player2);

        List<Country> l_countryList1 = new ArrayList<>();

        Country l_country1 = new Country(1,"USA",1);
        l_countryList1.add(l_country1);

        Country l_country2 = new Country(2,"Canada",1);
        l_countryList1.add(l_country2);

        List<Country> l_mapCountries = new ArrayList<>();
        Country l_sourceCountry = new Country(1,"USA",1);
        Country l_countryToBeBlocked = new Country(2,"Canada",1);
        l_countryToBeBlocked.setD_armies(5);
        l_mapCountries.add(l_sourceCountry);
        l_mapCountries.add(l_countryToBeBlocked);

        Map l_map = new Map();
        l_map.setD_mapCountries(l_countryList1);
        l_map.setD_mapCountries(l_mapCountries);
        d_player1.setD_currentCountries(l_countryList1);
        d_currentState.setD_players(l_playersList);
        d_currentState.setD_map(l_map);

        d_cardBlockadeOrder1 = new CardBlockade(d_player1, "Canada");
        d_cardBlockadeOrder2 = new CardBlockade(d_player1, "India");
    }

    /**
     * Valid order check.
     * This test case checks the validity of the CardBlockade order.
     * It verifies if the CardBlockade order is valid for a country owned by the player (Canada) and invalid for a country not owned by the player (India).
     */
    @Test
    public void validOrderCheck() {
        boolean l_trueResult = d_cardBlockadeOrder1.valid(d_currentState);
        assertTrue(l_trueResult);

        boolean l_falseResult = d_cardBlockadeOrder2.valid(d_currentState);
        assertFalse(l_falseResult);
    }

    /**
     * Execute.
     * This test case checks the execution of the CardBlockade order.
     * It verifies that the number of armies in the blocked country is correctly updated after the CardBlockade order is executed.
     */
    @Test
    public void execute() {
        d_cardBlockadeOrder1.execute(d_currentState);
        Country l_blockedCountry = d_currentState.getD_map().getCountryByName("Canada");

        assertEquals("15", l_blockedCountry.getD_armies().toString());
    }
}
