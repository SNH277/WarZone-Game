package Controller;

import Model.*;

import java.io.Serializable;
import java.util.Scanner;


/**
 * The {@code MainGameEngine} class serves as the main controller for handling game commands and logic.
 * It manages game states, player operations, and map functionalities.
 * @author Shrey Hingu
 */
public class MainGameEngine implements Serializable {

    /** The current state of the game. */
    CurrentState d_currentGameState = new CurrentState();

    /** The current phase of the game. */
    Phase d_currentPhase = new StartupPhase(this, d_currentGameState);

    /** Flag to indicate if the game is in tournament mode. */
    boolean d_isTournamentMode = false;


    /**
     * Default constructor for the MainGameEngine.
     * Initializes the game engine without setting any initial state or phase.
     */
    public MainGameEngine(){

    }

    /**
     * Retrieves the current game state.
     *
     * @return the current {@link CurrentState} of the game
     */
    public CurrentState getD_currentGameState() {
        return d_currentGameState;
    }

    /**
     * Sets the current game state.
     *
     * @param d_currentGameState the new {@link CurrentState} to be set
     */
    public void setD_currentGameState(CurrentState d_currentGameState) {
        this.d_currentGameState = d_currentGameState;
    }

    /**
     * Retrieves the current phase of the game.
     *
     * @return the current {@link Phase}
     */
    public Phase getD_currentPhase() {
        return d_currentPhase;
    }

    /**
     * Sets the current phase of the game.
     *
     * @param d_currentPhase the new {@link Phase} to be set
     */
    public void setD_currentPhase(Phase d_currentPhase) {
        this.d_currentPhase = d_currentPhase;
    }

    /** Returns whether the game is in tournament mode. */
    public boolean isD_isTournamentMode() {
        return d_isTournamentMode;
    }

    /** Sets the tournament mode flag for the game. */
    public void setD_isTournamentMode(boolean p_isTournamentMode) {
        this.d_isTournamentMode = p_isTournamentMode;
    }

    /**
     * Set startup phase.
     */
    public void setStartupPhase() {
        this.setD_mainEngineLog("Startup Phase of the Game", "phase");
        setD_currentPhase(new StartupPhase(this,d_currentGameState));
        getD_currentPhase().initPhase(d_isTournamentMode);
    }

    /**
     * Transitions the game to the Issue Order Phase.
     * Logs the phase change and initializes the {@link IssueOrderPhase}.
     */
    public void setIssueOrderPhase(boolean p_isTournamentMode){
        this.setD_mainEngineLog("Issue Order Phase","phase");
        setD_currentPhase(new IssueOrderPhase(d_currentGameState,this));
        getD_currentPhase().initPhase(p_isTournamentMode);
    }

    /**
     * Transitions the game to the Order Execution Phase.
     * Logs the phase change and initializes the {@link OrderExecutionPhase}.
     */
    public void setOrderExecutionPhase(){
        this.setD_mainEngineLog("Order Execution Phase","phase");
        setD_currentPhase(new OrderExecutionPhase(d_currentGameState, this));
        getD_currentPhase().initPhase(d_isTournamentMode);
    }



