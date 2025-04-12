package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A computer-controlled player that issues commands based on random choices.
 * RandomPlayer selects actions like deploy, advance, or use a card randomly,
 * making its behavior unpredictable in the game.
 * Implements the PlayerBehaviourStrategy interface.
 * @author Taksh rana
 */
public class RandomPlayer extends PlayerBehaviourStrategy{
    /**
     * List of {@link Country} objects where the player can deploy armies.
     * This is used to track valid countries owned by a player during the deployment phase.
     */
    ArrayList<Country> d_deployCountries = new ArrayList<>();


    public RandomPlayer(){
    }

    public String createOrder(Player p_player, CurrentState p_gameState) {
        System.out.println("Creating order for : " + p_player.getD_playerName());
        String l_command;

        if (!checkIfArmiesDeployed(p_player)) {
            l_command = (p_player.getD_unallocatedArmies() > 0)
                    ? createDeployOrder(p_player, p_gameState)
                    : createAdvanceOrder(p_player, p_gameState);
        } else {
            List<String> l_cards = p_player.getD_cardOwnedByPlayer();
            Random l_random = new Random();

            if (!l_cards.isEmpty()) {
                int l_index = l_random.nextInt(3); // Generates 0, 1, or 2

                switch (l_index) {
                    case 0:
                        l_command = createDeployOrder(p_player, p_gameState);
                        break;
                    case 1:
                        l_command = createAdvanceOrder(p_player, p_gameState);
                        break;
                    case 2:
                        int l_cardIndex = l_random.nextInt(p_player.getD_cardOwnedByPlayer().size());
                        l_command = createCardOrder(p_player, p_gameState, p_player.getD_cardOwnedByPlayer().get(l_cardIndex));
                        break;
                    default:
                        l_command = createAdvanceOrder(p_player, p_gameState);
                        break;
                }
            } else {
                boolean l_randomBoolean = new Random().nextBoolean();
                l_command = l_randomBoolean ? createDeployOrder(p_player, p_gameState) : createAdvanceOrder(p_player, p_gameState);
            }
        }

        return l_command;
    }

    private boolean checkIfArmiesDeployed(Player p_player) {
        for(Country l_eachCountry : p_player.getD_currentCountries()){
            if(l_eachCountry.getD_armies() > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPlayerBehaviour() {
        return "Random";
    }

    @Override
    public String createCardOrder(Player p_player, CurrentState p_currentState, String p_cardName) {
        Random l_random = new Random();
        int l_armiesToSend;
        Country l_randomOwnedCountry = getRandomCountry(p_player.getD_currentCountries());
        Country l_randomNeighbour = p_currentState.getD_map().getCountryById(l_randomOwnedCountry.getD_neighbouringCountriesId().get(l_random.nextInt(l_randomOwnedCountry.getD_neighbouringCountriesId().size())));
        Player l_randomPlayer = getRandomPlayer(p_currentState, p_player);
        if(l_randomOwnedCountry.getD_armies() > 1){
            l_armiesToSend = l_random.nextInt(l_randomOwnedCountry.getD_armies() - 1) + 1;
        }
        else {
            l_armiesToSend = 1;
        }
        switch (p_cardName) {
            case "bomb":
                return "bomb " + l_randomNeighbour.getD_countryName();
            case "blockade":
                return "blockade " + l_randomOwnedCountry.getD_countryName();
            case "airlift":
                return "airlift " + l_randomOwnedCountry.getD_countryName() + " " + getRandomCountry(p_player.getD_currentCountries()) + " " + l_armiesToSend;
            case "negotiate":
                return "negotiate " + l_randomPlayer.getD_playerName();
            default:
                return null;
        }
    }

    private Player getRandomPlayer(CurrentState p_currentState, Player p_player) {
        ArrayList<Player> l_PlayerList = new ArrayList<Player>();
        Random l_random = new Random();
        for(Player l_eachPlayer : p_currentState.getD_players()){
            if(l_eachPlayer.equals(p_player)){
                l_PlayerList.add(l_eachPlayer);
            }
        }
        return l_PlayerList.get(l_random.nextInt(l_PlayerList.size()));
    }

    @Override
    public String createAdvanceOrder(Player p_player, CurrentState p_currentState) {
        int l_armiesToAdvance = 1;
        Random l_random = new Random();
        Country l_randomOwnedCountry = getRandomCountry(d_deployCountries);
        int l_randomIndex = l_random.nextInt(l_randomOwnedCountry.getD_neighbouringCountriesId().size());
        Country l_randomNeighbour;
        if(l_randomOwnedCountry.getD_neighbouringCountriesId().size() > 1){
            l_randomNeighbour = p_currentState.getD_map().getCountryById(l_randomOwnedCountry.getD_neighbouringCountriesId().get(l_randomIndex));
        }
        else {
            l_randomNeighbour = p_currentState.getD_map().getCountryById(l_randomOwnedCountry.getD_neighbouringCountriesId().get(0));
        }
        if(l_randomOwnedCountry.getD_armies() > 1){
            l_armiesToAdvance = l_random.nextInt(l_randomOwnedCountry.getD_armies() - 1) + 1;
        }
        else {
            l_armiesToAdvance = 1;
        }
        return "advance " + l_randomOwnedCountry.getD_countryName() + " " + l_randomNeighbour.getD_countryName() + " " + l_armiesToAdvance;
    }

    @Override
    public String createDeployOrder(Player p_player, CurrentState p_currentState) {
        if(p_player.getD_unallocatedArmies() > 0){
            Random l_random = new Random();
            Country l_randomCountry = getRandomCountry(p_player.getD_currentCountries());
            d_deployCountries.add(l_randomCountry);
            int l_armiesToDeploy = 1;
            if(p_player.getD_unallocatedArmies() > 1){
                l_armiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmies() - 1) + 1;
            }
            return String.format("deploy %s %d", l_randomCountry.getD_countryName(), l_armiesToDeploy);
        }
        else{
            return createAdvanceOrder(p_player, p_currentState);
        }
    }

    private Country getRandomCountry(List<Country> p_currentCountries) {
        Random l_random = new Random();
        int l_randomIndex = l_random.nextInt(p_currentCountries.size());
        return p_currentCountries.get(l_randomIndex);
    }

}
