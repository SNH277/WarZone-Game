package Controller;

import Model.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class PlayerController {
    public void assignCountry(CurrentState p_currentState) {
        if(p_currentState.getD_players() == null || p_currentState.getD_players().isEmpty()) {
            System.out.println("No players found. Please add players using 'gameplayer -add playerName' command first.");
            return;
        }
        if(p_currentState.getD_map() == null || p_currentState.getD_map().getD_mapCountries() == null) {
            System.out.println("Map data is missing. Ensure a valid map is loaded before assigning countries");
        }

        List<Player> l_players = p_currentState.getD_players();
        List<Country> l_countries = p_currentState.getD_map().getD_mapCountries();

        int l_playerCount = l_players.size();
        int l_countryCount = l_countries.size();
        if (l_playerCount > l_countryCount) {
            System.out.println("More players than available countries. Reduce player count or add more countries.");
        }

        int l_countriesPerPlayer = Math.floorDiv(l_countryCount, l_playerCount);

        randomCountryDistribution(l_players, l_countries, l_countriesPerPlayer);

        displayAssignedCountries(l_players);

        updatePlayerContinentOwnership(l_players, p_currentState.getD_map().getD_mapContinents());
    }

    private void updatePlayerContinentOwnership(List<Player> p_Players, List<Continent> p_MapContinents) {
        for(Player l_player : p_Players) {
            List<Country> l_countriesOwnedByPlayer = l_player.getD_currentCountries();
            if(l_countriesOwnedByPlayer == null || l_countriesOwnedByPlayer.isEmpty()) {
                continue;
            }
            for(Continent l_continent : p_MapContinents) {
                if(new HashSet<>(l_countriesOwnedByPlayer).containsAll(l_continent.getD_countries())) {
                    l_player.setContinent(l_continent);
                }
            }
        }
    }

    private void displayAssignedCountries(List<Player> p_Players) {
        for(Player l_currentPlayer : p_Players) {
            StringBuilder l_output = new StringBuilder("Player " + l_currentPlayer.getD_playerName() + " has assigned countries: ");

            List<Country> l_countries = l_currentPlayer.getD_currentCountries();
            if(l_countries == null || l_countries.isEmpty()) {
                l_output.append("No countries assigned.");
            } else {
                for(Country l_country : l_countries) {
                    l_output.append(l_country.getD_countryName()).append(", ");
                }
            }
            System.out.println(l_output.toString().trim());
        }
    }

    private void randomCountryDistribution(List<Player> p_Players, List<Country> p_Countries, int p_CountriesPerPlayer) {
        List<Country> l_unassignedCountries = new ArrayList<>(p_Countries);

        if(l_unassignedCountries.isEmpty()) {
            System.out.println("No Countries in the map.");
            return;
        }
        Random rand = new Random();

        for (Player l_Player : p_Players) {
            if(l_Player.getD_currentCountries() == null) {
                l_Player.setD_currentCountries(new ArrayList<>());
            }
            for (int i = 0; i <= p_CountriesPerPlayer && !l_unassignedCountries.isEmpty(); i++) {
                int l_randomIndex = rand.nextInt(l_unassignedCountries.size());
                l_Player.getD_currentCountries().add(l_unassignedCountries.get(l_randomIndex));
                l_unassignedCountries.remove(l_randomIndex);
            }
        }

        if(!l_unassignedCountries.isEmpty()) {
            randomCountryDistribution(p_Players, l_unassignedCountries, 1);
        }
    }


}
