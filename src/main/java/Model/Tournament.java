package Model;

import Controller.MainGameEngine;
import Controller.MapController;
import Exceptions.*;
import Constants.*;
import Utils.CommandHandler;

import java.io.Serializable;
import java.util.*;

public class Tournament implements Serializable {

    MapController d_mapController =new MapController();
    List<CurrentState> d_currentStateList = new ArrayList<>();

    public Tournament(){

    }

    public MapController getD_mapController() {
        return d_mapController;
    }

    public void setD_mapController(MapController d_mapController) {
        this.d_mapController = d_mapController;
    }

    public List<CurrentState> getD_currentStateList() {
        return d_currentStateList;
    }

    public void setD_currentStateList(List<CurrentState> d_currentStateList) {
        this.d_currentStateList = d_currentStateList;
    }

    public boolean parseTournamentCommand(CurrentState p_currentState, String p_operation, String p_argument, MainGameEngine p_maingameEngine) throws CommandValidationException {

        if (p_operation == null || p_argument == null || p_maingameEngine == null) {
            throw new CommandValidationException(ProjectConstants.INVALID_TOURNAMENT_MODE_COMMAND);
        }

        switch (p_operation.toUpperCase()) {
            case "M":
                return parseMapArguments(p_argument, p_maingameEngine);
            case "P":
                return parseStrategyArguments(p_currentState, p_argument, p_maingameEngine);
            case "G":
                return parseNoOfGameArgument(p_argument, p_maingameEngine);
            case "D":
                return parseNoOfTurnArgument(p_argument, p_maingameEngine);
            default:
                throw new CommandValidationException(ProjectConstants.INVALID_TOURNAMENT_MODE_COMMAND);
        }
    }

    private boolean parseNoOfTurnArgument(String p_argument, MainGameEngine p_maingameEngine) {
        if (p_argument == null || p_argument.trim().isEmpty()) {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_TURN_COUNT, "effect");
            return false;
        }

        try {
            String[] l_args = p_argument.split(" ");
            int l_maxNoOfTurns = Integer.parseInt(l_args[0]); // Safely parsing integer

            if (l_maxNoOfTurns < 10 || l_maxNoOfTurns > 50) {
                p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_TURN_COUNT, "effect");
                return false;
            }

            for (CurrentState l_currentState : d_currentStateList) {
                l_currentState.setD_maxNoOfTurns(l_maxNoOfTurns);
                l_currentState.setD_noOfTurnsLeft(l_maxNoOfTurns);
            }

