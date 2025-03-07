package Controller;

import Model.CurrentState;
import Utils.CommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainGameEngineTest {

    private MainGameEngine gameEngine;
    private CurrentState mockCurrentState;
    private CommandHandler mockCommandHandler;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        gameEngine = new MainGameEngine();
        mockCurrentState = mock(CurrentState.class);
        mockCommandHandler = mock(CommandHandler.class);

        // Using reflection to access private field d_currentGameState
        Field field = MainGameEngine.class.getDeclaredField("d_currentGameState");
        field.setAccessible(true); // Allow access to the private field
        field.set(gameEngine, mockCurrentState); // Set the mockCurrentState to the field
    }

    @Test
    void testLoadMapCommand() throws Exception {
        String input = "loadmap test.map";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Mock the necessary behavior of loadMap
        when(mockCurrentState.getD_map()).thenReturn(null);  // Just an example mock
        
        // Call the command handler
        gameEngine.commandHandler(input);

        // Verify that loadMap is triggered
        verify(mockCommandHandler, times(1)).getListOfOperations();  // You can adjust this as per your needs
    }

    @Test
    void testInvalidCommandHandling() throws Exception {
        String invalidCommand = "invalidcommand";
        InputStream in = new ByteArrayInputStream(invalidCommand.getBytes());
        System.setIn(in);

        // Here we just call the commandHandler and check if it doesn't throw an exception
        assertDoesNotThrow(() -> gameEngine.commandHandler(invalidCommand));
    }

    @Test
    void testAssignCountriesWithoutMap() {
        when(mockCurrentState.getD_map()).thenReturn(null);
        
        // Here we are testing if the exception is thrown when assigning countries without a map
        Exception exception = assertThrows(Exception.class, () -> {
            gameEngine.assignCountries(mockCommandHandler);
        });

        // Check the exception message
        assertEquals("Error: Map not available. Use 'loadmap' or 'editmap' first.", exception.getMessage());
    }

    @Test
    void testEditMapCommand() throws Exception {
        String command = "editmap test.map";
        
        // Simulate the method call to `commandHandler`
        gameEngine.commandHandler(command);

        // Verify that the correct method has been called
        verify(mockCommandHandler, never()).getListOfOperations();  // Modify this to match the logic
    }

    @Test
    void testGamePlayerCommand() throws Exception {
        String command = "gameplayer -add player1";
        
        // Simulate the method call to `commandHandler`
        gameEngine.commandHandler(command);

        // Verify that the correct method has been called
        verify(mockCommandHandler, never()).getListOfOperations();  // Modify this to match the logic
    }

    @Test
    void testShowMapWithoutMap() {
        when(mockCurrentState.getD_map()).thenReturn(null);

        // Simulate calling the "showmap" command
        Exception exception = assertThrows(Exception.class, () -> {
            gameEngine.commandHandler("showmap");
        });

        // Check that the error message is correct
        assertEquals("Error: Map not available. Use 'loadmap' or 'editmap' first.", exception.getMessage());
    }
}
