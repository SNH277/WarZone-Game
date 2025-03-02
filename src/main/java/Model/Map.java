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

    public void addCountry(String p_countryName, String p_continentName) {
        if (d_mapCountries == null) {
            d_mapCountries = new ArrayList<>();
        }

        if (getCountryByName(p_countryName) != null) {
            System.out.println("Country '" + p_countryName + "' already exists.");
            return;
        }

        int l_continentID = getContinentIDByName(p_continentName);
        if (l_continentID == -1) {
            System.out.println("Continent '" + p_continentName + "' does not exist.");
            return;
        }

        int l_countryID = getMaxCountryID() + 1;
        Country l_newCountry = new Country(l_countryID, p_countryName, l_continentID);
        d_mapCountries.add(l_newCountry);

        if (d_mapContinents != null) {
            for (Continent l_continent : d_mapContinents) {
                if (l_continent.getD_continentID() == l_continentID) {
                    l_continent.addCountry(l_newCountry);
                    break;
                }
            }
        }

        System.out.println("Country '" + p_countryName + "' added successfully!");
    }

    public Country getCountryByName(String p_countryName) {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            return null;
        }

        for (Country l_country : d_mapCountries) {
            if (l_country.getD_countryName().equalsIgnoreCase(p_countryName)) {
                return l_country;
            }
        }

        return null;
    }

    private int getMaxCountryID() {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            return 0;
        }

        int l_max = 0;
        for (Country l_eachCountry : d_mapCountries) {
            if (l_eachCountry.getD_countryID() > l_max) {
                l_max = l_eachCountry.getD_countryID();
            }
        }
        return l_max;
    }

    private int getContinentIDByName(String p_continentName) {
        if (d_mapContinents == null || d_mapContinents.isEmpty()) {
            return -1;
        }

        for (Continent l_eachContinent : d_mapContinents) {
            if (l_eachContinent.getD_continentName().equals(p_continentName)) {
                return l_eachContinent.getD_continentID();
            }
        }

        return -1;
    }

    public void removeCountry(String p_removeCountryName) {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            System.out.println("Country: " + p_removeCountryName + " does not exist.");
            return;
        }

        Country l_countryToRemove = getCountryByName(p_removeCountryName);
        if (l_countryToRemove == null) {
            System.out.println("Country: " + p_removeCountryName + " does not exist.");
            return;
        }

        if (d_mapContinents != null) {
            for (Continent l_eachContinent : d_mapContinents) {
                if (Objects.equals(l_eachContinent.getD_continentID(), l_countryToRemove.getD_continentID())) {
                    l_eachContinent.removeCountry(l_countryToRemove);
                    break;
                }
            }
        }

        removeCountryFromNeighbours(l_countryToRemove);
        d_mapCountries.remove(l_countryToRemove);
        System.out.println("Country: " + p_removeCountryName + " removed successfully.");
    }

    private void removeCountryFromNeighbours(Country p_country) {
        List<Integer> neighbourIDs = p_country.getD_neighbouringCountriesId();

        for (Integer neighbourID : neighbourIDs) {
            Country neighbourCountry = getCountryById(neighbourID);

            if (neighbourCountry != null) {
                neighbourCountry.getD_neighbouringCountriesId().remove(p_country.getD_countryID());
            }
        }

        p_country.getD_neighbouringCountriesId().clear();
    }

    private Country getCountryById(int p_countryID) {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            return null;
        }

        for (Country country : d_mapCountries) {
            if (country.getD_countryID() == p_countryID) {
                return country;
            }
        }

        return null;
    }

    public void addNeighbour(int p_countryID, int p_neighbourID) {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            System.out.println("No countries exist in the map.");
            return;
        }

        Country l_country1 = getCountryById(p_countryID);
        Country l_country2 = getCountryById(p_neighbourID);

        if (l_country1 == null || l_country2 == null) {
            if (l_country1 == null) {
                System.out.println("Country with ID: " + p_countryID + " does not exist.");
            }
            if (l_country2 == null) {
                System.out.println("Country with ID: " + p_neighbourID + " does not exist.");
            }
            return;
        }

        l_country1.addCountryNeighbour(p_neighbourID);
        l_country2.addCountryNeighbour(p_countryID);

        System.out.println("Country " + p_neighbourID + " added as a neighbor to " + p_countryID);
        System.out.println("Country " + p_countryID + " added as a neighbor to " + p_neighbourID);
    }

    public void removeNeighbour(int p_countryID, int p_neighbourID) {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            System.out.println("No country in Map.");
            return;
        }

        Country l_country = getCountryById(p_countryID);
        Country l_neighbour = getCountryById(p_neighbourID);

        if (l_country == null || l_neighbour == null) {
            if (l_country == null) {
                System.out.println("Country with ID: " + p_countryID + " does not exist in the Map.");
            }
            if (l_neighbour == null) {
                System.out.println("Country with ID: " + p_neighbourID + " does not exist in the Map.");
            }
            return; // Stop execution if either country doesn't exist
        }

        l_country.removeCountryNeighbour(p_neighbourID);
        l_neighbour.removeCountryNeighbour(p_countryID);

        System.out.println("Country " + p_neighbourID + " removed as a neighbor from " + p_countryID);
        System.out.println("Country " + p_countryID + " removed as a neighbor from " + p_neighbourID);
    }

    private Country getCountryById(Integer p_countryID) {
        if (d_mapCountries == null || d_mapCountries.isEmpty()) {
            return null;
        }

        for (Country l_country : d_mapCountries) {
            if (Objects.equals(l_country.getD_countryID(), p_countryID)) {
                return l_country;
            }
        }
        return null;
    }

}
