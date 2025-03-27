package Model;

import Controller.PlayerController;
import Model.Player;

import java.util.ArrayList;
import java.util.List;

import static jdk.internal.joptsimple.internal.Strings.isNullOrEmpty;

/**
 * Represents an Advance order in the game, which moves armies from a source country
 * to a target country. This order is issued by a player and contains the number of
 * armies to be moved.
 * @author Akhilesh Kanbarkar
 */
public class Advance{

    /**
     * The D source country.
     */
    String d_sourceCountry;

    /**
     * The D target country.
     */
    String d_targetCountry;

    /**
     * The D no of armies to place.
     */
    Integer d_noOfArmiesToPlace;

    /**
     * The D intitiating player.
     */
    Player d_intitiatingPlayer;

    /**
     * Instantiates a new Advance.
     *
     * @param p_sourceCountry     the p source country
     * @param p_targetCountry     the p target country
     * @param p_noOfArmiesToPlace the p no of armies to place
     * @param p_intitiatingPlayer the p intitiating player
     */
    public Advance(String p_sourceCountry, String p_targetCountry, Integer p_noOfArmiesToPlace, Player p_intitiatingPlayer) {
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_noOfArmiesToPlace = p_noOfArmiesToPlace;
        this.d_intitiatingPlayer = p_intitiatingPlayer;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Advance{" +
                "d_sourceCountry='" + d_sourceCountry + '\'' +
                ", d_targetCountry='" + d_targetCountry + '\'' +
                ", d_noOfArmiesToPlace=" + d_noOfArmiesToPlace +
                ", d_intitiatingPlayer=" + d_intitiatingPlayer +
                '}';
    }

    /**
     * Sets d order execution log.
     *
     * @param p_orderExecutionLog the p order execution log
     * @param p_messageType       the p message type
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_messageType) {
        if (p_messageType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Executes the Advance Order by handling army movement between countries.
     * It checks for ownership, executes deployment, triggers battles, or conquers territory.
     *
     * @param p_currentState the current game state
     */
    @Override
    public void execute(CurrentState p_currentState) {
        if (valid(p_currentState)) {
            Player l_playerOfTargetCountry = getplayerOfTargetCounrty(p_currentState);
            Country l_sourceCountry = p_currentState.getD_map().getCountryByName(d_sourceCountry);
            Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountry);
            Integer l_armiesToUpdate = l_sourceCountry.getD_armies() - this.d_noOfArmiesToPlace;
            l_sourceCountry.setD_armies(l_armiesToUpdate);

            if (l_playerOfTargetCountry.getD_playerName().equalsIgnoreCase(this.d_intitiatingPlayer.getD_name())) {
                deployArmiesToTarget(l_targetCountry);
            } else if (l_targetCountry.getD_armies() == 0) {
                conquerTargetCountry(p_currentState, l_playerOfTargetCountry, l_targetCountry);
                this.d_intitiatingPlayer.assignCard();
            } else {
                battleOrderResult(p_currentState, l_playerOfTargetCountry, l_sourceCountry, l_targetCountry);
            }
        } else {
            p_currentState.updateLog("Cannot execute advance Order", "effect");
        }
    }

    /**
     * Handles battle logic between attacking and defending countries.
     *
     * @param p_currentState          the game state
     * @param p_playerOfTargetCountry the defender
     * @param p_sourceCountry         the attacking country
     * @param p_targetCountry         the defending country
     */
    private void battleOrderResult(CurrentState p_currentState, Player p_playerOfTargetCountry,
                                   Country p_sourceCountry, Country p_targetCountry) {
        int l_armiesInAttack = Math.min(d_noOfArmiesToPlace, p_targetCountry.getD_armies());
        List<Integer> l_defenderArmies = generateRandomArmyUnits(l_armiesInAttack, "defender");
        List<Integer> l_attackerArmies = generateRandomArmyUnits(l_armiesInAttack, "attacker");
        this.produceBattleResult(p_sourceCountry, p_targetCountry, l_attackerArmies, l_defenderArmies, p_playerOfTargetCountry);
        updateContinents(d_intitiatingPlayer, p_playerOfTargetCountry, p_currentState);
    }

    /**
     * Compares attacker and defender armies and computes battle outcome.
     */
    private void produceBattleResult(Country p_sourceCountry, Country p_targetCountry,
                                     List<Integer> p_attackerArmies, List<Integer> p_defenderArmies,
                                     Player p_playerOfTargetCountry) {
        Integer l_attackerArmiesLeft = Math.max(0, d_noOfArmiesToPlace - p_targetCountry.getD_armies());
        Integer l_defenderArmiesLeft = Math.max(0, p_targetCountry.getD_armies() - d_noOfArmiesToPlace);

        for (int i = 0; i < p_attackerArmies.size(); i++) {
            if (p_attackerArmies.get(i) > p_defenderArmies.get(i)) {
                l_attackerArmiesLeft++;
            } else {
                l_defenderArmiesLeft++;
            }
        }

        handleSurvivingArmies(l_attackerArmiesLeft, l_defenderArmiesLeft, p_sourceCountry, p_targetCountry, p_playerOfTargetCountry);
    }

