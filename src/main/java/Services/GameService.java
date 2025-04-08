package Services;

import Model.*;

import java.io.*;

/**
 * GameService handles saving and loading the game state (Phase object) using serialization.
 *
 * Author: Akhilesh Kanbarkar
 */
public class GameService {

    /**
     * Default constructor.
     */
    public GameService() {
    }
    /**
     * Serializes and saves the current game phase to a file.
     *
     * @param p_currentPhase The current phase object to save.
     * @param p_fileName The name of the file to which the game will be saved.
     */
    public static void saveGame(Phase p_currentPhase, String p_fileName){
        try {
            FileOutputStream l_gameSaveFile = new FileOutputStream("src/main/maps/" + p_fileName);
            ObjectOutputStream l_gameSaveFileObjectStream = new ObjectOutputStream(l_gameSaveFile);
            l_gameSaveFileObjectStream.writeObject(p_currentPhase);
            l_gameSaveFileObjectStream.flush();
            l_gameSaveFileObjectStream.close();
        } catch (Exception l_e) {
            l_e.printStackTrace();
        }
    }

}