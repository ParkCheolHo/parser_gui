package sample.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by ParkCheolHo on 2016-02-20.
 */
public class CalCulateModel implements Runnable{
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    String staticurl = "http://movie.naver.com";
    InformationReader reader;
    int startpage = 0;
    int endpage = 0;
    private ArrayList<String> moviesurl = new ArrayList<>();
    Iterator<String>  it = moviesurl.iterator();
    private ArrayList<String> actors = new ArrayList<>();


    public CalCulateModel(String year, int startpage, int endpage){
        this.year = year;
        sb.append(this.year);
        this.startpage = startpage;
        this.endpage = endpage;


    }
    public void getSpecificMovieData() throws IOException {

        if(moviesurl.isEmpty() != true) {
            for (String value : moviesurl) {

                    Document doc = Jsoup.connect(staticurl + value).get();
//                    GetPageInfo.logger.info()
                    int movieindex = Integer.parseInt(getIndex(value));
                    Elements movieInfoElement = doc.select("div.mv_info");
                    //영화 무비포스터 url
                    Elements posterElement = doc.select("#content > div.article > div.mv_info_area > div.poster > a > img");
                    String moviePoster = posterElement.attr("src");

                    //영화이름
                    String moviename = movieInfoElement.select("h3 > a:nth-child(1)").text();
                    //영화 영어이름
                    String[] movieEngName = movieInfoElement.select("strong").text().split(",");

                    //get movie infomation element
                    Elements movieinfo = movieInfoElement.select("dl > dd:nth-child(2) > p > span > a");
                    //get moive gnere, country, opening year, month, day get to array

                    for (Element li : movieinfo) {
                        String grenecode = getIndex(li.attr("href"));
                        reader.reader(grenecode);
                    }
                    int year = Integer.parseInt(this.year);
                    ArrayList<String> genre = reader.getgreneList();
                    String countrycode = reader.countrycode();


                    //movie story name
                    String h_tx_story = doc.select("h5.h_tx_story").text();

                    //moive story
                    String con_tx = doc.select("div.story_area > p").text();

                    Elements peoples = doc.select("div.people > ul > li");
                    String director;
                    for (Element peopleli : peoples) {
                        if ("감독".equals(peopleli.select("dt.staff_dir").text())) {
                            director = peopleli.select("a.tx_people").attr("title");
                        } else {
                            actors.add(peopleli.select("a.tx_people").attr("title"));
                        }
                        //System.out.println(peopleli.select("dl").text());
                        //System.out.println(peopleli.select("a.tx_people").attr("title"));
                    }
//                    if(posterheight != null && posterheight.length() != 0){
//                        posterheight2 =Integer.parseInt(posterheight);
//                    }

                    if (con_tx != null && con_tx.length() != 0 && moviePoster.length() != 0 && moviePoster != null) {
                        if (actors.isEmpty() != true && genre.isEmpty() != true) {

                        }
                    }
                    reader.eraseList();
                }
            }
        }
    public int execute( int startpage, int endpage) throws IOException {
        int k = 0;
        for(int i=startpage+1;i<=endpage;i++){
            Document doc = Jsoup.connect(sb.toString() +"&page="+String.valueOf(i)).get();
            Elements newsHeadlines = doc.select("ul.directory_list > li");
            for(Element li : newsHeadlines){
                Elements iterElem = li.getElementsByTag("a");
                //System.out.println(iterElem.attr("href"));
                moviesurl.add(iterElem.attr("href"));
                k++;
            }
        }
        return k;
    }

    public String getIndex(String text){
        String[] results = text.split("=");
        return results[1];
    }

    @Override
    public void run() {
        try {
            execute(startpage, endpage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
