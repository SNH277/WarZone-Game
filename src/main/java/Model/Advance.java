package Model;

import Model.Player;

/**
 * The type Advance.
 */
public class Advance{

    /**
     * The D source country.
     */
    String d_sourceCountry;

    /**
     * The D target country.
     */
    String d_targetCountry;

    /**
     * The D no of armies to place.
     */
    Integer d_noOfArmiesToPlace;

    /**
     * The D intitiating player.
     */
    Player d_intitiatingPlayer;

    /**
     * Instantiates a new Advance.
     *
     * @param p_sourceCountry     the p source country
     * @param p_targetCountry     the p target country
     * @param p_noOfArmiesToPlace the p no of armies to place
     * @param p_intitiatingPlayer the p intitiating player
     */
    public Advance(String p_sourceCountry, String p_targetCountry, Integer p_noOfArmiesToPlace, Player p_intitiatingPlayer) {
        this.d_sourceCountry = p_sourceCountry;
        this.d_targetCountry = p_targetCountry;
        this.d_noOfArmiesToPlace = p_noOfArmiesToPlace;
        this.d_intitiatingPlayer = p_intitiatingPlayer;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Advance{" +
                "d_sourceCountry='" + d_sourceCountry + '\'' +
                ", d_targetCountry='" + d_targetCountry + '\'' +
                ", d_noOfArmiesToPlace=" + d_noOfArmiesToPlace +
                ", d_intitiatingPlayer=" + d_intitiatingPlayer +
                '}';
    }
}