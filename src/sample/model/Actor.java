package sample.model;

/**
 * Created by WhiteNight on 2016-04-22.
 * 배우 클래스
 */
class Actor {
    private int index;
    private String name;
    private String img = null;
    private String rule = null;
    Actor(int index, String name, String info, String rule){
        this.index = index;
        this.name = name;
        this.img = info;
        this.rule = rule;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }
}
