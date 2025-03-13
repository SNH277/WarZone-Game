package Models;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Card negotiate test.
 */
public class CardNegotiateTest {
    private Map d_map;
    private Player d_player1;
    private Player d_player2;
    private CardNegotiate d_cardNegotiate;
    private CurrentState d_currentState;

    /**
     * Sets up test environment.
     */
    @Before
    public void setUp() {
        d_currentState = new CurrentState();
        d_map = new Map(); // Ensure map is initialized
        d_player1 = new Player("player1");
        d_player2 = new Player("player2");

        List<Country> l_countryList1 = new ArrayList<>();
        List<Country> l_countryList2 = new ArrayList<>();
        List<Country> l_mapCountries = new ArrayList<>();

        // Player 1's country
        Country l_country1 = new Country(1, "India", 1);
        l_country1.setD_armies(10);
        l_countryList1.add(l_country1);
        l_mapCountries.add(l_country1);
        d_player1.setD_currentCountries(l_countryList1);

        // Player 2's country
        Country l_country2 = new Country(2, "Morocco", 2);
        l_country2.setD_armies(5);
        l_countryList2.add(l_country2);
        l_mapCountries.add(l_country2);
        d_player2.setD_currentCountries(l_countryList2);

        // Set neighboring relationships
        l_country2.addCountryNeighbour(1);
        l_country1.addCountryNeighbour(2);

        // Initialize the map
        d_map.setD_mapCountries(l_mapCountries);
        d_currentState.setD_map(d_map);

        // Add players to the game state
        List<Player> l_playerList = new ArrayList<>();
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_currentState.setD_players(l_playerList);

        // Initialize the negotiation card
        d_cardNegotiate = new CardNegotiate(d_player1, "player2");
    }

    /**
     * Tests execution of the negotiation card.
     */
    @Test
    public void execute() {
        d_cardNegotiate.execute(d_currentState);

        // Ensure negotiation is recorded
        assertEquals(d_player2.d_name, d_player1.d_negotiatePlayers.get(0).d_name);

        // Try an attack order after negotiation
        Orders advanceOrder = new Advance("India", "Morocco", 5, d_player1);
        assertFalse("Player1 should not be able to attack Player2 after negotiation", advanceOrder.valid(d_currentState));
    }

    /**
     * Tests that after negotiation, both players cannot attack each other.
     */
    @Test
    public void testMutualNonAttack() {
        d_cardNegotiate.execute(d_currentState);

        // Player1 cannot attack Player2
        Orders attackOrder1 = new Advance("India", "Morocco", 5, d_player1);
        assertFalse(attackOrder1.valid(d_currentState));

        // Player2 cannot attack Player1
        Orders attackOrder2 = new Advance("Morocco", "India", 3, d_player2);
        assertFalse(attackOrder2.valid(d_currentState));
    }
}
