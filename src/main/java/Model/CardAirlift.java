package Model;

/**
 * The {@code CardAirlift} class represents an Airlift card in the game.
 * This card allows a player to transfer armies from one owned country
 * to another owned country during the game.
 * <p>
 * It implements the {@link Card} interface and stores details about the
 * number of armies, source and target countries, and the player who owns the card.
 * </p>
 *
 * @author Yash Koladiya
 */
public class CardAirlift implements Card{
    /**
     * Number of armies to be airlifted.
     */
    Integer d_armyCount;
    /**
     * Name of the source country from which armies are being airlifted.
     */
    String d_sourceCountryName;
    /**
     * Player who owns this Airlift card.
     */
    Player d_cardOwner;
    /**
     * Name of the target country to which armies are being sent.
     */
    String d_targetCountryName;
    /**
     * Log of the order execution, used to store any result or messages.
     */
    String d_logOfOrderExecution;

    /**
     * Constructs a {@code CardAirlift} with specified army count, source country,
     * card owner, and target country.
     *
     * @param p_armyCount         the number of armies to be transferred
     * @param p_sourceCountryName the name of the source country
     * @param p_cardOwner         the player who owns the card
     * @param p_targetCountryName the name of the target country
     */
    public CardAirlift(Integer p_armyCount, String p_sourceCountryName, Player p_cardOwner, String p_targetCountryName) {
        this.d_armyCount = p_armyCount;
        this.d_sourceCountryName = p_sourceCountryName;
        this.d_cardOwner = p_cardOwner;
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * Returns a string representation of the Airlift card, including the number of armies,
     * source and target country names, and the card owner.
     *
     * @return a formatted string representing the Airlift card
     */
    @Override
    public String toString() {
        return "CardAirlift{" +
                "d_armyCount=" + d_armyCount +
                ", d_sourceCountryName='" + d_sourceCountryName + '\'' +
                ", d_cardOwner=" + d_cardOwner +
                ", d_targetCountryName='" + d_targetCountryName + '\'' +
                '}';
    }

    /**
     * Returns the log message of the order execution.
     *
     * @return the execution log message
     */
    public String orderExecutionLog() {
        return this.d_logOfOrderExecution;
    }

    /**
     * Sets the log message for order execution and prints it to the console.
     * Displays as an error if the message type is "error"; otherwise, prints normally.
     *
     * @param p_orderExecutionLog the log message to be set
     * @param p_messageType       the type of message ("error" or other)
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
     * Checks if both the source and target countries exist in the current game state.
     * If either country is missing from the map, logs an appropriate error message
     * and returns {@code false}.
     *
     * @param p_currentState the current state of the game containing the map and game data
     * @return {@code true} if both source and target countries exist; {@code false} otherwise
     */
    public Boolean validOrderCheck(CurrentState p_currentState) {
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);
        Country l_sourceCountry = p_currentState.getD_map().getCountryByName(d_sourceCountryName);

        if (l_targetCountry == null || l_sourceCountry == null) {
            String l_errorMessage = (l_targetCountry == null ? "Target" : "Source") +
                    " country does not exist: " +
                    (l_targetCountry == null ? d_targetCountryName : d_sourceCountryName);

            logError(p_currentState, l_errorMessage);
            return false;
        }
        return true;
    }
    /**
     * Logs an error message for an invalid Airlift order, updates the internal log,
     * and appends the message to the game state's log.
     *
     * @param p_currentState the current state of the game
     * @param p_message      the error message to be logged
     */
    private void logError(CurrentState p_currentState, String p_message) {
        this.setD_orderExecutionLog("Invalid! " + p_message, "error");
        p_currentState.updateLog(orderExecutionLog(), "effect");
    }

    /**
     * Executes the Airlift card action if the order is valid.
     * <p>
     * Transfers the specified number of armies from the source country
     * to the target country, updates the army counts, and logs the result.
     * If validation fails, logs an error and aborts execution.
     *
     * @param p_currentState the current game state used for validation, country access, and logging
     */
    public void execute(CurrentState p_currentState) {
        if (!valid(p_currentState)) {
            this.setD_orderExecutionLog("Invalid! Airlift card cannot be used", "error");
            p_currentState.updateLog("Invalid! Airlift card cannot be used", "effect");
            return;
        }

        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);
        Country l_sourceCountry = p_currentState.getD_map().getCountryByName(d_sourceCountryName);


        // Execute airlift action
        l_sourceCountry.setD_armies(l_sourceCountry.getD_armies() - d_armyCount);
        l_targetCountry.setD_armies(l_targetCountry.getD_armies() + d_armyCount);

        // Remove airlift card from player
//        d_cardOwner.removeCard("airlift");
//        d_cardOwner.setD_oneCardPerTurn(false);

        // Log the successful airlift action
        String l_successMessage = "Airlift card used to move " + d_armyCount +
                " armies from " + d_sourceCountryName +
                " to " + d_targetCountryName;
        this.setD_orderExecutionLog(l_successMessage, "default");
        p_currentState.updateLog(l_successMessage, "effect");
    }

    /**
     * Validates whether the Airlift card order is legitimate based on the player's ownership
     * and army availability in the current game state.
     * <p>
     * This method checks:
     * <ul>
     *     <li>Whether the player owns the source and target countries.</li>
     *     <li>Whether the source country has enough armies for the airlift.</li>
     * </ul>
     * If any of these conditions fail, an error message is logged and the method returns {@code false}.
     *
     * @param p_currentState the current state of the game used for validation and logging
     * @return {@code true} if the order is valid; {@code false} otherwise
     */
    public boolean valid(CurrentState p_currentState) {
        Country l_targetCountry = null;
        Country l_sourceCountry = null;

        // Single loop to find both source and target countries
        for (Country l_eachCountry : d_cardOwner.getD_currentCountries()) {
            if (l_eachCountry.getD_countryName().equals(d_targetCountryName)) {
                l_targetCountry = l_eachCountry;
            }
            if (l_eachCountry.getD_countryName().equals(d_sourceCountryName)) {
                l_sourceCountry = l_eachCountry;
            }
            if (l_targetCountry != null && l_sourceCountry != null) {
                break; // Stop searching early if both are found
            }
        }

        // Validate target country ownership
        if (l_targetCountry == null) {
            String l_errorMessage = "Invalid! Target country does not belong to player";
            this.setD_orderExecutionLog(l_errorMessage, "error");
            p_currentState.updateLog(l_errorMessage, "effect");
            return false;
        }

        // Validate source country ownership
        if (l_sourceCountry == null) {
            String l_errorMessage = "Invalid! Source country does not belong to player";
            this.setD_orderExecutionLog(l_errorMessage, "error");
            p_currentState.updateLog(l_errorMessage, "effect");
            return false;
        }

        // Validate sufficient armies in source country
        if (l_sourceCountry.getD_armies() < d_armyCount) {
            String l_errorMessage = "Invalid! Source country does not have enough armies";
            this.setD_orderExecutionLog(l_errorMessage, "error");
            p_currentState.updateLog(l_errorMessage, "effect");
            return false;
        }

        return true; // If all validations pass
    }

}
