package View;

import Model.*;
import java.util.List;

public class MapView {
    Map d_map;
    List<Country> d_countries;
    List<Continent> d_continents;
    CurrentState d_currentState;

    public MapView(CurrentState p_currentState) {
        this.d_currentState = p_currentState;
        this.d_map = p_currentState.getD_map();
        this.d_countries = p_currentState.getD_map().getD_mapCountries();
        this.d_continents = p_currentState.getD_map().getD_mapContinents();
    }

    public void showMap() {
        System.out.println("Show Map");
    }
}
