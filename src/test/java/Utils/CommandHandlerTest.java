package Utils;

import Controller.MapController;
import Models.CurrentState;
import Models.Map;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * The type Command handler test.
 */
public class CommandHandlerTest {

    private CurrentState d_currentState;
    private MapController d_mapController;
    private CommandHandler d_commandHandler;
    private String d_mapName;
    private Map d_map;

    /**
     * Setup before each test.
     */
    @Before
    public void setup() {
        d_currentState = new CurrentState();
        d_mapController = new MapController();
        d_mapName = "test.map";
        d_map = d_mapController.loadMap(d_currentState, d_mapName);
    }

    /**
     * Test extracting main command from input.
     */
    @Test
    public void testGetMainCommand() {
        d_commandHandler = new CommandHandler("loadmap test.map");
        assertEquals("Main command extraction failed!", "loadmap", d_commandHandler.getMainCommand());

        d_commandHandler = new CommandHandler("gameplayer -add Player1");
        assertEquals("Main command extraction failed!", "gameplayer", d_commandHandler.getMainCommand());

        d_commandHandler = new CommandHandler("assigncountries");
        assertEquals("Main command extraction failed!", "assigncountries", d_commandHandler.getMainCommand());
    }

    /**
     * Test parsing list of operations from a command.
     */
    @Test
    public void testGetListOfOperations() {
        d_commandHandler = new CommandHandler("loadmap test.map");
        List<Map<String, String>> operations = d_commandHandler.getListOfOperations();

        assertNotNull("Operations list should not be null!", operations);
        assertEquals("Arguments parsing failed!", "test.map", operations.get(0).get("Arguments"));

        d_commandHandler = new CommandHandler("gameplayer -add Player1");
        operations = d_commandHandler.getListOfOperations();

        assertEquals("Operation extraction failed!", "add", operations.get(0).get("Operation"));
        assertEquals("Arguments extraction failed!", "Player1", operations.get(0).get("Arguments"));
    }

    /**
     * Test checking if a required key exists.
     */
    @Test
    public void testCheckRequiredKey() {
        d_commandHandler = new CommandHandler("editcountry -add India Africa");
        List<Map<String, String>> operations = d_commandHandler.getListOfOperations();
        Map<String, String> operation = operations.get(0);

        assertTrue("Key 'Arguments' should exist and be non-empty!", 
                   d_commandHandler.checkRequiredKey("Arguments", operation));
        assertTrue("Key 'Operation' should exist and be non-empty!", 
                   d_commandHandler.checkRequiredKey("Operation", operation));

        operation.remove("Operation");
        assertFalse("Key 'Operation' should return false after removal!", 
                    d_commandHandler.checkRequiredKey("Operation", operation));

        operation.put("Arguments", ""); // Set empty value
        assertFalse("Key 'Arguments' should return false when empty!", 
                    d_commandHandler.checkRequiredKey("Arguments", operation));
    }
}
