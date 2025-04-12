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
    /** Represents the player who owns the card used in the order. */
    Player d_cardOwner;

    /** Name of the country targeted by the order. */
    String d_targetCountryName;

    /** Log message detailing the outcome of the order execution. */
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
     * Print order.
     */
    @Override
    public void printOrder() {
        this.d_logOfOrderExecution = "Bomb Card : "+d_cardOwner.getD_playerName()+" is using bomb card on "+d_targetCountryName;
        System.out.println(d_logOfOrderExecution);
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
     * Executes the Bomb card action if the order is valid.
     * <p>
     * Reduces the number of armies in the target country to half (minimum 1),
     * updates the game state accordingly, and logs the result.
     * If the order is invalid, logs an error message.
     *
     * @param p_currentState the current state of the game used for validation, applying effects, and logging
     */
    public void execute(CurrentState p_currentState) {
        if (valid(p_currentState)) {
            Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);

            // Simplified army count handling
            Integer l_armyCountOnTargetCountry = Math.max(l_targetCountry.getD_armies(), 1);

            // Calculate the new army count after bomb effect
            Integer l_newArmies = (int) Math.floor((double) l_armyCountOnTargetCountry / 2);

            // Apply the new army count
            l_targetCountry.setD_armies(l_newArmies);

            // Remove the Bomb card and disable one card per turn for the player
            d_cardOwner.removeCard("bomb");
            d_cardOwner.setD_oneCardPerTurn(false);

            // Log the effect of using the Bomb card
            this.setD_orderExecutionLog("Bomb card used to reduce the armies of " + this.d_targetCountryName + " to " + l_newArmies, "default");
            p_currentState.updateLog(orderExecutionLog(), "effect");
        }
        else {
            // Handle invalid card usage
            this.setD_orderExecutionLog("Invalid! Bomb card cannot be used", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
        }
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
        Country l_country = null;
        for(Country l_eachCountry : d_cardOwner.getD_currentCountries()){
            if(l_eachCountry.getD_countryName().equals(d_targetCountryName)){
                l_country = l_eachCountry;
            }
        }
        boolean l_isTargetCountryNeighbour = false;
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);
        for (Country l_eachCountry : d_cardOwner.getD_currentCountries()) {
            for (Integer l_eachNeighbour : l_eachCountry.getD_neighbouringCountriesId()) {
                if (l_eachNeighbour.equals(l_targetCountry.getD_countryID())) {
                    l_isTargetCountryNeighbour = true;
                    break;
                }
            }
        }
        if(!d_cardOwner.negotiationValidation(this.d_targetCountryName)){
            this.setD_orderExecutionLog("Invalid! Negotiation is in place with the target country", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        if(l_country != null || !l_isTargetCountryNeighbour){
            this.setD_orderExecutionLog("Invalid! Bomb card cannot be used on own country or non-neighbouring Country", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }
}
