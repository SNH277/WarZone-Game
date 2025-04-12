package Model;

import Constants.ProjectConstants;
import Controller.MainGameEngine;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Services.GameService;
import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents the Issue Order Phase of the game.
 * In this phase, players issue orders such as deploy, advance, or use cards.
 * This phase continues until all players have issued their orders.
 * @author Shrey Hingu
 */
public class IssueOrderPhase extends Phase{

    /** Controller responsible for handling player-related operations such as assigning countries, armies, and processing orders. */
    PlayerController d_playerController = new PlayerController();


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
    public void initPhase(boolean p_isTournamentMode) {
        while(d_mainGameEngine.getD_currentPhase() instanceof IssueOrderPhase){
            try {
                issueOrder(p_isTournamentMode);
            } catch (Exception p_e) {
                d_currentState.updateLog(p_e.getMessage(),"error");
            }
        }

    }

    /**
     * Handles execution of a card-based command (e.g., bomb, blockade, airlift, negotiate)
     * for the given player. If the player owns the specified card, the command is executed
     * and the result is logged.
     *
     * @param p_inputCommand the full card command entered by the player.
     * @param p_player       the player issuing the card command.
     */
    protected void cardHandle(String p_inputCommand, Player p_player) {
        if(p_player.getD_cardOwnedByPlayer().contains(p_inputCommand.split(" ")[0])){
            p_player.handleCardCommand(p_inputCommand, d_currentState);
            d_mainGameEngine.setD_mainEngineLog(p_player.d_playerLog,"effect");
        }
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


    /**
     * This method is overridden in IssueOrderPhase but is currently not implemented.
     * Typically used to display the game map if allowed in this phase.
     *
     * @throws CommandValidationException if map viewing is restricted in this phase
     */
    @Override
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
    protected void gamePlayer(CommandHandler lCommandHandler,Player p_player) throws CommandValidationException, IOException {
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
    protected void assignCountries(CommandHandler lCommandHandler, Player p_player, Boolean p_isTournamentMode, CurrentState p_currentState) throws CommandValidationException, IOException {
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


    /**
     * Invalid command in Issue Order Phase.
     * Saving the map is not allowed once the game has progressed past the startup phase.
     *
     * @param lCommandHandler the command handler containing the input command
     * @throws CommandValidationException always thrown to indicate the command is invalid in this phase
     */
    @Override
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
    private void issueOrder(boolean p_isTournamentMode) throws Exception {
        do {
            for (Player l_eachPlayer : d_currentState.getD_players()) {
                if (l_eachPlayer.getD_currentCountries().size() == 0) {
                    l_eachPlayer.setD_moreOrders(false);
                }
                if(l_eachPlayer.isD_moreOrders() && !l_eachPlayer.getD_playerName().equals("Neutral")) {
                    l_eachPlayer.issueOrder(this);
                    l_eachPlayer.checkForMoreOrder(p_isTournamentMode);
                }
            }
        }while(d_playerController.checkForMoreOrders(d_currentState.getD_players()));

        d_mainGameEngine.setOrderExecutionPhase();
    }

    /**
     * Prompts a specific player to enter an order via command line.
     *
     * @param p_player the player issuing the order
     * @throws Exception if an error occurs while reading input or processing the command
     */
    public void askForOrders(Player p_player) throws Exception {
        String l_commandEntered = p_player.getPlayerOrder(d_currentState);
        if (l_commandEntered != null) {
            d_currentState.updateLog("Player : " + p_player.getD_playerName() + " has entered command : " + l_commandEntered ,"order");
            handleCommand(l_commandEntered, p_player);
        }
        else {
            return;
        }
    }

    /**
     * Extracts and returns a list of country names from the provided list of countries.
     *
     * @param p_countries A list of {@link Country} objects from which country names will be extracted.
     * @return A list of country names as {@link String}.
     */
    public static List<String> getCountryNames(List<Country> p_countries) {
        List<String> countryNames = new ArrayList<>();

        for (Country l_country : p_countries) {
            countryNames.add(l_country.getD_countryName());  // Extract and add country name
        }

        return countryNames;
    }



    /**
     * Creates and processes a deploy order for the given player.
     *
     * @param p_inputCommand the full deploy command string
     * @param p_player       the player issuing the deploy order
     */
    @Override
    protected void deploy(String p_inputCommand, Player p_player) {
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        if(l_commandHandler.getMainCommand().equals("deploy")){
            if(p_inputCommand.split(" ").length == 3){
                p_player.createDeployOrder(p_inputCommand);
                d_currentState.updateLog(p_player.getD_playerLog(), "effect");
            }
        }
    }


    /**
     * Creates and processes an advance order for the given player.
     *
     * @param p_inputCommand the full advance command string
     * @param p_player       the player issuing the advance order
     */
    @Override
    protected void advance(String p_inputCommand, Player p_player) {
        CommandHandler l_commandHandler = new CommandHandler(p_inputCommand);
        if(l_commandHandler.getMainCommand().equals("advance")){
            if(p_inputCommand.split(" ").length == 4){
                p_player.createAdvanceOrder(p_inputCommand, d_currentState);
                d_currentState.updateLog(p_player.getD_playerLog(), "effect");
            }
            else{
                System.err.println("Invalid! command for advance order.");
                d_currentState.updateLog("Invalid! command for advance order.","error");
            }
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
