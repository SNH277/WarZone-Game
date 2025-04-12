package Model;

import Controller.PlayerController;

import java.io.IOException;
import java.util.*;

/**
 * Represents the "Cheater" strategy for a player.
 * A cheater player automatically conquers neighboring enemy countries and doubles armies
 * on its countries that have enemy neighbors. It does not follow the typical order creation rules.
 * This behavior is intended to simulate an unfair advantage.
 */
public class CheaterPlayer extends PlayerBehaviourStrategy{

    /**
     * Default constructor.
     */
    public CheaterPlayer(){

    }

    /**
     * Gets the name of the player behavior strategy.
     *
     * @return "Cheater" as the strategy name.
     */
    @Override
    public String getPlayerBehaviour() {
        return "Cheater";
    }

    /**
     * Executes the cheater strategy: auto-deploys remaining armies, conquers all neighboring enemy countries,
     * and doubles armies on countries with enemy neighbors.
     *
     * @param p_player        The player executing the behavior.
     * @param p_currentState  The current state of the game.
     * @return null (as no standard order string is returned).
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public String createOrder(Player p_player, CurrentState p_currentState) throws IOException {
        if(p_player.getD_unallocatedArmies() !=0) {
            while (p_player.getD_unallocatedArmies() > 0) {
                Random l_random = new Random();
                Country l_randomCountry = getRandomCountry(p_player.getD_currentCountries());
                int l_armiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmies()) + 1;
                l_randomCountry.setD_armies(l_armiesToDeploy);
                p_player.setD_unallocatedArmies(p_player.getD_unallocatedArmies() - l_armiesToDeploy);

                String l_logMessage = "Cheater Player: " + p_player.getD_playerName() +
                        " assigned " + l_armiesToDeploy +
                        " armies to  " + l_randomCountry.getD_countryName();

                d_currentState.updateLog(l_logMessage, "effect");
            }
        }
        try{
            conquerNeighboringEnemies(p_player, p_currentState);
        } catch (ConcurrentModificationException l_e) {
        }

        doubleArmyOnEnemyNeighbourCountries(p_player, p_currentState);

        p_player.checkForMoreOrder(true);
        return null;
    }

    /**
     * Doubles armies on countries owned by the player that have enemy neighbors.
     *
     * @param p_player       The cheater player.
     * @param p_currentState The current state of the game.
     */
    private void doubleArmyOnEnemyNeighbourCountries(Player p_player, CurrentState p_currentState) {
        List<Country> l_countriesOwned = p_player.getD_currentCountries();

        for(Country l_eachCountry : l_countriesOwned){
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_eachCountry);

            if(l_countryEnemies.size() == 0) {
                continue;
            }
            Integer l_armies = l_eachCountry.getD_armies();

            if(l_armies == 0) {
                continue;
            }
            l_eachCountry.setD_armies(l_armies * 2);
            String l_logMessage = "Cheater Player: " + p_player.getD_playerName() +
                    " doubled the armies on " + l_eachCountry.getD_countryName();

            p_currentState.updateLog(l_logMessage,"effect");
        }
    }

    /**
     * Automatically conquers all enemy countries neighboring the player’s owned countries.
     *
     * @param p_player       The cheater player.
     * @param p_currentState The current game state.
     */
    private void conquerNeighboringEnemies(Player p_player, CurrentState p_currentState) {
        List<Country> l_countriesOwned = p_player.getD_currentCountries();

        for(Country l_eachCountry : l_countriesOwned){
            ArrayList<Integer> l_countryEnemies = getEnemies(p_player, l_eachCountry);

            for(Integer l_enemyId: l_countryEnemies) {
                Map l_map = p_currentState.getD_map();
                Player l_enemyCountryOwner = getCountryOwner(p_currentState, l_enemyId);
                Country l_enemyCountry =l_map.getCountryById(l_enemyId);
                conquerTargetCountry(p_currentState, l_enemyCountryOwner, p_player, l_enemyCountry);
                String l_logMessage = "Cheater Player: " + p_player.getD_playerName() +
                        " conquered " + l_enemyCountry.getD_countryName() +
                        " from " + l_enemyCountryOwner.getD_playerName();
                p_currentState.updateLog(l_logMessage, "effect");
            }
        }
    }

    /**
     * Transfers a country from the enemy to the cheater player.
     *
     * @param p_currentState        The current game state.
     * @param p_enemyCountryOwner   The current owner of the target country.
     * @param p_player              The cheater player.
     * @param p_enemyCountry        The country being conquered.
     */
    private void conquerTargetCountry(CurrentState p_currentState, Player p_enemyCountryOwner, Player p_player, Country p_enemyCountry) {
        p_enemyCountryOwner.getD_currentCountries().remove(p_enemyCountry);
        p_player.getD_currentCountries().add(p_enemyCountry);
        updateContinents(p_player, p_enemyCountryOwner, p_currentState);
    }

    /**
     * Reassigns continent control after a country transfer.
     *
     * @param p_player              The cheater player.
     * @param p_enemyCountryOwner  The previous owner.
     * @param p_currentState       The current game state.
     */
    private void updateContinents(Player p_player, Player p_enemyCountryOwner, CurrentState p_currentState) {
        List<Player> l_players = new ArrayList<>();
        p_player.setD_currentContinents(Collections.emptySet());
        p_enemyCountryOwner.setD_currentContinents(Collections.emptySet());
        l_players.add(p_player);
        l_players.add(p_enemyCountryOwner);

        PlayerController l_playerController = new PlayerController();
        l_playerController.assignContinentToPlayers(l_players, p_currentState.getD_map().getD_mapContinents());
    }

    /**
     * Finds the owner of a given country.
     *
     * @param p_currentState The current game state.
     * @param l_enemyId      The ID of the country.
     * @return The player who owns the country.
     */
    private Player getCountryOwner(CurrentState p_currentState, Integer l_enemyId) {
        List<Player> l_players = p_currentState.getD_players();
        Player l_owner = null;
        for(Player l_eachPlayer : l_players){
            List<Integer> l_countriesOwned = l_eachPlayer.getCountryIDs();
            if(l_countriesOwned.contains(l_enemyId)){
                l_owner = l_eachPlayer;
                break;
            }
        }
        return l_owner;
    }

    /**
     * Retrieves IDs of enemy countries adjacent to the given country.
     *
     * @param p_player       The cheater player.
     * @param p_eachCountry  The country to check neighbors for.
     * @return A list of neighboring enemy country IDs.
     */
    private ArrayList<Integer> getEnemies(Player p_player, Country p_eachCountry) {
        ArrayList<Integer> l_enemyCountries = new ArrayList<>();

        for(Integer l_countryId : p_eachCountry.getD_neighbouringCountriesId()){
            if(!p_player.getCountryIDs().contains(l_countryId)){
                l_enemyCountries.add(l_countryId);
            }
        }
        return l_enemyCountries;
    }

    /**
     * Selects a random country from the player's owned countries.
     *
     * @param p_currentCountries The list of countries owned.
     * @return A randomly selected country.
     */
    private Country getRandomCountry(List<Country> p_currentCountries) {
        Random l_random = new Random();
        int l_randomIndex = l_random.nextInt(p_currentCountries.size());
        return p_currentCountries.get(l_randomIndex);
    }

    /**
     * Not implemented for cheater strategy. Returns null.
     */
    @Override
    public String createCardOrder(Player p_player, CurrentState p_currentState, String p_cardName) {
        return null;
    }

    /**
     * Not implemented for cheater strategy. Returns null.
     */
    @Override
    public String createAdvanceOrder(Player p_player, CurrentState p_currentState) {
        return null;
    }

    /**
     * Not implemented for cheater strategy. Returns null.
     */
    @Override
    public String createDeployOrder(Player p_player, CurrentState p_currentState) {
        return null;
    }
}
