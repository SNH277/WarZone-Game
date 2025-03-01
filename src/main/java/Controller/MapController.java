package Controller;

import Model.CurrentState;
import Model.Map;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
}
