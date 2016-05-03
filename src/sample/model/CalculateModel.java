
/**
 * Created by WhiteNight on 2016-03-28.
 */
package sample.model;

import javafx.concurrent.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by ParkCheolHo on 2016-02-20.
 * 각 영화별 url과 영화 데이터를 크롤링 하는 Task
 */
class CalculateModel extends Task {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?open=");
    String year;
    int start = 0;
    int end = 0;
    private ArrayList<String> movie = new ArrayList<>(); //영화의 url이 담기는 Arraylist
    private ArrayList<String> errors = new ArrayList<>(); //한번 에러가 났을때 찌꺼기 url을 담는 Arraylist
    private List rest = new ArrayList<>();
    String mobile = "http://m.movie.naver.com/m/endpage/movie/Basic.nhn?movieCode=";
    String normal = "http://movie.naver.com/movie/bi/mi/basic.nhn?code=";
    GetPageInfo getpageinfo;
    MakeXml xml;
    SystemInfo systeminfo;

    //생성자
    protected CalculateModel(String year, int start, int end, GetPageInfo getpageinfo, MakeXml xml) {
        this.year = year;
        sb.append(this.year);
        this.start = start;
        this.end = end;
        this.getpageinfo = getpageinfo;
        this.xml = xml;
        this.systeminfo = SystemInfo.getInstance();
    }

    protected void getMovieData(MakeXml xml, ArrayList<String> movie, ArrayList<String> errors, boolean flag) throws InterruptedException {
        InformationReader reader = new InformationReader();
        ArrayList<Actor> actors = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        Iterator<String> it;
        MySql mysql = null;
        if (systeminfo.isUseDB()) {
            mysql = new MySql(systeminfo.getHost(), systeminfo.getDb(), systeminfo.getId(), systeminfo.getPassword());
            mysql.Connection();
        }
        //iterator 생성
        if (flag) {
            it = movie.iterator();
        } else {
            it = errors.iterator();
        }
        if (movie.isEmpty() != true) {
            Document doc = null;
            while (it.hasNext()) {
                String value = it.next();
                try {
                    doc = Jsoup.connect(normal + value).get();
                } catch (IOException e) {
                    systeminfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + mobile + value);
                    if (flag == true)
                        errors.add(value);
                    continue;
                }
                Elements movieInfoElement = doc.select("div.mv_info");
                String name = movieInfoElement.select("h3 > a:nth-child(1)").text();
                String[] engName = movieInfoElement.select("strong.h_movie2").text().split(",");
                Elements genreInfo = movieInfoElement.select("dl > dd:nth-child(2) > p > span > a");
                String poster = doc.select("#content > div.article > div.mv_info_area > div.poster > a > img").attr("src");
                Elements peoples = doc.select("div.people > ul > li");
                String h_tx_story = doc.select("h5.h_tx_story").text();
                String con_tx = doc.select("div.story_area > p").text();
                String grade = doc.select("#content > div.article > div.mv_info_area > div.mv_info > dl > dd:nth-child(8) > p >  a:nth-child(1)").text();

                boolean adult = checkgenre(genreInfo, grade, reader);

                if (adult == false) {
                    title.clear();
                    reader.eraseList();
                    actors.clear();
                    it.remove();
                    getpageinfo.updateProgress();
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    continue;
                }

                int country = reader.countrycode();

                if (systeminfo.isUseOption()) {
                    if ("".equals(con_tx) || "".equals(poster) || genreInfo == null || "".equals(grade)) {
                        it.remove();
                        actors.clear();
                        reader.eraseList();
                        title.clear();
                        getpageinfo.updateProgress();
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        continue;
                    }

                    try {
                        if (getImgageHeight(poster) < 180) {
                            title.clear();
                            actors.clear();
                            reader.eraseList();
                            it.remove();
                            getpageinfo.updateProgress();
                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }
                            continue;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                reader.setGradecode(grade);

                for (Element people : peoples) {
                    int index = 0;
                    title.add(people.select("dl.staff > dt").text());
                    try {
                        index = Integer.parseInt(getIndex(people.select("a.tx_people").attr("href")));
                    }catch (Exception e){
                        continue;
                    }
                    String actorname = people.select("a.tx_people").text();
                    String imgsrc = people.select("a.thumb_people > img").attr("src");
                    if ("http://static.naver.net/movie/2012/06/dft_img111x139.png".equals(imgsrc))
                        imgsrc = null;

                    actors.add(new Actor(index, actorname, imgsrc));
                }

                if (systeminfo.isUsePoster()) {
                    saveimage(poster, "/Poster/", value);
                    for (Actor i : actors) {
                        if (i.imgsrc != null)
                            saveimage(i.imgsrc, "/Actors/", String.valueOf(i.index));
                    }
                }

                if (systeminfo.isUseDB()) {
                    try {
                        mysql.insertMovie(value, name, engName[0], country, h_tx_story, con_tx, reader.getGradecode(),reader.getgreneList(), actors, title, Integer.parseInt(systeminfo.getYear()));
                    } catch (SQLException e) {
                        System.out.println("sql 에러");
                    }
                    getpageinfo.updateProgress();
                } else {
                    if (doc != null) {
                                xml.add(value, name, engName[0], country, h_tx_story, con_tx, actors, title, reader.getgrene());
                        getpageinfo.updateProgress();
                    }
                }

                it.remove();
                actors.clear();
                reader.eraseList();
                title.clear();
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }

            }
        }

    }

