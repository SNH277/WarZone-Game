package Controller;

import Models.Country;
import Models.CurrentState;
import Models.Map;
import Models.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for PlayerController.
 */
public class PlayerControllerTest {
    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private Player d_player1;
    private Player d_player2;
    private String d_mapName;
    private PlayerController d_playerController;
    private List<Player> l_playerList;

    /**
     * Setup test environment.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_playerController = new PlayerController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_player1 = new Player("Player1");
        d_player2 = new Player("Player2");
        l_playerList = new ArrayList<>();
    }

    @Test
    public void testAssignCountries() {
        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_currentState.setD_players(l_playerList);
        d_playerController.assignCountries(d_currentState);
        assertEquals(2, d_player1.getD_currentCountries().size());
        assertEquals(2, d_player2.getD_currentCountries().size());
    }

    @Test
    public void testAssignContinentToPlayers() {
        l_playerList.add(d_player1);
        d_player1.setD_currentCountries(d_map.getD_mapCountries());
        d_currentState.setD_players(l_playerList);
        d_playerController.assignContinentToPlayers(l_playerList, d_map.getD_mapContinents());
        assertEquals(2, d_player1.getD_currentContinents().size());

        d_player1.getD_currentContinents().clear();
        d_player1.getD_currentCountries().remove(0);
        d_playerController.assignContinentToPlayers(l_playerList, d_map.getD_mapContinents());
        assertEquals(1, d_player1.getD_currentContinents().size());
    }

    @Test
    public void testGetNoOfArmies() {
        List<Country> l_countryList = new ArrayList<>();
        l_countryList.add(d_map.getCountryByName("India"));
        l_countryList.add(d_map.getCountryByName("China"));
        d_player1.setD_currentCountries(l_countryList);

        l_countryList = new ArrayList<>();
        l_countryList.add(d_map.getCountryByName("Morocco"));
        l_countryList.add(d_map.getCountryByName("Nigeria"));
        d_player2.setD_currentCountries(l_countryList);

        l_playerList.add(d_player1);
        l_playerList.add(d_player2);
        d_currentState.setD_players(l_playerList);
        d_playerController.assignContinentToPlayers(l_playerList, d_map.getD_mapContinents());
        d_playerController.assignArmies(d_currentState);

        assertEquals(18, d_playerController.getNoOfArmies(d_player1));
        assertEquals(13, d_playerController.getNoOfArmies(d_player2));
    }

    @Test
    public void testCreateDeployOrder() {
        l_playerList.add(d_player1);
        d_currentState.setD_players(l_playerList);
        d_player1.setD_currentCountries(d_map.getD_mapCountries());
        d_playerController.assignCountries(d_currentState);
        d_playerController.assignArmies(d_currentState);
        d_player1.createDeployOrder("deploy India 10");
        d_player1.createDeployOrder("deploy China 8");
        assertEquals(10, d_player1.getD_unallocatedArmies().intValue());
        assertEquals(2, d_player1.getD_orders().size());
    }

    @Test
    public void testValidateInvalidDeployOrder() {
        l_playerList.add(d_player1);
        d_currentState.setD_players(l_playerList);
        d_player1.setD_currentCountries(d_map.getD_mapCountries());
        d_playerController.assignCountries(d_currentState);
        d_playerController.assignArmies(d_currentState);
        d_player1.createDeployOrder("deploy India 10");
        assertEquals(18, d_player1.getD_unallocatedArmies().intValue());
        d_player1.createDeployOrder("deploy China 20");
        assertEquals(18, d_player1.getD_unallocatedArmies().intValue());
    }
}
