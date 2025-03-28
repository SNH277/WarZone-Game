package Model;

import Constants.ProjectConstants;
import Controller.MainGameEngine;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;
import View.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

/**
 * Represents the Startup Phase of the game.
 * In this phase, players can perform operations such as loading and editing the map,
 * adding/removing players, assigning countries, and preparing the game for the main loop.
 * Commands like deploy or advance are not valid during this phase.
 * @author Shrey Hingu
 */
public class StartupPhase extends Phase{
    PlayerController d_playerController = new PlayerController();

    /**
     * Constructs the StartupPhase and initializes controllers.
     *
     * @param p_mainGameEngine the main game engine instance
     * @param p_currentState   the current game state
     */
    public StartupPhase(MainGameEngine p_mainGameEngine, CurrentState p_currentState) {
        super(p_mainGameEngine, p_currentState);
    }

    /**
     * Invalid command for Startup Phase.
     * Advance commands are not allowed during map editing or player setup.
     *
     * @param p_inputCommand the input command string
     * @param p_player       the player issuing the command
     */
    protected void advance(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command for Startup Phase.
     * Deploy commands are not allowed during the Startup Phase.
     *
     * @param p_inputCommand the input command string
     * @param p_player       the player issuing the command
     */
    protected void deploy(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Starts the game and continuously listens for player commands.
     */
    public void initPhase(){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        commandDescription();

        while (d_mainGameEngine.getD_currentPhase() instanceof StartupPhase) {
            System.out.print("Enter your command: ");
            try {
                String l_inputCommand = l_bufferedReader.readLine();
                handleCommand(l_inputCommand);
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * Displays a list of available game commands and their descriptions.
     */
    private void commandDescription() {
        System.out.println("================================== COMMAND Description ===================================");
        System.out.println("1. Initiate the Map:");
        System.out.println("   - Loads an existing map file into the game.");
        System.out.println("   - Usage: 'loadmap <your_filename(.map)>'");
        System.out.println("   - Example: 'loadmap world.map'");
        System.out.println();
        System.out.println("2. Edit the Map:");
        System.out.println("   - Opens an existing map for editing or creates a new one if the file does not exist.");
        System.out.println("   - Allows adding, modifying, or deleting continents, countries, and connections.");
        System.out.println("   - Usage: 'editmap <filename>(.map)'");
        System.out.println("   - Example: 'editmap mycustommap.map'");
        System.out.println();
        System.out.println("3. Validate the Map:");
        System.out.println("   - Checks if the map is correctly structured.");
        System.out.println("   - Ensures all countries are connected, continents are properly defined, and no isolated territories exist.");
        System.out.println("   - Usage: 'validatemap'");
        System.out.println();
        System.out.println("4. Show the Map:");
        System.out.println("   - Displays the current map in a structured text format on the command line.");
        System.out.println("   - Shows continents, countries, and their neighboring connections.");
        System.out.println("   - Usage: 'showmap'");
        System.out.println();
        System.out.println("5. Save the Map:");
        System.out.println("   - Saves the current map exactly as it was edited, preserving all changes.");
        System.out.println("   - The saved map can be reloaded later for further modifications or gameplay.");
        System.out.println("   - Usage: 'savemap <filename>'");
        System.out.println("   - Example: 'savemap editedworld.map'");
        System.out.println();
        System.out.println("6. Edit the Continent:");
        System.out.println("   - Adds or removes a continent from the map.");
        System.out.println("   - Adding a continent: 'editcontinent -add <continent_name> <control_value>'");
        System.out.println("   - Removing a continent: 'editcontinent -remove <continent_name>'");
        System.out.println("   - Example: 'editcontinent -add Europe 5'");
        System.out.println();
        System.out.println("7. Edit the Country:");
        System.out.println("   - Adds or removes a country from the map.");
        System.out.println("   - Adding a country: 'editcountry -add <country_name> <continent_name>'");
        System.out.println("   - Removing a country: 'editcountry -remove <country_name>'");
        System.out.println("   - Example: 'editcountry -add France Europe'");
        System.out.println();
        System.out.println("8. Edit the Neighbour:");
        System.out.println("   - Manages adjacency between countries.");
        System.out.println("   - Adding a connection: 'editneighbour -add <country_1> <country_2>'");
        System.out.println("   - Removing a connection: 'editneighbour -remove <country_1> <country_2>'");
        System.out.println("   - Example: 'editneighbour -add France Germany'");
        System.out.println();
        System.out.println("9. Add or Remove a Player:");
        System.out.println("   - Adds or removes a player in the game.");
        System.out.println("   - Adding: 'gameplayer -add <player_name>'");
        System.out.println("   - Removing: 'gameplayer -remove <player_name>'");
        System.out.println("   - Example: 'gameplayer -add Alex'");
        System.out.println();
        System.out.println("10. Assign Countries to Players:");
        System.out.println("   - Distributes all countries among players and assigns initial armies.");
        System.out.println("   - Must be done before starting the game.");
        System.out.println("   - Usage: 'assigncountries'");
        System.out.println();
        System.out.println("11. Exit the Game:");
        System.out.println("   - Closes the game and ends the session.");
        System.out.println("   - Usage: 'exit'");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        String userInput;

        do {
            System.out.print("Do you understand all the commands? Press 'y' or 'Y' to continue: ");
            userInput = scanner.nextLine().trim();
        } while (!userInput.equalsIgnoreCase("y"));

        System.out.println("Continuing the game...");
    }

    protected void cardHandle(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Saves the current map to a file.
     *
     * @param p_commandHandler The command handler containing the filename argument.
     */
    protected void saveMap(CommandHandler p_commandHandler) throws CommandValidationException{
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);

        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_SAVEMAP_COMMAND);
        } else {
            for(java.util.Map<String,String> l_singleOperation : l_listOfOperations){
                if(l_singleOperation.containsKey("Arguments") && l_singleOperation.get("Arguments")!=null){
                    boolean l_isMapSaved = d_mapController.saveMap(d_currentState, l_singleOperation.get("Arguments"));
                    if(l_isMapSaved){
                        System.out.println("Map : "+d_currentState.getD_map().getD_mapName()+" saved successfully.");
                    }
                    else{
                        System.out.println("An error occured while saving the map.");
                    }
                }
                else {
                    System.out.println(ProjectConstants.INVALID_SAVEMAP_COMMAND);
                }
            }
        }
    }

    /**
     * Validates the current map structure.
     *
     * @param p_commandHandler The command handler to process the validation request.
     */
    protected void validateMap(CommandHandler p_commandHandler) throws CommandValidationException{
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        if (l_listOfOperations != null && !l_listOfOperations.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_VALIDATEMAP_COMMAND);
            return;
        }

        Model.Map l_map = d_currentState.getD_map();
        if (l_map == null) {
            System.out.println(ProjectConstants.MAP_NOT_AVAILABLE);
            return;
        }

        if (l_map.validateMap()) {
            System.out.println(ProjectConstants.VALID_MAP);
            d_mainGameEngine.setD_mainEngineLog(ProjectConstants.VALID_MAP,"effect");
        } else {
            System.out.println(ProjectConstants.INVALID_MAP);
        }
    }

    /**
     * Assigns countries to players and starts the game.
     *
     * @param p_commandHandler The command handler to process the assignment request.
     * @throws IOException If an I/O error occurs.
     */
    protected void assignCountries(CommandHandler p_commandHandler) throws CommandValidationException,IOException {
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            d_playerController.assignCountry(d_currentState);
            d_playerController.assignArmies(d_currentState);
            d_mainGameEngine.setIssueOrderPhase();
        }
    }

    /**
     * Adds or removes players based on the command.
     *
     * @param p_commandHandler The command handler containing player operations.
     */
    protected void gamePlayer(CommandHandler p_commandHandler) throws CommandValidationException{
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_GAMEPLAYER_COMMAND);
        }
        else {
            for (java.util.Map<String, String> l_eachMap : l_listOfOperations) {
                if (l_eachMap.containsKey("Operation") && l_eachMap.containsKey("Arguments")) {
                    d_currentState.addOrRemovePlayer(l_eachMap.get("Operation"), l_eachMap.get("Arguments"));
                }
            }
        }
    }

    /**
     * Modifies the neighboring country relationships based on the provided command.
     *
     * @param p_commandHandler The command handler containing the neighbor modification operations.
     * @throws CommandValidationException If the command is invalid or improperly formatted.
     */
    protected void editNeighbourCountry(CommandHandler p_commandHandler) throws  CommandValidationException {
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if(l_listOfOperations == null || l_listOfOperations.isEmpty()){
            throw new CommandValidationException("Invalid command entered for editmap.");
        }else {
            for (java.util.Map<String ,String > l_singleOperation : l_listOfOperations){
                if(l_singleOperation.containsKey("Operation") && l_singleOperation.get("Operation")!=null && l_singleOperation.containsKey("Arguments") && l_singleOperation.get("Arguments")!=null){
                    d_mapController.editNeighbourCountry(d_currentState,l_singleOperation.get("Operation"),l_singleOperation.get("Arguments"));
                }
            }
        }
    }

    /**
     * Edits the continents on the map based on the provided command.
     *
     * @param p_commandHandler The command handler containing continent modification details.
     * @throws CommandValidationException If the command is invalid or improperly formatted.
     */
    protected void editContinent(CommandHandler p_commandHandler) throws CommandValidationException {
        List<java.util.Map<String,String>> l_listOfOperations = p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);

        if (l_listOfOperations.isEmpty()) {
            throw new CommandValidationException("Invalid Command for edit Continent");
        }

        for (java.util.Map<String, String> l_singleOperation : l_listOfOperations) {
            String l_operation = l_singleOperation.get("Operation");
            String l_arguments = l_singleOperation.get("Arguments");

            if (l_operation != null && !l_operation.isEmpty() && l_arguments != null && !l_arguments.isEmpty()) {
                d_mapController.editContinent(d_currentState, l_operation, l_arguments);
            } else {
                throw new CommandValidationException("Missing or invalid 'Operation' or 'Arguments' in command.");
            }
        }
    }

    /**
     * Edits the countries on the map based on the provided command.
     *
     * @param p_commandHandler The command handler containing country modification details.
     * @throws CommandValidationException If the command is invalid or improperly formatted.
     */
    protected void editCountry(CommandHandler p_commandHandler) throws CommandValidationException {
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations.isEmpty()) {
            throw new CommandValidationException("Invalid Command for edit Country");
        }

        for (java.util.Map<String, String> l_singleOperation : l_listOfOperations) {
            String l_operation = l_singleOperation.get("Operation");
            String l_arguments = l_singleOperation.get("Arguments");

            if (l_operation != null && !l_operation.isEmpty() && l_arguments != null && !l_arguments.isEmpty()) {
                d_mapController.editCountry(d_currentState, l_operation, l_arguments);
            } else {
                throw new CommandValidationException("Missing or invalid 'Operation' or 'Arguments' in command.");
            }
        }

    }

    /**
     * Modifies the map based on the provided command operations.
     *
     * @param p_commandHandler The command handler containing the map modification details.
     * @throws CommandValidationException If the command is invalid or missing required arguments.
     * @throws IOException If an I/O error occurs during map modification.
     */
    protected void editMap(CommandHandler p_commandHandler) throws CommandValidationException,IOException {
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            throw new CommandValidationException("Invalid Command for edit map");
        }

        for (java.util.Map<String, String> l_singleOperation : l_listOfOperations) {
            if (!p_commandHandler.checkRequiredKey("Arguments", l_singleOperation)) {
                throw new CommandValidationException("Invalid Command for edit map operation");
            }
            d_mapController.editMap(d_currentState, l_singleOperation.get("Arguments"));
        }
    }

    /**
     * Loads a game map from a file.
     *
     * @param p_commandHandler The command handler with map loading arguments.
     * @throws CommandValidationException If the command is invalid or missing required arguments.
     */
    protected void loadMap(CommandHandler p_commandHandler) throws CommandValidationException {
        List<java.util.Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if(l_listOfOperations == null || l_listOfOperations.isEmpty()){
            throw new CommandValidationException("Invalid command for loadmap. Use 'loadmap file_name.map' command");
        }
        for(java.util.Map<String,String> l_singleOperation : l_listOfOperations){
            if(l_singleOperation.containsKey("Arguments")&& l_singleOperation.get("Arguments")!=null){
                Model.Map l_map =d_mapController.loadMap(d_currentState,l_singleOperation.get("Arguments"));
                System.out.println(l_map);
                if(l_map.validateMap()){
                    System.out.println(ProjectConstants.VALID_MAP);
                }
                else{
                    System.out.println(ProjectConstants.INVALID_MAP);
                }
            }
        }
    }

    /**
     * Displays the current state of the map including continents, countries, and neighbors.
     *
     * @throws CommandValidationException if map view encounters issues
     */
    protected void showMap() throws CommandValidationException {
        MapView l_mapView = new MapView(d_currentState);
        l_mapView.showMap();
    }

}
