package Model;

import Model.Player;

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
}