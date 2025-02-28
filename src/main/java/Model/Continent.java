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

    public Integer getD_continentID() {
        return d_continentID;
    }

    public String getD_continentName() {
        return d_continentName;
    }

    public Integer getD_continentValue() {
        return d_continentValue;
    }

    public List<Country> getD_countries() {
        return d_countries;
    }

    public void setD_continentID(Integer d_continentID) {
        this.d_continentID = d_continentID;
    }

    public void setD_continentName(String d_continentName) {
        this.d_continentName = d_continentName;
    }

    public void setD_continentValue(Integer d_continentValue) {
        this.d_continentValue = d_continentValue;
    }

    public void setD_countries(List<Country> d_countries) {
        this.d_countries = d_countries;
    }

    @Override
    public String toString() {
        return "Continent{" +
                "d_continentID=" + d_continentID +
                ", d_continentName='" + d_continentName + '\'' +
                ", d_continentValue=" + d_continentValue +
                ", d_countries=" + d_countries +
                '}';
    }
}


