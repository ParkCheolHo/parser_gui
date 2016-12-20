package sample.model;

import java.util.ArrayList;

/**
 * Created by beakya on 2016. 12. 19..
 */
public class Movie {
    private String movieIndex;
    private String title;
    private String engTitle;
    private String summary;
    private String grade;
    private String runningTime;
    private ArrayList<Actor> actors;
    private ArrayList<String> genre;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }
    public void addGenre(String genre){
        this.genre.add(genre);
    }
    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }
    public void PrintAll(){
        System.out.println("movieIndex : " + movieIndex);
        System.out.println("title : " + title);
        System.out.println("engTitle : " + engTitle);
        System.out.println("summary : " + summary);
        System.out.println("grade : " + grade);
        System.out.println("actors : " + actors);
        System.out.println("genre : " + genre);
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
}
