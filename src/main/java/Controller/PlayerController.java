package Controller;

import Model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
/**
 * Controller Class PlayerController.
 * <p>
 * This class handles the assignment of countries to players, the distribution of armies,
 * and the creation and validation of deploy orders.
 * </p>
 */
public class PlayerController {
    /**
     * Assigns countries to players in the current state.
     * <p>
     * This method checks for the existence of players and a valid map, distributes the countries randomly among players,
     * displays the assigned countries, and updates the players' continent ownership.
     * </p>
     *
     * @param p_currentState the current state containing players and map data
     */
    public void assignCountry(CurrentState p_currentState) {
        if(p_currentState.getD_players() == null || p_currentState.getD_players().isEmpty()) {
            System.out.println("No players found. Please add players using 'gameplayer -add playerName' command first.");
            return;
        }
        if(p_currentState.getD_map() == null || p_currentState.getD_map().getD_mapCountries() == null) {
            System.out.println("Map data is missing. Ensure a valid map is loaded before assigning countries");
        }

        List<Player> l_players = p_currentState.getD_players();
        List<Country> l_countries = p_currentState.getD_map().getD_mapCountries();

        int l_playerCount = l_players.size();
        int l_countryCount = l_countries.size();
        if (l_playerCount > l_countryCount) {
            System.out.println("More players than available countries. Reduce player count or add more countries.");
        }

        int l_countriesPerPlayer = Math.floorDiv(l_countryCount, l_playerCount);

        randomCountryDistribution(l_players, l_countries, l_countriesPerPlayer);

        displayAssignedCountries(l_players);

        updatePlayerContinentOwnership(l_players, p_currentState.getD_map().getD_mapContinents());
    }
    /**
     * Updates the continent ownership for each player.
     * <p>
     * For each player, if the player owns all countries in a continent,
     * that continent is assigned to the player.
     * </p>
     *
     * @param p_Players      the list of players
     * @param p_MapContinents the list of continents in the map
     */
    private void updatePlayerContinentOwnership(List<Player> p_Players, List<Continent> p_MapContinents) {
        for(Player l_player : p_Players) {
            List<Country> l_countriesOwnedByPlayer = l_player.getD_currentCountries();
            if(l_countriesOwnedByPlayer == null || l_countriesOwnedByPlayer.isEmpty()) {
                continue;
            }
            for(Continent l_continent : p_MapContinents) {
                if(new HashSet<>(l_countriesOwnedByPlayer).containsAll(l_continent.getD_countries())) {
                    l_player.setContinent(l_continent);
                }
            }
        }
    }
    /**
     * Displays the assigned countries for each player.
     * <p>
     * This method prints out the list of countries assigned to each player.
     * </p>
     *
     * @param p_Players the list of players whose assigned countries are to be displayed
     */
    private void displayAssignedCountries(List<Player> p_Players) {
        for(Player l_currentPlayer : p_Players) {
            StringBuilder l_output = new StringBuilder("Player " + l_currentPlayer.getD_playerName() + " has assigned countries: ");

            List<Country> l_countries = l_currentPlayer.getD_currentCountries();
            if(l_countries == null || l_countries.isEmpty()) {
                l_output.append("No countries assigned.");
            } else {
                for(Country l_country : l_countries) {
                    l_output.append(l_country.getD_countryName()).append(" ");
                }
            }
            System.out.println(l_output.toString().trim());
        }
    }
    /**
     * Distributes countries randomly among players.
     * <p>
     * This method assigns a specified number of countries per player randomly.
     * If unassigned countries remain, it recursively distributes one additional country per player until all countries are assigned.
     * </p>
     *
     * @param p_Players          the list of players
     * @param p_Countries        the list of countries to distribute
     * @param p_CountriesPerPlayer the number of countries to assign to each player
     */
    private void randomCountryDistribution(List<Player> p_Players, List<Country> p_Countries, int p_CountriesPerPlayer) {
        List<Country> l_unassignedCountries = new ArrayList<>(p_Countries);

        if(l_unassignedCountries.isEmpty()) {
            System.out.println("No Countries in the map.");
            return;
        }
        Random rand = new Random();

        for (Player l_Player : p_Players) {
            if(l_Player.getD_currentCountries() == null) {
                l_Player.setD_currentCountries(new ArrayList<>());
            }
            for (int i = 0; i < p_CountriesPerPlayer && !l_unassignedCountries.isEmpty(); i++) {
                int l_randomIndex = rand.nextInt(l_unassignedCountries.size());
                l_Player.getD_currentCountries().add(l_unassignedCountries.get(l_randomIndex));
                l_unassignedCountries.remove(l_randomIndex);
            }
        }

        if(!l_unassignedCountries.isEmpty()) {
            randomCountryDistribution(p_Players, l_unassignedCountries, 1);
        }
    }
    /**
     * Calculates and returns the number of armies to be allocated to a player.
     * <p>
     * The number of armies is determined by the number of countries owned by the player (with a minimum of 3)
     * plus any bonus armies from owning continents.
     * </p>
     *
     * @param p_Player the player for whom to calculate the armies
     * @return the calculated number of armies for the player
     */
    public int getNumberOfArmies(Player p_Player) {
        int l_currentArmySize = 0;
        if (p_Player.getD_currentCountries() != null && !p_Player.getD_currentCountries().isEmpty()){
            l_currentArmySize = Math.max(3, Math.round((float)(p_Player.getD_currentCountries().size() / 3)));
        }
        if (p_Player.getD_currentContinents() != null && !p_Player.getD_currentContinents().isEmpty()){
            int l_ContinentValue = 0;
            for (Continent l_continent : p_Player.getD_currentContinents()) {
                l_ContinentValue = l_continent.getD_continentValue();
            }
            l_currentArmySize += l_ContinentValue;
        }
        return l_currentArmySize;
    }
    /**
     * Assigns armies to each player based on the number of countries and continent bonuses.
     * <p>
     * For each player in the current state, this method calculates the number of armies using {@link #getNumberOfArmies(Player)}
     * and sets the unallocated armies for the player.
     * </p>
     *
     * @param p_CurrentState the current state containing the players
     */
    public void assignArmies(CurrentState p_CurrentState) {
        List<Player> l_players = p_CurrentState.getD_players();
        if(l_players == null || l_players.isEmpty()) {
            System.out.println("No players found.");
            return;
        }
        for (Player l_player : l_players) {
            int l_NumberOfArmiesPerPlayer = getNumberOfArmies(l_player);
            l_player.setD_unallocatedArmies(l_NumberOfArmiesPerPlayer);
            System.out.println("Player" + l_player.getD_playerName() + " got assigned: " + l_NumberOfArmiesPerPlayer + " armies.");
        }
    }
    /**
     * Creates and adds a deploy order for the specified player.
     * <p>
     * This method parses the order string to extract the target country and the number of armies to deploy.
     * It then validates the player's country ownership and available armies before adding the order to the player's order queue.
     * </p>
     *
     * @param p_OrderName the deploy order command string (expected format: "deploy <country> <number>")
     * @param p_Player    the player creating the deploy order
     */
    public void createDeployOrder(String p_OrderName, Player p_Player) {
        List<Orders> l_orders;
        if(p_Player.getD_orders() == null || p_Player.getD_orders().isEmpty()) {
            l_orders = new ArrayList<>();
        } else {
            l_orders = p_Player.getD_orders();
        }

        String l_countryName = p_OrderName.split(" ")[1];
        int l_numberOfArmiesToDeploy = Integer.parseInt(p_OrderName.split(" ")[2]);

        if(!validateCountryOwnership(p_Player, l_countryName)){
            System.out.println("Country " + l_countryName + " is not owned by " + p_Player.getD_playerName());
        } else if (!hasSufficientArmies(p_Player, l_numberOfArmiesToDeploy)) {
            System.out.println("Player does not have enough armies to deploy.");
        } else {
            Orders l_order = new Orders(p_OrderName.split(" ")[0], l_countryName, l_numberOfArmiesToDeploy);
            l_orders.add(l_order);
            p_Player.setD_orders(l_orders);

            Integer l_unallocatedArmies = p_Player.getD_unallocatedArmies() - l_numberOfArmiesToDeploy;
            p_Player.setD_unallocatedArmies(l_unallocatedArmies);

            System.out.println("Order is added to queue for execution.");
        }

    }
    /**
     * Checks if the player has sufficient unallocated armies to deploy.
     *
     * @param p_Player               the player to check
     * @param p_NumberOfArmiesToDeploy the number of armies intended for deployment
     * @return true if the player has enough armies, false otherwise
     */
    private boolean hasSufficientArmies(Player p_Player, int p_NumberOfArmiesToDeploy) {
        if(p_Player.getD_unallocatedArmies() >= p_NumberOfArmiesToDeploy) {
            return true;
        }
        return false;
    }
    /**
     * Validates whether the specified country is owned by the player.
     *
     * @param p_Player     the player whose ownership is to be verified
     * @param p_CountryName the name of the country to validate
     * @return true if the player owns the country, false otherwise
     */
    private boolean validateCountryOwnership(Player p_Player, String p_CountryName) {
        for(Country l_country : p_Player.getD_currentCountries()) {
            if(l_country.getD_countryName().equals(p_CountryName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if any player has unallocated armies.
     *
     * @param p_currentState the current state containing the players
     * @return true if at least one player has unallocated armies, false otherwise
     */
    public boolean isUnallocatedArmiesExist(CurrentState p_currentState) {
        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if (l_eachPlayer.getD_unallocatedArmies() > 0) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if any player has unexecuted orders.
     *
     * @param p_currentState the current state containing the players
     * @return true if at least one player has pending orders, false otherwise
     */
    public boolean isUnexecutedOrdersExist(CurrentState p_currentState) {
        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if (l_eachPlayer.getD_orders().size() > 0) {
                return true;
            }
        }
        return false;
    }
}
