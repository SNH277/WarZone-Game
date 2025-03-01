package Controller;

import Model.Continent;
import Model.Country;
import Model.CurrentState;
import Model.Map;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapController {

    public Map loadMap(CurrentState p_currentState, String p_fileName){
        Map l_map=new Map();
        List<String> l_fileLines=loadFile(p_fileName);
        return l_map;
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

            // File path validation
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

        p_writer.write(("[continents]" + System.lineSeparator()).getBytes());
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

        p_writer.write(("[countries]" + System.lineSeparator()).getBytes());
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

        p_writer.write(("[borders]" + System.lineSeparator()).getBytes());
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

}
