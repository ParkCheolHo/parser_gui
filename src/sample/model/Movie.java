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
    private Boolean adult;
    private ArrayList<Actor> actors;
    private ArrayList<Integer> genre;

    public Movie(String movieIndex) {
        actors = new ArrayList<>();
        genre = new ArrayList<>();
        this.movieIndex = movieIndex;
    }

    String getMovieIndex() {
        return movieIndex;
    }

    void setMovieIndex(String movieIndex) {
        this.movieIndex = movieIndex;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getEngTitle() {
        return engTitle;
    }

    void setEngTitle(String engTitle) {
        this.engTitle = engTitle;
    }

    public String getSummary() {
        return summary;
    }

    void setSummary(String summary) {
        this.summary = summary;
    }

    int getGrade() {
        return grade;
    }

    void setGrade(int grade) {
        this.grade = grade;
    }

    ArrayList<Actor> getActors() {
        return actors;
    }

    void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    ArrayList<Integer> getGenre() {
        return genre;
    }

    void addGenre(Integer genre) {
        this.genre.add(genre);
    }

    void setGenre(ArrayList<Integer> genre) {
        this.genre = genre;
    }

    String getRunningTime() {
        return runningTime;
    }

    void setRunningTime(String runningTime) {
        this.runningTime = runningTime;
    }

    void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public int getCountry() {
        return country;
    }

    void setCountry(int country) {
        this.country = country;
    }

    String getOpeningDate() {
        return openingDate;
    }

    void setOpeningDate(String openingDate) {
        this.openingDate = openingDate;
    }

    String getImgAddress() {
        return imgAddress;
    }

    void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }

    Boolean getAdult() {
        return adult;
    }

    void setAdult(Boolean adult) {
        this.adult = adult;
    }

    void printAll() {
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
        for (Actor actor : actors) {
            System.out.print(actor.getRule() + "// " + actor.getName() + " : ");
        }
        System.out.println();
    }

}
