package Model;

/**
 * Represents a Blockade card in the game, allowing the player to apply a blockade
 * on a specified country. The class stores the card owner and the target country.
 * It provides methods for logging execution results and generating a string representation.
 *
 * This class implements the {@link Card} interface.
 *
 * @author Yash Koladiya
 */
public class CardBlockade implements Card {
    Player d_cardOwner;
    String d_targetCountryName;
    String d_logOfOrderExecution;

    /**
     * Constructs a Blockade card with the specified owner and target country.
     *
     * @param p_cardOwner         the player who owns the card
     * @param p_targetCountryName the name of the country where the blockade is to be applied
     */
    public CardBlockade(Player p_cardOwner, String p_targetCountryName) {
        this.d_cardOwner = p_cardOwner;
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * Sets the order execution log message and prints it.
     * If the message type is "error", prints to standard error.
     *
     * @param p_orderExecutionLog the message to be logged
     * @param p_messageType       the type of the message (e.g., "error" or "default")
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
     * Retrieves the stored order execution log message.
     *
     * @return the log message of the order execution
     */
    public String orderExecutionLog() {
        return this.d_logOfOrderExecution;
    }

    /**
     * Returns a string representation of the Blockade card, showing the owner
     * and the target country name.
     *
     * @return a string describing the card
     */
    @Override
    public String toString() {
        return "CardBlockade{" +
                "d_cardOwner=" + d_cardOwner +
                ", d_targetCountryName='" + d_targetCountryName + '\'' +
                '}';
    }
    /**
     * Validates whether the target country specified in the Blockade card exists
     * in the current game state. Logs an error if the country is not found.
     *
     * @param p_currentState the current state of the game used to look up the map and countries
     * @return {@code true} if the target country exists; {@code false} otherwise
     */
    public Boolean validOrderCheck(CurrentState p_currentState) {
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);
        if (l_targetCountry == null) {
            this.setD_orderExecutionLog("Invalid! Target country '" + d_targetCountryName + "' does not exist.", "error");
            return false;
        }
        return true;
    }
}