
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ParkCheolHo on 2016-02-20.
 * 각 영화별 url과 영화 데이터를 크롤링 하는 Task
 */
class CalculateModel extends Task {
    private StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?open=");
    private int start = 0;
    private int end = 0;
    private ArrayList<String> movie = new ArrayList<>(); //영화의 url이 담기는 Arraylist
    private ArrayList<String> errors = new ArrayList<>(); //한번 에러가 났을때 찌꺼기 url을 담는 Arraylist
    private List rest = new ArrayList<>();
    private GetPageInfo getpageinfo;
    private WriteFile xml;
    private SystemInfo systeminfo;
    //생성자
    CalculateModel(String year, int start, int end, GetPageInfo getpageinfo, WriteFile xml) {
        sb.append(year);
        this.start = start;
        this.end = end;
        this.getpageinfo = getpageinfo;
        this.xml = xml;
        this.systeminfo = SystemInfo.getInstance();
    }
    private void getMovieData(ArrayList<String> movie, ArrayList<String> errors, boolean flag) throws InterruptedException {
        InformationReader reader = new InformationReader();
        ArrayList<Actor> actors = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        Iterator<String> it;
        WriteFile mysql = null;
        if (systeminfo.isUseDB()) {
            mysql = new MySql(systeminfo.getHost(), systeminfo.getDb(), systeminfo.getId(), systeminfo.getPassword());
            mysql.start(false);
        } else {
            mysql = xml;
        }
        //iterator 생성
        if (flag) {
            it = movie.iterator();
        } else {
            it = errors.iterator();
        }
        if (!movie.isEmpty()) {
            Document doc = null;
            while (it.hasNext()) {
                String value = it.next();
                try {
                    String normal = "http://movie.naver.com/movie/bi/mi/basic.nhn?code=";
                    doc = Jsoup.connect(normal + value).get();
                } catch (IOException e) {
                    SystemInfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + value);
                    if (flag)
                        errors.add(value);
                    continue;
                }
                Elements movieInfoElement = doc.select("div.mv_info");
                String name = movieInfoElement.select("h3 > a:nth-child(1)").text();
                String[] engName = movieInfoElement.select("strong.h_movie2").text().split(",");
                String poster = doc.select("#content > div.article > div.mv_info_area > div.poster > a > img").attr("src");
                Elements peoples = doc.select("div.people > ul > li");
                String h_tx_story = doc.select("h5.h_tx_story").text();
                String con_tx = doc.select("div.story_area > p").text();
                String grade = doc.select("#content > div.article > div.mv_info_area > div.mv_info > dl > dd:nth-child(8) > p >  a:nth-child(1)").text();
                Elements outline = movieInfoElement.select("dl.info_spec > dd:nth-child(2) > p > span");
                reader.setGrade(grade);
                boolean adult = checkGenre(outline, reader);
                if (!adult) {
                    title.clear();
                    reader.eraseList();
                    actors.clear();
                    it.remove();
                    getpageinfo.update();
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    continue;
                }
                int country = reader.getCountry();
                if (systeminfo.isUseOption()) {
                    if ("".equals(con_tx) || "".equals(poster) || outline == null || "".equals(grade)) {
                        it.remove();
                        actors.clear();
                        reader.eraseList();
                        title.clear();
                        getpageinfo.update();
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        continue;
                    }
                    if (engName[0].length() < 5) {
                        try {
                            int val = Integer.parseInt(engName[0]);
                            it.remove();
                            actors.clear();
                            reader.eraseList();
                            title.clear();
                            getpageinfo.update();
                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }
                            continue;
                        } catch (Exception e) {
                            System.out.println("val " + engName[0]);
                        }
                    }
                    try {
                        if (getImageHeight(poster) < 180) {
                            title.clear();
                            actors.clear();
                            reader.eraseList();
                            it.remove();
                            getpageinfo.update();
                            if (Thread.interrupted()) {
                                throw new InterruptedException();
                            }
                            continue;
                        }
                    } catch (IOException e) {
                        System.out.println("Exception Check");
                        e.printStackTrace();
                    }
                }
                for (Element people : peoples) {
                    int index = 0;
                    title.add(people.select("dl.staff > dt").text());
                    try {
                        index = Integer.parseInt(getIndex(people.select("a.tx_people").attr("href")));
                    } catch (Exception e) {
                        continue;
                    }
                    String actorName = people.select("a.tx_people").text();
                    String img = people.select("a.thumb_people > img").attr("src");
                    if ("http://static.naver.net/movie/2012/06/dft_img111x139.png".equals(img))
                        img = null;
                    actors.add(new Actor(index, actorName, img));
                }
                if (systeminfo.isUsePoster()) {
                    saveImage(poster, "/Poster/", value);
                    for (Actor i : actors) {
                        if (i.img != null)
                            saveImage(i.img, "/Actors/", String.valueOf(i.index));
                    }
                }
//                System.out.println("============================================================================");
//                System.out.println("index " + value);
//                System.out.println("name : " + name);
//                System.out.println("engname : " + engName[0]);
//                System.out.println("country : " + country);
//                System.out.println("h_tx_story : " + h_tx_story);
//                System.out.println("con_tx : " + con_tx);
//                System.out.println("genre : " + reader.getgrene());
//                System.out.println("grade : " + reader.getGrade());
//                System.out.println("opendate : " + reader.getOpen_date());
//                System.out.println("getRunning_time : " + reader.getRunning_time());
//                System.out.println("actors" + actors);
                try {
                    mysql.add(value, name, engName[0], country, h_tx_story, con_tx, reader, actors, title, Integer.parseInt(systeminfo.getYear()));
                } catch (Exception e) {
                    SystemInfo.logger.info(value + " : " + e.getMessage());
                }
                getpageinfo.update();
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

    private void saveImage(String val, String path, String index) {
        try {
            URL url = new URL(val);
            File file = new File(systeminfo.getPosterfile().getPath() + path + index + ".jpg");
            if (!file.exists()) {
                InputStream in = url.openStream();
                OutputStream out = new BufferedOutputStream(new FileOutputStream(systeminfo.getPosterfile().getPath() + path + index + ".jpg"));
                for (int b; (b = in.read()) != -1; ) {
                    out.write(b);
                }
                in.close();
                out.close();
            }
        } catch (IOException e) {
            SystemInfo.logger.info("이미지 저장 에러 : " + index);
        }
    }
    //포스터의 세로 픽셀을 구하는 메소드
    private int getImageHeight(String val) throws IOException {
        URL url = new URL(val);
        Image image = ImageIO.read(url);
        return image.getHeight(null);
    }
    private boolean checkGenre(Elements li, InformationReader reader) {
        for (Element i : li) {
            Elements aTag = i.select("a");
            if (aTag.hasText()) {
                for (Element t : aTag) {
                    String genre = getIndex(t.attr("href"));
                    reader.reader(genre);
                }
            } else {
                reader.setRunning_time(i.text());
            }
        }
        return true;
    }
    //할당받은 페이지 내에서 전체 영화의 url을 크롤링
    private int execute(int start, int end, ArrayList<String> movies) throws InterruptedException {
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
                SystemInfo.logger.info(Thread.currentThread().getName() + " jsoup 오류 " + i);
                //여기에 수정 해야 함
                systeminfo.addLog("중대한 오류 발생 다시 시작해주세요.");
                List<Thread> threadList = systeminfo.getThreads();
                threadList.forEach(Thread::interrupt);
            }

        }
        return k;
    }
    private String getIndex(String text) {
        String[] results = text.split("=");
        return results[1];
    }
    @Override
    protected Object call() throws Exception {
        double result;
        try {
            result = execute(start, end, movie);
            systeminfo.addLog(Thread.currentThread().getName() + " : 영화별 웹페이지 주소 얻기 완료");
            getpageinfo.setMax(result);
            systeminfo.addLog(Thread.currentThread().getName() + " : 웹페이지에서 정보 크롤링 중");
            getMovieData(movie, errors, true);
            while (!errors.isEmpty()) {
                getMovieData(errors, errors, false);
            }
            movie.clear();
            rest.clear();
            errors.clear();
        } catch (InterruptedException e) {
            SystemInfo.logger.info(Thread.currentThread().getName() + " 인터럽트");
        }
        SystemInfo.logger.info(Thread.currentThread().getName() + " is done!");
        return null;
    }
}


