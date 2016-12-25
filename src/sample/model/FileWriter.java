package sample.model;

import java.util.ArrayList;

/**
 * Created by WhiteNight on 2016-06-28.
 */
interface FileWriter {
    int start();
    Object startup();
    void add(Movie movie) throws Exception;
    void end();
    
}
