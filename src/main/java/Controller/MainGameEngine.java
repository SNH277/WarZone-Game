package Controller;

import Model.*;



/**
 * The {@code MainGameEngine} class serves as the main controller for handling game commands and logic.
 * It manages game states, player operations, and map functionalities.
 */
public class MainGameEngine {


    /** The current state of the game. */
    CurrentState d_currentGameState = new CurrentState();
    Phase d_currentPhase = new StartupPhase(this,d_currentGameState);

    public MainGameEngine(){

    }

    public CurrentState getD_currentGameState() {
        return d_currentGameState;
    }

    public void setD_currentGameState(CurrentState d_currentGameState) {
        this.d_currentGameState = d_currentGameState;
    }

    public Phase getD_currentPhase() {
        return d_currentPhase;
    }

    public void setD_currentPhase(Phase d_currentPhase) {
        this.d_currentPhase = d_currentPhase;
    }

//    public void setIssueOrderPhase(){
//        this.setD_mainEngineLog("Issue Order Phase","phase");
//        setD_currentPhase(new IssueOrderPhase(this, d_currentGameState));
//        getD_currentPhase().initPhase();
//    }
//
//    public void setOrderExecutionPhase(){
//        this.setD_mainEngineLog("Order Execution Phase","phase");
//        setD_currentPhase(new OrderExecutionPhase(d_currentGameState, this));
//        getD_currentPhase().initPhase();
//    }



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

    private void startGame(MainGameEngine p_mainGameEngine){
        p_mainGameEngine.getD_currentPhase().initPhase();
    }

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
