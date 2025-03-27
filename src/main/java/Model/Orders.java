package Model;

/**
 * Interface representing a generic order in the game.
 * All types of orders (e.g., deploy, advance, airlift) must implement this interface.
 * @author Shrey Hingu
 */
public interface Orders {

    /**
     * Executes the order and applies changes to the current game state.
     *
     * @param p_currentState the current state of the game to be modified
     */
    void execute(CurrentState p_currentState);

    /**
     * Returns a log message describing the result of the order execution.
     *
     * @return the order execution log message
     */
    String orderExecutionLog();

    /**
     * Sets the log message after order execution.
     *
     * @param p_orderExecutionLog the log message to be stored
     * @param p_logType the type/category of the log (e.g., INFO, ERROR)
     */
    void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType);

    /**
     * Validates the order based on the current game state.
     *
     * @param p_currentState the current state of the game
     * @return true if the order is valid, false otherwise
     */
    boolean valid(CurrentState p_currentState);
}
