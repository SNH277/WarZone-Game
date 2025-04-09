package Model;

import java.io.IOException;
import java.util.*;
import java.util.Map;

/**
 * Represents a Benevolent strategy for a player in the game.
 * This player focuses on protecting weaker countries and avoids aggressive moves.
 */
public class BenevolentPlayer extends PlayerBehaviourStrategy{

    /**
     * List to keep track of countries that have received deployments.
     */
    ArrayList<Country> d_deployCountries = new ArrayList<>();

    /**
     * Default constructor for BenevolentPlayer.
     */
    public BenevolentPlayer(){
    }

    /**
     * Gets the type of player behavior.
     *
     * @return String representing the behavior type.
     */
    @Override
    public String getPlayerBehaviour() {
        return "Benevolent";
    }

    /**
     * Creates an order for the player based on current game state and owned cards.
     *
     * @param p_player        The player.
     * @param p_currentState  The current state of the game.
     * @return The created order as a String.
     * @throws IOException If an I/O error occurs.
     */
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

    /**
     * Checks if any country owned by the player has armies deployed.
     *
     * @param p_player The player.
     * @return true if any country has armies, otherwise false.
     */
    private boolean checkIfArmiesDeployed(Player p_player) {
        for(Country l_country : p_player.getD_currentCountries()){
            if(l_country.getD_armies() > 0){
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a card order for the benevolent player.
     * Ignores bomb cards as the player is peaceful.
     *
     * @param p_player       The player.
     * @param p_currentState The game state.
     * @param p_cardName     The card to be used.
     * @return Order string.
     */
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

    /**
     * Selects a random enemy player from the current game state.
     *
     * @param p_currentState The current game state.
     * @param p_player       The current player.
     * @return A random enemy player.
     */
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

    /**
     * Selects a random country from a given list.
     *
     * @param p_deployCountries List of countries.
     * @return A randomly selected country.
     */
    private Country getRandomCountry(List<Country> p_deployCountries) {
        Random l_random = new Random();
        int l_index = l_random.nextInt(p_deployCountries.size());
        return p_deployCountries.get(l_index);
    }

    /**
     * Creates an advance order for a benevolent player.
     * Armies are moved from stronger to weaker neighboring countries.
     *
     * @param p_player       The player.
     * @param p_currentState The game state.
     * @return The advance order string.
     */
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

    /**
     * Gets the weakest neighboring country owned by the player.
     *
     * @param p_randomSourceCountry The source country.
     * @param p_currentState        The game state.
     * @param p_player              The player.
     * @return The weakest neighboring country.
     */
    private Country getWeakestNeighbour(Country p_randomSourceCountry, CurrentState p_currentState, Player p_player) {
        List<Integer> l_neighbourCountryIds = p_randomSourceCountry.getD_neighbouringCountriesId();
        List<Country> l_listOfNeighbours = new ArrayList<>();
        for(Integer l_index =0; l_index < l_neighbourCountryIds.size(); l_index++){
            Country l_country = p_currentState.getD_map().getCountryById(p_randomSourceCountry.getD_neighbouringCountriesId().get(l_index));
            if(p_player.getD_currentCountries().contains(l_country)){
                l_listOfNeighbours.add(l_country);
            }
        }
        if(!l_listOfNeighbours.isEmpty()){
            return evaluateWeakestCountry(l_listOfNeighbours);
        }
        return null;
    }

    /**
     * Evaluates the weakest country (least armies) from a list.
     *
     * @param p_countries List of countries.
     * @return The weakest country.
     */
    private Country evaluateWeakestCountry(List<Country> p_countries) {
        LinkedHashMap<Country, Integer> l_countryArmyMap = new LinkedHashMap<>();
        int l_smallestArmy;
        Country l_country = null;
        for(Country l_eachCountry : p_countries){
            l_countryArmyMap.put(l_eachCountry, l_eachCountry.getD_armies());
        }
        l_smallestArmy = Collections.min(l_countryArmyMap.values());
        for(Map.Entry<Country, Integer> l_entry : l_countryArmyMap.entrySet()){
            if(l_entry.getValue() == l_smallestArmy){
                return l_entry.getKey();
            }
        }
        return l_country;
    }

    /**
     * Creates a deploy order that places armies on the weakest owned country.
     *
     * @param p_player       The player.
     * @param p_currentState The game state.
     * @return The deploy order string.
     */
    @Override
    public String createDeployOrder(Player p_player, CurrentState p_currentState) {
        if(p_player.getD_unallocatedArmies() > 0){
            Country l_weakestCountry = getWeakestCountry(p_player);
            d_deployCountries.add(l_weakestCountry);

            Random l_random = new Random();
            int l_armiesToDeploy = 1;
            if(p_player.getD_unallocatedArmies() > 1) {
                l_armiesToDeploy = l_random.nextInt(p_player.getD_unallocatedArmies() - 1) + 1;
            }
            System.out.println("deploy " + l_weakestCountry.getD_countryName() + " " + l_armiesToDeploy);
            return String.format("deploy %s %d", l_weakestCountry.getD_countryName(), l_armiesToDeploy);
        }
        else{
            return createAdvanceOrder(p_player, p_currentState);
        }
    }

    /**
     * Gets the weakest country owned by the player.
     *
     * @param p_player The player.
     * @return The weakest country.
     */
    private Country getWeakestCountry(Player p_player) {
        List<Country> l_countries = p_player.getD_currentCountries();
        Country l_Country = evaluateWeakestCountry(l_countries);
        return l_Country;
    }

}

