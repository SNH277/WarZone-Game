package View;

import Model.*;
import Constants.ProjectConstants;

import java.util.List;

/**
 * @author Taksh rana
 *
 */

public class TournamentView {

    Tournament d_tournament;
    List<CurrentState> d_currentStatesObject;

    public TournamentView(Tournament p_tournament) {
        d_tournament = p_tournament;
        d_currentStatesObject = d_tournament.getD_currentStateList();
    }

    public void renderCenterString(int p_width, String p_string) {
        String l_centeredString = String.format("%" + p_width + "s", String.format("%" + (p_string.length() + (p_width - p_string.length()) / 2) + "s", p_string));
        System.out.format(l_centeredString+"\n");
    }

    public void renderSeparator() {
        StringBuilder l_separator = new StringBuilder();
        for (int i = 0; i < ProjectConstants.WIDTH - 2; i++) {
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
    }

    public void renderMapName(Integer p_index, String p_mapName) {
        String l_formattedString = String.format("%s %s %d %s", p_mapName, "(Game Number:", p_index, ")");
        renderSeparator();
        renderCenterString(ProjectConstants.WIDTH, l_formattedString);
        renderSeparator();
    }

    public void renderGames(CurrentState p_currentState) {
        if (p_currentState == null) {
            System.out.println("Game state is null. Cannot render results.");
            return;
        }

        String l_winner;
        String l_conclusion;

        if (p_currentState.getD_winner() == null) {
            l_winner = "None";
            l_conclusion = "Draw";
        } else {
            l_winner = p_currentState.getD_winner().getD_playerName();
            l_conclusion = "Winner Player Strategy: " +
                    p_currentState.getD_winner().getD_playerBehaviourStrategy().getPlayerBehaviour();
        }

        String l_winnerString = "Winner: " + l_winner;

        StringBuilder l_commaSeparatedPlayers = new StringBuilder();
        List<Player> l_failedPlayers = p_currentState.getD_playersFailed();

        for (int i = 0; i < l_failedPlayers.size(); i++) {
            l_commaSeparatedPlayers.append(l_failedPlayers.get(i).getD_playerName());
            if (i < l_failedPlayers.size() - 1) {
                l_commaSeparatedPlayers.append(", ");
            }
        }

        String l_losingPlayer = "Losing Players: " + l_commaSeparatedPlayers;
        String l_conclusionString = "Conclusion of game: " + l_conclusion;

        System.out.println(l_winnerString);
        System.out.println(l_losingPlayer);
        System.out.println(l_conclusionString);
    }


    public void viewTournament(){
        int l_counter = 0;
        System.out.println();
        if (d_tournament != null && d_currentStatesObject != null) {
            for (CurrentState l_currentState : d_tournament.getD_currentStateList()) {
                l_counter++;
                renderMapName(l_counter, l_currentState.getD_map().getD_mapName());
                renderGames(l_currentState);
            }
        }
    }

}
