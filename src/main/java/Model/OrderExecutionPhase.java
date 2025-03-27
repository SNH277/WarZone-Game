package Model;

import Controller.MainGameEngine;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;
import View.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OrderExecutionPhase extends Phase {

    public OrderExecutionPhase(CurrentState p_currentState, MainGameEngine p_mainGameEngine){
        super(p_mainGameEngine,p_currentState);
    }

    public void initPhase() {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

        while (d_mainGameEngine.getD_currentPhase() instanceof OrderExecutionPhase) {
            executeOrders();
            new MapView(d_currentState).showMap();

            if (checkEndOfGame(d_currentState)) {
                System.out.println("Game Over. Exiting...");
                System.exit(0);
            }

            while(d_currentState.getD_players() != null){
                System.out.println("Press Y/y if you want to continue for next turn or else press N/n");
                l_reader = new BufferedReader(new InputStreamReader(System.in));

                try{
                    String l_continue = l_reader.readLine();
                    if(l_continue.equalsIgnoreCase("N")){
                        System.exit(0);
                    }
                    else if(l_continue.equalsIgnoreCase("Y")){
                        d_gameplayController.assignArmies(d_currentState);
                        d_mainGameEngine.setIssueOrderPhase();
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
    }

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

    @Override
    protected void cardHandle(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    @Override
    protected void advance(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
    }

    @Override
    protected void deploy(String p_inputCommand, Player p_player) {
        printInvalidCommandInPhase();
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
        MapView l_mapView = new MapView(d_currentState);
        l_mapView.showMap();
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

    private void executeOrders() {
        addNeutralPlayer(d_currentState);
        d_mainGameEngine.setD_mainEngineLog("\nStarting Execution of Orders......", "start");

        while (d_gameplayController.isUnexecutedOrdersExist(d_currentState.getD_players())) {
            for (Player l_eachPlayer : d_currentState.getD_players()) {
                if (!l_eachPlayer.isD_moreOrders()) { // Skip players with no orders
                    continue;
                }

                Orders l_orderToExecute = l_eachPlayer.nextOrder();
                if (l_orderToExecute != null) {
                    d_currentState.updateLog(l_orderToExecute.orderExecutionLog(), "effect"); // Uncommented logging
                    l_orderToExecute.execute(d_currentState);
                }
            }
        }

        d_gameplayController.resetPlayerFlag(d_currentState.getD_players());
    }

    private void addNeutralPlayer(CurrentState p_currentState) {
        if (p_currentState.getD_players() == null) { // Ensure players list is not null
            return;
        }

        for (Player l_eachPlayer : p_currentState.getD_players()) {
            if (l_eachPlayer.getD_playerLog().equalsIgnoreCase("Neutral")) {
                return; // Exit early if a Neutral player already exists
            }
        }

        // No Neutral player found, so add one
        Player l_neutralPlayer = new Player("Neutral");
        l_neutralPlayer.setD_moreOrders(false);
        p_currentState.getD_players().add(l_neutralPlayer);
    }

}
