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

    private boolean checkIfArmiesDeployed(Player p_player) {
        for(Country l_country : p_player.getD_currentCountries()){
            if(l_country.getD_armies() > 0){
                return true;
            }
        }
        return false;
    }

    @Override
    public String createCardOrder(Player p_player, CurrentState p_currentState, String p_cardName) {
        int l_armiesToDeploy = 0;
        Random l_random = new Random();
        Country l_randomOwnedCountry = getRandomCountry(p_player.getD_currentCountries());
        if(l_randomOwnedCountry.getD_armies() > 1){
            l_armiesToDeploy = l_random.nextInt(l_randomOwnedCountry.getD_armies() - 1) + 1;
        }
        else{
            l_armiesToDeploy = 1;
        }
        switch (p_cardName){
            case "bomb":
                System.err.println("I donot hurt anyone as I am a benevolent player.");
                return ("bomb  false");
            case "blockade":
                return String.format("blockade %s", l_randomOwnedCountry.getD_countryName());
            case "airlift":
                return String.format("airlift %s %s %d", l_randomOwnedCountry.getD_countryName(), getRandomCountry(p_player.getD_currentCountries()), l_armiesToDeploy);
            case "negotiate":
                return String.format("negotiate %s", getRandomEnemyPlayer(p_currentState, p_player).getD_playerName());
        }
        return null;
    }

    private Player getRandomEnemyPlayer(CurrentState p_currentState, Player p_player) {
        ArrayList<Player> l_players = new ArrayList<>();
        Random l_random = new Random();
        for(Player l_eachPlayer : p_currentState.getD_players()){
            if(!l_eachPlayer.equals(p_player)){
                l_players.add(l_eachPlayer);
            }
        }
        return l_players.get(l_random.nextInt(l_players.size()));
    }

    private Country getRandomCountry(List<Country> p_deployCountries) {
        Random l_random = new Random();
        int l_index = l_random.nextInt(p_deployCountries.size());
        return p_deployCountries.get(l_index);
    }

    @Override
    public String createAdvanceOrder(Player p_player, CurrentState p_currentState) {
        int l_armiesToAdvance;
        Random l_random = new Random();
        Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
        System.out.println("Source Country: " + l_randomSourceCountry.getD_countryName());
        Country l_weakestTargetCountry = getWeakestNeighbour(l_randomSourceCountry, p_currentState, p_player);
        if(l_weakestTargetCountry == null){
            return null;
        }
        System.out.println("Target Country: " + l_weakestTargetCountry.getD_countryName());
        if(l_randomSourceCountry.getD_armies() > 1){
            l_armiesToAdvance = l_random.nextInt(l_randomSourceCountry.getD_armies() - 1) + 1;
        }
        else{
            l_armiesToAdvance = 1;
        }
        System.out.println("advance " + l_randomSourceCountry.getD_countryName() + " " + l_weakestTargetCountry.getD_countryName() + " " + l_armiesToAdvance);
        return String.format("advance %s %s %d", l_randomSourceCountry.getD_countryName(), l_weakestTargetCountry.getD_countryName(), l_armiesToAdvance);
    }

    private Country getWeakestNeighbour(Country lRandomSourceCountry, CurrentState pCurrentState, Player pPlayer) {
    }

}

