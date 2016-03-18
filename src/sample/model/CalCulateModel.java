
package sample.model;

import javafx.concurrent.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/**
 * Created by ParkCheolHo on 2016-02-20.
 * 각 영화별 url과 영화 데이터를 크롤링 하는 Task
 */
public class CalCulateModel extends Task {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    int startpage = 0;
    int endpage = 0;
    private ArrayList<String> moviesurl = new ArrayList<>();
    private ArrayList<String> errors = new ArrayList<>();
    private List rest = new ArrayList<>();
    String staticurl = "http://m.movie.naver.com/movie/bi/mi/basic.nhn?code=";
    GetPageInfo getpageinfo;
    MakeXml makemxl;
    SystemInfo systeminfo;

    public CalCulateModel(String year, int startpage, int endpage, GetPageInfo getpageinfo, MakeXml makemxl) {
        this.year = year;
        sb.append(this.year);
        this.startpage = startpage;
        this.endpage = endpage;
        this.getpageinfo = getpageinfo;
        this.makemxl = makemxl;
        this.systeminfo = SystemInfo.getInstance();

    }
    public void getSpecificMovieData(MakeXml xmlwriter, ArrayList<String> moviesurl, ArrayList<String> errors, boolean flag) throws InterruptedException {

        ArrayList<String> actors = new ArrayList<>();
        Iterator<String> it;
        if (flag) {
            it = moviesurl.iterator();
        } else {
            it = errors.iterator();
        }
        if (moviesurl.isEmpty() != true) {
            Document doc = null;
            while (it.hasNext()) {
                String value = it.next();
                try {
                    doc = Jsoup.connect(staticurl + value).get();
                    Elements movieInfoElement = doc.select("div.movie_spot");
                    Elements peoples = doc.select("#module_center > div > section:nth-child(3) > div:nth-child(1) > div.movie_card_cont > div > ul > li");
                    String name = movieInfoElement.select("div.movie_spot_header > h2 > a > span").text();
                    String grenecode = movieInfoElement.select("div.movie_basic_info > p.movie_rate > span.movie_genre").text();
                    String con_tx = doc.select("#navermovie_synopsis > div.movie_card_cont > div").text();
                    String director = null;
                    for (Element peopleli : peoples) {
                        String identifier = peopleli.select("div > a.movie_list_info > span.movie_desc_cast > em").text();
//                    GetPageInfo.logger.info(identifier);
                        if ("감독".equals(identifier)) {
                            director = peopleli.select("div > a.movie_list_info > h4").text();
                        } else {
                            actors.add(peopleli.select("div > a.movie_list_info > h4").text());
                        }
                    }
                    if (doc != null) {
                        xmlwriter.add(value, name, grenecode, actors, con_tx, director);
                        getpageinfo.updateProgress();
                    }
                    it.remove();
                    actors.clear();
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                } catch (IOException e) {
                    GetPageInfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + staticurl + value);
                    errors.add(value);
                }
                doc = null;
            }
        }
    }

    public int execute(int startpage, int endpage, ArrayList<String> moviesurl) throws InterruptedException {

        int k = 0;
        for (int i = startpage + 1; i <= endpage; i++) {
            Document doc = null;
            try {
                doc = Jsoup.connect(sb.toString() + "&page=" + String.valueOf(i)).timeout(0).get();
            } catch (IOException e) {
                System.out.println("오류 발생");
                rest.add(i);
            }
            Elements newsHeadlines = doc.select("ul.directory_list > li");
            for (Element li : newsHeadlines) {
                Elements iterElem = li.getElementsByTag("a");
                moviesurl.add(getIndex(iterElem.attr("href")));
                k++;
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        }
        return k;
    }

    public String getIndex(String text) {
        String[] results = text.split("=");
        return results[1];
    }

    @Override
    protected Object call() throws Exception {
        double result;
        try {
            result = execute(startpage, endpage, moviesurl);
            systeminfo.addLog(Thread.currentThread().getName()+" : 영화별 웹페이지 주소 얻기 완료");
            getpageinfo.setMax(result);
            errors.clear();
            systeminfo.addLog(Thread.currentThread().getName()+" : 웹페이지에서 정보 크롤링 중");
            getSpecificMovieData(makemxl, moviesurl, errors, true);
            while (errors.isEmpty() == false) {
                getSpecificMovieData(makemxl, errors, errors, false);
            }
        } catch (InterruptedException e) {
            GetPageInfo.logger.info(Thread.currentThread().getName() + " 인터럽트");
        }

        GetPageInfo.logger.info(Thread.currentThread().getName() + " is done!");
        return null;
    }
}
