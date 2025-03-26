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
    Boolean validOrderCheck(CurrentState p_currentState);
}
