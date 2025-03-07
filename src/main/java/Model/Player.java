package Model;

import Controller.PlayerController;
import Utils.CommandHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a player in the game.
 */
public class Player {

    /** The name of the player. */
    String d_playerName;

    /** The number of unallocated armies the player has. */
    Integer d_unallocatedArmies;

    /** The list of countries the player currently controls. */
    List<Country> d_currentCountries;

    /** The set of continents the player currently controls. */
    Set<Continent> d_currentContinents;

    /** The list of orders the player has issued. */
    List<Orders> d_orders;

    /**
     * Constructor to initialize a new player.
     *
     * @param p_playerName The name of the player.
     */
    public Player(String p_playerName) {
        this.d_playerName = p_playerName;
        this.d_unallocatedArmies = 0;
        this.d_orders = new ArrayList<Orders>();
        this.d_currentCountries = new ArrayList<Country>();
        this.d_currentContinents = new HashSet<>();
    }

    /**
     * Gets the name of the player.
     *
     * @return The player's name.
     */
    public String getD_playerName() {
        return d_playerName;
    }

    /**
     * Sets the name of the player.
     *
     * @param p_playerName The player's name.
     */
    public void setD_playerName(String p_playerName) {
        this.d_playerName = p_playerName;
    }

    /**
     * Gets the number of unallocated armies for the player.
     *
     * @return The number of unallocated armies.
     */
    public Integer getD_unallocatedArmies() {
        return d_unallocatedArmies;
    }

    /**
     * Sets the number of unallocated armies for the player.
     *
     * @param p_unallocatedArmies The number of unallocated armies.
     */
    public void setD_unallocatedArmies(Integer p_unallocatedArmies) {
        this.d_unallocatedArmies = p_unallocatedArmies;
    }

    /**
     * Gets the list of countries the player currently controls.
     *
     * @return The list of countries.
     */
    public List<Country> getD_currentCountries() {
        return d_currentCountries;
    }

    /**
     * Sets the list of countries the player currently controls.
     *
     * @param p_currentCountries The list of countries.
     */
    public void setD_currentCountries(List<Country> p_currentCountries) {
        this.d_currentCountries = p_currentCountries;
    }

    /**
     * Gets the set of continents the player currently controls.
     *
     * @return The set of continents.
     */
    public Set<Continent> getD_currentContinents() {
        return d_currentContinents;
    }

    /**
     * Sets the set of continents the player currently controls.
     *
     * @param p_currentContinents The set of continents.
     */
    public void setD_currentContinents(Set<Continent> p_currentContinents) {
        this.d_currentContinents = p_currentContinents;
    }

    /**
     * Gets the list of orders the player has issued.
     *
     * @return The list of orders.
     */
    public List<Orders> getD_orders() {
        return d_orders;
    }

    /**
     * Sets the list of orders the player has issued.
     *
     * @param p_orders The list of orders.
     */
    public void setD_orders(List<Orders> p_orders) {
        this.d_orders = p_orders;
    }

    /**
     * Assigns a continent to the player.
     *
     * @param p_continent The continent to be assigned.
     */
    public void setContinent(Continent p_continent) {
        if(d_currentContinents == null) {
            d_currentContinents = new HashSet<>();
        }
        if(!d_currentContinents.add(p_continent)) {
            System.out.println("Continent : "+p_continent.getD_continentName()+" already assigned to Player : "+d_playerName);
        }
    }

    /**
     * Issues an order for the player to deploy armies.
     *
     * @throws IOException If there is an issue with reading the input.
     */
    public void issueOrder() throws IOException {
        BufferedReader l_bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter command to deploy armies on the map for Player: " + d_playerName + " | Armies left: " + d_unallocatedArmies);

        String l_command = l_bufferedReader.readLine().trim();
        CommandHandler l_commandHandler = new CommandHandler(l_command);

        if("deploy".equals(l_commandHandler.getMainCommand())) {
            String[] l_commandParts = l_command.split(" ");

            if(l_commandParts.length == 3) {
                PlayerController l_playerController = new PlayerController();
                l_playerController.createDeployOrder(l_command, this);
            } else {
                System.out.println("Invalid command format. Please provide a valid command.");
            }
        }
    }

    /**
     * Retrieves the next order the player has issued.
     *
     * @return The next order, or null if no orders exist.
     */
    public Orders nextOrder() {
        if(d_orders == null || d_orders.isEmpty()) {
            return null;
        }
        Orders l_order = d_orders.get(0);
        d_orders.remove(l_order);
        return l_order;
    }

    /**
     * Returns a string representation of the player, including their name, number of unallocated armies,
     * the number of countries and continents they control, and the number of orders they have issued.
     *
     * @return The string representation of the player.
     */
    public String toString() {
        return "Player [Player Name=" + d_playerName + ", Unallocated Armies=" + d_unallocatedArmies + ", Current Countries=" + d_currentCountries.size() + ", Current Continents=" + d_currentContinents.size() + ", Orders=" + d_orders.size() + "]";
    }

}
