package Model;

import Constants.ProjectConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code CurrentState} class represents the current state of the game, including the list of players and the game map.
 */
public class CurrentState {
    /** The list of players in the game. */
    List<Player> d_players;
    /** The game map. */
    Map d_map;
    ModelLogger d_modelLogger = new ModelLogger();

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

    public ModelLogger getD_modelLogger() {
        return d_modelLogger;
    }

    public void setD_modelLogger(ModelLogger p_modelLogger) {
        this.d_modelLogger = p_modelLogger;
    }

    /**
     * Adds or removes a player based on the specified action.
     *
     * @param p_action The action to perform ("add" or "remove").
     * @param p_player The name of the player to add or remove.
     */
    public void addOrRemovePlayer(String p_action, String p_player) {
        if(p_action.equals("add")) {
            addPlayer(p_player);
        }
        else if(p_action.equals("remove")) {
            removePlayer(p_player);
        }
        else {
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
     * @param p_command The name of the player to add.
     */
    private void addPlayer(String p_command) {
        if(p_command.split((" ")).length == 1){
            String l_player = p_command.split(" ")[0];

            if(d_players == null) {
                d_players = new ArrayList<Player>();
            } else if (d_players.stream().anyMatch(p -> p.getD_playerName().equals(l_player))) {
                System.out.println("Player " + l_player + " already exists.");
                return;
            }

            d_players.add(new Player(l_player));
            System.out.println("Player " + l_player + " added.");
        }
    }

    public void updateLog(String p_logMessage,String p_logType){
        d_modelLogger.setD_message(p_logMessage,p_logType);
    }

}
