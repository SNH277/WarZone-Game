package Model;

import java.util.List;

public class Continent {
    Integer d_continentID;
    String d_continentName;
    Integer d_continentValue;
    List<Country> d_countries;

    public Continent(Integer p_continentID, String p_continentName, Integer p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }
    public Continent(Integer p_continentID, Integer p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentValue = p_continentValue;
    }


}

