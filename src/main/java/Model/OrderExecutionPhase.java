package Model;

import Constants.ProjectConstants;
import Controller.MainGameEngine;
import Exceptions.CommandValidationException;
import Services.GameService;
import Utils.CommandHandler;
import View.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Represents the Order Execution Phase in the game.
 * Handles the execution of player orders, checks for end of game,
 * and transitions to the next phase.
 * @author Shrey Hingu
 */
public class OrderExecutionPhase extends Phase {

    /**
     * Constructor for the OrderExecutionPhase.
     *
     * @param p_currentState    the current game state.
     * @param p_mainGameEngine  the main game engine instance.
     */
    public OrderExecutionPhase(CurrentState p_currentState, MainGameEngine p_mainGameEngine){
        super(p_mainGameEngine,p_currentState);
    }

    /**
     * Initializes and manages the order execution phase loop,
     * executes player orders, displays the map, checks for game end,
     * and transitions to the next phase based on user input.
     */
    @Override
    public void initPhase(boolean p_isTournamentMode) {
        while(d_mainGameEngine.getD_currentPhase() instanceof OrderExecutionPhase){
            executeOrders();

            MapView l_mapView = new MapView(d_currentState);
            l_mapView.showMap();

            if(this.checkEndOfGame(d_currentState)){
                System.exit(0);
            }
            try{
                String l_continue = this.continueForNextTurn(p_isTournamentMode);
                if(l_continue.equalsIgnoreCase("N") && p_isTournamentMode){
                    d_mainGameEngine.setD_mainEngineLog("Startup Phase", "phase");
                    d_mainGameEngine.setD_currentPhase(new StartupPhase(d_mainGameEngine,d_currentState));
                }
                else if(l_continue.equalsIgnoreCase("N") && !p_isTournamentMode){
                    d_mainGameEngine.setStartupPhase();
                }
                else if(l_continue.equalsIgnoreCase("Y")){
                    d_gameplayController.assignArmies(d_currentState);
                    d_mainGameEngine.setIssueOrderPhase(p_isTournamentMode);
                }
                else{
                    System.out.println("Invalid Input");
                }
            }
            catch (IOException l_e){
                System.out.println("Invalid Input");
            }
        }
    }

    /**
     * Continue for next turn string.
     *
     * @param pIsTournamentMode the p is tournament mode
     * @return the string
     * @throws IOException the io exception
     */
    private String continueForNextTurn(boolean pIsTournamentMode) throws IOException {
        String l_continue;
        if(pIsTournamentMode){
            d_currentState.setD_noOfTurnsLeft(d_currentState.getD_noOfTurnsLeft() - 1);
            l_continue = d_currentState.getD_noOfTurnsLeft() == 0 ? "N" : "Y";
        }else {
            System.out.println("Do you want to continue to next turn? (Y/N)");
            BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            l_continue = l_bufferedReader.readLine();
        }
        return l_continue;
    }

    /**
     * Checks whether the game has ended by verifying if a player (excluding Neutral)
     * owns all the countries on the map.
     *
     * @param p_currentState the current game state.
     * @return true if the game has ended and a player has won; false otherwise.
     */
    public boolean checkEndOfGame(CurrentState p_currentState) {
        int l_totalCountries = p_currentState.getD_map().getD_mapCountries().size();
        int l_neutralCountries = 0;
        Player l_winningPlayer = null;

        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if (l_eachPlayer.getD_playerName().equalsIgnoreCase("Neutral")) {
                l_neutralCountries += l_eachPlayer.getD_currentCountries().size(); // Accumulate instead of overwrite
            } else if (l_eachPlayer.getD_currentCountries().size() + l_neutralCountries == l_totalCountries) {
                l_winningPlayer = l_eachPlayer; // Store the potential winner
            }
        }

        if (l_winningPlayer != null) {
            d_mainGameEngine.setD_mainEngineLog(l_winningPlayer.getD_playerName() + " has won the game. Exiting the game....", "end");
            return true;
        }

