package Model;

import java.util.List;

public class Map {
    String d_mapName;
    List<Country> d_mapCountries;
    List<Continent> d_mapContinents;

    public Map() {
    }

    public Map(String p_mapName, List<Country> p_mapCountries, List<Continent> p_mapContinents) {
        this.d_mapName = p_mapName;
        this.d_mapCountries = p_mapCountries;
        this.d_mapContinents = p_mapContinents;
    }

    public String getD_mapName() {
        return d_mapName;
    }

    public void setD_mapName(String d_mapName) {
        this.d_mapName = d_mapName;
    }

    public List<Continent> getD_mapContinents() {
        return d_mapContinents;
    }

    public void setD_mapContinents(List<Continent> d_mapContinents) {
        this.d_mapContinents = d_mapContinents;
    }

    public List<Country> getD_mapCountries() {
        return d_mapCountries;
    }

    public void setD_mapCountries(List<Country> d_mapCountries) {
        this.d_mapCountries = d_mapCountries;
    }

    @Override
    public String toString() {
        return "Map{" +
                "d_mapName='" + d_mapName + '\'' +
                ", d_mapCountries=" + d_mapCountries +
                ", d_mapContinents=" + d_mapContinents +
                '}';
    }
}
