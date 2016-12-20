package sample.model;

import java.util.ArrayList;

/**
 * Created by beakya on 2016. 12. 19..
 */
public class Movie {
    private int grade;
    private int country;
    private String imgAddress;
    private String openingDate = null;
    private String movieIndex;
    private String title;
    private String engTitle;
    private String summary;
    private String runningTime;
    private ArrayList<Actor> actors;
    private ArrayList<Integer> genre;
    public Movie(String movieIndex){
        actors = new ArrayList<>();
        genre = new ArrayList<>();
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
        this.title = title;
    }

    public String getEngTitle() {
        return engTitle;
    }

    public void setEngTitle(String engTitle) {
        this.engTitle = engTitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<Integer> getGenre() {
        return genre;
    }
    public void addGenre(Integer genre){
        this.genre.add(genre);
    }
    public void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }

    public String getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    public void addActor(Actor actor){
        this.actors.add(actor);
    }

    public int getCountry() {
        return country;
    }

    public void setCountry(int country) {
        this.country = country;
    }

    public String getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    public void PrintAll(){
        System.out.println("movieIndex : " + movieIndex);
        System.out.println("moviePosterAddress : " + getImgAddress());
        System.out.println("title : " + title);
        System.out.println("engTitle : " + engTitle);
        System.out.println("summary : " + summary);
        System.out.println("grade : " + grade);
        System.out.println("genre : " + genre);
        System.out.println("country : " + country);
        System.out.println("running Time : " + runningTime);
        System.out.println("opening Date = " + openingDate);
        for(Actor actor : actors){
            System.out.print( actor.getRule() + "// " + actor.getName() + " : ");
        }
    }

}