        return false;
    }

    /**
     * Tournament mode.
     *
     * @param lCommandHandler the l command handler
     */
    @Override
    protected void tournamentMode(CommandHandler lCommandHandler) {
        printInvalidCommandInPhase();
    }

    /**
     * Load game.
     *
     * @param p_commandHandler the p command handler
     * @param p_player         the p player
     */
    @Override
    public void loadGame(CommandHandler p_commandHandler, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'card' command, which is not valid during the Order Execution Phase.
     *
     * @param p_inputCommand the command entered by the player.
     * @param p_player       the player issuing the command.
     */
    @Override
    protected void cardHandle(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'advance' command, which is not valid during the Order Execution Phase.
     *
     * @param p_inputCommand the command entered by the player.
     * @param p_player       the player issuing the command.
     */
    @Override
    protected void advance(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'deploy' command, which is not valid during the Order Execution Phase.
     *
     * @param p_inputCommand the command entered by the player.
     * @param p_player       the player issuing the command.
     */
    @Override
    protected void deploy(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'loadmap' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     */
    @Override
    protected void loadMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'editmap' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     * @throws IOException                 if an I/O error occurs (though unlikely here).
     */
    @Override
    protected void editMap(CommandHandler lCommandHandler) throws CommandValidationException, IOException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'editcountry' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     */
    @Override
    protected void editCountry(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'editcontinent' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     */
    @Override
    protected void editContinent(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'editneighbour' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     */
    @Override
    protected void editNeighbourCountry(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Displays the current state of the game map to the console.
     *
     * @throws CommandValidationException if there is an issue displaying the map.
     */
    @Override
    protected void showMap() throws CommandValidationException {
        MapView l_mapView = new MapView(d_currentState);
        l_mapView.showMap();
    }

    /**
     * Handles the 'gameplayer' command, which is not valid during the Order Execution Phase.
     *
     * @param p_ommandHandler the p ommand handler
     * @param p_player        the p player
     * @throws CommandValidationException the command validation exception
     */
    @Override
    protected void gamePlayer(CommandHandler p_ommandHandler, Player p_player) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'assigncountries' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler    the l command handler
     * @param p_player           the p player
     * @param p_isTournamentMode the p is tournament mode
     * @param p_currentState     the p current state
     * @throws CommandValidationException the command validation exception
     * @throws IOException                the io exception
     */
    @Override
    protected void assignCountries(CommandHandler lCommandHandler, Player p_player, Boolean p_isTournamentMode, CurrentState p_currentState) throws CommandValidationException, IOException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'validatemap' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     */
    @Override
    protected void validateMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Handles the 'savemap' command, which is not valid during the Order Execution Phase.
     *
     * @param lCommandHandler the command handler instance.
     * @throws CommandValidationException always thrown as this command is invalid in this phase.
     */
    @Override
    protected void saveMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    /**
     * Executes the next order for each player in the current state,
     * looping until all players have no more orders to execute.
     * Also logs the execution of each order.
     */
    private void executeOrders() {
        addNeutralPlayer(d_currentState);
        d_mainGameEngine.setD_mainEngineLog("\nStarting Execution of Orders......", "start");
        while(d_gameplayController.isUnexecutedOrdersExist(d_currentState.getD_players())){
            for(Player l_eachPlayer : d_currentState.getD_players()){
                Orders l_orderToExecute = l_eachPlayer.nextOrder();
                if(l_orderToExecute != null){
                    l_orderToExecute.printOrder();
                    l_orderToExecute.execute(d_currentState);
                }
            }
        }
        d_gameplayController.resetPlayerFlag(d_currentState.getD_players());
    }

    /**
     * Adds a Neutral player to the game state if one does not already exist.
     * The Neutral player is used to hold conquered territories with no owner.
     *
     * @param p_currentState the current game state.
     */
    private void addNeutralPlayer(CurrentState p_currentState) {
        Player l_player = null;
        for(Player l_eachPlayer : p_currentState.getD_players()){
            if(l_eachPlayer.getD_playerName().equalsIgnoreCase("Neutral")){
                l_player = l_eachPlayer;
                break;
            }
        }
        if(l_player == null){
            Player l_neutralPlayer = new Player("Neutral");
            l_neutralPlayer.setD_moreOrders(false);
            p_currentState.getD_players().add(l_neutralPlayer);
        }
        else{
            return;
        }
    }

    /**
     * Save game.
     *
     * @param p_commandHandler the p command handler
     * @param p_player         the p player
     * @throws CommandValidationException the command validation exception
     */
    @Override
    protected void saveGame(CommandHandler p_commandHandler, Player p_player) throws CommandValidationException {
        List<Map<String,String>> l_operationsList = p_commandHandler.getListOfOperations();
        if(l_operationsList == null || l_operationsList.isEmpty()){
            System.out.println(ProjectConstants.INVALID_SAVEGAME_COMMAND);
        }
        for(java.util.Map<String,String> l_map :l_operationsList){
            if(p_commandHandler.checkRequiredKey("Arguments", l_map)) {
                String l_fileName = l_map.get("Arguments");
                GameService.saveGame(this,l_fileName);
                d_mainGameEngine.setD_mainEngineLog("Game saved successfully to Filename : " + l_fileName, "effect");
            } else {
                throw new CommandValidationException(ProjectConstants.INVALID_SAVEGAME_COMMAND);
            }

        }
    }

}
