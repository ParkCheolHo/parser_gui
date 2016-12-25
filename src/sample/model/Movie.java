package sample.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by beakya on 2016. 12. 19..
 */
public class Movie {

    private Boolean adult;
    private String imgAddress = "";
    private String openingDate = null;
    private String movieIndex = "";
    private String title = "";
    private String engTitle = "";
    private String summary = "";
    private String runningTime = "";
    private String summaryTitle = "";
    private String[] grade;
    private String[] country;
    private ArrayList<Actor> actors;
    private HashMap<Integer,String> genre;

    public Movie(String movieIndex) {
        actors = new ArrayList<>();
        genre = new HashMap<>();
        this.movieIndex = movieIndex;
    }

    public String getMovieIndex() {
        return movieIndex;
    }

    public void setMovieIndex(String movieIndex) {
        this.movieIndex = movieIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = (title == null)? "" : title;
    }

    public String getEngTitle() {
        return engTitle;
    }

    public void setEngTitle(String engTitle) {
        this.engTitle = (engTitle != null)? engTitle : "";
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = (summary != null)? summary : "";
    }

    public String[] getGrade() {
        return grade;
    }

    public void setGrade(String[] grade) {
        this.grade = grade;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public HashMap<Integer, String> getGenre() {
        return genre;
    }

    public void addGenre(HashMap<Integer, String> genre) {
        this.genre = genre;
    }

    public void setGenre(HashMap<Integer, String> genre) {
        this.genre = genre;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = (runningTime != null) ? runningTime: "";
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public String[] getCountry() {
        return country;
    }

    public void setCountry(String[] country) {
        this.country = country;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = (openingDate != null) ? openingDate : "";
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = (imgAddress != null)? imgAddress : "";
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getSummaryTitle() {
        return summaryTitle;
    }

    public void setSummaryTitle(String summaryTitle) {
        this.summaryTitle = (summaryTitle != null)? summaryTitle : "";
    }


    public void printAll() {
        System.out.println("movieIndex : " + movieIndex);
        System.out.println("moviePosterAddress : " + getImgAddress());
        System.out.println("title : " + title);
        System.out.println("engTitle : " + engTitle);
        System.out.println("summary : " + summary);
        for(String gradeInfo : grade){
            System.out.print( gradeInfo + ": ");
        }
        System.out.println();

        for(String countryInfo : country){
            System.out.print( countryInfo + ": ");
        }
        System.out.println();
        for( Map.Entry<Integer, String> elem : genre.entrySet() ){
            System.out.println( String.format("genre : %s, 값 : %s", elem.getKey(), elem.getValue()) );
        }

        System.out.println("running Time : " + runningTime);
        System.out.println("opening Date = " + openingDate);
        for (Actor actor : actors) {
            System.out.print(actor.getRule() + "// " + actor.getName() + " : " + actor.getImg());
            System.out.println();
        }
        System.out.println();
        System.out.println("is adult : " + adult);
    }

}
