
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
    private ArrayList<String> moviesurl = new ArrayList<>(); //영화의 url이 담기는 Arraylist
    private ArrayList<String> errors = new ArrayList<>(); //한번 에러가 났을때 찌꺼기 url을 담는 Arraylist
    private List rest = new ArrayList<>();
    String mobile = "http://m.movie.naver.com/m/endpage/movie/Basic.nhn?movieCode=";
    String normal = "http://movie.naver.com/movie/bi/mi/basic.nhn?code=";
    GetPageInfo getpageinfo;
    MakeXml makeXml;
    SystemInfo systeminfo;

    //생성자
    public CalCulateModel(String year, int startpage, int endpage, GetPageInfo getpageinfo, MakeXml makemxl) {
        this.year = year;
        sb.append(this.year);
        this.startpage = startpage;
        this.endpage = endpage;
        this.getpageinfo = getpageinfo;
        this.makeXml = makemxl;
        this.systeminfo = SystemInfo.getInstance();

    }

    public void getMovieData(MakeXml xmlwriter, ArrayList<String> moviesurl, ArrayList<String> errors, boolean flag) throws InterruptedException {
        InformationReader reader = new InformationReader();
        ArrayList<String> actors = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        Iterator<String> it;
        //iterator 생성
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
                    doc = Jsoup.connect(normal + value).get();
                    Elements movieInfoElement = doc.select("div.mv_info");
                    String name = movieInfoElement.select("h3 > a:nth-child(1)").text();

                    String[] engName = movieInfoElement.select("strong").text().split(",");
                    Elements movieinfo = movieInfoElement.select("dl > dd:nth-child(2) > p > span > a");
                    //peoples 틀린듯
                    Elements peoples = doc.select("div.people > ul > li");
                    String countrycode = null;
                    String h_tx_story = doc.select("h5.h_tx_story").text();
                    String con_tx = doc.select("div.story_area > p").text();


                    for (Element li : movieinfo) {
                        String grenecode = getIndex(li.attr("href"));
                        reader.reader(grenecode);
                    }

                    countrycode = reader.countrycode();

//
                    for (Element people : peoples) {
                        title.add(people.select("dl.staff > dt").text());
                        actors.add(people.select("a.tx_people").attr("title"));

                    }


                    if (doc != null) {
                        xmlwriter.add(value, name, engName[0], countrycode, h_tx_story, con_tx, actors, title,reader.getgreneList());
                        getpageinfo.updateProgress();
                    }
                    it.remove();
                    actors.clear();
                    reader.eraseList();
                    title.clear();
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                } catch (IOException e) {
                    GetPageInfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + mobile + value);
                    errors.add(value);
                }

            }
        }

    }


    //모바일용 페이지의 영화 정보를 크롤링 하는 메소드
    public void getMobileMovieData(MakeXml xmlwriter, ArrayList<String> moviesurl, ArrayList<String> errors, boolean flag) throws InterruptedException {

        ArrayList<String> actors = new ArrayList<>();
        Iterator<String> it;
        //iterator 생성
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
                    //jsoup을 이용한 커넥션 document 생성 exception이 났을때는 error에 url 저장
                    doc = Jsoup.connect(mobile + value).get();
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
                    GetPageInfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + mobile + value);
                    errors.add(value);
                }
                doc = null;
            }
        }
    }

    //할당받은 페이지 내에서 전체 영화의 url을 크롤링
    public int execute(int start, int end, ArrayList<String> movies) throws InterruptedException {
        //start 부터 end 까지 페이지별로 영화 url 크롤링
        int k = 0;
        for (int i = start + 1; i <= end; i++) {
            Document doc = null;
            try {
                doc = Jsoup.connect(sb.toString() + "&page=" + String.valueOf(i)).timeout(3000).get();
            } catch (IOException e) {
                //여기에 수정 해야 함
                System.out.println("오류 발생");
                rest.add(i);
            }
            Elements newsHeadlines = doc.select("ul.directory_list > li");
            for (Element li : newsHeadlines) {
                Elements iterElem = li.getElementsByTag("a");
                movies.add(getIndex(iterElem.attr("href")));
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
            systeminfo.addLog(Thread.currentThread().getName() + " : 영화별 웹페이지 주소 얻기 완료");
            getpageinfo.setMax(result);
            systeminfo.addLog(Thread.currentThread().getName() + " : 웹페이지에서 정보 크롤링 중");

            if (systeminfo.getToggle() == "1") {
                System.out.println("test1");
                getMovieData(makeXml, moviesurl, errors, true);
                while (errors.isEmpty() == false) {
                    getMovieData(makeXml, errors, errors, false);
                }
            } else {
                getMobileMovieData(makeXml, moviesurl, errors, true);
                while (errors.isEmpty() == false) {
                    getMobileMovieData(makeXml, errors, errors, false);
                }
            }
            errors.clear();
        } catch (InterruptedException e) {
            GetPageInfo.logger.info(Thread.currentThread().getName() + " 인터럽트");
        }

        GetPageInfo.logger.info(Thread.currentThread().getName() + " is done!");
        return null;
    }
}
