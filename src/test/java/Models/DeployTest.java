package Models;

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * The type Deploy test.
 */
public class DeployTest {
    private Map d_map;
    private Player d_player;
    private CurrentState d_currentState;
    private MapController d_mapController;

    /**
     * Sets up.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {
        d_mapController = new MapController();
        d_currentState = new CurrentState();
        d_map = d_mapController.loadMap(d_currentState, "test.map");

        // Ensure the map is loaded successfully
        assertNotNull("Map should be loaded successfully", d_map);

        d_player = new Player("Player1");
        List<Country> l_countryList = d_map.getD_mapCountries();

        // Ensure the map contains countries before assigning to the player
        assertFalse("Map should have at least one country", l_countryList.isEmpty());

        d_player.setD_currentCountries(l_countryList);
    }

    /**
     * Execute.
     */
    @Test
    public void execute() {
        Country l_country = d_map.getCountryByName("India");

        // Ensure "India" exists in the map
        assertNotNull("Country 'India' should exist in the map", l_country);

        int l_initialArmies = l_country.getD_armies();
        int l_deployArmies = 5;

        Deploy l_deploy = new Deploy(d_player, "India", l_deployArmies);
        l_deploy.execute(d_currentState);

        int l_expectedArmies = l_initialArmies + l_deployArmies;
        assertEquals("Army count should increase correctly", l_expectedArmies, l_country.getD_armies());
    }
}
