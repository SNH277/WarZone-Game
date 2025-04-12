package Controller;

import Constants.ProjectConstants;
import Model.Continent;
import Model.Country;
import Model.CurrentState;
import Model.Map;
import Services.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import static Constants.ProjectConstants.FILE_ALREADY_EXISTS;

/**
 * Controller Class MapController.
 * <p>
 * This class handles the loading, editing, and saving of map data.
 * </p>
 * @author Shrey Hingu,Akhilesh Kanbarkar, Yash Koladiya
 */
public class MapController implements Serializable {

    /**
     * The D current state.
     */
    CurrentState d_currentState = new CurrentState();

    /**
     * Instantiates a new Map controller.
     */
    public MapController() {
    }

    /**
     * Loads a map from a file and updates the current state.
     *
     * @param p_currentState the current state object to update with the loaded map
     * @param p_fileName     the name of the map file
     * @return the loaded Map object
     */
    public Map loadMap(CurrentState p_currentState, String p_fileName){

        Map l_map=new Map();
        List<String> l_fileLines=loadFile(p_fileName);
        if(!l_fileLines.isEmpty()) {
            if(l_fileLines.contains("[Territories]")){
                MapReaderAdapter l_mapReaderAdapter = new MapReaderAdapter(new ConquestMapFileReader());
                l_mapReaderAdapter.parseMapFile(p_currentState, l_map, l_fileLines, p_fileName);
            }
            else if(l_fileLines.contains("[Continents]")){
                new MapFileReader().parseMapFile(p_currentState, l_map, l_fileLines, p_fileName);
            }
        }
        return l_map;
    }

