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
    }

    private void addPlayer(String p_command) {
    }

}
