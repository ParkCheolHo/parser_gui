package sample.model;

import java.util.ArrayList;

/**
 * Created by WhiteNight on 2016-06-28.
 */
interface WriteFile {
    int start(boolean flag);
    void add(String index, String name, String eng_name, int country,
             String story_name, String story, InformationReader reader, ArrayList<Actor> actors, ArrayList<String> title, int year) throws Exception;
    void end();
    
}
