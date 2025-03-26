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
}
