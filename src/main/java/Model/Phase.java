package Model;

import Constants.ProjectConstants;
import Controller.MainGameEngine;
import Controller.MapController;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * Abstract class representing a phase in the game engine.
 * All specific phases (e.g., StartUp, Reinforcement, Attack) should extend this class.
 * It manages command handling, controller access, and phase-specific command execution.
 * @author Shrey Hingu
 */
public abstract class Phase implements Serializable {
    /** Reference to the main game engine. */
    MainGameEngine d_mainGameEngine;

    /** Current state of the game. */
    CurrentState d_currentState;

    /** Controller for map-related actions. */
    MapController d_mapController;

    /** Controller for gameplay/player-related actions. */
    PlayerController d_gameplayController;

    /** Instance of the Tournament class used to manage tournament-related operations. */
    Tournament d_tournament;


    /**
     * Constructor to initialize a Phase with its core dependencies.
     *
     * @param p_mainGameEngine the main game engine
     * @param p_currentState the current state of the game
     */
    public Phase(MainGameEngine p_mainGameEngine, CurrentState p_currentState) {
        this.d_mainGameEngine = p_mainGameEngine;
        this.d_currentState = p_currentState;
        this.d_mapController = new MapController();
        this.d_gameplayController = new PlayerController();
        this.d_tournament = new Tournament();
    }

    /**
     * Returns the main game engine instance that controls the game flow.
     *
     * @return the main game engine instance.
     */
    public MainGameEngine getD_mainGameEngine() {
        return d_mainGameEngine;
    }

    /**
     * Sets the main game engine instance that controls the game flow.
     *
     * @param p_mainGameEngine the main game engine to set.
     */
    public void setD_mainGameEngine(MainGameEngine p_mainGameEngine) {
        this.d_mainGameEngine = p_mainGameEngine;
    }

    /**
     * Returns the current state of the game, including players and map.
     *
     * @return the current game state.
     */
    public CurrentState getD_currentState() {
        return d_currentState;
    }

    /**
     * Sets the current state of the game, including players and map.
     *
     * @param p_currentState the game state to set.
     */
    public void setD_currentState(CurrentState p_currentState) {
        this.d_currentState = p_currentState;
    }

    /**
     * Returns the map controller responsible for managing the game map.
     *
     * @return the map controller.
     */
    public MapController getD_mapController() {
        return d_mapController;
    }

    /**
     * Sets the map controller responsible for managing the game map.
     *
     * @param p_mapController the map controller to set.
     */
    public void setD_mapController(MapController p_mapController) {
        this.d_mapController = p_mapController;
    }

    /**
     * Returns the gameplay controller responsible for handling player actions.
     *
     * @return the gameplay controller.
     */
    public PlayerController getD_gameplayController() {
        return d_gameplayController;
    }

    /**
     * Sets the gameplay controller responsible for handling player actions.
     *
     * @param p_gameplayController the gameplay controller to set.
     */
    public void setD_gameplayController(PlayerController p_gameplayController) {
        this.d_gameplayController = p_gameplayController;
    }

    /**
     * Initializes the phase when it becomes active.
     * This method is called to set up the phase-specific details and behaviors.
     *
     * @param p_isTournamentMode A boolean flag indicating whether the game is in tournament mode.
     */
    public abstract void initPhase(boolean p_isTournamentMode);

    /**
     * Handles a command entered by the user (no player context).
     *
     * @param p_inputCommand the command string
     * @throws CommandValidationException if the command is invalid
     * @throws IOException if an I/O error occurs
     */
    public void handleCommand(String p_inputCommand) throws CommandValidationException, IOException {
        commandHandler(p_inputCommand,null);
    }

    /**
     * Handles a command entered by a specific player.
     *
     * @param p_inputCommand the command string
     * @param p_player the player who issued the command
     * @throws CommandValidationException if the command is invalid
     * @throws IOException if an I/O error occurs
     */
    public void handleCommand(String p_inputCommand, Player p_player) throws CommandValidationException, IOException {
        commandHandler(p_inputCommand, p_player);
    }

    /**
     * Internal command dispatcher and validator. Parses the command, checks conditions, and delegates to appropriate handler methods.
     *
     * @param p_inputCommand the command string
     * @param p_player the issuing player (can be null)
     * @throws CommandValidationException if command is invalid
     * @throws IOException if an I/O error occurs
     */
    private void commandHandler(String p_inputCommand,Player p_player) throws CommandValidationException, IOException {
        CommandHandler l_commandHandler =new CommandHandler(p_inputCommand);
        String l_mainCommand = l_commandHandler.getMainCommand();
        boolean l_isMapAvailable = (d_currentState.getD_map() != null);
        d_currentState.updateLog(l_commandHandler.getMainCommand(),"command");

        Set<String> requiresMap = Set.of(
                "editcountry", "editcontinent", "editneighbour",
                "showmap", "assigncountries",
                "validatemap", "savemap", "deploy", "advance",
                "bomb", "blockade", "airlift","savegame"
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
                showMap();
                break;
            case "gameplayer":
                gamePlayer(l_commandHandler,p_player);
                break;
            case "assigncountries":
                assignCountries(l_commandHandler,p_player,false,d_currentState);
                break;
            case "validatemap":
                validateMap(l_commandHandler);
                break;
            case "savemap":
                saveMap(l_commandHandler);
                break;
            case "deploy":
                deploy(p_inputCommand,p_player);
                break;
            case "advance":
                advance(p_inputCommand,p_player);
                break;
            case "bomb":
            case "blockade":
            case "airlift":
                cardHandle(p_inputCommand,p_player);
                break;
            case "negotiate":
                if(!l_isMapAvailable)
                    d_currentState.getD_modelLogger().setD_message("Command 'negotiate' failed: Map is not available.","effect");
                else
                    cardHandle(p_inputCommand,p_player);
            case "loadgame":
                loadGame(l_commandHandler,p_player);
                break;
            case "savegame":
                saveGame(l_commandHandler,p_player);
                break;
            case "tournament":
                tournamentMode(l_commandHandler);
                break;
            case "exit":
                d_currentState.getD_modelLogger().setD_message("------------Game Session Terminated. All progress saved.------------","effect");
                System.out.println("Closing game... Thank you for playing");
                System.exit(0);
                break;
            default:
                d_mainGameEngine.setD_mainEngineLog("Invalid command for this phase.","effect");
                System.out.println(ProjectConstants.INVALID_COMMAND);
                break;
        }
    }