            return true;
        } catch (NumberFormatException e) {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_TURN_COUNT, "effect");
            return false;
        }
    }

    private boolean parseNoOfGameArgument(String p_argument, MainGameEngine p_maingameEngine) {
        int l_noOfGames = Integer.parseInt(p_argument.split(" ")[0]);
        if(l_noOfGames >= 1 && l_noOfGames <= 5){
            List<CurrentState> l_additionalCurrentStates = new ArrayList<>();
            for(int l_gameNumber =0 ; l_gameNumber < l_noOfGames -1; l_gameNumber++) {
                for (CurrentState l_eachState : d_currentStateList) {
                    CurrentState l_eachStateToAdd = new CurrentState();
                    Map l_loadedMap = d_mapController.loadMap(l_eachStateToAdd, l_eachState.getD_map().getD_mapName());
                    l_loadedMap.setD_mapName(l_eachState.getD_map().getD_mapName());
                    List<Player> l_playersToCopy = getPlayersToAdd(l_eachState.getD_players());
                    l_eachStateToAdd.setD_players(l_playersToCopy);
                    l_eachStateToAdd.setD_loadCommand(true);
                    l_additionalCurrentStates.add(l_eachStateToAdd);
                }
            }
            d_currentStateList.addAll(l_additionalCurrentStates);
            return true;
        }else {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_GAME_COUNT,"effect");
            return false;
        }
    }

    private boolean parseStrategyArguments(CurrentState p_currentState, String p_argument, MainGameEngine p_maingameEngine) {
        if (p_argument == null || p_argument.trim().isEmpty()) {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_STRATEGY, "effect");
            return false;
        }

        String[] l_listOfStrategies = p_argument.split(" ");
        int l_strategySize = l_listOfStrategies.length;

        if (l_strategySize < 2 || l_strategySize > 4) {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_STRATEGY_COUNT, "effect");
            return false;
        }

        Set<String> l_uniqueStrategies = new HashSet<>();  // Using HashSet instead of List
        List<Player> l_playersInGame = new ArrayList<>();

        for (String l_strategy : l_listOfStrategies) {
            if (!l_uniqueStrategies.add(l_strategy)) {  // HashSet returns false if duplicate
                p_maingameEngine.setD_mainEngineLog(ProjectConstants.DUPLICATE_STRATEGY, "effect");
                return false;
            }
            if (!ProjectConstants.TOURNAMENT_PLAYER_BEHAVIOUR.contains(l_strategy)) {
                p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_STRATEGY, "effect");
                return false;
            }
        }

        // Assign players based on strategies
        setTournamentPlayers(p_maingameEngine, l_listOfStrategies, p_currentState.getD_players(), l_playersInGame);

        for (CurrentState l_currentState : d_currentStateList) {
            l_currentState.setD_players(getPlayersToAdd(l_playersInGame));
        }

        return true;
    }

    private void setTournamentPlayers(MainGameEngine p_maingameEngine, String[] p_listOfStrategies, List<Player> p_players, List<Player> p_playersInGame) {
        for(String l_strategy : p_listOfStrategies) {
            for(Player l_eachPlayer : p_players){
                if(l_eachPlayer.getD_playerBehaviourStrategy().getPlayerBehaviour().equalsIgnoreCase(l_strategy)){
                    p_playersInGame.add(l_eachPlayer);
                    p_maingameEngine.setD_mainEngineLog("Player : "+l_eachPlayer.getD_playerName()+" added to the game with strategy : "+l_eachPlayer.getD_playerBehaviourStrategy().getPlayerBehaviour(),"effect");
                }
            }
        }
    }

    private List<Player> getPlayersToAdd(List<Player> p_playersInGame) {
        List<Player> l_players = new ArrayList<>();
        for(Player l_player : p_playersInGame){
            Player l_newPlayer = new Player(l_player.getD_playerName());

            if(l_player.getD_playerBehaviourStrategy() instanceof AggressivePlayer){
                l_newPlayer.setD_playerBehaviourStrategy(new AggressivePlayer());
            } else if (l_player.getD_playerBehaviourStrategy() instanceof BenevolentPlayer){
                l_newPlayer.setD_playerBehaviourStrategy(new BenevolentPlayer());
            } else if (l_player.getD_playerBehaviourStrategy() instanceof CheaterPlayer){
                l_newPlayer.setD_playerBehaviourStrategy(new CheaterPlayer());
            } else if (l_player.getD_playerBehaviourStrategy() instanceof RandomPlayer){
                l_newPlayer.setD_playerBehaviourStrategy(new RandomPlayer());
            }
            l_players.add(l_newPlayer);
        }
        return l_players;
    }

    private boolean parseMapArguments(String p_argument, MainGameEngine p_maingameEngine) {
        if (p_argument == null || p_argument.trim().isEmpty()) {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_MAP_FILE_COUNT, "effect");
            return false;
        }

        String[] l_listOfMapFiles = p_argument.split(" ");
        int l_mapFileSize = l_listOfMapFiles.length;

        if (l_mapFileSize < 1 || l_mapFileSize > 5) {
            p_maingameEngine.setD_mainEngineLog(ProjectConstants.INVALID_MAP_FILE_COUNT, "effect");
            return false;
        }

        boolean l_allMapsLoaded = true;

        for (String l_mapFile : l_listOfMapFiles) {
            CurrentState l_currentState = new CurrentState();
            Map l_map = d_mapController.loadMap(l_currentState, l_mapFile);

            if (l_map == null) {
                p_maingameEngine.setD_mainEngineLog("Error: Failed to load map: " + l_mapFile, "effect");
                l_allMapsLoaded = false;
                continue;
            }

            if (!l_map.validateMap()) {
                d_mapController.resetMap(l_currentState, l_mapFile);
                p_maingameEngine.setD_mainEngineLog("Error: Invalid map file: " + l_mapFile, "effect");
                l_allMapsLoaded = false;
                continue;
            }

            l_map.setD_mapName(l_mapFile);
            l_currentState.setD_loadCommand(true);
            p_maingameEngine.setD_mainEngineLog("Map: " + l_mapFile + " Loaded Successfully", "effect");
            d_currentStateList.add(l_currentState);
        }

        return l_allMapsLoaded;
    }

    public boolean requiredTournamentArgPresent(List<java.util.Map<String, String>> p_operationsList, CommandHandler p_commandHandler) {
        if (p_operationsList == null || p_operationsList.size() != 4) {
            return false;
        }

        Set<String> l_expectedOperations = new HashSet<>(Arrays.asList("M", "P", "G", "D"));
        Set<String> l_foundOperations = new HashSet<>();

        for (java.util.Map<String, String> l_operation : p_operationsList) {
            if (p_commandHandler.checkRequiredKey("Arguments", l_operation) && p_commandHandler.checkRequiredKey("Operation", l_operation)) {
                String l_op = l_operation.get("Operation");
                l_foundOperations.add(l_op);
            }
        }

        return l_foundOperations.equals(l_expectedOperations);
    }
}
