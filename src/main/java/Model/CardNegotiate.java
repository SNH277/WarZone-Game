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
}