    // Abstract methods to be implemented in subclasses for specific phase behavior
    /**
     * Tournament mode.
     *
     * @param p_commandHandler the p command handler
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    protected abstract void tournamentMode(CommandHandler p_commandHandler) throws CommandValidationException, IOException;

    /**
     * Save game.
     *
     * @param p_commandHandler the p command handler
     * @param p_player         the p player
     * @throws CommandValidationException the command validation exception
     */
    protected abstract void saveGame(CommandHandler p_commandHandler, Player p_player) throws CommandValidationException;

    /**
     * Load game.
     *
     * @param p_commandHandler the p command handler
     * @param p_player         the p player
     */
    public abstract void loadGame(CommandHandler p_commandHandler, Player p_player);

    /**
     * Handles execution of card-based commands such as bomb, blockade, airlift, and negotiate.
     *
     * @param p_inputCommand the command string entered by the player.
     * @param p_player       the player issuing the command.
     */
    protected abstract void cardHandle(String p_inputCommand, Player p_player);

    /**
     * Handles the logic for the 'advance' command, which moves armies between countries.
     *
     * @param p_inputCommand the command string entered by the player.
     * @param p_player       the player issuing the command.
     */
    protected abstract void advance(String p_inputCommand, Player p_player);

    /**
     * Handles the logic for the 'deploy' command, which places armies on owned countries.
     *
     * @param p_inputCommand the command string entered by the player.
     * @param p_player       the player issuing the command.
     */
    protected abstract void deploy(String p_inputCommand, Player p_player);

    /**
     * Saves the current map to a file.
     *
     * @param p_commandHandler the command handler containing map saving parameters
     * @throws CommandValidationException if the save command is invalid
     */
    protected abstract void saveMap(CommandHandler p_commandHandler) throws CommandValidationException;

    /**
     * Validates the current map to ensure it is playable (e.g., connected graph, correct configuration).
     *
     * @param p_commandHandler the command handler containing validation parameters
     * @throws CommandValidationException if the map is invalid or the command is incorrect
     */
    protected abstract void validateMap(CommandHandler p_commandHandler) throws CommandValidationException;

    /**
     * Assign countries.
     *
     * @param p_commandHandler   the l command handler
     * @param p_player           the p player
     * @param p_isTournamentMode the p is tournament mode
     * @param p_currentState     the p current state
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    protected abstract void assignCountries(CommandHandler p_commandHandler, Player p_player, Boolean p_isTournamentMode, CurrentState p_currentState) throws CommandValidationException, IOException;

    /**
     * Adds or removes players from the game setup.
     *
     * @param p_commandHandler the command handler containing player setup instructions
     * @param p_player         the p player
     * @throws CommandValidationException if the command is invalid
     * @throws IOException                the io exception
     */
    protected abstract void gamePlayer(CommandHandler p_commandHandler,Player p_player) throws CommandValidationException, IOException;

    /**
     * Displays the current state of the map including continents and countries.
     *
     * @throws CommandValidationException if the command is invalid in the current context
     */
    protected abstract void showMap() throws CommandValidationException;

    /**
     * Adds or removes a neighboring relationship between two countries.
     *
     * @param p_commandHandler the command handler containing neighbor editing instructions
     * @throws CommandValidationException if the command is invalid
     */
    protected abstract void editNeighbourCountry(CommandHandler p_commandHandler) throws CommandValidationException;

    /**
     * Adds, edits, or removes a continent in the map.
     *
     * @param p_commandHandler the command handler containing continent editing instructions
     * @throws CommandValidationException if the command is invalid
     */
    protected abstract void editContinent(CommandHandler p_commandHandler) throws  CommandValidationException;

    /**
     * Adds, edits, or removes a country in the map.
     *
     * @param p_commandHandler the command handler containing country editing instructions
     * @throws CommandValidationException if the command is invalid
     */
    protected abstract void editCountry(CommandHandler p_commandHandler) throws CommandValidationException;

    /**
     * Enters map editing mode to create or modify a map file.
     *
     * @param p_commandHandler the command handler containing map file parameters
     * @throws CommandValidationException if the command is invalid
     * @throws IOException if an I/O error occurs during map editing
     */
    protected abstract void editMap(CommandHandler p_commandHandler) throws CommandValidationException, IOException;

    /**
     * Loads an existing map file into the current game state.
     *
     * @param p_commandHandler the command handler containing map loading instructions
     * @throws CommandValidationException if the map file is invalid or command is incorrect
     */
    protected abstract void loadMap(CommandHandler p_commandHandler) throws CommandValidationException;

    /**
     * Utility method to log invalid commands during a phase.
     */
    public void printInvalidCommandInPhase(){
        d_mainGameEngine.setD_mainEngineLog("Invalid Command entered for this phase.","effect");
    }

}
