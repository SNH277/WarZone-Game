package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public boolean validateMap() {
        return true;
    }

    public void addContinent(String p_mapContinentName, Integer p_continentValue) {
        if (d_mapContinents == null) {
            d_mapContinents = new ArrayList<>();
        }

        for (Continent l_continent : d_mapContinents) {
            if (l_continent.getD_continentName().equals(p_mapContinentName)) {
                System.out.println("Continent: " + p_mapContinentName + " already exists.");
                return;
            }
        }

        int l_mapContinentId = (d_mapContinents.isEmpty()) ? 1 : getMaxContinentID() + 1;

        Continent l_newContinent = new Continent(l_mapContinentId, p_mapContinentName, p_continentValue);
        d_mapContinents.add(l_newContinent);
        System.out.println(d_mapContinents);

        System.out.println("Continent " + p_mapContinentName + " added successfully!");
    }

    private int getMaxContinentID() {
        if (d_mapContinents == null || d_mapContinents.isEmpty()) {
            return 0;
        }

        int l_max = 0;
        for (Continent l_eachContinent : d_mapContinents) {
            if (l_eachContinent.getD_continentID() > l_max) {
                l_max = l_eachContinent.getD_continentID();  // Update max ID
            }
        }
        return l_max;
    }
}
