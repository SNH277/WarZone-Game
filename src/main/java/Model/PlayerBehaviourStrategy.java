package Model;

import java.io.IOException;
import java.io.Serializable;

/**
 * Abstract class representing a strategy for player behavior in the game.
 * Subclasses should implement specific behavior types (e.g., aggressive, defensive).
 */
public abstract class PlayerBehaviourStrategy implements Serializable {

    /** Current state of the game. */
    CurrentState d_currentState;

    /** The player associated with this strategy. */
    Player d_player;

    /**
     * Default constructor.
     */
    public PlayerBehaviourStrategy() {
    }

    /**
     * Gets the type of player behavior.
     *
     * @return a string indicating the behavior type.
     */
    public abstract String getPlayerBehaviour();

    /**
     * Creates a general order for the player.
     *
     * @param p_player the player issuing the order
     * @param p_currentState the current game state
     * @return the generated order as a string
     * @throws IOException if there is an I/O error
     */
    public abstract String createOrder(Player p_player, CurrentState p_currentState) throws IOException;

    /**
     * Creates an order to use a specific card.
     *
     * @param p_player the player issuing the order
     * @param p_currentState the current game state
     * @param p_cardName the name of the card
     * @return the card order as a string
     */
    public abstract String createCardOrder(Player p_player, CurrentState p_currentState, String p_cardName);

    /**
     * Creates an advance order (e.g., to move or attack).
     *
     * @param p_player the player issuing the order
     * @param p_currentState the current game state
     * @return the advance order as a string
     */
    public abstract String createAdvanceOrder(Player p_player, CurrentState p_currentState);

    /**
     * Creates a deploy order to place armies.
     *
     * @param p_player the player issuing the order
     * @param p_currentState the current game state
     * @return the deploy order as a string
     */
    public abstract String createDeployOrder(Player p_player, CurrentState p_currentState);


}
