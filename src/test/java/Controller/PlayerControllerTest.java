package Controller;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import Model.Orders;

import Model.Country;
import Model.CurrentState;
import Model.Map;
import Model.Player;

/**
 * The type Player controller test.
 */
public class PlayerControllerTest {

    // Instance variables for map and players
    private Map d_map;
    private CurrentState d_currentState;
    private Player d_player1;
    private Player d_player2;
    private PlayerController d_playerController;
    private final List<Player> l_playerList = new ArrayList<>();

    /**
     * Setup for the tests.
     */
    @Before
    public void setup() {
        // Initialize the required objects before each test
        d_currentState = new CurrentState();
        MapController d_mapController = new MapController();
        d_playerController = new PlayerController();
        String d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        l_playerList.clear();
    }

    /**
     * Test case for assigning countries to players.
     */
    @Test
    public void assignCountry() {
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_currentState.setD_players(l_playerList);
        
        // Execute country assignment logic
        d_playerController.assignCountry(d_currentState);

        // Assert that each player has at least one country
        assertEquals(4, d_player1.getD_currentCountries().size());
        assertEquals(4, d_player2.getD_currentCountries().size());
    }

    /**
     * Test case for assigning continents to players based on their countries.
     */
    @Test
    public void updatePlayerContinentOwnership() {
        l_playerList.add(d_player1);
        d_player1.setD_currentCountries(d_map.getD_mapCountries());
        d_currentState.setD_players(l_playerList);
        
        // Assign continents to players
        d_playerController.updatePlayerContinentOwnership(l_playerList, d_map.getD_mapContinents());
        
        // Assert that the continents are assigned correctly
        assertEquals(3, d_player1.getD_currentContinents().size());

        // Test removing a country and checking continent assignment again
        d_player1.getD_currentContinents().clear();
        d_player1.getD_currentCountries().remove(0);
        d_playerController.updatePlayerContinentOwnership(l_playerList, d_map.getD_mapContinents());
        assertEquals(2, d_player1.getD_currentContinents().size());
    }

    /**
     * Test case for assigning random countries to players.
     */
    @Test
    public void randomCountryDistribution() {
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        
        // Assign random countries to players
        d_playerController.randomCountryDistribution(l_playerList, d_map.getD_mapCountries(), 2);
        d_playerController.randomCountryDistribution(l_playerList, d_map.getD_mapCountries(), 2);

        // Ensure each player has the correct number of countries
        assertEquals(8, d_player1.getD_currentCountries().size());
        assertEquals(8, d_player2.getD_currentCountries().size());

        // Remove one country and verify again
        List<Country> l_countryList = new ArrayList<>(d_player1.getD_currentCountries());
        l_countryList.removeFirst();
        d_player1.setD_currentCountries(l_countryList);
        assertEquals(7, d_player1.getD_currentCountries().size());
    }

    /**
     * Test case for checking the number of armies assigned to players.
     */
    @Test
    public void getNumberOfArmies() {
        List<Country> l_countryList = new ArrayList<>();
        l_countryList.add(d_map.getCountryByName("USA"));
        l_countryList.add(d_map.getCountryByName("Canada"));
        d_player1.setD_currentCountries(l_countryList);

        l_countryList = new ArrayList<>();
        l_countryList.add(d_map.getCountryByName("UK"));
        l_countryList.add(d_map.getCountryByName("Chile"));
        d_player2.setD_currentCountries(l_countryList);

        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_currentState.setD_players(l_playerList);

        // Assign continents and armies to players
        d_playerController.updatePlayerContinentOwnership(l_playerList, d_map.getD_mapContinents());
        d_playerController.assignArmies(d_currentState);

        // Assert the number of armies
        assertEquals(13, d_playerController.getNumberOfArmies(d_player1));
        assertEquals(3, d_playerController.getNumberOfArmies(d_player2));

        // Deploy armies to countries and check the remaining unallocated armies
        d_playerController.createDeployOrder("deploy USA 10", d_player1);
        assertEquals(3, d_player1.getD_unallocatedArmies().intValue());

        d_playerController.createDeployOrder("deploy UK 8", d_player2);
        assertEquals(3, d_player2.getD_unallocatedArmies().intValue());

        d_playerController.createDeployOrder("deploy Canada 8", d_player1);
        assertEquals(3, d_player1.getD_unallocatedArmies().intValue());

        d_playerController.createDeployOrder("deploy Chile 5", d_player2);
        assertEquals(3, d_player2.getD_unallocatedArmies().intValue());
    }

    /**
     * Test case for creating deployment orders.
     */
    @Test
    public void createDeployOrder() {
        l_playerList.add(d_player1);
        d_currentState.setD_players(l_playerList);
        d_player1.setD_currentCountries(d_map.getD_mapCountries());

        d_playerController.assignCountry(d_currentState);
        d_playerController.assignArmies(d_currentState);

        // Create deploy orders and verify the results
        d_playerController.createDeployOrder("deploy USA 10", d_player1);
        d_playerController.createDeployOrder("deploy Canada 8", d_player1);

//        assertEquals(5, d_player1.getD_unallocatedArmies().intValue());
//        assertEquals(2, d_player1.getD_orders().size());
    }

    /**
     * Test case for validating invalid deploy orders.
     */
    @Test
    public void validateInvalidDeployOrder() {
        l_playerList.add(d_player1);
        d_currentState.setD_players(l_playerList);
        d_player1.setD_currentCountries(d_map.getD_mapCountries());

        d_playerController.assignCountry(d_currentState);
        d_playerController.assignArmies(d_currentState);

        d_playerController.createDeployOrder("deploy USA 10", d_player1);
//        assertEquals(5, d_player1.getD_unallocatedArmies().intValue());

        // Test invalid deploy order: more armies than available
        d_playerController.createDeployOrder("deploy Canada 20", d_player1);
//        assertEquals(3, d_player1.getD_unallocatedArmies().intValue());
    }
}
