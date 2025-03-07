package Models; // Ensure package consistency

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Card blockade test.
 */
public class CardBlockadeTest {
    private CurrentState d_currentState;
    private CardBlockade d_cardBlockadeOrder1;
    private CardBlockade d_cardBlockadeOrder2;
    private Player d_player1;
    private Player d_neutralPlayer;

    /**
     * Sets up the test environment.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_player1 = new Player("player1");
        d_neutralPlayer = new Player("Neutral");

        List<Player> l_playersList = new ArrayList<>();
        l_playersList.add(d_player1);
        l_playersList.add(d_neutralPlayer);

        // Create and assign countries
        List<Country> l_countryList1 = new ArrayList<>();
        Country l_countryToBeBlocked = new Country(3, "Nigeria", 1);
        l_countryToBeBlocked.setD_armies(5);
        l_countryList1.add(l_countryToBeBlocked);

        Country l_otherCountry = new Country(1, "India", 1);
        l_countryList1.add(l_otherCountry);

        // Assign map
        Map l_map = new Map();
        l_map.setD_mapCountries(l_countryList1);
        d_player1.setD_currentCountries(l_countryList1);
        d_currentState.setD_players(l_playersList);
        d_currentState.setD_map(l_map);

        // Initialize blockade orders
        d_cardBlockadeOrder1 = new CardBlockade(d_player1, "Nigeria");
        d_cardBlockadeOrder2 = new CardBlockade(d_player1, "Brazil"); // Invalid order
    }

    /**
     * Test valid order check.
     */
    @Test
    public void validOrderCheck() {
        assertTrue(d_cardBlockadeOrder1.valid(d_currentState));  // Should be valid
        assertFalse(d_cardBlockadeOrder2.valid(d_currentState)); // Should be invalid (Brazil doesn't exist)
    }

    /**
     * Test execution of blockade order.
     */
    @Test
    public void execute() {
        d_cardBlockadeOrder1.execute(d_currentState);
        Country l_blockedCountry = d_currentState.getD_map().getCountryByName("Nigeria");

        // Ensure army count is tripled
        assertEquals(Integer.valueOf(15), l_blockedCountry.getD_armies());

        // Ensure ownership transferred to neutral player
        assertTrue(d_neutralPlayer.getD_currentCountries().contains(l_blockedCountry));
    }
}
