package Model;
/**
 * Model Class Orders.
 */
public class Orders {
    /**
     * The D order command.
     */
    String d_order;
    /**
     * The D target country's name.
     */
    String d_targetName;
    /**
     * The D number of armies to move.
     */
    Integer d_noOfArmiesToMove;
    /**
     * Instantiates a new Orders.
     *
     * @param p_order the p order command
     * @param p_targetName the p target country's name
     * @param p_noOfArmiesToMove the p number of armies to move
     */
    public Orders(String p_order, String p_targetName, Integer p_noOfArmiesToMove){
        this.d_order = p_order;
        this.d_targetName = p_targetName;
        this.d_noOfArmiesToMove = p_noOfArmiesToMove;
    }
    /**
     * Gets the D order command.
     *
     * @return the D order command
     */
    public String getD_order() {
        return d_order;
    }
    /**
     * Gets the D target country's name.
     *
     * @return the D target country's name
     */
    public String getD_targetName() {
        return d_targetName;
    }
    /**
     * Gets the D number of armies to move.
     *
     * @return the D number of armies to move
     */
    public Integer getD_noOfArmiesToMove() {
        return d_noOfArmiesToMove;
    }
    /**
     * Sets the D order command.
     *
     * @param d_order the new D order command
     */
    public void setD_order(String d_order) {
        this.d_order = d_order;
    }
    /**
     * Sets the D target country's name.
     *
     * @param d_targetName the new D target country's name
     */
    public void setD_targetName(String d_targetName) {
        this.d_targetName = d_targetName;
    }
    /**
     * Sets the D number of armies to move.
     *
     * @param d_noOfArmiesToMove the new D number of armies to move
     */
    public void setD_noOfArmiesToMove(Integer d_noOfArmiesToMove) {
        this.d_noOfArmiesToMove = d_noOfArmiesToMove;
    }
    /**
     * Executes the order for the given player.
     * <p>
     * If the order command is "deploy", this method iterates through the player's current countries,
     * and when it finds the country whose name matches the target name, it adds the specified number of armies.
     * </p>
     *
     * @param p_eachPlayer the player executing the order
     */
    public void execute(Player p_eachPlayer) {
        if(d_order.equals("deploy")){
            for(Country l_eachCountry : p_eachPlayer.getD_currentCountries()){
                if(l_eachCountry.getD_countryName().equals(this.d_targetName)){
                    l_eachCountry.setD_armies(l_eachCountry.getD_armies() + this.d_noOfArmiesToMove);
                    break;
                }
            }
        }
    }
}
