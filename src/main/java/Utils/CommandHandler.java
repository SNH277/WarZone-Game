package Utils;

import java.util.*;

public class CommandHandler {
    String d_command;

    public CommandHandler(String p_command) {
        this.d_command = p_command;
    }

    public String getMainCommand(){
        String[] l_commands= d_command.split(" ");
        return l_commands[0];
    }

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
                l_listOfOperations.add(getOperatiionMap(l_operation));
            }
        }
        return l_listOfOperations;


    }

    private Map<String, String> getOperatiionMap(String p_operation) {
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

    public boolean checkRequiredKey(String l_arguments, Map<String, String> l_singleOperation) {
        if(l_singleOperation.containsKey((l_arguments)) && l_singleOperation.get(l_arguments) != null && !l_singleOperation.get(l_arguments).isEmpty()){
            return true;
        }
        return false;
    }
}