    /**
     * Applies post-battle results to game state depending on who wins.
     */
    private void handleSurvivingArmies(Integer p_attackerArmiesLeft, Integer p_defenderArmiesLeft,
                                       Country p_sourceCountry, Country p_targetCountry,
                                       Player p_playerOfTargetCountry) {
        if (p_defenderArmiesLeft == 0) {
            p_playerOfTargetCountry.getD_currentCountries().remove(p_targetCountry);
            p_targetCountry.setD_armies(p_attackerArmiesLeft);
            d_intitiatingPlayer.getD_currentCountries().add(p_targetCountry);
            this.setD_orderExecutionLog("Player:" + this.d_intitiatingPlayer.getD_name() +
                    " has won country: " + p_targetCountry.getD_countryName(), "default");
            d_intitiatingPlayer.assignCard();
        } else {
            p_targetCountry.setD_armies(p_defenderArmiesLeft);
            Integer l_sourceArmiesToUpdate = p_sourceCountry.getD_armies() + p_attackerArmiesLeft;
            p_sourceCountry.setD_armies(l_sourceArmiesToUpdate);

            String l_country1 = "Country: " + p_targetCountry.getD_countryName() + " now has " +
                    p_targetCountry.getD_armies() + " remaining armies";
            String l_country2 = "Country: " + p_sourceCountry.getD_countryName() + " now has " +
                    p_sourceCountry.getD_armies() + " remaining armies";

            this.setD_orderExecutionLog(l_country1 + System.lineSeparator() + l_country2, "default");
        }
    }

    /**
     * Generates randomized army unit strength based on role.
     */
    private List<Integer> generateRandomArmyUnits(int p_armiesInAttack, String p_role) {
        List<Integer> l_armyList = new ArrayList<>();
        double l_probability = p_role.equals("attacker") ? 0.6 : 0.7;

        for (int i = 0; i < p_armiesInAttack; i++) {
            int l_randomNumber = getRandomInteger(1, 10);
            l_armyList.add((int) Math.round(l_randomNumber * l_probability));
        }
        return l_armyList;
    }

    /**
     * Returns a random integer between min (inclusive) and max (exclusive).
     */
    private int getRandomInteger(int p_min, int p_max) {
        return ((int) (Math.random() * (p_max - p_min))) + p_min;
    }

    /**
     * Handles the conquest of a target country with no defending armies.
     */
    private void conquerTargetCountry(CurrentState p_currentState, Player p_playerOfTargetCountry,
                                      Country p_targetCountry) {
        p_targetCountry.setD_armies(d_noOfArmiesToPlace);
        p_playerOfTargetCountry.getD_currentCountries().remove(p_targetCountry);
        this.d_intitiatingPlayer.d_currentCountries.add(p_targetCountry);
        System.out.println("Player : " + d_intitiatingPlayer.getD_name() +
                " is assigned with Country : " + p_targetCountry.getD_countryName() +
                " and Armies : " + p_targetCountry.getD_armies());
        this.updateContinents(this.d_intitiatingPlayer, p_playerOfTargetCountry, p_currentState);
    }

    /**
     * Updates the continent ownership for both players involved in battle.
     */
    private void updateContinents(Player p_intitiatingPlayer, Player p_playerOfTargetCountry,
                                  CurrentState p_currentState) {
        System.out.println("Updating continents of players involved in battle....");
        List<Player> l_playerList = new ArrayList<>();
        p_intitiatingPlayer.setD_currentContinents(new ArrayList<>());
        p_playerOfTargetCountry.setD_currentContinents(new ArrayList<>());

        l_playerList.add(p_intitiatingPlayer);
        l_playerList.add(p_playerOfTargetCountry);

        PlayerController l_playerController = new PlayerController();
        l_playerController.assignContinentToPlayers(l_playerList, p_currentState.getD_map().getD_mapContinents());
    }

    /**
     * Retrieves the player who currently owns the target country.
     */
    private Player getplayerOfTargetCounrty(CurrentState p_currentState) {
        Player l_player = null;
        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if (!l_eachPlayer.getD_name().equals("Neutral")) {
                String l_cont = l_eachPlayer.getCountryNames().stream()
                        .filter(l_country -> l_country.equalsIgnoreCase(this.d_targetCountry)).findFirst().orElse(null);
                if (!isNullOrEmpty(l_cont)) {
                    l_player = l_eachPlayer;
                }
            }
        }
        return l_player;
    }
}