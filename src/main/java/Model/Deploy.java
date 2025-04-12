package Model;


import Model.Player;

import java.io.Serializable;

/**
 * Represents a Deploy order in the game, where a player deploys armies to a country.
 * @author Akhilesh Kanbarkar
 */
public class Deploy implements Orders, Serializable {

    /**
     * The name of the target country for the order.
     */
    String d_targetCountryName;

    /**
     * The number of armies to be moved as part of the order.
     */
    Integer d_noOfArmiesToMove;

    /**
     * The player who initiated the order.
     */
    Player d_initiatingPlayer;

    /**
     * The log of the order execution detailing the actions taken.
     */
    String d_logOfOrderExecution;

    /**
     * Instantiates a new Deploy order.
     *
     * @param p_initiatingPlayer the initiating player
     * @param p_targetCountryName the target country name
     * @param p_noOfArmiesToMove the number of armies to move
     */
    public Deploy(Player p_initiatingPlayer, String p_targetCountryName, Integer p_noOfArmiesToMove) {
        this.d_initiatingPlayer = p_initiatingPlayer;
        this.d_targetCountryName = p_targetCountryName;
        this.d_noOfArmiesToMove = p_noOfArmiesToMove;
    }

    /**
     * Gets the name of the target country.
     *
     * @return the name of the target country
     */
    public String getTargetCountryName() {
        return d_targetCountryName;
    }

    @Override
    public String orderExecutionLog(){
        return d_logOfOrderExecution;
    }

    /**
     * Gets the number of armies to move.
     *
     * @return the number of armies to move
     */
    public Integer getNoOfArmiesToMove() {
        return d_noOfArmiesToMove;
    }

    /**
     * Gets the player who initiated the move.
     *
     * @return the initiating player
     */
    public Player getInitiatingPlayer() {
        return d_initiatingPlayer;
    }

    /**
     * Returns a string representation of the Deploy order.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Deploy{" +
                "targetCountryName='" + d_targetCountryName + '\'' +
                ", noOfArmiesToMove=" + d_noOfArmiesToMove +
                ", initiatingPlayer=" + d_initiatingPlayer +
                '}';
    }

    /**
     * Print order.
     */
    @Override
    public void printOrder(){
        this.d_logOfOrderExecution = "Deploy Order : "+d_initiatingPlayer.d_playerName+" is deploying "+d_noOfArmiesToMove+" armies to "+d_targetCountryName;
        System.out.println(d_logOfOrderExecution);
    }
    /**
     * Prints the order execution log message based on its type.
     *
     * @param p_orderExecutionLog the log message to be printed
     * @param p_messageType       the type of message ("error" or other)
     */
    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_messageType) {
        this.d_logOfOrderExecution=p_orderExecutionLog;
        if (p_messageType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }

    /**
     * Executes the deploy order by adding armies to the target country,
     * if the order is valid (i.e., the player owns the target country).
     *
     * @param p_currentState the current game state
     */
    @Override
    public void execute(CurrentState p_currentState) {
        if (valid(p_currentState)) {
            for (Country l_eachCountry : p_currentState.getD_map().getD_mapCountries()) {
                if (l_eachCountry.getD_countryName().equalsIgnoreCase(this.d_targetCountryName)) {
                    Integer l_updatedArmies = l_eachCountry.getD_armies() + this.d_noOfArmiesToMove;
                    l_eachCountry.setD_armies(l_updatedArmies);
                    this.setD_orderExecutionLog(d_initiatingPlayer.d_playerName + " Armies have been deployed successfully", "default");
                }
            }
        } else {
            this.setD_orderExecutionLog("Given Deploy Order cannot be executed since the target country does not belong to player.", "error");
            p_currentState.updateLog("Given Deploy Order cannot be executed since the target country does not belong to player.", "effect");
        }
    }

    /**
     * Validates whether the deploy order can be executed by checking if
     * the initiating player owns the target country.
     *
     * @param p_currentState the current game state
     * @return true if the player owns the target country, false otherwise
     */
    public boolean valid(CurrentState p_currentState) {
        for (Country l_eachCountry : d_initiatingPlayer.getD_currentCountries()) {
            if (l_eachCountry.getD_countryName().equals(d_targetCountryName)) {
                return true;
            }
        }
        return false;
    }
}