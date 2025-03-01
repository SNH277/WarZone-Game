package Model;

import java.util.ArrayList;
import java.util.List;

public class CurrentState {
    List<Player> d_players;
    Map d_map;

    public List<Player> getD_players() {
        return d_players;
    }

    public void setD_players(List<Player> p_players) {
        this.d_players = p_players;
    }

    public Map getD_map() {
        return d_map;
    }

    public void setD_map(Map p_map) {
        this.d_map = p_map;
    }

    public void addOrRemovePlayer(String p_action, String p_player) {
        if(p_action.equals("add")) {
            addPlayer(p_player);
        }
        else if(p_action.equals("remove")) {
            removePlayer(p_player);
        }
        else {
            System.out.println("Command is invalid. To add or remover player, use '-add playerName' or '-remove playerName'.");
        }
    }

    private void removePlayer(String p_command) {
        if(p_command.split((" ")).length == 1){
            String l_player = p_command.split(" ")[0];

            if(d_players == null || d_players.isEmpty()) {
                System.out.println("No players found.");
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

    private Player getPlayerFromName(String p_player) {
        for(Player l_eachPlayer : d_players) {
            if (l_eachPlayer.getD_playerName().equals(p_player)) {
                return l_eachPlayer;
            }
        }
        return null;
    }

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

}
