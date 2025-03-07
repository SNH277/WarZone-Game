package Models; // Ensure package consistency

import Controller.MapController;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Advance order test.
 */
public class AdvanceOrderTest {
    private CurrentState d_currentState;
    private Player d_player1;
    private Player d_player2;
    private Map d_map;
    private final MapController d_mapController = new MapController();

    /**
     * Setup.
     */
    @Before
    public void setup() {
        d_player1 = new Player("mehak");
        d_player2 = new Player("amir");
        d_currentState = new CurrentState();
        
        d_map = d_mapController.loadMap(d_currentState, "test.map");
        
        List<Player> l_players = new ArrayList<>();
        l_players.add(d_player1);
        l_players.add(d_player2);
        d_currentState.setD_players(l_players);
        d_currentState.setD_map(d_map);
        
        // Fetch country references once
        Country l_india = d_map.getCountryByName("India");
        Country l_china = d_map.getCountryByName("China");

        // Initialize army counts
        l_india.setD_armies(10);
        l_china.setD_armies(0);

        // Assign controlled countries
        d_player1.getD_currentCountries().add(l_india);
        d_player2.getD_currentCountries().add(l_china);
    }

    /**
     * Execute.
     */
    @Test
    public void execute() {
        Orders l_advanceOrder = new Advance("India", "China", 9, d_player1);
        l_advanceOrder.execute(d_currentState);
        
        Assert.assertEquals(Integer.valueOf(9), d_map.getCountryByName("China").getD_armies());
    }
}
