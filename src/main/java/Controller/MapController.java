package Controller;

import Model.Continent;
import Model.Country;
import Model.CurrentState;
import Model.Map;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MapController {

    public Map loadMap(CurrentState p_currentState, String p_fileName){
        Map l_map=new Map();
        List<String> l_fileLines=loadFile(p_fileName);

        if(!l_fileLines.isEmpty()){
            List<String> l_continentData=getContinentData(l_fileLines);
            List<String> l_countryData=getCountryData(l_fileLines);
            List<String> l_borderData=getBorderData(l_fileLines);

            List<Continent> l_continents = modifyContinentData(l_continentData);
            List<Country> l_countries = modifyCountryData(l_countryData);

            l_countries = updateCountryBorders(l_borderData,l_countries);
            l_continents = updateContinentCountries(l_continents,l_countries);

            l_map.setD_mapContinents(l_continents);
            l_map.setD_mapCountries(l_countries);
            l_map.setD_mapName(p_fileName);
            p_currentState.setD_map(l_map);
        }
        return l_map;
    }

    private List<Continent> updateContinentCountries(List<Continent> p_continents, List<Country> p_countries) {
        for(Country l_country : p_countries){
            for(Continent l_continent:p_continents){
                if(l_country.getD_continentID().equals(l_continent.getD_continentID())){
                    l_continent.setCountry(l_country);
                }
            }
        }
        return p_continents;
    }

    private List<Country> updateCountryBorders(List<String> p_borderData, List<Country> p_countries) {
        LinkedHashMap<Integer,List<Integer>> l_borderDataMap =new LinkedHashMap<>();

        for (String l_eachCountryNeighbour : p_borderData) {
            if (l_eachCountryNeighbour == null || l_eachCountryNeighbour.isEmpty())
                continue;

            String[] l_borderDataSplit = l_eachCountryNeighbour.split(" ");
            if (l_borderDataSplit.length < 2)
                continue;

            int l_countryId;
            List<Integer> l_neighbourCountries = new ArrayList<>();

            try {
                l_countryId = Integer.parseInt(l_borderDataSplit[0]);
                for (int i = 1; i < l_borderDataSplit.length; i++) {
                    l_neighbourCountries.add(Integer.parseInt(l_borderDataSplit[i]));
                }
            } catch (NumberFormatException e) {
                continue;
            }

            l_borderDataMap.put(l_countryId, l_neighbourCountries);
        }

        for (Country l_eachCountry : p_countries) {
            l_eachCountry.setD_neighbouringCountriesId(l_borderDataMap.getOrDefault(l_eachCountry.getD_countryID(), new ArrayList<>()));
        }

        return p_countries;
    }

    private List<Country> modifyCountryData(List<String> p_countryData) {
        List<Country> l_countries =new ArrayList<>();

        for(String l_country:p_countryData){
            String[] l_splitCountryData= l_country.split(" ");
            if(l_splitCountryData.length < 3)
                continue;

            String l_countryName = l_splitCountryData[1];
            int l_countryId,l_continentId;

            try{
                l_countryId =Integer.parseInt(l_splitCountryData[0]);
                l_continentId=Integer.parseInt(l_splitCountryData[2]);
            } catch (NumberFormatException l_e) {
                continue;
            }
            l_countries.add(new Country(l_countryId,l_countryName,l_continentId));
        }
        return l_countries;
    }

    private List<Continent> modifyContinentData(List<String> p_continentData) {
        List<Continent> l_continents =new ArrayList<>();
        int l_continentIds=1;

        for(String l_continent:p_continentData){
            String[] l_splitContinentData=l_continent.split(" ");
            if(l_splitContinentData.length <2)
                continue;

            String l_continentName=l_splitContinentData[0];
            int l_continentValue;
            try{
                l_continentValue=Integer.parseInt(l_splitContinentData[1]);
            }
            catch (NumberFormatException l_e){
                continue;
            }

            l_continents.add(new Continent(l_continentIds,l_continentName,l_continentValue));
            l_continentIds++;
        }
        return l_continents;
    }

    private List<String> getBorderData(List<String> p_fileLines) {
        int l_startIndex=p_fileLines.indexOf("[Borders]")+1;
        int l_endIndex=p_fileLines.size();

        return (l_startIndex>0 && l_endIndex>l_startIndex) ? p_fileLines.subList(l_startIndex,l_endIndex) : new ArrayList<>();
    }

    private List<String> getCountryData(List<String> p_fileLines) {
        int l_startIndex=p_fileLines.indexOf("[Countries]")+1;
        int l_endIndex=p_fileLines.indexOf("[Borders]")-1;

        return (l_startIndex>0 && l_endIndex>l_startIndex) ? p_fileLines.subList(l_startIndex,l_endIndex) : new ArrayList<>();
    }

    private List<String> getContinentData(List<String> p_fileLines) {
        int l_startIndex=p_fileLines.indexOf("[Continents]")+1;
        int l_endIndex=p_fileLines.indexOf("[Countries]")-1;

        return (l_startIndex>0 && l_endIndex>l_startIndex) ? p_fileLines.subList(l_startIndex,l_endIndex) : new ArrayList<>();
    }

    private List<String> loadFile(String p_filename){
        String l_fileLocation = getFilePath(p_filename);
        List<String> l_fileLines =new ArrayList<>();

        try(BufferedReader l_reader = new BufferedReader(new FileReader(l_fileLocation))){
            l_fileLines=l_reader.lines().collect(Collectors.toList());
        }
        catch (FileNotFoundException l_e){
            System.out.println("Error : File "+p_filename +" not found!");
        }
        catch (IOException l_e){
            System.out.println("Error : Unable to read file "+p_filename+" .");
        }
        return l_fileLines;
    }

    private String getFilePath(String p_fileName){
        return Paths.get("src","main","Maps",p_fileName).toString();
    }

    public boolean saveMap(CurrentState p_currentState, String p_arguments) {
        try {
            Map l_map = p_currentState.getD_map();

            if (l_map == null || !l_map.validateMap()) {
                System.out.println("Either the map is not present or it is invalid.");
                return false;
            }

            if (!l_map.getD_mapName().equals(p_arguments)) {
                System.out.println("Filename mismatch! Expected: " + l_map.getD_mapName());
                return false;
            }

            String l_filePath = getFilePath(p_arguments);
            try (FileOutputStream l_writer = new FileOutputStream(l_filePath, false)) {
                l_writer.write("".getBytes()); // Clear file

                if (!writeContinents(l_writer, l_map)) return false;
                if (!writeCountries(l_writer, l_map)) return false;
                if (!writeBorders(l_writer, l_map)) return false;
            }

            return true;
        } catch (IOException p_exception) {
            System.out.println("Error while saving map: " + p_exception.getMessage());
            return false;
        }
    }

    private boolean writeContinents(FileOutputStream p_writer, Map p_map) throws IOException {
        if (p_map.getD_mapContinents() == null || p_map.getD_mapContinents().isEmpty()) {
            System.out.println("No Continents in this map. Can't save an incorrect map.");
            return false;
        }

        p_writer.write((System.lineSeparator() + "[Continents]" + System.lineSeparator()).getBytes());
        for (Continent l_eachContinent : p_map.getD_mapContinents()) {
            String l_content = l_eachContinent.getD_continentName() + " " + l_eachContinent.getD_continentValue();
            p_writer.write((l_content + System.lineSeparator()).getBytes());
        }
        return true;
    }

    private boolean writeCountries(FileOutputStream p_writer, Map p_map) throws IOException {
        if (p_map.getD_mapCountries() == null || p_map.getD_mapCountries().isEmpty()) {
            System.out.println("No Countries in this map. Can't save an incorrect map.");
            return false;
        }

        p_writer.write((System.lineSeparator() + "[Countries]" + System.lineSeparator()).getBytes());
        for (Country l_eachCountry : p_map.getD_mapCountries()) {
            String l_content = l_eachCountry.getD_countryID() + " " + l_eachCountry.getD_countryName() + " " + l_eachCountry.getD_continentID();
            p_writer.write((l_content + System.lineSeparator()).getBytes());
        }
        return true;
    }

    private boolean writeBorders(FileOutputStream p_writer, Map p_map) throws IOException {
        if (p_map.getD_mapCountries() == null || p_map.getD_mapCountries().isEmpty()) {
            System.out.println("Borders cannot be saved without countries.");
            return false;
        }

        p_writer.write((System.lineSeparator() + "[Borders]" + System.lineSeparator()).getBytes());
        boolean hasBorders = false;

        for (Country l_eachCountry : p_map.getD_mapCountries()) {
            if (l_eachCountry.getD_neighbouringCountriesId() != null && !l_eachCountry.getD_neighbouringCountriesId().isEmpty()) {
                String l_borderEntry = l_eachCountry.getD_countryID() + " " +
                        String.join(" ", l_eachCountry.getD_neighbouringCountriesId().stream().map(String::valueOf).toArray(String[]::new));
                p_writer.write((l_borderEntry + System.lineSeparator()).getBytes());
                hasBorders = true;
            }
        }

        if (!hasBorders) {
            System.out.println("No borders defined. This is not a connected graph.");
            return false;
        }

        return true;
    }

    public void editCountry(CurrentState p_currentState, String p_operation, String p_argument) {
        String l_filename = p_currentState.getD_map().getD_mapName();
        Map l_mapToBeEdited = p_currentState.getD_map();

        if (l_mapToBeEdited.getD_mapCountries() == null && l_mapToBeEdited.getD_mapContinents() == null) {
            l_mapToBeEdited = this.loadMap(p_currentState, l_filename);
        }

        if (l_mapToBeEdited != null) {
            Map l_updatedMap = addRemoveCountry(l_mapToBeEdited, p_operation, p_argument);
            p_currentState.setD_map(l_updatedMap);
            p_currentState.getD_map().setD_mapName(l_filename);
        }
    }


    private Map addRemoveCountry(Map p_mapToUpdate, String p_operation, String p_arguments) {
        String[] splitArgs = p_arguments.split(" ");

        if (p_operation.equals("add")) {
            if (splitArgs.length == 2) {
                String countryName = splitArgs[0];
                String continentName = splitArgs[1];

                p_mapToUpdate.addCountry(countryName, continentName);
            } else {
                System.out.println("Error: Invalid format. Use 'add <country> <continent>'.");
            }
        } else if (p_operation.equals("remove")) {
            if (splitArgs.length == 1) {
                String countryName = splitArgs[0];

                p_mapToUpdate.removeCountry(countryName);
                System.out.println("Country " + countryName + " removed successfully!");
            } else {
                System.out.println("Error: Invalid format. Use 'remove <country>'.");
            }
        } else {
            System.out.println("Error: Invalid operation. Use 'add' or 'remove'.");
        }

        return p_mapToUpdate;
    }



    private Map addRemoveNeighbour(Map p_mapToUpdate, String p_operation, String p_arguments) {
        String[] l_args = p_arguments.split(" ");

        if (l_args.length != 2) {
            System.out.println("Invalid arguments. Please provide exactly two country IDs.");
            return p_mapToUpdate;
        }

        int l_countryID, l_neighbourID;
        try {
            l_countryID = Integer.parseInt(l_args[0]);
            l_neighbourID = Integer.parseInt(l_args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: Country IDs must be integers.");
            return p_mapToUpdate;
        }

        if (p_operation.equals("add")) {
            p_mapToUpdate.addNeighbour(l_countryID, l_neighbourID);
        }
        else if (p_operation.equals("remove")) {
            p_mapToUpdate.removeNeighbour(l_countryID, l_neighbourID);
        }
        else {
            System.out.println("Invalid operation. Please use 'add' or 'remove'.");
        }

        return p_mapToUpdate;
    }

    public void editNeighbourCountry(CurrentState p_currentState, String p_operation, String p_arguments) {
        String l_mapName = p_currentState.getD_map().getD_mapName();
        Map l_map = p_currentState.getD_map();

        if (l_map.getD_mapCountries() == null && l_map.getD_mapContinents() == null) {
            l_map = this.loadMap(p_currentState, l_mapName);
        }

        if (l_map != null) {
            Map l_updatedMap = addRemoveNeighbour(l_map, p_operation, p_arguments);
            p_currentState.setD_map(l_updatedMap);
        }
    }

    public void editMap(CurrentState p_currentState, String p_editFileName) throws IOException {
        String l_fileLocation = getFilePath(p_editFileName);
        File l_fileToEdit = new File(l_fileLocation);

        if (l_fileToEdit.createNewFile()) {
            System.out.println("File has been created");
            Map l_map = new Map();
            l_map.setD_mapName(p_editFileName);
            p_currentState.setD_map(l_map);
        } else {
            System.out.println("File already exists");
            Map l_map = this.loadMap(p_currentState, p_editFileName);
            l_map.setD_mapName(p_editFileName);
            p_currentState.setD_map(l_map);
        }
    }
}
