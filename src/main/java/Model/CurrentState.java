package Model;

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
}
