package Controller;

import Model.*;



/**
 * The {@code MainGameEngine} class serves as the main controller for handling game commands and logic.
 * It manages game states, player operations, and map functionalities.
 * @author Shrey Hingu
 */
public class MainGameEngine {


    /** The current state of the game. */
    CurrentState d_currentGameState = new CurrentState();
    Phase d_currentPhase = new StartupPhase(this,d_currentGameState);

    /**
     * Default constructor for the MainGameEngine.
     * Initializes the game engine without setting any initial state or phase.
     */
    public MainGameEngine(){

    }

    /**
     * Retrieves the current game state.
     *
     * @return the current {@link CurrentState} of the game
     */
    public CurrentState getD_currentGameState() {
        return d_currentGameState;
    }

    /**
     * Sets the current game state.
     *
     * @param d_currentGameState the new {@link CurrentState} to be set
     */
    public void setD_currentGameState(CurrentState d_currentGameState) {
        this.d_currentGameState = d_currentGameState;
    }

    /**
     * Retrieves the current phase of the game.
     *
     * @return the current {@link Phase}
     */
    public Phase getD_currentPhase() {
        return d_currentPhase;
    }

    /**
     * Sets the current phase of the game.
     *
     * @param d_currentPhase the new {@link Phase} to be set
     */
    public void setD_currentPhase(Phase d_currentPhase) {
        this.d_currentPhase = d_currentPhase;
    }

    /**
     * Transitions the game to the Issue Order Phase.
     * Logs the phase change and initializes the {@link IssueOrderPhase}.
     */
    public void setIssueOrderPhase(){
        this.setD_mainEngineLog("Issue Order Phase","phase");
        setD_currentPhase(new IssueOrderPhase(d_currentGameState,this));
        getD_currentPhase().initPhase();
    }

    /**
     * Transitions the game to the Order Execution Phase.
     * Logs the phase change and initializes the {@link OrderExecutionPhase}.
     */
    public void setOrderExecutionPhase(){
        this.setD_mainEngineLog("Order Execution Phase","phase");
        setD_currentPhase(new OrderExecutionPhase(d_currentGameState, this));
        getD_currentPhase().initPhase();
    }



    /**
     * The main entry point of the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        MainGameEngine l_mainGameEngine = new MainGameEngine();
        l_mainGameEngine.getD_currentPhase().getD_currentState().updateLog("Game Session Started","start");
        l_mainGameEngine.setD_mainEngineLog("Startup Phase of the Game","phase");
        l_mainGameEngine.startGame(l_mainGameEngine);
    }

    /**
     * Starts the game by initializing the current phase of the provided game engine.
     *
     * @param p_mainGameEngine the main game engine instance whose phase will be initialized
     */
    private void startGame(MainGameEngine p_mainGameEngine){
        p_mainGameEngine.getD_currentPhase().initPhase();
    }

    /**
     * Sets a log message for the main game engine and prints it to the console.
     * If the log type is "phase", the message is formatted with visual separators.
     *
     * @param p_logForMainEngine the log message to be recorded
     * @param p_logType          the type of the log (e.g., "phase", "effect", etc.)
     */
    public void setD_mainEngineLog(String p_logForMainEngine,String p_logType){
        d_currentPhase.getD_currentState().updateLog(p_logForMainEngine,p_logType);
        String l_consoleMessage;
        if (p_logType.equalsIgnoreCase("phase")){
            l_consoleMessage = "\n=============================== "+p_logForMainEngine+" ===============================\n";
        }else {
            l_consoleMessage = p_logForMainEngine;
        }
        System.out.println(l_consoleMessage);
    }
}
