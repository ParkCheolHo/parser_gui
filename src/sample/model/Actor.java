package sample.model;

/**
 * Created by WhiteNight on 2016-04-22.
 */
public class Actor {
    int index;
    String name;
    String imgsrc = null;
    public Actor(int index, String name, String info){
        this.index = index;
        this.name = name;
        this.imgsrc = info;
    }
}
