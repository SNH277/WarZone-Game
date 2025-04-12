package Model;

import Constants.ProjectConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CurrentState} class represents the current state of the game, including the list of players and the game map.
 * @author Disha Padsala,Shrey Hingu
 */
public class CurrentState  implements Serializable {
    /** The list of players in the game. */
    List<Player> d_players;
    /** The game map. */
    Map d_map;
    /**
     * Logger instance for recording model-related events, errors, and game logs.
     * Used for tracking state changes, debugging, and displaying execution output.
     */
    ModelLogger d_modelLogger = new ModelLogger();

    /**
     * Maximum number of turns a game can have.
     */
    int d_maxNoOfTurns = 0;

    /**
     * Number of turns remaining in the current game.
     */
    int d_noOfTurnsLeft = 0;

    /**
     * The player who won the game. Null if there is no winner yet or if the game ended in a draw.
     */
    Player d_winner;

    /**
     * List of players who failed (i.e., were eliminated or lost) during the game.
     */
    List<Player> d_playersFailed = new ArrayList<>();

    /**
     * Flag to indicate whether the game state was loaded using the load command.
     */
    Boolean d_loadCommand = false;

    /**
     * Gets the list of players in the game.
     *
     * @return The list of players.
     */
    public List<Player> getD_players() {
        return d_players;
    }

    /**
     * Sets the list of players in the game.
     *
     * @param p_players The list of players to set.
     */
    public void setD_players(List<Player> p_players) {
        this.d_players = p_players;
    }

    /**
     * Gets the game map.
     *
     * @return The game map.
     */
    public Map getD_map() {
        return d_map;
    }

    /**
     * Sets the game map.
     *
     * @param p_map The game map to set.
     */
    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    /**
     * Gets the current {@link ModelLogger} instance used for logging model-related events.
     *
     * @return the current model logger.
     */
    public ModelLogger getD_modelLogger() {
        return d_modelLogger;
    }

    /**
     * Sets the {@link ModelLogger} instance used for logging model-related events.
     *
     * @param p_modelLogger the model logger to set.
     */
    public void setD_modelLogger(ModelLogger p_modelLogger) {
        this.d_modelLogger = p_modelLogger;
    }

    /**
     * Gets the maximum number of turns for the game.
     *
     * @return the maximum number of turns.
     */
    public int getD_maxNoOfTurns() {
        return d_maxNoOfTurns;
    }

    /**
     * Sets the maximum number of turns for the game.
     *
     * @param p_maxNoOfTurns the maximum number of turns to be set.
     */
    public void setD_maxNoOfTurns(int p_maxNoOfTurns) {
        this.d_maxNoOfTurns = p_maxNoOfTurns;
    }

    /**
     * Gets the number of turns left in the game.
     *
     * @return the number of turns left.
     */
    public int getD_noOfTurnsLeft() {
        return d_noOfTurnsLeft;
    }

    /**
     * Sets the number of turns left in the game.
     *
     * @param p_noOfTurnsLeft the number of turns left to be set.
     */
    public void setD_noOfTurnsLeft(int p_noOfTurnsLeft) {
        this.d_noOfTurnsLeft = p_noOfTurnsLeft;
    }

    /**
     * Gets the winner of the game.
     *
     * @return the player who won the game.
     */
    public Player getD_winner() {
        return d_winner;
    }

    /**
     * Sets the winner of the game.
     *
     * @param p_winner the player who won the game.
     */
    public void setD_winner(Player p_winner) {
        this.d_winner = p_winner;
    }

    /**
     * Gets the list of players who failed in the game.
     *
     * @return the list of failed players.
     */
    public List<Player> getD_playersFailed() {
        return d_playersFailed;
    }

    /**
     * Sets the list of players who failed (lost or were eliminated).
     *
     * @param p_playersFailed list of players to mark as failed.
     */
    public void setD_playersFailed(List<Player> p_playersFailed) {
        this.d_playersFailed = p_playersFailed;
    }

    /**
     * Gets the load command status.
     *
     * @return true if game was loaded using a command, false otherwise.
     */
    public Boolean getD_loadCommand() {
        return d_loadCommand;
    }

    /**
     * Sets the status of the load command flag.
     *
     * @param p_loadCommand true if game was loaded using a command, false otherwise.
     */
    public void setD_loadCommand(Boolean p_loadCommand) {
        this.d_loadCommand = p_loadCommand;
    }

    /**
     * Sets log message.
     *
     * @param p_modelLoggerMessage the p model logger message
     * @param p_messageType        the p message type
     */
    public void setLogMessage(String p_modelLoggerMessage, String p_messageType) {
        d_modelLogger.setD_message(p_modelLoggerMessage, p_messageType);
    }

