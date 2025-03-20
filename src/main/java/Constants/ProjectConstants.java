package Constants;

import javax.swing.plaf.PanelUI;

public class ProjectConstants {

    public ProjectConstants() {
    }

    public static final String MAP_NOT_AVAILABLE = "Map is not available. Use 'loadmap <filename>' or 'editmap <filename>' first.";

    public static final String INVALID_COMMAND = "Invalid command. Please check the command menu and try again.";

    public static final String INVALID_SAVEMAP_COMMAND = "Save map command is not correct. Use 'savemap <filename>' command.";

    public static final String INVALID_VALIDATEMAP_COMMAND = "Validate map command is not correct. Use 'validatemap' command.";

    public static final String VALID_MAP = "Map is valid.";

    public static final String INVALID_MAP = "Map is not valid.";

    public static final String NO_PLAYERS = "No players in the game. Please add players using 'gameplayer -add <player_name>' command first.";

    public static final String INVALID_GAMEPLAYER_COMMAND = "Command is invalid. Please use correct gameplayer command. To add or remover player, use 'gameplayer -add <player_name>' or 'gameplayer -remove <player_name>'.";

    public static final String FILE_CREATED = "File has been created.";

    public static final String FILE_ALREADY_EXISTS = "File already exists.";

    public static final String NO_CONTINENTS = "No Continents in this map.";

    public static final String NO_COUNTRIES = "No Countries in this map.";

    public static final String NO_BORDERS = "No borders defined. This is not a connected graph.";

    public static final String INVALID_ADD_COUNTRY_COMMAND = "Invalid format. Use '-add <country> <continent>'.";

    public static final String INVALID_REMOVE_COUNTRY_COMMAND = "Invalid format. Use '-remove <country>'.";

    public static final String INVALID_OPERATION = "Invalid operation. Please use 'add' or 'remove'.";

    public static final String INVALID_ARGUMENTS = "Invalid arguments.";

    public static final String INVALID_ADD_CONTINENT_COMMAND = "Invalid format. Use '-add <continent> <controlValue>'.";

    public static final String INVALID_REMOVE_CONTINENT_COMMAND = "Invalid format. Use '-remove <continent>'.";

    public static final String MORE_PLAYERS_THAN_COUNTRIES = "More players than available countries. Reduce player count or add more countries.";

    public static final String NOT_ENOUGH_ARMIES = "Player does not have enough armies to deploy.";

    public static final String ORDER_ADDED = "Order is added to queue for execution.";

    public static final String EMPTY_CONTINENT = "No countries exist in this continent.";

    public static final String COUNTRY_NOT_IN_CONTINENT = "This country does not exist in this continent.";

    public static final String NEIGHBOUR_ALREADY_EXISTS = "Neighbour already exists.";

    public static final String NO_NEIGHBOURING_COUNTRIES = "No neighbouring countries present.";

}
