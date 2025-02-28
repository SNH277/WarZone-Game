package Utils;

public class CommandHandler {
    String d_command;

    public CommandHandler(String p_command) {
        this.d_command = p_command;
    }

    public String getMainCommand(){
        String[] l_commands= d_command.split(" ");
        return l_commands[0];
    }
}
