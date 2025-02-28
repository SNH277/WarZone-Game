package Model;

import java.util.List;

public class Country {
    Integer d_countryID;
    String d_countryName;
    List<Integer> d_neighbouringCountriesId;
    Integer d_armies = 0;

    public Country(){
    }

    public Country(Integer p_countryID,String p_countryName,List<Integer> p_neighbouringCountriesId,Integer p_armies){
        this.d_countryID= p_countryID;
        this.d_countryName=p_countryName;
        this.d_neighbouringCountriesId=p_neighbouringCountriesId;
        this.d_armies=p_armies;
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
                ", d_neighbouringCountriesId=" + d_neighbouringCountriesId +
                ", d_armies=" + d_armies +
                '}';
    }
}
