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
}