    /**
     * Handles the addition or removal of a player from the game.
     *
     * @param p_operation The operation to perform: "add" to add a player or "remove" to remove a player.
     * @param p_arguments The name of the player to be added or removed.
     * @throws IOException If an input or output error occurs during strategy input.
     */
    public void addOrRemovePlayer(String p_operation, String p_arguments) throws IOException {
        if (p_operation.equals("add")) {
            addPlayer(p_arguments);
        } else if (p_operation.equals("remove")) {
            removePlayer(p_arguments);
        } else {
            System.out.println(ProjectConstants.INVALID_GAMEPLAYER_COMMAND);
        }
    }


    /**
     * Removes a player from the game.
     *
     * @param p_command The name of the player to remove.
     */
    private void removePlayer(String p_command) {
        if(p_command.split((" ")).length == 1){
            String l_player = p_command.split(" ")[0];

            if(d_players == null || d_players.isEmpty()) {
                System.out.println(ProjectConstants.NO_PLAYERS);
                return;
            }

            Player l_player_found = getPlayerFromName(l_player);
            if(l_player_found != null) {
                d_players.remove(l_player_found);
                System.out.println("Player " + l_player + " removed.");
            } else {
                System.out.println("Player " + l_player + " not found.");
            }
        }
    }

    /**
     * Retrieves a player object based on the player's name.
     *
     * @param p_player The name of the player.
     * @return The player object if found, otherwise {@code null}.
     */
    public Player getPlayerFromName(String p_player) {
        for(Player l_eachPlayer : d_players) {
            if (l_eachPlayer.getD_playerName().equals(p_player)) {
                return l_eachPlayer;
            }
        }
        return null;
    }

    /**
     * Adds a player to the game.
     *
     * @param p_arguments The name of the player to add.
     */
    public void addPlayer(String p_arguments) throws IOException {
        if (p_arguments == null || p_arguments.trim().isEmpty()) {
            System.out.println("Invalid command. Please provide a player name.");
            return;
        }

        String[] l_arguments = p_arguments.trim().split(" ");
        if (l_arguments.length != 1) {
            System.out.println("Invalid number of arguments. Usage: addplayer <PlayerName>");
            return;
        }

        String l_playerName = l_arguments[0];
        if (d_players == null) {
            d_players = new ArrayList<>();
        }

        for (Player l_eachPlayer : d_players) {
            if (l_eachPlayer.getD_playerName().equalsIgnoreCase(l_playerName)) {
                System.out.println(ProjectConstants.NAME_ALREADY_EXISTS);
                return;
            }
        }

        Player l_gamePlayer = new Player(l_playerName);
        String l_playerStrategy = getPlayerStrategy(l_gamePlayer);

        switch (l_playerStrategy) {
            case "Aggressive":
                l_gamePlayer.setD_playerBehaviourStrategy(new AggressivePlayer());
                break;
            case "Benevolent":
                l_gamePlayer.setD_playerBehaviourStrategy(new BenevolentPlayer());
                break;
            case "Random":
                l_gamePlayer.setD_playerBehaviourStrategy(new RandomPlayer());
                break;
            case "Cheater":
                l_gamePlayer.setD_playerBehaviourStrategy(new CheaterPlayer());
                break;
            case "Human":
                l_gamePlayer.setD_playerBehaviourStrategy(new HumanPlayer());
                break;
            default:
                updateLog("Invalid player strategy entered.", "error");
                return;
        }

        d_players.add(l_gamePlayer);
        System.out.println("Player: " + l_playerName + " added to the game with Strategy: " + l_playerStrategy);
    }


    /**
     * Updates the model log with a new message and log type.
     * Delegates the logging to the {@link ModelLogger} instance.
     *
     * @param p_logMessage the message to be logged.
     * @param p_logType    the type of the log (e.g., "start", "effect", "error", "end").
     */
    public void updateLog(String p_logMessage,String p_logType){
        d_modelLogger.setD_message(p_logMessage,p_logType);
    }

    /**
     * Gets player strategy.
     *
     * @param p_gamePlayer the p game player
     * @return the player strategy
     * @throws IOException the io exception
     */
    private String getPlayerStrategy(Player p_gamePlayer) throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        String l_playerStrategy;
        while (true) {
            System.out.println("Please enter the strategy for player: " + p_gamePlayer.getD_playerName() +
                    " (Aggressive, Benevolent, Random, Cheater, Human):");
            l_playerStrategy = l_reader.readLine().trim();
            if (!ProjectConstants.PLAYER_BEHAVIOR.contains(l_playerStrategy)) {
                this.updateLog("Invalid player strategy entered.", "error");
                return getPlayerStrategy(p_gamePlayer);
            }
            return l_playerStrategy;
        }
    }


}
