package View;

import Model.*;

import java.util.HashMap;
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
        if (d_continents != null && !d_continents.isEmpty()) {
            System.out.println("************************************************************************************************************************");
            System.out.println("                                                   MAP VIEW - CONTINENT DETAILS                                                    ");
            System.out.println("************************************************************************************************************************");

            for (Continent l_continent : d_continents) {
                System.out.println("\n---------------------------------");
                System.out.println("Continent: " + l_continent.getD_continentName() + " (ID: " + l_continent.getD_continentID() + ")");
                System.out.println("---------------------------------");

                List<Country> l_countries = l_continent.getD_countries();
                if (l_countries != null && !l_countries.isEmpty()) {
                    HashMap<Object, Object> countryToPosition = new HashMap<>();

                    for (Country l_country : l_countries) {
                        countryToPosition.put(l_country.getD_countryID(), l_country.getD_countryName());
                    }

                    System.out.println("  +--------------------------+-------+------------+--------------------------+");
                    System.out.println("  | Country Name             | ID    | Armies     | Neighboring Countries    |");
                    System.out.println("  +--------------------------+-------+------------+--------------------------+");

                    for (int i = 0; i < l_countries.size(); i++) {
                        Country l_country = l_countries.get(i);
                        String countryName = l_country.getD_countryName();
                        int countryId = l_country.getD_countryID();
                        int armies = l_country.getD_armies();
                        String connectedCountries = "";

                        for (Integer neighborID : l_country.getD_neighbouringCountriesId()) {
                            connectedCountries += d_map.getCountryNameById(neighborID) + ", ";
                        }

                        if (connectedCountries.length() > 0) {
                            connectedCountries = connectedCountries.substring(0, connectedCountries.length() - 2);
                        }

                        System.out.printf("  | %-24s | %-5d | %-10d | %-24s |\n", countryName, countryId, armies, connectedCountries);

                        if (i == l_countries.size() - 1) {
                            System.out.println("  +--------------------------+-------+------------+--------------------------+");
                        }
                    }

                    for (Country l_country : l_countries) {
                        String countryName = l_country.getD_countryName();
                        System.out.println("\nConnections from " + countryName + " (ID: " + l_country.getD_countryID() + "):");
                        for (Integer l_neighborID : l_country.getD_neighbouringCountriesId()) {
                            String neighborName = d_map.getCountryNameById(l_neighborID);
                            System.out.println("    -> " + neighborName + " (ID: " + l_neighborID + ")");
                        }
                        System.out.println("-------------------------------------------------------------");
                    }
                }
            }
            System.out.println("************************************************************************************************************************");
        }
    }

}