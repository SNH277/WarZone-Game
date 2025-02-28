package Controller;

import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainGameEngine {

    public static void main(String[] args) {
        MainGameEngine l_mainGameEngine = new MainGameEngine();
        l_mainGameEngine.startGame();
    }

    private void startGame(){
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            displayMenu();

            System.out.print("Enter your command: ");
            try {
                String l_inputCommand = l_bufferedReader.readLine();
                if (l_inputCommand.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting the game. Goodbye!");
                    System.exit(0);
                } else {
                    commandHandler(l_inputCommand);
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("================================== COMMAND MENU ===================================");
        System.out.println("1. Initiate the map: (Usage: 'loadmap <your_filename(.map)>')");
        System.out.println("2. Edit the Map: (Usage: 'editmap <filename>(.map)')");
        System.out.println("3. Validate the Map: (Usage: 'validatemap')");
        System.out.println("4. Show the Map: (Usage: 'showmap')");
        System.out.println("5. Save the Map: (Usage: 'savemap <file_name_same_used_in_loadmap>')");
        System.out.println("6. Edit the Continent: (Usage: 'editcontinent -add/-remove <continent_name>')");
        System.out.println("7. Edit the Country: (Usage: 'editcountry -add/-remove <country_name>')");
        System.out.println("8. Edit the Neighbour: (Usage: 'editneighbour -add/-remove <country_id_1> <country_id_2>')");
        System.out.println("9. Add a player: (Usage: 'gameplayer -add/-remove <player_name>')");
        System.out.println("10. Assign countries and allocate armies to players: (Usage: 'assigncountries')");
        System.out.println("11. Exit the game: (Usage: 'exit')");
        System.out.println();
    }

    private void commandHandler(String p_inputCommand) {
        CommandHandler l_commandHandler =new CommandHandler(p_inputCommand);
        String l_mainCommand = l_commandHandler.getMainCommand();
        boolean l_isMapAvailable=false;
    }


}
