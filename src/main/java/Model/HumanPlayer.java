package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The {@code HumanPlayer} class represents a human player's behavior in the game.
 * It extends the {@code PlayerBehaviourStrategy} class and implements the necessary methods
 * to interact with the game by providing commands via user input.
 * This class allows the human player to provide deployment and advancement orders through
 * the console interface during the game. It also handles the retrieval of orders based on
 * the player's actions and interaction with the game.
 *
 * @author Taksh Rana
 */
public class HumanPlayer extends PlayerBehaviourStrategy{
    /** Constructs a new HumanPlayer object. */
    public HumanPlayer(){
    }

    @Override
    public String getPlayerBehaviour() {
        return "Human";
    }

    @Override
    public String createOrder(Player p_player, CurrentState p_currentState) throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please Enter command for Player : " + p_player.getD_playerName() + "   Armies left : " + p_player.getD_unallocatedArmies());
        System.out.println("1. Deploy Order Command : 'deploy <countryName> <noOfArmies>'");
        System.out.println("2. Advance Order Command : 'advance <countryFromName> <countryToName> <noOfArmies>");
        System.out.println();
        System.out.print("Enter your command: ");
        String l_command = l_reader.readLine();
        return l_command;
    }

    @Override
    public String createCardOrder(Player p_player, CurrentState p_currentState, String p_cardName) {
        return null;
    }

    @Override
    public String createAdvanceOrder(Player p_player, CurrentState p_currentState) {
        return null;
    }

    @Override
    public String createDeployOrder(Player p_player, CurrentState p_currentState) {
        return null;
    }
}
