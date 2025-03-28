package Model;

import Controller.PlayerController;
import Model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents an Advance order in the game, which moves armies from a source country
 * to a target country. This order is issued by a player and contains the number of
 * armies to be moved.
 * @author Akhilesh Kanbarkar
 */
public class Advance implements  Orders{

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

    String d_logOfOrderExecution;

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
     * Retrieves the log of order executions for the current player or phase.
     *
     * @return a string containing the log of all executed orders.
     */
    @Override
    public String orderExecutionLog() {
        return this.d_logOfOrderExecution;
    }

    /**
     * Sets d order execution log.
     *
     * @param p_orderExecutionLog the p order execution log
     * @param p_messageType       the p message type
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_messageType) {
        this.d_logOfOrderExecution = p_orderExecutionLog;
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
    public void execute(CurrentState p_currentState) {
        if (valid(p_currentState)) {
            Player l_playerOfTargetCountry = getPlayerOfTargetCountry(p_currentState);
            Country l_sourceCountry = p_currentState.getD_map().getCountryByName(d_sourceCountry);
            Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountry);

            if (l_sourceCountry == null || l_targetCountry == null) {
                this.setD_orderExecutionLog("Execution failed: Invalid source or target country.", "error");
                p_currentState.updateLog(orderExecutionLog(), "effect");
                return;
            }

            // Update source country's army count
            int l_armiesToUpdate = l_sourceCountry.getD_armies() - this.d_noOfArmiesToPlace;
            l_sourceCountry.setD_armies(l_armiesToUpdate);

            // Check if attacking own territory
            if (l_playerOfTargetCountry.getD_playerName().equalsIgnoreCase(this.d_intitiatingPlayer.getD_playerName())) {
                deployArmiesToTarget(l_targetCountry);
            }
            // Conquer empty target country
            else if (l_targetCountry.getD_armies() == 0) {
                conquerTargetCountry(p_currentState, l_playerOfTargetCountry, l_targetCountry);
                this.d_intitiatingPlayer.assignCard();
            }
            // Battle scenario
            else {
                battleOrderResult(p_currentState, l_playerOfTargetCountry, l_sourceCountry, l_targetCountry);
            }
        } else {
            p_currentState.updateLog(orderExecutionLog(), "effect");
        }
    }

    /**
     * Transfers ownership of the target country from the defending player to the attacking player,
     * updates army count, logs the conquest, and refreshes continent ownership.
     *
     * @param p_currentState         the current game state.
     * @param p_playerOfTargetCountry the player currently owning the target country.
     * @param p_targetCountry         the country being conquered.
     */
    private void conquerTargetCountry(CurrentState p_currentState, Player p_playerOfTargetCountry, Country p_targetCountry) {
        // Remove target country from the previous owner
        p_playerOfTargetCountry.getD_currentCountries().remove(p_targetCountry);

        // Assign the country to the initiating player
        this.d_intitiatingPlayer.d_currentCountries.add(p_targetCountry);

        // Update army count in the newly conquered country
        p_targetCountry.setD_armies(d_noOfArmiesToPlace);

        // Log the conquest event
        this.setD_orderExecutionLog(
                "Player: " + d_intitiatingPlayer.getD_playerName() +
                        " conquered Country: " + p_targetCountry.getD_countryName() +
                        " with " + p_targetCountry.getD_armies() + " armies.",
                "default"
        );
        p_currentState.updateLog(orderExecutionLog(), "effect");

        // Update continent control status
        this.updateContinents(this.d_intitiatingPlayer, p_playerOfTargetCountry, p_currentState);
    }

    /**
     * Updates the continent ownership for the two players involved in the battle,
     * based on the current ownership of countries.
     *
     * @param p_intitiatingPlayer      the player who initiated the attack.
     * @param p_playerOfTargetCountry  the player who owned the target country.
     * @param p_currentState           the current state of the game.
     */
    private void updateContinents(Player p_intitiatingPlayer, Player p_playerOfTargetCountry, CurrentState p_currentState) {
        System.out.println("Updating continents of players involved in battle...");

        // Reset the continent ownership for both players
        p_intitiatingPlayer.getD_currentContinents().clear();
        p_playerOfTargetCountry.getD_currentContinents().clear();

        // Create a list of affected players
        List<Player> l_playerList = Arrays.asList(p_intitiatingPlayer, p_playerOfTargetCountry);

        // Assign continents based on updated state
        new PlayerController().assignContinentToPlayers(l_playerList, p_currentState.getD_map().getD_mapContinents());
    }