    /**
     * Loads the file content from the given filename.
     *
     * @param p_filename the name of the file to load
     * @return the list of lines read from the file
     */
    private List<String> loadFile(String p_filename){
        String l_fileLocation = getFilePath(p_filename);
        List<String> l_fileLines =new ArrayList<>();

        try(BufferedReader l_reader = new BufferedReader(new FileReader(l_fileLocation))){
            l_fileLines=l_reader.lines().collect(Collectors.toList());
        }
        catch (FileNotFoundException l_e){
            System.out.println("Error : File "+p_filename +" not found!");
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.FILE_NOT_FOUND,"error");
        }
        catch (IOException l_e){
            System.out.println("Error : Unable to read file "+p_filename+" .");
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.CORRUPTED_FILE,"error");
        }
        return l_fileLines;
    }
    /**
     * Constructs the file path for the given map file.
     *
     * @param p_fileName the name of the file
     * @return the constructed file path as a string
     */
    private String getFilePath(String p_fileName){
        return Paths.get("src","main","Maps",p_fileName).toString();
    }
    /**
     * Saves the current map state to a file.
     *
     * @param p_currentState the current state containing the map to save
     * @param p_fileName    the expected map filename
     * @return true if the map is saved successfully, false otherwise
     */
    public boolean saveMap(CurrentState p_currentState, String p_fileName){
        try {
            String l_mapFormat = null;
            if(!p_fileName.equalsIgnoreCase(p_currentState.getD_map().getD_mapName())){
                p_currentState.setLogMessage("Kindly provide same filename to save the map which you have given for edit.", "error");
                return false;
            }
            else{
                if(p_currentState.getD_map() != null){
                    Map l_currentMap = p_currentState.getD_map();
                    if(l_currentMap.validateMap()){
                        l_mapFormat = this.getFormatToSave();
                        Files.deleteIfExists(Paths.get(getFilePath(p_fileName)));
                        FileWriter l_writer = new FileWriter(getFilePath(p_fileName));

                        parseMapToFile(p_currentState, l_writer, l_mapFormat);
                        p_currentState.updateLog("Map saved successfully.","effect");
                        l_writer.close();
                    }
                }
                else{
                    p_currentState.updateLog("Map is not valid. Kindly provide valid map.","error");
                    return false;
                }
            }
            return true;
        }
        catch (Exception l_e){
            p_currentState.updateLog("Error in saving map","error");
            return false;
        }
    }

    /**
     * Parse map to file.
     *
     * @param p_currentState the p current state
     * @param p_writer       the p writer
     * @param p_mapFormat    the p map format
     * @throws IOException the io exception
     */
    private void parseMapToFile(CurrentState p_currentState, FileWriter p_writer, String p_mapFormat) throws IOException {
        if(p_mapFormat.equalsIgnoreCase("ConquestMap")) {
            MapWriterAdapter l_mapWriterAdapter = new MapWriterAdapter(new ConquestMapFileWriter());
            l_mapWriterAdapter.parseMapToFile(p_currentState, p_writer, p_mapFormat);
        }
        else{
            new MapFileWriter().parseMapToFile(p_currentState, p_writer, p_mapFormat);
        }
    }

    /**
     * Gets format to save.
     *
     * @return the format to save
     * @throws IOException the io exception
     */
    private String getFormatToSave() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Kindly press 1 to save the map as 'Conquest Map Format' or else press 2 to save the map as 'Domination Map Format'");
        String l_nextOrder = l_reader.readLine();
        if(l_nextOrder.equalsIgnoreCase("1")){
            return "ConquestMap";
        } else if (l_nextOrder.equalsIgnoreCase("2")) {
            return "NormalMap";
        }
        else {
            System.err.println("Invalid input passed.");
            return this.getFormatToSave();
        }
    }

    /**
     * Edits the map by either creating a new file or loading an existing one.
     *
     * @param p_currentState the current state containing the map to edit
     * @param p_editFileName the filename of the map to edit
     * @throws IOException if an I/O error occurs while editing the map
     */
    public void editMap(CurrentState p_currentState, String p_editFileName) throws IOException {
        String l_fileLocation = getFilePath(p_editFileName);
        File l_fileToEdit = new File(l_fileLocation);

        if (l_fileToEdit.createNewFile()) {
            System.out.println(ProjectConstants.FILE_CREATED);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.FILE_CREATED,"effect");
            Map l_map = new Map();
            l_map.setD_mapName(p_editFileName);
            p_currentState.setD_map(l_map);
        } else {
            System.out.println(FILE_ALREADY_EXISTS);
            d_currentState.getD_modelLogger().setD_message(FILE_ALREADY_EXISTS,"effect");
            Map l_map = this.loadMap(p_currentState, p_editFileName);
            l_map.setD_mapName(p_editFileName);
            p_currentState.setD_map(l_map);
        }
    }


    /**
     * Edits the country information in the current map based on the specified operation.
     *
     * @param p_currentState the current state containing the map to edit
     * @param p_operation    the operation to perform ("add" or "remove")
     * @param p_argument     the argument for the operation (country name and optionally continent name)
     */
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
    /**
     * Adds or removes a country from the map based on the specified operation.
     *
     * @param p_mapToUpdate the map object to update
     * @param p_operation   the operation to perform ("add" or "remove")
     * @param p_arguments   the arguments required for the operation
     * @return the updated map object
     */
    private Map addRemoveCountry(Map p_mapToUpdate, String p_operation, String p_arguments) {
        String[] splitArgs = p_arguments.split(" ");

        if (p_operation.equals("add")) {
            if (splitArgs.length == 2) {
                String countryName = splitArgs[0];
                String continentName = splitArgs[1];

                p_mapToUpdate.addCountry(countryName, continentName);
            } else {
                System.out.println(ProjectConstants.INVALID_ADD_COUNTRY_COMMAND);
            }
        } else if (p_operation.equals("remove")) {
            if (splitArgs.length == 1) {
                String countryName = splitArgs[0];

                p_mapToUpdate.removeCountry(countryName);
                System.out.println("Country " + countryName + " removed successfully!");
            } else {
                System.out.println(ProjectConstants.INVALID_REMOVE_COUNTRY_COMMAND);
            }
        } else {
            System.out.println(ProjectConstants.INVALID_OPERATION);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.INVALID_OPERATION,"effect");

        }
        return p_mapToUpdate;
    }
    /**
     * Adds or removes a neighbour relationship between countries in the map.
     *
     * @param p_mapToUpdate the map object to update
     * @param p_operation   the operation to perform ("add" or "remove")
     * @param p_arguments   the arguments required for the operation (two country IDs)
     * @return the updated map object
     */
    private Map addRemoveNeighbour(Map p_mapToUpdate, String p_operation, String p_arguments) {
        String[] l_args = p_arguments.split(" ");

        if (l_args.length != 2) {
            System.out.println(ProjectConstants.INVALID_ARGUMENTS);
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
            System.out.println(ProjectConstants.INVALID_OPERATION);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.INVALID_OPERATION,"effect");
        }
        return p_mapToUpdate;
    }
    /**
     * Edits the neighbouring countries in the current map based on the specified operation.
     *
     * @param p_currentState the current state containing the map to edit
     * @param p_operation    the operation to perform ("add" or "remove")
     * @param p_arguments    the arguments required for the operation (two country IDs)
     */
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
    /**
     * Adds or removes a continent from the map based on the specified operation.
     *
     * @param p_mapToUpdate the map object to update
     * @param p_operation   the operation to perform ("add" or "remove")
     * @param p_arguments   the arguments required for the operation
     * @return the updated map object
     */
    private Map addRemoveContinents(Map p_mapToUpdate, String p_operation, String p_arguments) {
        String[] splitArgs = p_arguments.split(" ");

        if (p_operation.equals("add")) {
            if (splitArgs.length == 2) {
                String continentName = splitArgs[0];
                try {
                    int controlValue = Integer.parseInt(splitArgs[1]);
                    p_mapToUpdate.addContinent(continentName, controlValue);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Control value must be a valid integer.");
                }
            } else {
                System.out.println(ProjectConstants.INVALID_ADD_CONTINENT_COMMAND);
            }
        } else if (p_operation.equals("remove")) {
            if (splitArgs.length == 1) {
                String continentName = splitArgs[0];
                p_mapToUpdate.removeContinent(continentName);
            } else {
                System.out.println(ProjectConstants.INVALID_REMOVE_CONTINENT_COMMAND);
            }
        } else {
            System.out.println(ProjectConstants.INVALID_OPERATION);
            d_currentState.getD_modelLogger().setD_message(ProjectConstants.INVALID_OPERATION,"effect");
        }

        return p_mapToUpdate;
    }
    /**
     * Edits the continent information in the current map based on the specified operation.
     *
     * @param p_currentState the current state containing the map to edit
     * @param p_operation    the operation to perform ("add" or "remove")
     * @param p_argument     the argument required for the operation (continent name and optionally control value)
     */
    public void editContinent(CurrentState p_currentState, String p_operation, String p_argument) {
        String l_filename = p_currentState.getD_map().getD_mapName();
        Map l_mapToBeEdited = p_currentState.getD_map();

        if (l_mapToBeEdited.getD_mapCountries() == null && l_mapToBeEdited.getD_mapContinents() == null) {
            l_mapToBeEdited = this.loadMap(p_currentState, l_filename);
        }

        if (l_mapToBeEdited != null) {
            Map l_updatedMap = addRemoveContinents(l_mapToBeEdited, p_operation, p_argument);
            p_currentState.setD_map(l_updatedMap);
            p_currentState.getD_map().setD_mapName(l_filename);
        }
    }

    /**
     * Reset map.
     *
     * @param p_currentState the p current state
     * @param p_mapFile      the p map file
     */
    public void resetMap(CurrentState p_currentState, String p_mapFile) {
        System.err.println("Map is not valid. Kindly provide valid map.");
        p_currentState.updateLog(p_mapFile + " map is not valid. Kindly provide valid map.","effect");
        p_currentState.setD_map(new Map());
    }
}
