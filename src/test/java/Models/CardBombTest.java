package Models;

import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Card bomb test.
 * This class contains unit tests to verify the functionality of the CardBomb order in the game.
 * It tests the validity of the CardBomb order and its execution when applied to a country.
 @author Taksh Rana
 */
public class CardBombTest {

    /**
     * The D current state.
     * The current state of the game, including the players, map, and other game-related information.
     */
    CurrentState d_currentState;

    /**
     * The D card bomb order.
     * The instance of the CardBomb order for the valid case.
     */
    CardBomb d_cardBombOrder;

    /**
     * The D card bomb order 1.
     * The instance of the CardBomb order for the invalid case.
     */
    Card d_cardBombOrder1;

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
     * The L map.
     * The map containing all countries and their relations (adjacency and armies).
     */
    Map l_map;

    /**
     * Sets up the game environment for testing.
     * This method initializes the game state, players, countries, and map, and assigns the CardBomb orders.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");

        List<Country> l_countryList1 = new ArrayList<>();
        List<Country> l_countryList2 = new ArrayList<>();
        List<Country> l_mapCountries = new ArrayList<>();

        Country l_country1 = new Country(1,"USA",1);
        l_country1.setD_armies(10);
        l_countryList1.add(l_country1);
        l_mapCountries.add(l_country1);

        Country l_country2 = new Country(2,"Canada",1);
        l_country2.setD_armies(5);
        l_countryList1.add(l_country2);
        l_mapCountries.add(l_country2);

        l_country2.addCountryNeighbour(1);
        l_country1.addCountryNeighbour(2);

        Country l_country3 = new Country(3,"Mexico",2);
        l_country3.setD_armies(5);
        l_countryList2.add(l_country3);
        l_mapCountries.add(l_country3);

        Country l_country4 = new Country(4,"India",2);
        l_country4.setD_armies(15);
        l_countryList2.add(l_country4);
        l_mapCountries.add(l_country4);

        l_country3.addCountryNeighbour(4);
        l_country4.addCountryNeighbour(3);
        l_country4.addCountryNeighbour(2);
        l_country2.addCountryNeighbour(4);

        l_map = new Map();
        l_map.setD_mapCountries(l_mapCountries);
        d_player1.setD_currentCountries(l_countryList1);
        d_player2.setD_currentCountries(l_countryList2);
        d_currentState.setD_map(l_map);

        d_cardBombOrder = new CardBomb(d_player1, "India");
        d_cardBombOrder1 = new CardBomb(d_player1,"USA");
    }

    /**
     * Execute card bomb.
     * This test case checks the execution of the CardBomb order.
     * It verifies that the number of armies in the bombed country is correctly updated after the CardBomb order is executed.
     */
    @Test
    public void executeCardBomb() {
        d_cardBombOrder.execute(d_currentState);
        Country l_bombedCountry = d_currentState.getD_map().getCountryByName("India");

        assertEquals("7", l_bombedCountry.getD_armies().toString());
    }

    /**
     * Valid order check card bomb.
     * This test case checks the validity of the CardBomb order.
     * It verifies if the CardBomb order is valid for a country owned by the enemy (India) and invalid for a country owned by the player (USA).
     */
    @Test
    public void validOrderCheckCardBomb() {
        assertFalse(d_cardBombOrder1.valid(d_currentState));
        assertTrue(d_cardBombOrder.valid(d_currentState));
    }
}
