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

public class Player {
    String d_playerName;
    Integer d_unallocatedArmies;
    List<Country> d_currentCountries;
    Set<Continent> d_currentContinents;
    List<Orders> d_orders;

    public Player(String p_playerName) {
        this.d_playerName = p_playerName;
        this.d_unallocatedArmies = 0;
        this.d_orders = new ArrayList<Orders>();
        this.d_currentCountries = new ArrayList<Country>();
        this.d_currentContinents = new HashSet<>();
    }

    public String getD_playerName() {
        return d_playerName;
    }

    public void setD_playerName(String p_playerName) {
        this.d_playerName = p_playerName;
    }

    public Integer getD_unallocatedArmies() {
        return d_unallocatedArmies;
    }

    public void setD_unallocatedArmies(Integer p_unallocatedArmies) {
        this.d_unallocatedArmies = p_unallocatedArmies;
    }

    public List<Country> getD_currentCountries() {
        return d_currentCountries;
    }

    public void setD_currentCountries(List<Country> p_currentCountries) {
        this.d_currentCountries = p_currentCountries;
    }

    public Set<Continent> getD_currentContinents() {
        return d_currentContinents;
    }

    public void setD_currentContinents(Set<Continent> p_currentContinents) {
        this.d_currentContinents = p_currentContinents;
    }

    public List<Orders> getD_orders() {
        return d_orders;
    }

    public void setD_orders(List<Orders> p_orders) {
        this.d_orders = p_orders;
    }

    public void setContinent(Continent p_continent) {
        if(d_currentContinents == null) {
            d_currentContinents = new HashSet<>();
        }
        if(!d_currentContinents.add(p_continent)) {
            System.out.println("Continent : "+p_continent.getD_continentName()+" already assigned to Player : "+d_playerName);
        }
    }

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

    public Orders nextOrder() {
        if(d_orders == null || d_orders.isEmpty()) {
            return null;
        }
        Orders l_order = d_orders.get(0);
        d_orders.remove(l_order);
        return l_order;
    }

    public String toString() {
        return "Player [Player Name=" + d_playerName + ", Unallocated Armies=" + d_unallocatedArmies + ", Current Countries=" + d_currentCountries.size() + ", Current Continents=" + d_currentContinents.size() + ", Orders=" + d_orders.size() + "]";
    }

}
