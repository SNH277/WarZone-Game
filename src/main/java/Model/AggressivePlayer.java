package Model;

import java.io.IOException;
import java.util.*;
import java.util.Map;

public class AggressivePlayer extends PlayerBehaviourStrategy{

    ArrayList<Country> d_deployCountries = new ArrayList<>();

    public AggressivePlayer(){
    }

    public String getPlayerBehaviour() {
        return "Aggressive";
    }

    public String createOrder(Player p_player, CurrentState p_currentState) throws IOException {
        System.out.println("Order creation for " + p_player.getD_playerName());
        String l_command = "";

        if (!checkIfArmiesDeployed(p_player)) {
            l_command = (p_player.getD_unallocatedArmies() > 0)
                    ? createDeployOrder(p_player, p_currentState)
                    : createAdvanceOrder(p_player, p_currentState);
        } else {
            List<String> l_cards = p_player.getD_cardOwnedByPlayer();
            Random l_random = new Random();

            if (!l_cards.isEmpty()) {
                int l_choice = l_random.nextInt(3); // 0 = Deploy, 1 = Advance, 2 = Card

                switch (l_choice) {
                    case 0:
                        System.out.println("Deploy order");
                        l_command = createDeployOrder(p_player, p_currentState);
                        break;
                    case 1:
                        System.out.println("Advance order");
                        l_command = createAdvanceOrder(p_player, p_currentState);
                        break;
                    case 2:
                        System.out.println("Card order");
                        String l_card = l_cards.get(l_random.nextInt(l_cards.size()));
                        l_command = createCardOrder(p_player, p_currentState, l_card);
                        break;
                    default:
                        l_command = createAdvanceOrder(p_player, p_currentState);
                }
            } else {
                boolean l_randomBool = l_random.nextBoolean();
                l_command = l_randomBool
                        ? createDeployOrder(p_player, p_currentState)
                        : createAdvanceOrder(p_player, p_currentState);
                System.out.println("Without card " + (l_randomBool ? "deploy" : "advance"));
            }
        }

        return l_command;
    }

    private boolean checkIfArmiesDeployed(Player p_player) {
        return p_player.getD_currentCountries().stream().anyMatch(l_country -> l_country.getD_armies() > 0);
    }

    public String createCardOrder(Player p_player, CurrentState p_currentState, String p_cardName) {
        Random l_random = new Random();
        Country l_strongestSourceCountry = getStrongestCountry(p_player, p_currentState);
        Country l_randomTargetCountry = p_currentState.getD_map().getCountryById(l_strongestSourceCountry.getD_neighbouringCountriesId().get(l_random.nextInt(l_strongestSourceCountry.getD_neighbouringCountriesId().size())));
        int l_noOfArmiesToMove = l_strongestSourceCountry.getD_armies() > 1 ? l_strongestSourceCountry.getD_armies() : 1;
        switch (p_cardName) {
            case "bomb":
                return "bomb " + l_randomTargetCountry.getD_countryName();
            case "blockade":
                return "blockade " + l_randomTargetCountry.getD_countryName();
            case "airlift":
                return "airlift " + l_strongestSourceCountry.getD_countryName() + " " + l_randomTargetCountry.getD_countryName() + " " + l_noOfArmiesToMove;
            case "negotiate":
                return "negotiate " + getRandomEnemyPlayer(p_player, p_currentState).getD_playerName();
        }
        return null;
    }

    private Player getRandomEnemyPlayer(Player pPlayer, CurrentState pCurrentState) {
    }

    private Country getStrongestCountry(Player pPlayer, CurrentState pCurrentState) {
    }


}

