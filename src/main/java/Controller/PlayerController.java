package Controller;

import Constants.ProjectConstants;
import Model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
/**
 * The PlayerController class manages player actions such as assigning countries,
 * distributing armies, and creating orders.
 * @author Disha Padsala,Shrey Hingu,Akhilesh Kanbarkar
 */
public class PlayerController implements Serializable {

    /**
     * The D current state.
     */
    CurrentState d_currentState = new CurrentState();

    /**
     * Default constructor for PlayerController.
     */
    public PlayerController() {
    }

    /**
     * Assigns countries to players based on the number of available players and countries.
     *
     * @param p_currentState The current game state containing players and the map.
     */
    public boolean assignCountry(CurrentState p_currentState) {
        if(p_currentState.getD_players() == null || p_currentState.getD_players().isEmpty()) {
            System.out.println(ProjectConstants.NO_PLAYERS);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.NO_PLAYERS, "effect");
            return false;
        }
        if(p_currentState.getD_map() == null || p_currentState.getD_map().getD_mapCountries() == null) {
            System.out.println(ProjectConstants.MAP_NOT_AVAILABLE);
            return false;
        }

        List<Player> l_players = p_currentState.getD_players();
        List<Country> l_countries = p_currentState.getD_map().getD_mapCountries();

        int l_playerCount = l_players.size();
        int l_countryCount = l_countries.size();

        Player l_neutralPlayer = null;
        for(Player l_eachPlayer : p_currentState.getD_players()){
            if(l_eachPlayer.getD_playerName().equalsIgnoreCase("Neutral")){
                l_neutralPlayer = l_eachPlayer;
                break;
            }
        }
        if(l_neutralPlayer != null){
            l_playerCount--;
        }

        if (l_playerCount > l_countryCount) {
            System.out.println(ProjectConstants.MORE_PLAYERS_THAN_COUNTRIES);
            return false;
        }

        int l_countriesPerPlayer = Math.floorDiv(l_countryCount, l_playerCount);

        randomCountryDistribution(l_players, l_countries, l_countriesPerPlayer);

        displayAssignedCountries(l_players);

