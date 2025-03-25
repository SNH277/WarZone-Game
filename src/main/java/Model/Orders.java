package Model;

public interface Orders {

    void execute(CurrentState p_currentState);

    String orderExecutionLog();

    void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType);

    boolean valid(CurrentState p_currentState);
}
