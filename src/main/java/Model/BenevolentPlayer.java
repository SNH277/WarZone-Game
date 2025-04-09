package Model;

import java.io.IOException;
import java.util.*;
import java.util.Map;

public class BenevolentPlayer extends PlayerBehaviourStrategy{

    ArrayList<Country> d_deployCountries = new ArrayList<>();

    public BenevolentPlayer(){
    }

    @Override
    public String getPlayerBehaviour() {
        return "Benevolent";
    }

    @Override
    public String createOrder(Player p_player, CurrentState p_currentState) throws IOException {
        System.out.println("Creating order for : " + p_player.getD_playerName());
        String l_command = "";

        // Check if armies are deployed
        if (!checkIfArmiesDeployed(p_player)) {
            l_command = (p_player.getD_unallocatedArmies() > 0)
                    ? createDeployOrder(p_player, p_currentState)
                    : createAdvanceOrder(p_player, p_currentState);
        } else {
            List<String> l_cards = p_player.getD_cardOwnedByPlayer();
            Random l_random = new Random();

            if (!l_cards.isEmpty()) {
                System.out.println("Enters Card Logic");
                int l_index = l_random.nextInt(3); // Random index 0, 1, or 2

                switch (l_index) {
                    case 0:
                        System.out.println("Deploy!");
                        l_command = createDeployOrder(p_player, p_currentState);
                        break;
                    case 1:
                        System.out.println("Advance!");
                        l_command = createAdvanceOrder(p_player, p_currentState);
                        break;
                    case 2:
                        System.out.println("Cards!");
                        // Choose a random card from player's owned cards
                        int l_cardIndex = l_random.nextInt(p_player.getD_cardOwnedByPlayer().size());
                        l_command = createCardOrder(p_player, p_currentState, p_player.getD_cardOwnedByPlayer().get(l_cardIndex));
                        break;
                    default:
                        l_command = createAdvanceOrder(p_player, p_currentState);
                        break;
                }
            } else {
                // Case where player has no cards
                boolean l_randomBoolean = l_random.nextBoolean();
                if (l_randomBoolean) {
                    System.out.println("Without Card Deploy Logic");
                    l_command = createDeployOrder(p_player, p_currentState);
                } else {
                    System.out.println("Without Card Advance Logic");
                    l_command = createAdvanceOrder(p_player, p_currentState);
                }
            }
        }

        return l_command;
    }

    private boolean checkIfArmiesDeployed(Player pPlayer) {
    }

}

