package Model;

import Constants.ProjectConstants;
import Controller.MainGameEngine;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Services.GameService;
import Utils.CommandHandler;
import View.MapView;
import View.TournamentView;

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
    /**
     * Creates a new instance of {@link PlayerController} to manage player actions and game logic.
     */
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
    public void initPhase(boolean p_isTournamentMode){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
//        commandDescription();

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
     * Load game.
     *
     * @param p_commandHandler the p command handler
     * @param p_player         the p player
     */
    @Override
    public void loadGame(CommandHandler p_commandHandler, Player p_player) {
        List <java.util.Map<String,String>> l_operationsList = p_commandHandler.getListOfOperations();

        if (l_operationsList == null || l_operationsList.isEmpty()) {
            System.out.println(ProjectConstants.INVALID_LOADGAME_COMMAND);
        }
        for (java.util.Map<String,String> l_map : l_operationsList) {
            if (p_commandHandler.checkRequiredKey("Arguments", l_map)) {
                String l_fileName = l_map.get("Arguments");
                try {
//                    System.out.println("Game loaded successfully from Filename : " + l_fileName);
                    d_mainGameEngine.setD_mainEngineLog("Game loaded successfully from Filename : " + l_fileName, "effect");
                    Phase l_phase = GameService.loadGame(l_fileName);
                    this.d_mainGameEngine.loadPhase(l_phase);
//                    System.out.println("Game loaded successfully from Filename : " + l_fileName);
//                    d_mainGameEngine.setD_mainEngineLog("Game loaded successfully from Filename : " + l_fileName, "effect");
                } catch (ClassNotFoundException | IOException e) {
                    System.out.println(ProjectConstants.INVALID_LOADGAME_COMMAND);
                }
            }
        }
    }

    /**
     * Save game.
     *
     * @param p_commandHandler the p command handler
     * @param p_player         the p player
     * @throws CommandValidationException the command validation exception
     */
    @Override
    public void saveGame(CommandHandler p_commandHandler, Player p_player) throws CommandValidationException {
        List<java.util.Map<String,String>> l_operationsList = p_commandHandler.getListOfOperations();
        if(l_operationsList == null || l_operationsList.isEmpty()){
            System.out.println(ProjectConstants.INVALID_SAVEGAME_COMMAND);
        }
        for(java.util.Map<String,String> l_map : l_operationsList){
            if(p_commandHandler.checkRequiredKey("Arguments", l_map)) {
                String l_fileName = l_map.get("Arguments");
                GameService.saveGame(this,l_fileName);
                d_mainGameEngine.setD_mainEngineLog("Game saved successfully to Filename : " + l_fileName, "effect");
            } else {
                throw new CommandValidationException(ProjectConstants.INVALID_SAVEGAME_COMMAND);
            }

        }
    }

    /**
     * Tournament mode.
     *
     * @param p_commandHandler the p command handler
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    @Override
    protected void tournamentMode(CommandHandler p_commandHandler) throws CommandValidationException, IOException {
        if(d_currentState.getD_players() != null && d_currentState.getD_players().size() > 1){
            List<java.util.Map<String, String>> l_operationsList = p_commandHandler.getListOfOperations();
            boolean l_parsingSuccess = false;
            if(l_operationsList.isEmpty() && !d_tournament.requiredTournamentArgPresent(l_operationsList,p_commandHandler)){
                throw new CommandValidationException(ProjectConstants.INVALID_TOURNAMENT_MODE_COMMAND);
            } else{
                for(java.util.Map<String, String> l_singleOperation : l_operationsList) {
                    if (p_commandHandler.checkRequiredKey("Arguments", l_singleOperation) && p_commandHandler.checkRequiredKey("Operation", l_singleOperation)) {
                        l_parsingSuccess = d_tournament.parseTournamentCommand(d_currentState, l_singleOperation.get("Operation"), l_singleOperation.get("Arguments"), d_mainGameEngine);
                        if (!l_parsingSuccess) {
                            break;
                        }
                    } else {
                        throw new CommandValidationException(ProjectConstants.INVALID_TOURNAMENT_MODE_COMMAND);
                    }
                }
            }
            if(l_parsingSuccess){
                for(CurrentState l_eachState : d_tournament.getD_currentStateList()){
                    d_mainGameEngine.setD_mainEngineLog("Starting new game on the map " + l_eachState.getD_map().getD_mapName() +" ...........", "effect");
                    assignCountries(new CommandHandler("assigncountries"), null, true , l_eachState);

                    d_mainGameEngine.setD_mainEngineLog("Game completed on map : " + l_eachState.getD_map().getD_mapName() + "................\n ", "effect");
                }
                d_mainGameEngine.setD_mainEngineLog("******** Tournament Completed ********", "effect");
                TournamentView l_tournamentView = new TournamentView(d_tournament);
                l_tournamentView.viewTournament();
                d_tournament = new Tournament();
            }
        } else {
            d_mainGameEngine.setD_mainEngineLog("Please add 2 or more players to start the tournament", "effect");
        }
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
     * @param p_commandHandler   the p command handler
     * @param p_player           the p player
     * @param p_isTournamentMode the p is tournament mode
     * @param p_currentState     the p current state
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    protected void assignCountries(CommandHandler p_commandHandler, Player p_player, Boolean p_isTournamentMode, CurrentState p_currentState) throws CommandValidationException, IOException {
        if(d_currentState != null && d_currentState.d_players != null && d_currentState.d_players.size() < 2){
            throw new CommandValidationException("Cannot assign Countries with only 1 Player");
        }
        else if(p_currentState.getD_loadCommand()){
            List<java.util.Map<String, String>> l_operationList = p_commandHandler.getListOfOperations();

            if(l_operationList.isEmpty() || p_isTournamentMode) {
                d_mainGameEngine.setD_currentGameState(p_currentState);
                d_mainGameEngine.setD_isTournamentMode(p_isTournamentMode);
                if(d_playerController.assignCountry(p_currentState)){
                    d_playerController.assignArmies(p_currentState);
                    d_mainGameEngine.setIssueOrderPhase(p_isTournamentMode);
                }
            } else {
                throw new CommandValidationException("Invalid Command for assign countries");
            }
        }else {
            d_mainGameEngine.setD_mainEngineLog("Please load a valid map first", "effect");
        }
    }

    /**
     * Adds or removes players based on the command.
     *
     * @param p_commandHandler The command handler containing player operations.
     */
    protected void gamePlayer(CommandHandler p_commandHandler,Player p_player) throws CommandValidationException, IOException{
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
                System.out.println("before validate");
                if(l_map.validateMap()){
                    d_currentState.setD_loadCommand(true);
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
