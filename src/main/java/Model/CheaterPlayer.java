package Model;

import Controller.PlayerController;

import java.io.IOException;
import java.util.*;

public class CheaterPlayer extends PlayerBehaviourStrategy{

    public CheaterPlayer(){

    }

    @Override
    public String getPlayerBehaviour() {
        return "Cheater";
    }

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

        //p_player.checkForMoreOrder(true);
        return null;
    }

    private Country getRandomCountry(List<Country> dCurrentCountries) {
    }

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

    private ArrayList<Integer> getEnemies(Player pPlayer, Country lEachCountry) {
    }

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

    private void conquerTargetCountry(CurrentState pCurrentState, Player lEnemyCountryOwner, Player pPlayer, Country lEnemyCountry) {
    }

    private Player getCountryOwner(CurrentState pCurrentState, Integer lEnemyId) {
    }

}
