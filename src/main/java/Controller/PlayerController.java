package Controller;

import Model.*;

import java.util.List;

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
    }

    private void displayAssignedCountries(List<Player> p_Players) {
    }

    private void randomCountryDistribution(List<Player> p_Players, List<Country> p_Countries, int p_CountriesPerPlayer) {
    }


}
