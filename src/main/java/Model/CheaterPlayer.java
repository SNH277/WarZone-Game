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

    private void doubleArmyOnEnemyNeighbourCountries(Player pPlayer, CurrentState pCurrentState) {
    }

    private void conquerNeighboringEnemies(Player pPlayer, CurrentState pCurrentState) {
    }

}
