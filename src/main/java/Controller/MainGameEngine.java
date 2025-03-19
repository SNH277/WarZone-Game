package Controller;

import Model.CurrentState;
import Model.Orders;
import Model.Player;
import Utils.CommandHandler;
import View.MapView;
import Constants.ProjectConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


/**
 * The {@code MainGameEngine} class serves as the main controller for handling game commands and logic.
 * It manages game states, player operations, and map functionalities.
 */
public class MainGameEngine {

    /** The map controller for handling map-related commands. */
    MapController d_mapController=new MapController();
    /** The player controller for managing player-related actions. */
    PlayerController d_playerController=new PlayerController();
    /** The current state of the game. */
    CurrentState d_currentGameState = new CurrentState();

    /**
     * The main entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        MainGameEngine l_mainGameEngine = new MainGameEngine();
        l_mainGameEngine.startGame();
    }

    /**
     * Starts the game and continuously listens for player commands.
     */
    private void startGame(){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        commandDescription();

        while (true) {
            System.out.print("Enter your command: ");
            try {
                String l_inputCommand = l_bufferedReader.readLine();
                if (l_inputCommand.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the game. Goodbye!");
                    System.exit(0);
                } else {
                    commandHandler(l_inputCommand);
                }
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

    /**
     * Handles the execution of commands entered by the user.
     *
     * @param p_inputCommand The command entered by the user.
     * @throws Exception If an invalid command is entered.
     */
    private void commandHandler(String p_inputCommand) throws Exception {
        CommandHandler l_commandHandler =new CommandHandler(p_inputCommand);
        String l_mainCommand = l_commandHandler.getMainCommand();
        boolean l_isMapAvailable = (d_currentGameState.getD_map() != null);

        Set<String> requiresMap = Set.of(
                "editcountry", "editcontinent", "editneighbour",
                "showmap", "gameplayer", "assigncountries",
                "validatemap", "savemap"
        );

        if (requiresMap.contains(l_mainCommand) && !l_isMapAvailable) {
            System.out.println(ProjectConstants.MAP_NOT_AVAILABLE);
            return;
        }

        switch (l_mainCommand) {
            case "loadmap":
                loadMap(l_commandHandler);
                break;
            case "editmap":
                editMap(l_commandHandler);
                break;
            case "editcountry":
                editCountry(l_commandHandler);
                break;
            case "editcontinent":
                editContinent(l_commandHandler);
                break;
            case "editneighbour":
                editNeighbourCountry(l_commandHandler);
                break;
            case "showmap":
                new MapView(d_currentGameState).showMap();
                break;
            case "gameplayer":
                gamePlayer(l_commandHandler);
                break;
            case "assigncountries":
                assignCountries(l_commandHandler);
                break;
            case "validatemap":
                validateMap(l_commandHandler);
                break;
            case "savemap":
                saveMap(l_commandHandler);
                break;
            default:
                System.out.println(ProjectConstants.INVALID_COMMAND);
                break;
        }
    }

    /**
     * Saves the current map to a file.
     *
     * @param p_commandHandler The command handler containing the filename argument.
     */
    private void saveMap(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);

        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_SAVEMAP_COMMAND);
        } else {
            for(Map<String,String> l_singleOperation : l_listOfOperations){
                if(l_singleOperation.containsKey("Arguments") && l_singleOperation.get("Arguments")!=null){
                    boolean l_isMapSaved = d_mapController.saveMap(d_currentGameState, l_singleOperation.get("Arguments"));
                    if(l_isMapSaved){
                        System.out.println("Map : "+d_currentGameState.getD_map().getD_mapName()+" saved successfully.");
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
    private void validateMap(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        if (l_listOfOperations != null && !l_listOfOperations.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_VALIDATEMAP_COMMAND);
            return;
        }

        Model.Map l_map = d_currentGameState.getD_map();
        if (l_map == null) {
            System.out.println(ProjectConstants.MAP_NOT_AVAILABLE);
            return;
        }

        if (l_map.validateMap()) {
            System.out.println(ProjectConstants.VALID_MAP);
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
    private void assignCountries(CommandHandler p_commandHandler) throws IOException {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            d_playerController.assignCountry(d_currentGameState);
            d_playerController.assignArmies(d_currentGameState);
            playGame();
        }
    }

    /**
     * Starts the game, allowing players to deploy armies and execute orders.
     *
     * @throws IOException If an I/O error occurs.
     */
    private void playGame() throws IOException {
        if (d_currentGameState.getD_players() == null || d_currentGameState.getD_players().isEmpty()) {
            System.out.println(ProjectConstants.NO_PLAYERS);
            return;
        }

        System.out.println("➡ Deploy armies: 'deploy <country> <num_of_armies>'");

        while (d_playerController.isUnallocatedArmiesExist(d_currentGameState)) {
            for (Player l_eachPlayer : d_currentGameState.getD_players()) {
                if (l_eachPlayer.getD_unallocatedArmies() > 0) {
                    l_eachPlayer.issueOrder();
                }
            }
        }

        while (d_playerController.isUnexecutedOrdersExist(d_currentGameState)) {
            for (Player l_eachPlayer : d_currentGameState.getD_players()) {
                Orders l_orderToExecute = l_eachPlayer.nextOrder();
                if (l_orderToExecute != null) {
                    l_orderToExecute.execute(l_eachPlayer); // Throws IOException if execution fails
                }
            }
        }

        System.out.println("All orders have been executed successfully.");
        System.out.println("Thank you for playing the game");
        System.exit(0);
    }

    /**
     * Adds or removes players based on the command.
     *
     * @param p_commandHandler The command handler containing player operations.
     */
    private void gamePlayer(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_GAMEPLAYER_COMMAND);
        }
        else {
            for (Map<String, String> l_eachMap : l_listOfOperations) {
                if (l_eachMap.containsKey("Operation") && l_eachMap.containsKey("Arguments")) {
                    d_currentGameState.addOrRemovePlayer(l_eachMap.get("Operation"), l_eachMap.get("Arguments"));
                }
            }
        }
    }

    /**
     * Edits the neighboring country relationships.
     *
     * @param p_commandHandler The command handler containing neighbor modifications.
     * @throws Exception If an invalid command is entered.
     */
    private void editNeighbourCountry(CommandHandler p_commandHandler) throws  Exception {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if(l_listOfOperations == null || l_listOfOperations.isEmpty()){
            throw new Exception("Invalid command entered for editmap.");
        }else {
            for (Map<String ,String > l_singleOperation : l_listOfOperations){
                if(l_singleOperation.containsKey("Operation") && l_singleOperation.get("Operation")!=null && l_singleOperation.containsKey("Arguments") && l_singleOperation.get("Arguments")!=null){
                    d_mapController.editNeighbourCountry(d_currentGameState,l_singleOperation.get("Operation"),l_singleOperation.get("Arguments"));
                }
            }
        }
    }

    /**
     * Edits the continents on the map based on the provided command.
     *
     * @param p_commandHandler The command handler containing continent modification details.
     * @throws Exception If an invalid command is provided.
     */
    private void editContinent(CommandHandler p_commandHandler) throws Exception {
        List<Map<String,String>> l_listOfOperations = p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);

        if (l_listOfOperations.isEmpty()) {
            throw new Exception("Invalid Command for edit Continent");
        }

        for (Map<String, String> l_singleOperation : l_listOfOperations) {
            String l_operation = l_singleOperation.get("Operation");
            String l_arguments = l_singleOperation.get("Arguments");

            if (l_operation != null && !l_operation.isEmpty() && l_arguments != null && !l_arguments.isEmpty()) {
                d_mapController.editContinent(d_currentGameState, l_operation, l_arguments);
            } else {
                throw new Exception("Missing or invalid 'Operation' or 'Arguments' in command.");
            }
        }
    }

    /**
     * Edits the countries on the map based on the provided command.
     *
     * @param p_commandHandler The command handler containing country modification details.
     * @throws Exception If an invalid command is provided.
     */
    private void editCountry(CommandHandler p_commandHandler) throws Exception {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations.isEmpty()) {
            throw new Exception("Invalid Command for edit Country");
        }

        for (Map<String, String> l_singleOperation : l_listOfOperations) {
            String l_operation = l_singleOperation.get("Operation");
            String l_arguments = l_singleOperation.get("Arguments");

            if (l_operation != null && !l_operation.isEmpty() && l_arguments != null && !l_arguments.isEmpty()) {
                d_mapController.editCountry(d_currentGameState, l_operation, l_arguments);
            } else {
                throw new Exception("Missing or invalid 'Operation' or 'Arguments' in command.");
            }
        }

    }

    /**
     * Edits the map based on the provided command.
     *
     * @param p_commandHandler The command handler containing map modification details.
     * @throws Exception If an invalid command is provided.
     */
    private void editMap(CommandHandler p_commandHandler) throws Exception {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if (l_listOfOperations == null || l_listOfOperations.isEmpty()) {
            throw new Exception("Invalid Command for edit map");
        }

        for (Map<String, String> l_singleOperation : l_listOfOperations) {
            if (!p_commandHandler.checkRequiredKey("Arguments", l_singleOperation)) {
                throw new Exception("Invalid Command for edit map operation");
            }
            d_mapController.editMap(d_currentGameState, l_singleOperation.get("Arguments"));
        }
    }

    /**
     * Loads a game map from a file.
     *
     * @param p_commandHandler The command handler with map loading arguments.
     * @throws Exception If an error occurs while loading the map.
     */
    private void loadMap(CommandHandler p_commandHandler) throws Exception{
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
        if(l_listOfOperations == null || l_listOfOperations.isEmpty()){
            throw new Exception("Invalid command for loadmap. Use 'loadmap file_name.map' command");
        }
        for(Map<String,String> l_singleOperation : l_listOfOperations){
            if(l_singleOperation.containsKey("Arguments")&& l_singleOperation.get("Arguments")!=null){
                Model.Map l_map =d_mapController.loadMap(d_currentGameState,l_singleOperation.get("Arguments"));
                System.out.println(l_map);
                if(l_map.validateMap()){
                    System.out.println("Map is valid.");
                }
                else{
                    System.out.println("Map is not valid.");
                }
            }
        }
    }
}
