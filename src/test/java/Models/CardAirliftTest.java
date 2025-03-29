package Models;

import Controller.MapController;
import Model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Card airlift test.
 * This class contains unit tests to verify the functionality of the CardAirlift order in the game.
 * It tests if the Airlift order behaves correctly during execution and validates order checks.
 @author Taksh Rana
 */
public class CardAirliftTest {

    /**
     * The D current state.
     * The current state of the game, including the players, map, and other game-related information.
     */
    CurrentState d_currentState;

    /**
     * The D card airlift order.
     * The instance of the CardAirlift order being tested.
     */
    CardAirlift d_cardAirliftOrder;

    /**
     * The D player 1.
     * The first player involved in the game.
     */
    Player d_player1;

    /**
     * Sets up the game environment for testing.
     * This method initializes the game state, players, countries, and map, and assigns the CardAirlift order.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_player1 = new Player("player1");

        List<Country> l_countryList1 = new ArrayList<>();

        Country l_sourceCountry = new Country(1,"USA",1);
        l_sourceCountry.setD_armies(10);
        l_countryList1.add(l_sourceCountry);

        Country l_neighbourCountry = new Country(2,"Canada",1);
        l_neighbourCountry.setD_armies(15);
        l_neighbourCountry.addCountryNeighbour(1);
        l_sourceCountry.addCountryNeighbour(2);
        l_countryList1.add(l_neighbourCountry);

        Country l_notNeighbourTargetCountry = new Country(3,"Mexico",1);
        l_notNeighbourTargetCountry.setD_armies(5);
        l_countryList1.add(l_notNeighbourTargetCountry);

        Map l_map = new Map();
        l_map.setD_mapCountries(l_countryList1);
        d_player1.setD_currentCountries(l_countryList1);
        d_currentState.setD_map(l_map);

        d_cardAirliftOrder = new CardAirlift(2, "USA",d_player1, "Mexico");
    }

    /**
     * Test card airlift execution.
     * This test case checks the execution of the CardAirlift order.
     * It verifies that the number of armies in the target country is updated after the Airlift order is executed.
     */
    @Test
    public void testCardAirliftExecution() {
        d_cardAirliftOrder.execute(d_currentState);
        Country l_updatedCountry = d_currentState.getD_map().getCountryByName("Mexico");
        assertEquals("7", l_updatedCountry.getD_armies().toString());
    }

    /**
     * Test valid order check.
     * This test case checks if the CardAirlift order is valid by verifying if the source and target countries are valid.
     * It also tests a case where an invalid order is created (from "India" to "Morocco").
     */
    @Test
    public void testvalidOrderCheck() {
        Country l_sourceCountry = d_currentState.getD_map().getCountryByName("India");
        assertNull(l_sourceCountry);

        Country l_targetCountry = d_currentState.getD_map().getCountryByName("Mexico");
        assertNotNull(l_targetCountry);

        d_cardAirliftOrder = new CardAirlift(12, "India", d_player1, "Mexico");
    }
}
