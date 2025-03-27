package Model;


import Model.Player;

/**
 * Represents a Deploy order in the game, where a player deploys armies to a country.
 * @author Akhilesh Kanbarkar
 */
public class Deploy{

    private String d_targetCountryName;
    private Integer d_noOfArmiesToMove;
    private Player d_initiatingPlayer;

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
}