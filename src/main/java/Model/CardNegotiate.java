package Model;

/**
 * Represents a Negotiate Card in the game, used by players to form temporary alliances.
 * @author Akhilesh Kanbarkar
 */
public class CardNegotiate implements Card {
    Player d_cardOwner;
    String d_targetPlayer;
    String d_logOfOrderExecution;

    /**
     * Constructor for CardNegotiate.
     *
     * @param p_cardOwner     the player who owns this card
     * @param p_targetPlayer  the name of the player to negotiate with
     */
    public CardNegotiate(Player p_cardOwner, String p_targetPlayer) {
        this.d_cardOwner = p_cardOwner;
        this.d_targetPlayer = p_targetPlayer;
    }

    @Override
    public String toString() {
        return "CardNegotiate{" +
                "d_cardOwner=" + d_cardOwner +
                ", d_targetPlayer='" + d_targetPlayer + '\'' +
                '}';
    }

    /**
     * Sets the order execution log and prints it based on the message type.
     *
     * @param p_orderExecutionLog  the log message
     * @param p_messageType        the type of message (e.g., "error", "default")
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
     * Gets the order execution log.
     *
     * @return the log of the order execution
     */
    public String orderExecutionLog() {
        return this.d_logOfOrderExecution;
    }

    /**
     * Checks if the negotiation order is valid by ensuring the target player exists.
     *
     * @param p_currentState the current game state
     * @return true if valid, false otherwise
     */
    public Boolean validOrderCheck(CurrentState p_currentState) {
        Player l_targetPlayer = p_currentState.getPlayerFromName(d_targetPlayer);
        if (l_targetPlayer == null) {
            this.setD_orderExecutionLog("Invalid! No player to negotiate", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }
}