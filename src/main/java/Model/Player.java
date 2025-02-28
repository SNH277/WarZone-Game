package Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    String playerName;
    Integer unallocatedArmies;
    List<Country> currentCountries;
    List<Continent> currentContinents;
    List<Orders> orders;

    public Player(String playerName) {
        this.playerName = playerName;
        this.unallocatedArmies = 0;
        this.orders = new ArrayList<Orders>();
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getUnallocatedArmies() {
        return unallocatedArmies;
    }

    public void setUnallocatedArmies(Integer unallocatedArmies) {
        this.unallocatedArmies = unallocatedArmies;
    }

    public List<Country> getCurrentCountries() {
        return currentCountries;
    }

    public void setCurrentCountries(List<Country> currentCountries) {
        this.currentCountries = currentCountries;
    }

    public List<Continent> getCurrentContinents() {
        return currentContinents;
    }

    public void setCurrentContinents(List<Continent> currentContinents) {
        this.currentContinents = currentContinents;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }


}
