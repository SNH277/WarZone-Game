package Utils;

import java.util.*;

/**
 * Handles and parses commands and operations.
 */
public class CommandHandler {

    /** The command string provided by the user. */
    String d_command;

    /**
     * Initializes the command handler with the given command.
     *
     * @param p_command The command string.
     */
    public CommandHandler(String p_command) {
        this.d_command = p_command;
    }

    /**
     * Returns the main command (first word) from the input string.
     *
     * @return The main command.
     */
    public String getMainCommand(){
        String[] l_commands= d_command.split(" ");
        return l_commands[0];
    }

    /**
     * Parses the command and returns a list of operations.
     *
     * @return A list of operation maps.
     */
    public List<Map<String,String>> getListOfOperations(){
        String l_mainCommand=this.getMainCommand();
        String l_remainingCommand=d_command.replace(l_mainCommand,"").trim();

        if(l_remainingCommand.isEmpty()){
            return Collections.emptyList();
        }

        if(!l_remainingCommand.contains("-")){
            l_remainingCommand="-filename "+l_remainingCommand;
        }

        List<Map<String,String>> l_listOfOperations = new ArrayList<>();
        String[] l_operations=l_remainingCommand.split("-");

        for(String l_operation : l_operations){
            if(!l_operation.isBlank()){
                l_listOfOperations.add(getOperationMap(l_operation)); // Updated method name
            }
        }
        return l_listOfOperations;
    }

    /**
     * Converts a single operation into a map.
     *
     * @param p_operation The operation string.
     * @return The operation map.
     */
    private Map<String, String> getOperationMap(String p_operation) { // Corrected method name
        Map<String,String> l_operationMap= new HashMap<>();
        String[] l_splitOperation=p_operation.split(" ");
        l_operationMap.put("Operation",l_splitOperation[0]);
        String l_argument="";
        if(l_splitOperation.length-1>0){
            String[] l_remainigArguments=Arrays.copyOfRange(l_splitOperation,1,l_splitOperation.length);
            l_argument=String.join(" ",l_remainigArguments);
        }
        l_operationMap.put("Arguments",l_argument);
        return l_operationMap;
    }

    /**
     * Checks if a required argument is present and not empty.
     *
     * @param l_arguments The argument key.
     * @param l_singleOperation The operation map.
     * @return {@code true} if the argument is valid, {@code false} otherwise.
     */
    public boolean checkRequiredKey(String l_arguments, Map<String, String> l_singleOperation) {
        return l_singleOperation.containsKey(l_arguments) && l_singleOperation.get(l_arguments) != null && !l_singleOperation.get(l_arguments).isEmpty();
    }
}
