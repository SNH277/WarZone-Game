package Model;

import Constants.ProjectConstants;
import Controller.MainGameEngine;
import Controller.MapController;
import Controller.PlayerController;
import Exceptions.CommandValidationException;
import Utils.CommandHandler;
import View.MapView;

import java.io.IOException;
import java.util.Set;

public abstract class Phase {
    MainGameEngine d_mainGameEngine;
    CurrentState d_currentState;
    MapController d_mapController;
    PlayerController d_gameplayController;

    public Phase(MainGameEngine p_mainGameEngine, CurrentState p_currentState) {
        this.d_mainGameEngine = p_mainGameEngine;
        this.d_currentState = p_currentState;
        this.d_mapController = new MapController();
        this.d_gameplayController = new PlayerController();
    }

    public MainGameEngine getD_mainGameEngine() {
        return d_mainGameEngine;
    }

    public void setD_mainGameEngine(MainGameEngine p_mainGameEngine) {
        this.d_mainGameEngine = p_mainGameEngine;
    }

    public CurrentState getD_currentState() {
        return d_currentState;
    }

    public void setD_currentState(CurrentState p_currentState) {
        this.d_currentState = p_currentState;
    }

    public MapController getD_mapController() {
        return d_mapController;
    }

    public void setD_mapController(MapController p_mapController) {
        this.d_mapController = p_mapController;
    }

    public PlayerController getD_gameplayController() {
        return d_gameplayController;
    }

    public void setD_gameplayController(PlayerController p_gameplayController) {
        this.d_gameplayController = p_gameplayController;
    }

    public abstract void initPhase();

    public void handleCommand(String p_inputCommand) throws CommandValidationException, IOException {
        commandHandler(p_inputCommand,null);
    }
    public void handleCommand(String p_inputCommand, Player p_player) throws CommandValidationException, IOException {
        commandHandler(p_inputCommand, p_player);
    }

    private void commandHandler(String p_inputCommand,Player p_player) throws CommandValidationException, IOException {
        CommandHandler l_commandHandler =new CommandHandler(p_inputCommand);
        String l_mainCommand = l_commandHandler.getMainCommand();
        boolean l_isMapAvailable = (d_currentState.getD_map() != null);
        d_currentState.updateLog(l_commandHandler.getMainCommand(),"command");

        Set<String> requiresMap = Set.of(
                "editcountry", "editcontinent", "editneighbour",
                "showmap", "gameplayer", "assigncountries",
                "validatemap", "savemap", "deploy", "advance",
                "bomb", "blockade", "airlift"
        );

        if (requiresMap.contains(l_mainCommand) && !l_isMapAvailable) {
            System.out.println(ProjectConstants.MAP_NOT_AVAILABLE);
            return;
        }

        switch (l_mainCommand) {
            case "loadmap":
                loadMap(l_commandHandler);
                break;
            case "editmap":
                editMap(l_commandHandler);
                break;
            case "editcountry":
                editCountry(l_commandHandler);
                break;
            case "editcontinent":
                editContinent(l_commandHandler);
                break;
            case "editneighbour":
                editNeighbourCountry(l_commandHandler);
                break;
            case "showmap":
                showMap();
                break;
            case "gameplayer":
                gamePlayer(l_commandHandler);
                break;
            case "assigncountries":
                assignCountries(l_commandHandler);
                break;
            case "validatemap":
                validateMap(l_commandHandler);
                break;
            case "savemap":
                saveMap(l_commandHandler);
                break;
            case "deploy":
                deploy(p_inputCommand,p_player);
                break;
            case "advance":
                advance(p_inputCommand,p_player);
                break;
            case "bomb":
            case "blockade":
            case "airlift":
                cardHandle(p_inputCommand,p_player);
                break;
            case "negotiate":
                if(!l_isMapAvailable)
                    d_currentState.getD_modelLogger().setD_message("Command 'negotiate' failed: Map is not available.","effect");
                else
                    cardHandle(p_inputCommand,p_player);
            case "exit":
                d_currentState.getD_modelLogger().setD_message("------------Game Session Terminated. All progress saved.------------","effect");
                System.out.println("Closing game... Thank you for playing");
                System.exit(0);
                break;
            default:
                d_mainGameEngine.setD_mainEngineLog("Invalid command for this phase.","effect");
                System.out.println(ProjectConstants.INVALID_COMMAND);
                break;
        }
    }

    protected abstract void cardHandle(String p_inputCommand, Player p_player);

    protected abstract void advance(String p_inputCommand, Player p_player);

    protected abstract void deploy(String p_inputCommand, Player p_player);

    protected abstract void saveMap(CommandHandler p_commandHandler) throws CommandValidationException;

    protected abstract void validateMap(CommandHandler p_commandHandler) throws CommandValidationException;

    protected abstract void assignCountries(CommandHandler p_commandHandler) throws CommandValidationException, IOException;

    protected abstract void gamePlayer(CommandHandler p_commandHandler) throws CommandValidationException;

    protected abstract void showMap() throws CommandValidationException;

    protected abstract void editNeighbourCountry(CommandHandler p_commandHandler) throws CommandValidationException;

    protected abstract void editContinent(CommandHandler p_commandHandler) throws  CommandValidationException;

    protected abstract void editCountry(CommandHandler p_commandHandler) throws CommandValidationException;

    protected abstract void editMap(CommandHandler p_commandHandler) throws CommandValidationException, IOException;

    protected abstract void loadMap(CommandHandler p_commandHandler) throws CommandValidationException;

    public void printInvalidCommandInPhase(){
        d_mainGameEngine.setD_mainEngineLog("Invalid Command entered for this phase.","effect");
    }

}
