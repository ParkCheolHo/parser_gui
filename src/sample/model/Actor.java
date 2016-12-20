package sample.model;

/**
 * Created by WhiteNight on 2016-04-22.
 * 배우 클래스
 */
class Actor {
    int index;
    String name;
    String img = null;
    String rule = null;
    Actor(int index, String name, String info, String rule){
        this.index = index;
        this.name = name;
        this.img = info;
        this.rule = rule;
    }
}
