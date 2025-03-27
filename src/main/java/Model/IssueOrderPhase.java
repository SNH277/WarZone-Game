package Model;

import Controller.MainGameEngine;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IssueOrderPhase extends Phase{

    PlayerController d_playerController = new PlayerController();

    public IssueOrderPhase(CurrentState p_currentState, MainGameEngine p_mainGameEngine){
        super(p_mainGameEngine,p_currentState);
    }

    public void initPhase() {
        while(d_mainGameEngine.getD_currentPhase() instanceof IssueOrderPhase){
            try {
                issueOrder();
            } catch (Exception p_e) {
                d_currentState.updateLog(p_e.getMessage(),"error");
            }
        }

    }

    protected void cardHandle(String p_inputCommand, Player p_player) {
        if(p_player.getD_cardOwnedByPlayer().contains(p_inputCommand.split(" ")[0])){
            p_player.handleCardCommand(p_inputCommand, d_currentState);
            d_mainGameEngine.setD_mainEngineLog(p_player.d_playerLog,"effect");
        }
        p_player.checkForMoreOrder();
    }

    @Override
    protected void loadMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void editMap(CommandHandler lCommandHandler) throws CommandValidationException, IOException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void editCountry(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void editContinent(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void editNeighbourCountry(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void showMap() throws CommandValidationException {

    }

    @Override
    protected void gamePlayer(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void assignCountries(CommandHandler lCommandHandler) throws CommandValidationException, IOException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void validateMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

    @Override
    protected void saveMap(CommandHandler lCommandHandler) throws CommandValidationException {
        printInvalidCommandInPhase();
    }

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
