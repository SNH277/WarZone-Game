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
}
