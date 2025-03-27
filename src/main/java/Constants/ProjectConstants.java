package Constants;

import java.util.Arrays;
import java.util.List;

/**
 * The {@code ProjectConstants} class defines a set of constant strings used throughout the project.
 * These constants represent various messages related to command validation, file operations,
 * map validation, player management, and other functionalities.
 * @author Disha Padsala
 */
public class ProjectConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    public ProjectConstants() {
    }

    /** Error message when the map is not available. */
    public static final String MAP_NOT_AVAILABLE = "Map is not available. Use 'loadmap <filename>' or 'editmap <filename>' first.";

    /** Error message for an invalid command. */
    public static final String INVALID_COMMAND = "Invalid command. Please provide a valid command.";

    /** Error message for incorrect 'savemap' command usage. */
    public static final String INVALID_SAVEMAP_COMMAND = "Save map command is not correct. Use 'savemap <filename>' command.";

    /** Error message for incorrect 'validatemap' command usage. */
    public static final String INVALID_VALIDATEMAP_COMMAND = "Validate map command is not correct. Use 'validatemap' command.";

    /** Success message when a map is valid. */
    public static final String VALID_MAP = "Map is valid.";

    /** Error message when a map is invalid. */
    public static final String INVALID_MAP = "Map is not valid.";

    /** Error message when there are no players in the game. */
    public static final String NO_PLAYERS = "No players in the game. Please add players using 'gameplayer -add <player_name>' command first.";

    /** Error message for invalid 'gameplayer' command usage. */
    public static final String INVALID_GAMEPLAYER_COMMAND = "Command is invalid. Please use correct gameplayer command. To add or remover player, use 'gameplayer -add <player_name>' or 'gameplayer -remove <player_name>'.";

    /** Success message when a file is created. */
    public static final String FILE_CREATED = "File has been created.";

    /** Error message when a file already exists. */
    public static final String FILE_ALREADY_EXISTS = "File already exists.";

    /** Error message when there are no continents in the map. */
    public static final String NO_CONTINENTS = "No Continents in this map.";

    /** Error message when there are no countries in the map. */
    public static final String NO_COUNTRIES = "No Countries in this map.";

    /** Error message when no borders are defined. */
    public static final String NO_BORDERS = "No borders defined. This is not a connected graph.";

    /** Error message for incorrect 'add country' command format. */
    public static final String INVALID_ADD_COUNTRY_COMMAND = "Invalid format. Use '-add <country> <continent>'.";

    /** Error message for incorrect 'remove country' command format. */
    public static final String INVALID_REMOVE_COUNTRY_COMMAND = "Invalid format. Use '-remove <country>'.";

    /** Error message for an invalid operation type. */
    public static final String INVALID_OPERATION = "Invalid operation. Please use 'add' or 'remove'.";

    /** Error message for invalid arguments. */
    public static final String INVALID_ARGUMENTS = "Invalid arguments.";

    /** Error message for incorrect 'add continent' command format. */
    public static final String INVALID_ADD_CONTINENT_COMMAND = "Invalid format. Use '-add <continent> <controlValue>'.";

    /** Error message for incorrect 'remove continent' command format. */
    public static final String INVALID_REMOVE_CONTINENT_COMMAND = "Invalid format. Use '-remove <continent>'.";

    /** Error message when there are more players than available countries. */
    public static final String MORE_PLAYERS_THAN_COUNTRIES = "More players than available countries. Reduce player count or add more countries.";

    /** Error message when a player does not have enough armies to deploy. */
    public static final String NOT_ENOUGH_ARMIES = "Player does not have enough armies to deploy.";

    /** Success message when an order is added to the queue. */
    public static final String ORDER_ADDED = "Order is added to queue for execution.";

    /** Error message when a continent has no countries. */
    public static final String EMPTY_CONTINENT = "No countries exist in this continent.";

    /** Error message when a country does not belong to a specified continent. */
    public static final String COUNTRY_NOT_IN_CONTINENT = "This country does not exist in this continent.";

    /** Error message when a neighboring country relationship already exists. */
    public static final String NEIGHBOUR_ALREADY_EXISTS = "Neighbour already exists.";

    /** Error message when no neighboring countries exist. */
    public static final String NO_NEIGHBOURING_COUNTRIES = "No neighbouring countries present.";

    /** Error message when the deploy order exceeds unallocated armies. */
    public static final String INVALID_NO_OF_ARMIES = "Given deploy order can't be executed as armies in deploy order is more than players unallocated armies";

    /** List of all available card types: bomb, blockade, airlift, negotiate. */
    public static final List<String> ALL_CARDS = Arrays.asList("bomb", "blockade", "airlift", "negotiate");

    /** Total number of available cards, based on ALL_CARDS size. */
    public static final int NO_OF_CARDS = ALL_CARDS.size();
}
