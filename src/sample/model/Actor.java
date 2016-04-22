package sample.model;

/**
 * Created by WhiteNight on 2016-04-22.
 */
public class Actor {
    int index;
    String name;
    String birthday = null;
    String nation = null;
    public Actor(int index, String name, String[] info){
        this.index = index;
        this.name = name;
        if(info.length>1) {
            this.birthday = info[0];
            this.nation = info[1];
        }else{
            this.birthday = info[0];
        }
    }
    public Actor(int index, String name){
        this.index = index;
        this.name = name;
    }
}
