package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Country} class represents a country with an ID, name, continent ID, neighboring countries, and armies.
 */
public class Country {
    /** The unique identifier for the country. */
    Integer d_countryID;
    /** The name of the country. */
    String d_countryName;
    /** The ID of the continent to which the country belongs. */
    Integer d_continentId;
    /** The list of neighboring country IDs. */
    List<Integer> d_neighbouringCountriesId;
    /** The number of armies stationed in the country. */
    Integer d_armies = 0;

    /**
     * Constructs a Country object with a specified ID, name, and continent ID.
     *
     * @param p_countryID   The unique ID of the country.
     * @param p_countryName The name of the country.
     * @param p_continentId The ID of the continent to which the country belongs.
     */
    public Country(Integer p_countryID, String p_countryName, Integer p_continentId) {
        this.d_countryID = p_countryID;
        this.d_countryName = p_countryName;
        this.d_continentId = p_continentId;
    }

    /**
     * Gets the continent ID.
     *
     * @return The continent ID.
     */
    public Integer getD_continentID() {
        return d_continentId;
    }

    /**
     * Sets the continent ID.
     *
     * @param d_continentId The new continent ID.
     */
    public void setD_continentId(Integer d_continentId) {
        this.d_continentId = d_continentId;
    }

    /**
     * Gets the country ID.
     *
     * @return The country ID.
     */
    public Integer getD_countryID() {
        return d_countryID;
    }

    /**
     * Gets the country name.
     *
     * @return The country name.
     */public String getD_countryName() {
        return d_countryName;
    }

    /**
     * Gets the list of neighboring country IDs.
     *
     * @return The list of neighboring country IDs.
     */
    public List<Integer> getD_neighbouringCountriesId() {
        if(d_neighbouringCountriesId == null)
            return new ArrayList<Integer>();
        else
            return d_neighbouringCountriesId;
    }

    /**
     * Gets the number of armies stationed in the country.
     *
     * @return The number of armies.
     */
    public Integer getD_armies() {
        return d_armies;
    }

    /**
     * Sets the country ID.
     *
     * @param d_countryID The new country ID.
     */
    public void setD_countryID(Integer d_countryID) {
        this.d_countryID = d_countryID;
    }

    /**
     * Sets the country name.
     *
     * @param d_countryName The new country name.
     */
    public void setD_countryName(String d_countryName) {
        this.d_countryName = d_countryName;
    }

    /**
     * Sets the list of neighboring country IDs.
     *
     * @param d_neighbouringCountriesId The new list of neighboring country IDs.
     */
    public void setD_neighbouringCountriesId(List<Integer> d_neighbouringCountriesId) {
        this.d_neighbouringCountriesId = d_neighbouringCountriesId;
    }

    /**
     * Sets the number of armies stationed in the country.
     *
     * @param d_armies The new number of armies.
     */
    public void setD_armies(Integer d_armies) {
        this.d_armies = d_armies;
    }

    @Override
    /**
     * Returns a string representation of the country object.
     *
     * @return A string containing the country's details.
     */
    public String toString() {
        return "Country{" +
                "d_countryID=" + d_countryID +
                ", d_countryName='" + d_countryName + '\'' +
                ", d_continentId=" + d_continentId +
                ", d_neighbouringCountriesId=" + d_neighbouringCountriesId +
                ", d_armies=" + d_armies +
                '}';
    }

    /**
     * Adds a neighboring country to the list.
     *
     * @param p_neighbourID The ID of the neighboring country to add.
     */
    public void addCountryNeighbour(int p_neighbourID){
        if (d_neighbouringCountriesId == null) {
            d_neighbouringCountriesId = new ArrayList<>();
        }
        if (d_neighbouringCountriesId.contains(p_neighbourID)) {
            System.out.println("Neighbour already exists.");
        } else {
            d_neighbouringCountriesId.add(p_neighbourID);
        }
    }

    /**
     * Removes a neighboring country from the list.
     *
     * @param p_removeCountryId The ID of the neighboring country to remove.
     */
    public void removeCountryNeighbour(int p_removeCountryId){
        if (d_neighbouringCountriesId == null || d_neighbouringCountriesId.isEmpty()) {
            System.out.println("No neighbouring countries present.");
            return;
        }
        // Remove all occurrences that match p_removeCountryId.
        boolean removed = d_neighbouringCountriesId.removeIf(neighbour -> neighbour == p_removeCountryId);
        if (!removed) {
            System.out.println("Country ID: " + p_removeCountryId + " is not a neighbour in the first place.");
        }
    }
}
