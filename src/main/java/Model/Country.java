package Model;

import java.util.ArrayList;
import java.util.List;

public class Country {
    Integer d_countryID;
    String d_countryName;
    Integer d_continentId;
    List<Integer> d_neighbouringCountriesId;
    Integer d_armies = 0;

    public Country(Integer p_countryID, String p_countryName, Integer p_continentId) {
        this.d_countryID = p_countryID;
        this.d_countryName = p_countryName;
        this.d_continentId = p_continentId;
    }

    public Integer getD_continentID() {
        return d_continentId;
    }

    public void setD_continentId(Integer d_continentId) {
        this.d_continentId = d_continentId;
    }

    public Integer getD_countryID() {
        return d_countryID;
    }

    public String getD_countryName() {
        return d_countryName;
    }

    public List<Integer> getD_neighbouringCountriesId() {
        return d_neighbouringCountriesId;
    }

    public Integer getD_armies() {
        return d_armies;
    }

    public void setD_countryID(Integer d_countryID) {
        this.d_countryID = d_countryID;
    }

    public void setD_countryName(String d_countryName) {
        this.d_countryName = d_countryName;
    }

    public void setD_neighbouringCountriesId(List<Integer> d_neighbouringCountriesId) {
        this.d_neighbouringCountriesId = d_neighbouringCountriesId;
    }

    public void setD_armies(Integer d_armies) {
        this.d_armies = d_armies;
    }

    @Override
    public String toString() {
        return "Country{" +
                "d_countryID=" + d_countryID +
                ", d_countryName='" + d_countryName + '\'' +
                ", d_continentId=" + d_continentId +
                ", d_neighbouringCountriesId=" + d_neighbouringCountriesId +
                ", d_armies=" + d_armies +
                '}';
    }

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
