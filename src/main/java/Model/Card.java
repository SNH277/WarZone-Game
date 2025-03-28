package Model;

/**
 * The {@code Card} interface represents a playing card in the game model,
 * and it extends the {@link Orders} interface. This interface defines
 * the behavior for checking the validity of a card when used in a specific
 * game state.
 *
 * <p>Implementing classes must provide logic to determine whether a card
 * can be legally played based on the current state of the game.</p>
 *
 * @author Yash Koladiya
 */
public interface Card extends Orders{
    /**
     * Validates whether the current order is logically and strategically valid
     * based on the given game state. This method is typically implemented by
     * specific order types (e.g., Advance, Deploy) to check if execution
     * conditions are met.
     *
     * @param p_currentState the current state of the game.
     * @return {@code true} if the order is valid and can be executed;
     *         {@code false} otherwise.
     */
    Boolean validOrderCheck(CurrentState p_currentState);
}