        updatePlayerContinentOwnership(l_players, p_currentState.getD_map().getD_mapContinents());
        return true;
    }
    /**
     * Updates the player's ownership of continents based on country ownership.
     *
     * @param p_Players The list of players.
     * @param p_MapContinents The list of continents in the map.
     */
    public void updatePlayerContinentOwnership(List<Player> p_Players, List<Continent> p_MapContinents) {
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
     *
     * @param p_Players The list of players.
     */
    public void displayAssignedCountries(List<Player> p_Players) {
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
     *
     * @param p_Players The list of players.
     * @param p_Countries The list of countries.
     * @param p_CountriesPerPlayer The number of countries per player.
     */
    public void randomCountryDistribution(List<Player> p_Players, List<Country> p_Countries, int p_CountriesPerPlayer) {
        List<Country> l_unassignedCountries = new ArrayList<>(p_Countries);

        if(l_unassignedCountries.isEmpty()) {
            System.out.println(ProjectConstants.NO_COUNTRIES);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.NO_COUNTRIES,"effect");
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
     * Gets the number of armies assigned to a player.
     *
     * @param p_Player The player.
     * @return The number of armies.
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
     * Assigns armies to players based on the number of countries they own and continent bonuses.
     *
     * @param p_CurrentState The current game state containing players.
     */
    public void assignArmies(CurrentState p_CurrentState) {
        List<Player> l_players = p_CurrentState.getD_players();
        if(l_players == null || l_players.isEmpty()) {
            System.out.println(ProjectConstants.NO_PLAYERS);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.NO_PLAYERS,"effect");
            return;
        }
        for (Player l_player : l_players) {
            int l_NumberOfArmiesPerPlayer = getNumberOfArmies(l_player);
            l_player.setD_unallocatedArmies(l_NumberOfArmiesPerPlayer);
            System.out.println("Player" + l_player.getD_playerName() + " got assigned: " + l_NumberOfArmiesPerPlayer + " armies.");
        }
    }

    /**
     * Checks if the player has sufficient armies to deploy.
     *
     * @param p_Player The player issuing the deploy order.
     * @param p_NumberOfArmiesToDeploy The number of armies the player wants to deploy.
     * @return True if the player has enough unallocated armies, otherwise false.
     */
    private boolean hasSufficientArmies(Player p_Player, int p_NumberOfArmiesToDeploy) {
        return p_Player.getD_unallocatedArmies() >= p_NumberOfArmiesToDeploy;
    }
    /**
     * Validates if the player owns the specified country.
     *
     * @param p_Player The player whose country ownership is being checked.
     * @param p_CountryName The name of the country to validate.
     * @return True if the player owns the country, otherwise false.
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
     * @param p_currentState The current game state.
     * @return True if unallocated armies exist, otherwise false.
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
     * Checks if any player in the given list has unexecuted orders.
     *
     * @param p_playerList the list of players to check for unexecuted orders
     * @return true if any player has unexecuted orders, false otherwise
     */
    public boolean isUnexecutedOrdersExist(List<Player> p_playerList) {
        // Iterate through each player to check if they have unexecuted orders
        for (Player l_eachPlayer : p_playerList) {
            // If the player has unexecuted orders, return true immediately
            if (!l_eachPlayer.getD_orders().isEmpty()) {
                return true;
            }
        }
        // Return false if no player has unexecuted orders
        return false;
    }

    /**
     * Checks whether any player in the provided list has more orders to issue.
     *
     * @param p_players the list of players to check.
     * @return {@code true} if at least one player has more orders to issue; {@code false} otherwise.
     */
    public boolean checkForMoreOrders(List<Player> p_players) {
        for(Player l_eachPlayer : p_players){
            if(l_eachPlayer.isD_moreOrders()){
                return true;
            }
        }
        return false;
    }

    /**
     * Assigns continents to players if they own all the countries within a continent.
     * A player is granted ownership of a continent if they already own all the countries in it.
     *
     * @param p_players        the list of players to evaluate.
     * @param p_mapContinents  the list of all continents in the game map.
     */
    public void assignContinentToPlayers(List<Player> p_players, List<Continent> p_mapContinents) {
        for (Player l_eachPlayer : p_players) {
            List<Country> l_countriesOwnedByPlayer = l_eachPlayer.getD_currentCountries();

            if (l_countriesOwnedByPlayer == null || l_countriesOwnedByPlayer.isEmpty()) {
                continue; // Skip players who own no countries
            }

            for (Continent l_eachContinent : p_mapContinents) {
                if (l_eachPlayer.getD_currentContinents().contains(l_eachContinent)) {
                    continue; // Skip if the player already owns this continent
                }

                boolean l_isContinentOwned = l_countriesOwnedByPlayer.containsAll(l_eachContinent.getD_countries());
                if (l_isContinentOwned) {
                    l_eachPlayer.getD_currentContinents().add(l_eachContinent);
                }
            }
        }
    }

    /**
     * Resets the player flags for the next turn.
     *
     * - Players (except Neutral) can issue more orders.
     * - Each player's "one card per turn" flag is reset.
     * - Any ongoing negotiations are reset.
     *
     * @param p_playerList List of players whose flags need to be reset.
     */
    public void resetPlayerFlag(List<Player> p_playerList) {
        for (Player l_eachPlayer : p_playerList) {
            // Only non-neutral players can issue more orders
            if (!l_eachPlayer.getD_playerName().equalsIgnoreCase("Neutral")) {
                l_eachPlayer.setD_moreOrders(true);
            }

            // Reset other flags for all players
            l_eachPlayer.setD_oneCardPerTurn(false);
            l_eachPlayer.resetNegotiation();
        }
    }


}
