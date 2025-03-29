package Models;

import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Card negotiate test.
 * This class contains unit tests to verify the functionality of the CardNegotiate order in the game.
 * It tests the execution of the CardNegotiate order, ensuring that players can negotiate and that certain orders become invalid.
 */
public class CardNegotiateTest {

    /**
     * The D map.
     * The map of countries in the game, including their armies and adjacencies.
     */
    Map d_map;

    /**
     * The D player 1.
     * The first player involved in the game.
     */
    Player d_player1;

    /**
     * The D player 2.
     * The second player involved in the game.
     */
    Player d_player2;

    /**
     * The D card negotiate.
     * The instance of the CardNegotiate order for initiating negotiations between players.
     */
    CardNegotiate d_cardNegotiate;

    /**
     * The D current state.
     * The current state of the game, including players, map, and other game-related details.
     */
    CurrentState d_currentState;

    /**
     * Sets up the game environment for testing.
     * This method initializes the game state, players, countries, and map, and assigns the CardNegotiate order.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        d_currentState = new CurrentState();
        d_player1 = new Player("player1");
        d_player2 = new Player("player2");

        List<Country> l_countryList1 = new ArrayList<>();
        List<Country> l_countryList2 = new ArrayList<>();
        List<Country> l_mapCountries = new ArrayList<>();

        Country l_country1 = new Country(1,"USA",1);
        l_country1.setD_armies(10);
        l_countryList1.add(l_country1);
        l_mapCountries.add(l_country1);
        d_player1.setD_currentCountries(l_countryList1);

        Country l_country2 = new Country(2,"Canada",1);
        l_country2.setD_armies(5);
        l_countryList2.add(l_country2);
        l_mapCountries.add(l_country2);
        d_player2.setD_currentCountries(l_countryList2);

        l_country2.addCountryNeighbour(1);
        l_country1.addCountryNeighbour(2);

        d_cardNegotiate = new CardNegotiate(d_player1, "player2");
        List<Player> l_playerList = new ArrayList<>();
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_currentState.setD_players(l_playerList);
    }

    /**
     * Execute card negotiate.
     * This test case checks the execution of the CardNegotiate order.
     * It ensures that after a negotiation is made, an advance order becomes invalid.
     * Additionally, it verifies that the negotiated player's name is correctly updated.
     */
    @Test
    public void execute() {
        d_cardNegotiate.execute(d_currentState);
        Orders advanceOrder = new Advance("USA","Canada",5,d_player1);
        assertFalse(advanceOrder.valid(d_currentState));
        assertEquals(d_player2.getD_playerName(), d_player1.getD_negotiatePlayer().get(0).getD_playerName());
    }
}
