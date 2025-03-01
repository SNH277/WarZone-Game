package Controller;

import Model.CurrentState;
import Utils.CommandHandler;
import View.MapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainGameEngine {

    MapController d_mapController=new MapController();
    PlayerController d_playerController=new PlayerController();
    CurrentState d_currentGameState = new CurrentState();

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
//        boolean l_mapAvailable = (d_currentState.getD_map() != null);

        Set<String> requiresMap = Set.of(
                "editcountry", "editcontinent", "editneighbour",
                "showmap", "gameplayer", "assigncountries",
                "validatemap", "savemap"
        );

        if (requiresMap.contains(l_mainCommand) && !l_isMapAvailable) {
            System.out.println("Error: Map not available. Use 'loadmap' or 'editmap' first.");
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
                new MapView(d_currentGameState).showMap();
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
            default:
                System.out.println("Invalid command. Please check the command menu and try again.");
                break;
        }
    }

    private void saveMap(CommandHandler lCommandHandler) {
    }

    private void validateMap(CommandHandler lCommandHandler) {
    }

    private void assignCountries(CommandHandler lCommandHandler) {
    }

    private void gamePlayer(CommandHandler lCommandHandler) {
    }

    private void editNeighbourCountry(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
    }

    private void editContinent(CommandHandler lCommandHandler) {
    }

    private void editCountry(CommandHandler lCommandHandler) {
    }

    private void editMap(CommandHandler lCommandHandler) {
    }

    private void loadMap(CommandHandler p_commandHandler) {
        List<Map<String,String>> l_listOfOperations=p_commandHandler.getListOfOperations();
        System.out.println(l_listOfOperations);
    }


}
