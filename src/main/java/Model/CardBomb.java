package Model;

/**
 * Represents a Bomb card in the game, which allows a player to reduce the
 * armies in an enemy country by half. This class stores the card owner and
 * the target country, and provides logging for execution status.
 *
 * Implements the {@link Card} interface.
 *
 * @author Yash Koladiya
 */
public class CardBomb implements Card {
    Player d_cardOwner;
    String d_targetCountryName;
    String d_logOfOrderExecution;

    /**
     * Constructs a Bomb card with the specified card owner and target country.
     *
     * @param p_cardOwner         the player who owns the Bomb card
     * @param p_targetCountryName the name of the target country to bomb
     */
    public CardBomb(Player p_cardOwner, String p_targetCountryName) {
        this.d_cardOwner = p_cardOwner;
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * Sets the execution log message and prints it to the console.
     * Prints to standard error if the message type is "error".
     *
     * @param p_orderExecutionLog the message to log
     * @param p_messageType       the type of the message ("error" or "default")
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
     * Returns the current execution log message for this card.
     *
     * @return the execution log message
     */
    public String orderExecutionLog() {
        return this.d_logOfOrderExecution;
    }

    /**
     * Returns a string representation of the Bomb card, including the owner and target country.
     *
     * @return a string describing the Bomb card
     */
    @Override
    public String toString() {
        return "CardBomb{" +
                "d_cardOwner=" + d_cardOwner +
                ", d_targetCountryName='" + d_targetCountryName + '\'' +
                '}';
    }

    /**
     * Validates whether the target country specified in the Bomb card exists
     * in the current game state.
     * <p>
     * If the country does not exist, logs an error and returns {@code false}.
     *
     * @param p_currentState the current game state used to access the map and countries
     * @return {@code true} if the target country exists; {@code false} otherwise
     */
    public Boolean validOrderCheck(CurrentState p_currentState) {
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);
        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Invalid! Target country '" + d_targetCountryName + "' does not exist.", "error");
            p_currentState.updateLog("Invalid! Target country does not exist", "effect");
            return false;
        }
        return true;
    }

    /**
     * Validates whether the Bomb card can be used based on game rules.
     * <p>
     * Checks that:
     * <ul>
     *   <li>The target country is not owned by the player.</li>
     *   <li>The target country is a neighbor of at least one of the player's countries.</li>
     *   <li>(Optional) No negotiation is in place with the target country.</li>
     * </ul>
     * Logs an error and returns {@code false} if any condition fails.
     *
     * @param p_currentState the current state of the game for validation and logging
     * @return {@code true} if the Bomb card usage is valid; {@code false} otherwise
     */
    public boolean valid(CurrentState p_currentState) {
        // Find the target country and check if it exists in the player's current countries
        Country l_country = null;
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);
        for (Country l_eachCountry : d_cardOwner.getD_currentCountries()) {
            if (l_eachCountry.getD_countryName().equals(d_targetCountryName)) {
                l_country = l_eachCountry;
                break; // Exit early once we find the target country
            }
        }

        // Check if the target country is a neighbour of any owned country
        boolean l_isTargetCountryNeighbour = false;
        if (l_targetCountry != null) {
            for (Country l_eachCountry : d_cardOwner.getD_currentCountries()) {
                for (Integer l_eachNeighbour : l_eachCountry.getD_neighbouringCountriesId()) {
                    if (l_eachNeighbour.equals(l_targetCountry.getD_countryID())) {
                        l_isTargetCountryNeighbour = true;
                        break; // Exit early if a neighbour is found
                    }
                }
                if (l_isTargetCountryNeighbour) {
                    break; // Exit the loop if the neighbour is confirmed
                }
            }
        }

        // Check if negotiation validation fails
//        if (!d_cardOwner.negotiationValidation(this.d_targetCountryName)) {
//            this.setD_orderExecutionLog("Invalid! Negotiation is in place with the target country", "error");
//            p_currentState.updateLog(orderExecutionLog(), "effect");
//            return false;
//        }

        // Validate the conditions for using the Bomb card
        if (l_country == null || !l_isTargetCountryNeighbour) {
            this.setD_orderExecutionLog("Invalid! Bomb card cannot be used on own country or non-neighbouring country", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }

        return true;
    }
}