    /**
     * Retrieves the player who currently owns the target country (excluding Neutral player).
     *
     * @param p_currentState the current game state.
     * @return the player who owns the target country, or null if not found.
     */
    private Player getPlayerOfTargetCountry(CurrentState p_currentState) {
        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if (!l_eachPlayer.getD_playerName().equalsIgnoreCase("Neutral")) {
                boolean l_ownsCountry = l_eachPlayer.getCountryNames()
                        .stream()
                        .anyMatch(l_country -> l_country.equalsIgnoreCase(this.d_targetCountry));

                if (l_ownsCountry) {
                    return l_eachPlayer;  // Return immediately when a match is found
                }
            }
        }
        return null;  // If no player owns the target country, return null
    }

    /**
     * Validates whether the current order can be executed.
     * Checks for null player/country, army availability, and negotiation pacts.
     *
     * @param p_currentState the current game state.
     * @return true if the order is valid, false otherwise.
     */
    public boolean valid(CurrentState p_currentState) {
        if (d_intitiatingPlayer == null || d_sourceCountry == null) {
            this.setD_orderExecutionLog("Invalid order: Player or source country is null.", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        // Find source country
        Country l_country = null;
        for (Country l_eachCountry : d_intitiatingPlayer.getD_currentCountries()) {
            if (l_eachCountry.getD_countryName().equalsIgnoreCase(d_sourceCountry)) {
                l_country = l_eachCountry;
                break; // Stop loop early
            }
        }

        if (l_country == null) {
            this.setD_orderExecutionLog("Cannot execute order: Source country does not belong to the player.", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        if (this.d_noOfArmiesToPlace > l_country.getD_armies()) {
            this.setD_orderExecutionLog("Cannot execute order: Insufficient armies in the source country.", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        if (this.d_noOfArmiesToPlace == l_country.getD_armies()) {
            this.setD_orderExecutionLog("Cannot execute order: At least one army unit must remain in the source country.", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        if (!this.d_intitiatingPlayer.negotiationValidation(this.d_targetCountry)) {
            this.setD_orderExecutionLog("Cannot execute order: Negotiation pact prevents attacking the target country.", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        return true;
    }

    /**
     * Adds the specified number of armies to the target country.
     *
     * @param p_targetCountry the country to which armies are to be deployed.
     */
    private void deployArmiesToTarget(Country p_targetCountry) {
        if (p_targetCountry != null) {
            int l_updatedArmyCount = p_targetCountry.getD_armies() + this.d_noOfArmiesToPlace;
            p_targetCountry.setD_armies(l_updatedArmyCount);
        }
    }

    /**
     * Executes the result of a battle between attacking and defending armies,
     * and updates continent control accordingly.
     *
     * @param p_currentState           the current game state.
     * @param p_playerOfTargetCountry  the player who owns the target country.
     * @param p_sourceCountry          the source country from which the attack was initiated.
     * @param p_targetCountry          the target country under attack.
     */
    private void battleOrderResult(CurrentState p_currentState, Player p_playerOfTargetCountry, Country p_sourceCountry, Country p_targetCountry) {
        int l_armiesInAttack = Math.min(d_noOfArmiesToPlace, p_targetCountry.getD_armies());

        List<Integer> l_defenderArmies = generateRandomArmyUnits(l_armiesInAttack, "defender");
        List<Integer> l_attackerArmies = generateRandomArmyUnits(l_armiesInAttack, "attacker");

        this.produceBattleResult(p_sourceCountry, p_targetCountry, l_attackerArmies, l_defenderArmies, p_playerOfTargetCountry);
        p_currentState.updateLog(orderExecutionLog(), "effect");

        updateContinents(d_intitiatingPlayer, p_playerOfTargetCountry, p_currentState);
    }

    /**
     * Produces the battle outcome based on attacker and defender army units.
     * Delegates to {@code handleSurvivingArmies} for post-battle updates.
     *
     * @param p_sourceCountry         the source country.
     * @param p_targetCountry         the target country.
     * @param p_attackerArmies        the list of attacker's army strengths.
     * @param p_defenderArmies        the list of defender's army strengths.
     * @param p_playerOfTargetCountry the player defending the target country.
     */
    private void produceBattleResult(Country p_sourceCountry, Country p_targetCountry,
                                     List<Integer> p_attackerArmies, List<Integer> p_defenderArmies,
                                     Player p_playerOfTargetCountry) {
        int l_attackerArmiesLeft = Math.max(0, d_noOfArmiesToPlace - p_targetCountry.getD_armies());
        int l_defenderArmiesLeft = Math.max(0, p_targetCountry.getD_armies() - d_noOfArmiesToPlace);

        // Process battle results
        for (int i = 0; i < p_attackerArmies.size(); i++) {
            if (p_attackerArmies.get(i) > p_defenderArmies.get(i)) {
                l_attackerArmiesLeft++;
            } else {
                l_defenderArmiesLeft++;
            }
        }

        // Handle the surviving armies after battle
        handleSurvivingArmies(l_attackerArmiesLeft, l_defenderArmiesLeft, p_sourceCountry, p_targetCountry, p_playerOfTargetCountry);
    }

    /**
     * Handles the outcome of the battle by updating army counts and ownership
     * of the involved countries depending on the remaining armies.
     *
     * @param p_attackerArmiesLeft     remaining attacker armies.
     * @param p_defenderArmiesLeft     remaining defender armies.
     * @param p_sourceCountry          the source country.
     * @param p_targetCountry          the target country.
     * @param p_playerOfTargetCountry  the player who owned the target country.
     */
    private void handleSurvivingArmies(Integer p_attackerArmiesLeft, Integer p_defenderArmiesLeft,
                                       Country p_sourceCountry, Country p_targetCountry,
                                       Player p_playerOfTargetCountry) {
        if (p_defenderArmiesLeft == 0) { // Attacker wins
            p_playerOfTargetCountry.getD_currentCountries().remove(p_targetCountry); // Defender loses the country
            p_targetCountry.setD_armies(p_attackerArmiesLeft); // Assign remaining attacker armies to new country
            d_intitiatingPlayer.getD_currentCountries().add(p_targetCountry); // Attacker gains the country
            this.setD_orderExecutionLog("Player: " + d_intitiatingPlayer.getD_playerName() +
                    " has conquered country: " + p_targetCountry.getD_countryName(), "default");
            d_intitiatingPlayer.assignCard(); // Attacker gets a card reward
        } else { // Defender survives
            p_targetCountry.setD_armies(p_defenderArmiesLeft); // Update remaining defender armies
            p_sourceCountry.setD_armies(p_sourceCountry.getD_armies() + p_attackerArmiesLeft); // Update attacker's source country

            String l_country1 = "Country: " + p_targetCountry.getD_countryName() +
                    " now has " + p_targetCountry.getD_armies() + " remaining armies.";
            String l_country2 = "Country: " + p_sourceCountry.getD_countryName() +
                    " now has " + p_sourceCountry.getD_armies() + " remaining armies.";
            this.setD_orderExecutionLog(l_country1 + System.lineSeparator() + l_country2, "default");
        }
    }

    /**
     * Generates a list of random integers representing army unit strengths
     * for either the attacker or the defender using a role-based probability.
     *
     * @param p_armiesInAttack number of armies participating in the battle.
     * @param p_role           "attacker" or "defender".
     * @return a list of random army strengths.
     */
    private List<Integer> generateRandomArmyUnits(int p_armiesInAttack, String p_role) {
        List<Integer> l_armyList = new ArrayList<>();
        double l_probability = p_role.equals("attacker") ? 0.6 : 0.7; // Attacker: 60%, Defender: 70%

        for (int i = 0; i < p_armiesInAttack; i++) {
            int l_randomNumber = getRandomInteger();
            int l_armyUnit = (int) Math.round(l_randomNumber * l_probability);
            l_armyList.add(l_armyUnit);
        }
        return l_armyList;
    }

    /**
     * Generates a random integer between 1 and 9 inclusive.
     *
     * @return a random integer.
     */
    private int getRandomInteger() {
        return ((int) (Math.random() * 9)) + 1;
    }

}