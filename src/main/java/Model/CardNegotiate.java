package Model;

/**
 * Represents a Negotiate Card in the game, used by players to form temporary alliances.
 * @author Akhilesh Kanbarkar
 */
public class CardNegotiate implements Card {
    /** Represents the player who owns the card used in the order. */
    Player d_cardOwner;

    /** Name of the player targeted by the order. */
    String d_targetPlayer;

    /** Log message detailing the outcome of the order execution. */
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
     * Print order.
     */
    @Override
    public void printOrder() {
        this.d_logOfOrderExecution = "Negotiate Card : "+d_cardOwner.getD_playerName()+" is using negotiate card with "+d_targetPlayer;
        System.out.println(d_logOfOrderExecution);
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

    /**
     * Executes the negotiation logic between the current player and target player.
     *
     * @param p_currentState the current game state
     */
    public void execute(CurrentState p_currentState) {
        Player l_targetPlayer = p_currentState.getPlayerFromName(d_targetPlayer);

        // Validate if the target player exists and negotiation is possible
        if (l_targetPlayer == null) {
            this.setD_orderExecutionLog("Invalid! Target player does not exist", "error");
            p_currentState.updateLog(d_logOfOrderExecution, "effect");
            return;
        }

        if (valid(p_currentState)) {
            l_targetPlayer.addNegotiatePlayer(d_cardOwner);
            d_cardOwner.addNegotiatePlayer(l_targetPlayer);
            d_cardOwner.removeCard("negotiate");
            d_cardOwner.setD_oneCardPerTurn(false);
            this.setD_orderExecutionLog("Negotiation Successful", "default");
            p_currentState.updateLog(d_logOfOrderExecution, "effect");
        } else {
            this.setD_orderExecutionLog("Invalid! Negotiation Unsuccessful", "error");
            p_currentState.updateLog(d_logOfOrderExecution, "effect");
        }
    }

    /**
     * Placeholder validation method for negotiation rules.
     * Should be extended with actual game-specific logic.
     *
     * @param p_currentState the current game state
     * @return true if valid, false otherwise
     */
    public boolean valid(CurrentState p_currentState) {
        return true;
    }
}