    void saveimage(String val, String path, String index) {


        try {
            URL url = new URL(val);

            Image image = ImageIO.read(url);

            InputStream in = url.openStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(systeminfo.getPosterfile().getPath() + path + index + ".jpg"));

            for (int b; (b = in.read()) != -1; ) {
                out.write(b);
            }
            out.close();
            in.close();
        } catch (IOException e) {
            systeminfo.logger.info("이미지 저장 에러 : " + index);
        }
    }

    int getImgageHeight(String val) throws IOException {
        URL url = new URL(val);
        Image image = ImageIO.read(url);
        int height = image.getHeight(null);
        return height;
    }

    boolean checkgenre(Elements li, String grade, InformationReader reader) {
        for (Element i : li) {
            String genre = getIndex(i.attr("href"));
            String genrename = i.text();
            if ("청소년 관람불가".equals(grade)) {
                if ("멜로/로맨스".equals(genrename) || "드라마".equals(genrename))
                    return false;
            }
            reader.reader(genre);
        }
        return true;
    }

    //할당받은 페이지 내에서 전체 영화의 url을 크롤링
    protected int execute(int start, int end, ArrayList<String> movies, List rest) throws InterruptedException {
        //start 부터 end 까지 페이지별로 영화 url 크롤링

        int k = 0;
        for (int i = start + 1; i <= end; i++) {
            Document doc = null;
            try {
                doc = Jsoup.connect(sb.toString() + "&page=" + String.valueOf(i)).timeout(3000).get();
                Elements newsHeadlines = doc.select("ul.directory_list > li");
                for (Element li : newsHeadlines) {
                    Elements a = li.getElementsByTag("a");
                    movies.add(getIndex(a.attr("href")));
                    k++;
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                }
            } catch (IOException e) {
                systeminfo.logger.info(Thread.currentThread().getName() + " jsoup 오류 " + i);
                //여기에 수정 해야 함
                systeminfo.addLog("중대한 오류 발생 다시 시작해주세요.");
                List<Thread> threadList = systeminfo.getThreads();
                threadList.forEach(Thread::interrupt);
            }

        }
        return k;
    }

    protected String getIndex(String text) {
        String[] results = text.split("=");
        return results[1];
    }

    @Override
    protected Object call() throws Exception {
        double result;
        try {
            result = execute(start, end, movie, rest);
            systeminfo.addLog(Thread.currentThread().getName() + " : 영화별 웹페이지 주소 얻기 완료");
            getpageinfo.setMax(result);
            systeminfo.addLog(Thread.currentThread().getName() + " : 웹페이지에서 정보 크롤링 중");



                getMovieData(xml, movie, errors, true);
                while (errors.isEmpty() == false) {
                    getMovieData(xml, errors, errors, false);
                }

            movie.clear();
            rest.clear();
            errors.clear();
        } catch (InterruptedException e) {
            systeminfo.logger.info(Thread.currentThread().getName() + " 인터럽트");
        }

        systeminfo.logger.info(Thread.currentThread().getName() + " is done!");
        return null;
    }
}


