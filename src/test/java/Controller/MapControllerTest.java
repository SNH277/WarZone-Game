package Controller;

import Models.CurrentState;
import Models.Map;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Test class for MapController.
 */
public class MapControllerTest {

    private Map d_map;
    private MapController d_mapController;
    private CurrentState d_currentState;
    private String d_mapName;
    private PlayerController d_playerController;

    /**
     * Setup before each test.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_playerController = new PlayerController();
        d_mapName = "testSaveMap.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
        d_currentState.setD_map(d_map);
    }

    /**
     * Test loading a map.
     */
    @Test
    public void loadMap() {
        d_mapController.loadMap(d_currentState, "Invalid.map");
        assertNull("Map should be null for an invalid file", d_currentState.getD_map());

        d_mapController.loadMap(d_currentState, d_mapName);
        assertNotNull("Map should be loaded successfully", d_currentState.getD_map());
        assertFalse("Loaded map should not be empty", d_currentState.getD_map().toString().isEmpty());
    }

    /**
     * Test adding and removing a country.
     */
    @Test
    public void editCountry() {
        d_mapController.editCountry(d_currentState, "add", "Paris Asia");
        assertNotNull("Paris should be added to the map", d_map.getCountryByName("Paris"));

        d_mapController.editCountry(d_currentState, "remove", "Paris");
        assertNull("Paris should be removed from the map", d_map.getCountryByName("Paris"));
    }

    /**
     * Test adding and removing a continent.
     */
    @Test
    public void editContinent() {
        d_mapController.editContinent(d_currentState, "add", "Europe 30");
        assertNotNull("Europe should be added to the map", d_map.getContinentByName("Europe"));

        d_mapController.editContinent(d_currentState, "remove", "Europe");
        assertNull("Europe should be removed from the map", d_map.getContinentByName("Europe"));
    }

    /**
     * Test adding and removing neighboring countries.
     */
    @Test
    public void editNeighbourCountry() {
        d_mapController.editNeighbourCountry(d_currentState, "add", "1 3");
        assertTrue("India should have Morocco as a neighbor",
                d_map.getCountryByName("India").getD_neighbouringCountriesId().contains(3));

        assertTrue("Morocco should have India as a neighbor",
                d_map.getCountryByName("Morocco").getD_neighbouringCountriesId().contains(1));

        d_mapController.editNeighbourCountry(d_currentState, "remove", "1 3");
        assertFalse("India should no longer have Morocco as a neighbor",
                d_map.getCountryByName("India").getD_neighbouringCountriesId().contains(3));

        assertFalse("Morocco should no longer have India as a neighbor",
                d_map.getCountryByName("Morocco").getD_neighbouringCountriesId().contains(1));
    }

    /**
     * Test saving the map after modifications.
     */
    @Test
    public void saveMap() {
        InputStream originalSystemIn = System.in;
        try {
            assertNull("Paris should not exist initially", d_map.getCountryByName("Paris"));

            d_mapController.editCountry(d_currentState, "add", "Paris Asia");
            d_mapController.editNeighbourCountry(d_currentState, "add", "5 2");

            assertNotNull("Paris should be added to the map", d_map.getCountryByName("Paris"));

            ByteArrayInputStream in = new ByteArrayInputStream("2\n".getBytes());
            System.setIn(in);
            d_mapController.saveMap(d_currentState, d_mapName);

            d_map = d_mapController.loadMap(d_currentState, d_mapName);
            assertNotNull("Paris should still exist after saving and loading", d_map.getCountryByName("Paris"));

            d_mapController.editCountry(d_currentState, "remove", "Paris");
            in = new ByteArrayInputStream("2\n".getBytes());
            System.setIn(in);
            d_mapController.saveMap(d_currentState, d_mapName);

            d_map = d_mapController.loadMap(d_currentState, d_mapName);
            assertNull("Paris should be removed after saving", d_map.getCountryByName("Paris"));

        } finally {
            System.setIn(originalSystemIn);
        }
    }
}
