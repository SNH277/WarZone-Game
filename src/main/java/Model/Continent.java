package Model;

import java.util.ArrayList;
import java.util.List;
/**
 * Model Class Continent.
 */
public class Continent {
    /**
     * The D continent id.
     */
    Integer d_continentID;
    /**
     * The D continent name.
     */
    String d_continentName;
    /**
     * The D continent value.
     */
    Integer d_continentValue;
    /**
     * The D countries.
     */
    List<Country> d_countries;
    /**
     * Instantiates a new Continent.
     *
     * @param p_continentID    the p continent id
     * @param p_continentName  the p continent name
     * @param p_continentValue the p continent value
     */
    public Continent(Integer p_continentID, String p_continentName, Integer p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }
    /**
     * Instantiates a new Continent.
     *
     * @param p_continentID    the p continent id
     * @param p_continentValue the p continent value
     */
    public Continent(Integer p_continentID, Integer p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentValue = p_continentValue;
    }
    /**
     * Gets d continent id.
     *
     * @return the d continent id
     */
    public Integer getD_continentID() {
        return d_continentID;
    }
    /**
     * Gets d continent name.
     *
     * @return the d continent name
     */
    public String getD_continentName() {
        return d_continentName;
    }
    /**
     * Gets d continent value.
     *
     * @return the d continent value
     */
    public Integer getD_continentValue() {
        return d_continentValue;
    }
    /**
     * Gets d countries.
     *
     * @return the d countries
     */
    public List<Country> getD_countries() {
        return d_countries;
    }
    /**
     * Sets d continent id.
     *
     * @param p_continentID the p continent id
     */
    public void setD_continentID(Integer p_continentID) {
        this.d_continentID = p_continentID;
    }
    /**
     * Sets d continent name.
     *
     * @param p_continentName the p continent name
     */
    public void setD_continentName(String p_continentName) {
        this.d_continentName = p_continentName;
    }
    /**
     * Sets d continent value.
     *
     * @param p_continentValue the p continent value
     */
    public void setD_continentValue(Integer p_continentValue) {
        this.d_continentValue = p_continentValue;
    }
    /**
     * Sets d countries.
     *
     * @param p_countries the p countries
     */
    public void setD_countries(List<Country> p_countries) {
        this.d_countries = p_countries;
    }
    /**
     * To string string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Continent{" +
                "d_continentID=" + d_continentID +
                ", d_continentName='" + d_continentName + '\'' +
                ", d_continentValue=" + d_continentValue +
                ", d_countries=" + d_countries +
                '}';
    }
    /**
     * Add country.
     *
     * @param p_countryObject the p country object
     */
    public void addCountry(Country p_countryObject) {
        if (d_countries == null) {
            d_countries = new ArrayList<>();
        }
        d_countries.add(p_countryObject);
    }
    /**
     * Remove country.
     *
     * @param p_countryObject the p country object
     */
    public void removeCountry(Country p_countryObject){
        if (d_countries == null) {
            System.out.println("No countries exist in this continent.");
            return;
        }
            if (!d_countries.remove(p_countryObject)) {
                System.out.println("This country does not exist in this continent.");
            }
    }
    /**
     * Sets country.
     *
     * @param p_eachCountry the p each country
     */
    public void setCountry(Country p_eachCountry){
        if(d_countries == null){
            d_countries = new ArrayList<>();
        }
        d_countries.add(p_eachCountry);
    }
}


