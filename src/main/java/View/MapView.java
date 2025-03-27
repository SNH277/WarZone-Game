package View;
import Model.*;
import java.util.HashMap;
import java.util.List;

/**
 * The {@code MapView} class provides a visual representation of the game map, including continents and countries.
 * @author Disha Padsala
 */
public class MapView {
    /** The game map. */
    Map d_map;
    /** The list of countries in the map. */
    List<Country> d_countries;
    /** The list of continents in the map. */
    List<Continent> d_continents;
    /** The current state of the game. */
    CurrentState d_currentState;

    /**
     * Constructs a {@code MapView} object based on the current state of the game.
     *
     * @param p_currentState The current game state.
     */
    public MapView(CurrentState p_currentState) {
        this.d_currentState = p_currentState;
        this.d_map = p_currentState.getD_map();
        this.d_countries = p_currentState.getD_map().getD_mapCountries();
        this.d_continents = p_currentState.getD_map().getD_mapContinents();
    }

    /**
     * Displays the map, including continents, countries, armies, and neighboring countries.
     */
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
                    HashMap<Object, Object> l_countryToPosition = new HashMap<>();

                    for (Country l_country : l_countries) {
                        l_countryToPosition.put(l_country.getD_countryID(), l_country.getD_countryName());
                    }

                    System.out.println("  +--------------------------+-------+------------+--------------------------+");
                    System.out.println("  | Country Name             | ID    | Armies     | Neighboring Countries    |");
                    System.out.println("  +--------------------------+-------+------------+--------------------------+");

                    for (int i = 0; i < l_countries.size(); i++) {
                        Country l_country = l_countries.get(i);
                        String l_countryName = l_country.getD_countryName();
                        int l_countryId = l_country.getD_countryID();
                        int l_armies = l_country.getD_armies();
                        String l_connectedCountries = "";

                        for (Integer l_neighborID : l_country.getD_neighbouringCountriesId()) {
                            l_connectedCountries += d_map.getCountryNameById(l_neighborID) + ", ";
                        }

                        if (l_connectedCountries.length() > 0) {
                            l_connectedCountries = l_connectedCountries.substring(0, l_connectedCountries.length() - 2);
                        }

                        System.out.printf("  | %-24s | %-5d | %-10d | %-24s |\n", l_countryName, l_countryId, l_armies, l_connectedCountries);

                        if (i == l_countries.size() - 1) {
                            System.out.println("  +--------------------------+-------+------------+--------------------------+");
                        }
                    }

                    for (Country l_country : l_countries) {
                        String l_countryName = l_country.getD_countryName();
                        System.out.println("\nConnections from " + l_countryName + " (ID: " + l_country.getD_countryID() + "):");
                        for (Integer l_neighborID : l_country.getD_neighbouringCountriesId()) {
                            String l_neighborName = d_map.getCountryNameById(l_neighborID);
                            System.out.println("    -> " + l_neighborName + " (ID: " + l_neighborID + ")");
                        }
                        System.out.println("-------------------------------------------------------------");
                    }
                }
            }
            System.out.println("************************************************************************************************************************");
        }
    }
}