    /**
     * The main entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        MainGameEngine l_mainGameEngine = new MainGameEngine();
        l_mainGameEngine.getD_currentPhase().getD_currentState().updateLog("Game Session Started","start");
        l_mainGameEngine.setD_mainEngineLog("Startup Phase of the Game","phase");
        l_mainGameEngine.startGame(l_mainGameEngine);
    }

    /**
     * Starts the game by initializing the current phase of the provided game engine.
     *
     * @param p_mainGameEngine the main game engine instance whose phase will be initialized
     */
    private void startGame(MainGameEngine p_mainGameEngine){
        commandDescription();
        p_mainGameEngine.getD_currentPhase().initPhase(d_isTournamentMode);
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
        System.out.println("11. Deploy Armies:");
        System.out.println("    - Allocates reinforcement armies to a specific territory controlled by the player.");
        System.out.println("    - Must be done before executing any attack or movement.");
        System.out.println("    - Usage: 'deploy <territory_name> <num_armies>'");
        System.out.println("    - Example: 'deploy Alaska 5'");
        System.out.println();
        System.out.println("12. Advance Armies:");
        System.out.println("    - Moves armies between two adjacent territories.");
        System.out.println("    - If moving to an enemy territory, an attack is initiated.");
        System.out.println("    - Usage: 'advance <from_territory> <to_territory> <num_armies>'");
        System.out.println("    - Example: 'advance Brazil Argentina 10'");
        System.out.println();
        System.out.println("13. Airlift Armies:");
        System.out.println("    - Transfers armies between any two territories controlled by the player, regardless of adjacency.");
        System.out.println("    - Used strategically to reinforce key areas.");
        System.out.println("    - Usage: 'airlift <from_territory> <to_territory> <num_armies>'");
        System.out.println("    - Example: 'airlift India Canada 15'");
        System.out.println();
        System.out.println("14. Bomb Enemy Territory:");
        System.out.println("    - Reduces an enemy territory’s army count by half before an attack.");
        System.out.println("    - Weakens a well-defended position, making it easier to conquer.");
        System.out.println("    - Usage: 'bomb <enemy_territory>'");
        System.out.println("    - Example: 'bomb WesternEurope'");
        System.out.println();
        System.out.println("15. Blockade a Territory:");
        System.out.println("    - Triples the army count on a chosen territory and makes it neutral (no longer controlled by any player).");
        System.out.println("    - Used for defensive purposes or to create barriers.");
        System.out.println("    - Usage: 'blockade <territory_name>'");
        System.out.println("    - Example: 'blockade Alaska'");
        System.out.println();
        System.out.println("16. Negotiate (Diplomacy):");
        System.out.println("    - Prevents two players from attacking each other for one full turn.");
        System.out.println("    - Can be used to form temporary truces or shift focus to other opponents.");
        System.out.println("    - Usage: 'negotiate <player_name>'");
        System.out.println("    - Example: 'negotiate Alex'");
        System.out.println();
        System.out.println("17. Tournament Mode:");
        System.out.println("    - Allows running multiple games automatically using different maps and strategies.");
        System.out.println("    - Useful for testing AI strategies and comparing performance across maps.");
        System.out.println("    - Parameters:");
        System.out.println("      -M <list_of_maps>           : Comma-separated map file names.");
        System.out.println("      -P <list_of_strategies>     : Comma-separated strategies (e.g., aggressive, benevolent, random, cheater).");
        System.out.println("      -G <number_of_games>        : Number of games to be played per map/strategy combo.");
        System.out.println("      -D <max_turns>              : Maximum turns allowed per game.");
        System.out.println("    - Usage: 'tournament -M map1,map2 -P aggressive,benevolent -G 3 -D 30'");
        System.out.println("    - Example: Runs 3 games on each map using each strategy, each limited to 30 turns.");
        System.out.println();
        System.out.println("18. Savegame:");
        System.out.println("    - Saves the current game state to a file.");
        System.out.println("    - You can later load this file to resume the game from where you left off.");
        System.out.println("    - Usage: 'savegame <filename>'");
        System.out.println("    - Example: 'savegame game1'");
        System.out.println("    - This will create a file named 'game1' containing all relevant game data.");
        System.out.println();
        System.out.println("19. Loadgame:");
        System.out.println("    - Loads a previously saved game state from a file.");
        System.out.println("    - Useful for resuming a paused or interrupted game.");
        System.out.println("    - Usage: 'loadgame <filename>'");
        System.out.println("    - Example: 'loadgame game1'");
        System.out.println("    - This will restore the game state from the file 'game1'.");
        System.out.println();
        System.out.println("20. Exit the Game:");
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
     * Sets a log message for the main game engine and prints it to the console.
     * If the log type is "phase", the message is formatted with visual separators.
     *
     * @param p_logForMainEngine the log message to be recorded
     * @param p_logType          the type of the log (e.g., "phase", "effect", etc.)
     */
    public void setD_mainEngineLog(String p_logForMainEngine,String p_logType){
        d_currentPhase.getD_currentState().updateLog(p_logForMainEngine,p_logType);
        String l_consoleMessage;
        if (p_logType.equalsIgnoreCase("phase")){
            l_consoleMessage = "\n=============================== "+p_logForMainEngine+" ===============================\n";
        }else {
            l_consoleMessage = p_logForMainEngine;
        }
        System.out.println(l_consoleMessage);
    }

    /**
     * Load phase.
     *
     * @param p_phase the p phase
     */
    public void loadPhase(Phase p_phase) {
        d_currentPhase = p_phase;
        d_currentGameState = p_phase.getD_currentState();
        getD_currentPhase().initPhase(d_isTournamentMode);
    }
}
