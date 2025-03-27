package Model;

import Controller.MainGameEngine;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Represents the Issue Order Phase of the game.
 * In this phase, players issue orders such as deploy, advance, or use cards.
 * This phase continues until all players have issued their orders.
 * @author Shrey Hingu
 */
public class IssueOrderPhase extends Phase{

    PlayerController d_playerController = new PlayerController();

    /**
     * Constructs the IssueOrderPhase with the given game engine and current state.
     *
     * @param p_currentState    the current state of the game
     * @param p_mainGameEngine  the game engine controlling the phase transitions
     */
    public IssueOrderPhase(CurrentState p_currentState, MainGameEngine p_mainGameEngine){
        super(p_mainGameEngine,p_currentState);
    }

    /**
     * Initializes the issue order phase and continuously processes player orders
     * while the current phase is IssueOrderPhase.
     */
    public void initPhase() {
        while(d_mainGameEngine.getD_currentPhase() instanceof IssueOrderPhase){
            try {
                issueOrder();
            } catch (Exception p_e) {
                d_currentState.updateLog(p_e.getMessage(),"error");
            }
        }

    }

    /**
     * Loops through all players and allows them to issue orders until all are done.
     *
     * @throws Exception if an error occurs while issuing orders
     */
    protected void cardHandle(String p_inputCommand, Player p_player) {
        if(p_player.getD_cardOwnedByPlayer().contains(p_inputCommand.split(" ")[0])){
            p_player.handleCardCommand(p_inputCommand, d_currentState);
            d_mainGameEngine.setD_mainEngineLog(p_player.d_playerLog,"effect");
        }
        p_player.checkForMoreOrder();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Loading a map is only allowed during the Startup Phase before the game begins.
     *
     * @param lCommandHandler the command handler containing the map loading command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
    protected void loadMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Editing the map is only permitted during the Startup Phase.
     *
     * @param lCommandHandler the command handler containing the map editing command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     * @throws IOException declared for compatibility but not used in this override
     */
    @Override
    protected void editMap(CommandHandler lCommandHandler) throws CommandValidationException, IOException {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Editing countries is restricted to the Startup Phase when the map is being configured.
     *
     * @param lCommandHandler the command handler containing the country editing command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
    protected void editCountry(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Editing continents is only permitted during the Startup Phase when the map is being configured.
     *
     * @param lCommandHandler the command handler containing the continent editing command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
    protected void editContinent(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Editing neighboring country relationships is restricted to the Startup Phase.
     *
     * @param lCommandHandler the command handler containing the neighbor editing command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
    protected void editNeighbourCountry(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    /**
     * This method is overridden in IssueOrderPhase but is currently not implemented.
     * Typically used to display the game map if allowed in this phase.
     *
     * @throws CommandValidationException if map viewing is restricted in this phase
     */
    protected void showMap() throws CommandValidationException {

    }

    /**
     * Invalid command in Issue Order Phase.
     * Adding or removing players is only allowed during the Startup Phase before gameplay begins.
     *
     * @param lCommandHandler the command handler containing the player management command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
    protected void gamePlayer(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Assigning countries is only allowed during the Startup Phase before the game begins.
     *
     * @param lCommandHandler the command handler containing the assignment command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     * @throws IOException never thrown here directly but declared for interface compatibility
     */
    @Override
    protected void assignCountries(CommandHandler lCommandHandler) throws CommandValidationException, IOException {
        printInvalidCommandInPhase();
    }

    /**
     * Invalid command in Issue Order Phase.
     * Map validation is only permitted during the Startup Phase and cannot be performed once the game has started.
     *
     * @param lCommandHandler the command handler containing the validation command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
    protected void validateMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    /**
     * Invalid command in Issue Order Phase.
     * Saving the map is not allowed once the game has progressed past the startup phase.
     *
     * @param lCommandHandler the command handler containing the input command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    protected void saveMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Iterates through all players in the game and allows each player
     * to issue orders one at a time if they have more orders to issue.
     * Neutral players are skipped.
     *
     * @throws Exception if an error occurs while players issue orders
     */
    private void issueOrder() throws Exception {
        do {
            for (Player l_eachPlayer : d_currentState.getD_players()) {
                if(l_eachPlayer.isD_moreOrders() && !l_eachPlayer.getD_playerName().equals("Neutral")) {
                    l_eachPlayer.issueOrder(this);
                }
            }
        }while(d_playerController.checkForMoreOrders(d_currentState.getD_players()));

//        d_mainGameEngine.setOrderExecutionPhase();
    }

    /**
     * Prompts a specific player to enter an order via command line.
     *
     * @param p_player the player issuing the order
     * @throws Exception if an error occurs while reading input or processing the command
     */
    public void askForOrders(Player p_player) throws Exception {
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please Enter command for Player : " + p_player.getD_playerName() + "   Armies left : " + p_player.getD_unallocatedArmies());
        System.out.println("1. Deploy Order Command : 'deploy <countryName> <noOfArmies>'");
        System.out.println("2. Advance Order Command : 'advance <countryFromName> <countryToName> <noOfArmies>");
        System.out.println();
        System.out.print("Enter your command: ");
        String l_commandEntered = l_bufferedReader.readLine();
        d_currentState.updateLog("Player : " + p_player.getD_playerName() + " has entered command : " + l_commandEntered ,"order");
        handleCommand(l_commandEntered, p_player);
    }


    @Override
    /**
     * Creates and processes a deploy order for the given player.
     *
     * @param p_inputCommand the full deploy command string
     * @param p_player       the player issuing the deploy order
     */
    protected void deploy(String p_inputCommand, Player p_player) {
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        if(l_commandHandler.getMainCommand().equals("deploy")){
            if(p_inputCommand.split(" ").length == 3){
                p_player.createDeployOrder(p_inputCommand);
                d_currentState.updateLog(p_player.getD_playerLog(), "effect");
                p_player.checkForMoreOrder();
            }
        }
    }

    @Override
    /**
     * Creates and processes an advance order for the given player.
     *
     * @param p_inputCommand the full advance command string
     * @param p_player       the player issuing the advance order
     */
    protected void advance(String p_inputCommand, Player p_player) {
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        if(l_commandHandler.getMainCommand().equals("advance")){
            if(p_inputCommand.split(" ").length == 4){
                p_player.createAdvanceOrder(p_inputCommand, d_currentState);
                d_currentState.updateLog(p_player.getD_playerLog(), "effect");

                p_player.checkForMoreOrder();
            }
            else{
                System.err.println("Invalid! command for advance order.");
                d_currentState.updateLog("Invalid! command for advance order.","error");
            }
        }
    }
}
