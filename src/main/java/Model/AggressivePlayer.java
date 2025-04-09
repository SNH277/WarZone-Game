package Model;

import java.io.IOException;
import java.util.*;
import java.util.Map;

/**
 * Represents the "Aggressive" strategy for a player.
 * This player focuses on its strongest country, deploying armies to it and attacking enemies aggressively.
 */
public class AggressivePlayer extends PlayerBehaviourStrategy{

    /**
     * List to store countries where armies have been deployed during the turn.
     */
    ArrayList<Country> d_deployCountries = new ArrayList<>();

    /**
     * Default constructor.
     */
    public AggressivePlayer(){
    }

    /**
     * Returns the name of the player behavior strategy.
     *
     * @return "Aggressive" as the strategy name.
     */
    public String getPlayerBehaviour() {
        return "Aggressive";
    }

    /**
     * Creates an order based on the aggressive player's strategy.
     * Focuses on deploying and attacking from the strongest country.
     *
     * @param p_player        The player using this strategy.
     * @param p_currentState  The current game state.
     * @return A string representing the command issued.
     * @throws IOException if an I/O error occurs.
     */
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

    /**
     * Checks if the player has deployed any armies this turn.
     *
     * @param p_player The player.
     * @return True if any of the player’s countries has armies deployed, otherwise false.
     */
    private boolean checkIfArmiesDeployed(Player p_player) {
        return p_player.getD_currentCountries().stream().anyMatch(l_country -> l_country.getD_armies() > 0);
    }

    /**
     * Creates a card order using one of the available cards randomly.
     *
     * @param p_player       The player issuing the order.
     * @param p_currentState The current game state.
     * @param p_cardName     The name of the card to play.
     * @return A string representing the card order.
     */
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

    /**
     * Returns a random enemy player from the current game state.
     *
     * @param p_player      The current player.
     * @param p_gameState   The current game state.
     * @return A random enemy player.
     */
    private Player getRandomEnemyPlayer(Player p_player, CurrentState p_gameState) {
        ArrayList<Player> l_playerList = new ArrayList<>();
        Random l_random = new Random();
        for (Player l_eachPlayer : p_gameState.getD_players()) {
            if (!l_eachPlayer.equals(p_player)) {
                l_playerList.add(l_eachPlayer);
            }
        }
        return l_playerList.get(l_random.nextInt(l_playerList.size()));
    }

    /**
     * Retrieves the strongest country owned by the player (i.e., with the highest number of armies).
     *
     * @param p_player       The player.
     * @param p_currentState The current game state.
     * @return The strongest country.
     */
    private Country getStrongestCountry(Player p_player, CurrentState p_currentState) {
        List<Country> l_countriesOwnedByPlayer = p_player.getD_currentCountries();
        Country l_strongestCountry = calculateStrongestCountry(l_countriesOwnedByPlayer);
        return l_strongestCountry;

    }

    /**
     * Calculates the strongest country from a list based on number of armies.
     *
     * @param p_countriesOwnedByPlayer List of countries.
     * @return The country with the highest number of armies.
     */
    private Country calculateStrongestCountry(List<Country> p_countriesOwnedByPlayer) {
        LinkedHashMap<Country,Integer> l_countryWithArmies = new LinkedHashMap<>();
        int l_largestNoOfArmies = 0;
        Country l_strongestCountry = null;
        for(Country l_eachCountry : p_countriesOwnedByPlayer){
            l_countryWithArmies.put(l_eachCountry,l_eachCountry.getD_armies());
        }
        l_largestNoOfArmies = Collections.max(l_countryWithArmies.values());
        for(Map.Entry<Country,Integer> l_entry : l_countryWithArmies.entrySet()){
            if(l_entry.getValue().equals(l_largestNoOfArmies)){
                return l_entry.getKey();
            }
        }
        return l_strongestCountry;
    }

    /**
     * Creates an advance order from a previously deployed country to a random neighboring country.
     *
     * @param p_player       The player.
     * @param p_currentState The current game state.
     * @return A string representing the advance command.
     */
    @Override
    public String createAdvanceOrder(Player p_player, CurrentState p_currentState) {
        Country l_randomSourceCountry = getRandomCountry(d_deployCountries);
        moveArmiesFromItsNeighbours(p_player, l_randomSourceCountry, p_currentState);
        Random l_random = new Random();
        Country l_randomTargetCountry = p_currentState.getD_map().getCountryById(l_randomSourceCountry.getD_neighbouringCountriesId().get(l_random.nextInt(l_randomSourceCountry.getD_neighbouringCountriesId().size())));
        int l_noOfArmiesToMove = l_randomSourceCountry.getD_armies() > 1 ? l_randomSourceCountry.getD_armies() : 1;

        return "advance " + l_randomSourceCountry.getD_countryName() + " " + l_randomTargetCountry.getD_countryName() + " " + l_noOfArmiesToMove;
    }

    /**
     * Moves armies from neighboring friendly countries into the selected source country.
     *
     * @param p_player             The player.
     * @param p_randomSourceCountry The source country.
     * @param p_currentState       The current game state.
     */
    private void moveArmiesFromItsNeighbours(Player p_player, Country p_randomSourceCountry, CurrentState p_currentState) {
        List<Integer> l_adjacentCountryIds = p_randomSourceCountry.getD_neighbouringCountriesId();
        List<Country> l_listOfNeighbours = new ArrayList<>();
        for(int l_index = 0; l_index < l_adjacentCountryIds.size(); l_index++){
            Country l_country = p_currentState.getD_map().getCountryById(p_randomSourceCountry.getD_neighbouringCountriesId().get(l_index));
            if(p_player.getD_currentCountries().contains(l_country)){
                l_listOfNeighbours.add(l_country);
            }
        }

        int l_armiesToMove = 0;
        for(Country l_eachCountry : l_listOfNeighbours){
            if(p_randomSourceCountry.getD_armies() > 0){
                l_armiesToMove = p_randomSourceCountry.getD_armies() + l_eachCountry.getD_armies();
            }
            else{
                l_armiesToMove = l_eachCountry.getD_armies();
            }
        }
        p_randomSourceCountry.setD_armies(l_armiesToMove);
    }

    /**
     * Selects a random country from the list of countries where armies were deployed.
     *
     * @param p_deployCountries The list of deployed countries.
     * @return A random country from the list.
     */
    private Country getRandomCountry(ArrayList<Country> p_deployCountries) {
        Random l_random = new Random();
        return p_deployCountries.get(l_random.nextInt(p_deployCountries.size()));
    }

    /**
     * Creates a deploy order by deploying armies to the strongest country.
     *
     * @param p_player       The player.
     * @param p_currentState The current game state.
     * @return A string representing the deploy command.
     */
    @Override
    public String createDeployOrder(Player p_player, CurrentState p_currentState) {
        Random l_random = new Random();
        Country l_strongestCountry = getStrongestCountry(p_player, p_currentState);
        d_deployCountries.add(l_strongestCountry);
        int l_noOfArmiesToDeploy = 1;
        if(p_player.getD_unallocatedArmies()>1){
            l_noOfArmiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmies()-1)+1;
        }
        return String.format("deploy %s %d", l_strongestCountry.getD_countryName(), l_noOfArmiesToDeploy);
    }
}

