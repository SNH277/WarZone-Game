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
    /** The player who owns the card used in the order. */
    Player d_cardOwner;

    /** The name of the country targeted by the order. */
    String d_targetCountryName;

    /** Log message recording the result of the order execution. */
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
     * Print order.
     */
    @Override
    public void printOrder() {
        this.d_logOfOrderExecution = "Blockade Order : " + d_cardOwner.getD_playerName() + " is using blockade card to triple the armies of " + d_targetCountryName;
        System.out.println(d_logOfOrderExecution);
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

    /**
     * Executes the Blockade card action if the order is valid.
     * <p>
     * This method triples the number of armies in the target country,
     * removes ownership of the country from the original player,
     * and transfers it to the Neutral player. If the Neutral player
     * is not found in the game state, an error is logged and execution stops.
     *
     * @param p_currentState the current state of the game used for validation,
     *                       updating country ownership, and logging
     */
    public void execute(CurrentState p_currentState) {
        if (!valid(p_currentState)) {
            return; // Exit early if validation fails
        }

        // Retrieve the target country from the map
        Country l_targetCountry = p_currentState.getD_map().getCountryByName(d_targetCountryName);

        // Triple the armies in the target country (handle 0 army case)
        int l_newArmies = (l_targetCountry.getD_armies() == 0) ? 1 : l_targetCountry.getD_armies();
        l_targetCountry.setD_armies(l_newArmies * 3);

        // Remove country from the original card owner
        d_cardOwner.getD_currentCountries().remove(l_targetCountry);

        // Find the Neutral player and transfer the country
        Player l_neutralPlayer = null;
        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if ("Neutral".equals(l_eachPlayer.getD_playerName())) {
                l_neutralPlayer = l_eachPlayer;
                break;
            }
        }

        if (l_neutralPlayer != null) {
            l_neutralPlayer.getD_currentCountries().add(l_targetCountry);
            System.out.println("Neutral Country: " + l_targetCountry.getD_countryName() + " has been assigned to Neutral Player");
        } else {
            // Log error if the Neutral player is not found
            this.setD_orderExecutionLog("Error! Neutral player not found", "error");
            p_currentState.updateLog(orderExecutionLog(), "effect");
            return;
        }

        // Remove the blockade card and disable further card usage for the turn
        d_cardOwner.removeCard("blockade");
        d_cardOwner.setD_oneCardPerTurn(false);

        // Log the successful blockade card use
        String logMessage = "Player " + d_cardOwner.getD_playerName() + " used blockade card to triple the armies of " + this.d_targetCountryName;
        this.setD_orderExecutionLog(logMessage, "default");
        p_currentState.updateLog(orderExecutionLog(), "effect");
    }

    /**
     * Validates whether the target country specified in the Blockade card
     * belongs to the player who owns the card.
     * <p>
     * If the player does not control the target country, logs an error and returns {@code false}.
     *
     * @param p_currentState the current state of the game used for validation and logging
     * @return {@code true} if the player owns the target country; {@code false} otherwise
     */
    public boolean valid(CurrentState p_currentState) {
        // Loop through the countries to check if the target country exists in the player's countries
        for (Country l_eachCountry : d_cardOwner.getD_currentCountries()) {
            if (l_eachCountry.getD_countryName().equals(d_targetCountryName)) {
                return true; // Target country belongs to the player
            }
        }

        // If we reach here, the target country was not found in the player's countries
        String errorMessage = "Invalid! Blockade card cannot be used because target country '"
                + d_targetCountryName + "' does not belong to player";
        this.setD_orderExecutionLog(errorMessage, "error");
        p_currentState.updateLog(orderExecutionLog(), "effect");
        return false;
    }
}