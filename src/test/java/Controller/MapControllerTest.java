package Controller;

import Model.CurrentState;
import Model.Map;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * The type Map controller test.
 */
public class MapControllerTest {

    /**
     * The D map.
     */
    Map d_map;
    /**
     * The D map controller.
     */
    MapController d_mapController;
    /**
     * The D current state.
     */
    CurrentState d_currentState;
    /**
     * The D map name.
     */
    String d_mapName;
    /**
     * The D player controller.
     */
    PlayerController d_playerController;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_playerController = new PlayerController();
        d_mapName = "test.map";
    }

    /**
     * Load map.
     */
    @Test
    public void loadMap() {
        d_mapController.loadMap(d_currentState, "notExist.map");
        assertNull(d_currentState.getD_map());
        d_mapController.loadMap(d_currentState, "test.map");
        assertFalse(d_currentState.getD_map().toString().isEmpty());
    }

    /**
     * Edit country.
     */
    @Test
    public void editCountry() {
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_currentState.setD_map(d_map);
        d_mapController.editCountry(d_currentState, "add", "Mexico NorthAmerica");
        assertEquals("Mexico", d_map.getCountryByName("Mexico").getD_countryName());
        d_mapController.editCountry(d_currentState, "remove", "Mexico");
        assertNull(d_map.getCountryByName("Mexico"));
    }

    /**
     * Edit continent.
     */
    @Test
    public void editContinent() {
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_currentState.setD_map(d_map);
        d_mapController.editContinent(d_currentState, "add", "Europe 30");
        assertEquals("Europe", d_map.getContinentByName("Europe").getD_continentName());
        d_mapController.editContinent(d_currentState, "remove", "Europe");
        assertNull(d_map.getContinentByName("Europe"));
    }

    /**
     * Edit neighbour country.
     */
    @Test
    public void editNeighbourCountry() {
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_currentState.setD_map(d_map);
        d_mapController.editNeighbourCountry(d_currentState, "add", "3 5");
        assertEquals("[2, 4, 5]", d_map.getCountryByName("UK").getD_neighbouringCountriesId().toString());
        assertEquals("[2, 4, 6, 3]", d_map.getCountryByName("France").getD_neighbouringCountriesId().toString());

        d_mapController.editNeighbourCountry(d_currentState, "remove", "3 5");
        assertEquals("[2, 4]", d_map.getCountryByName("UK").getD_neighbouringCountriesId().toString());
        assertEquals("[2, 4, 6]", d_map.getCountryByName("France").getD_neighbouringCountriesId().toString());
    }

//    /**
//     * Save map.
//     */
//    @Test
//    public void saveMap() {
//        d_map = d_mapController.loadMap(d_currentState, d_mapName);
//        d_currentState.setD_map(d_map);
//        assertNull(d_map.getCountryByName("Mexico"));
//
//        d_mapController.editCountry(d_currentState, "add", "Mexico NorthAmerica");
//        d_mapController.editNeighbourCountry(d_currentState, "add", "9 2");
//        assertEquals("Mexico", d_map.getCountryByName("Mexico").getD_countryName());
//
//        d_currentState.setD_map(d_map);
//        d_mapController.saveMap(d_currentState, d_mapName);
//        d_map = d_mapController.loadMap(d_currentState, d_mapName);
//        assertEquals("Mexico", d_map.getCountryByName("Mexico").getD_countryName());
//        d_mapController.editCountry(d_currentState, "remove", "Mexico");
//        d_mapController.saveMap(d_currentState, d_mapName);
//    }
}