package Models; // Ensure package consistency

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

/**
 * The type Card airlift test.
 */
public class CardAirliftTest {
    private CurrentState d_currentState;
    private CardAirlift d_cardAirliftOrder;
    private Player d_player1;

    /**
     * Sets up test data.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_player1 = new Player("player1");

        List<Country> l_countryList1 = new ArrayList<>();

        // Source country with 10 armies
        Country l_sourceCountry = new Country(1, "India", 1);
        l_sourceCountry.setD_armies(10);
        l_countryList1.add(l_sourceCountry);

        // Neighboring country with 15 armies
        Country l_neighbourCountry = new Country(2, "China", 1);
        l_neighbourCountry.setD_armies(15);
        l_neighbourCountry.addCountryNeighbour(1);
        l_sourceCountry.addCountryNeighbour(2);
        l_countryList1.add(l_neighbourCountry);

        // Non-neighbor target country with 5 armies
        Country l_notNeighbourTargetCountry = new Country(3, "Morocco", 1);
        l_notNeighbourTargetCountry.setD_armies(5);
        l_countryList1.add(l_notNeighbourTargetCountry);

        // Set up the map and player state
        Map l_map = new Map();
        l_map.setD_mapCountries(l_countryList1);
        d_player1.setD_currentCountries(l_countryList1);
        d_currentState.setD_map(l_map);

        // Create the airlift order
        d_cardAirliftOrder = new CardAirlift(d_player1, "India", "Morocco", 2);
    }

    /**
     * Test card airlift execution.
     */
    @Test
    public void testCardAirliftExecution() {
        d_cardAirliftOrder.execute(d_currentState);
        Country l_updatedCountry = d_currentState.getD_map().getCountryByName("Morocco");

        // Use Integer.valueOf instead of string conversion
        assertEquals(Integer.valueOf(7), l_updatedCountry.getD_armies());
    }

    /**
     * Test valid order check.
     */
    @Test
    public void testValidOrderCheck() {
        Country l_sourceCountry = d_currentState.getD_map().getCountryByName("Spain");
        assertNull(l_sourceCountry);

        Country l_targetCountry = d_currentState.getD_map().getCountryByName("Morocco");
        assertNotNull(l_targetCountry);

        // Check that an invalid order is correctly rejected
        d_cardAirliftOrder = new CardAirlift(d_player1, "India", "Morocco", 12);
        assertFalse(d_cardAirliftOrder.isValid(d_currentState));
    }
}
