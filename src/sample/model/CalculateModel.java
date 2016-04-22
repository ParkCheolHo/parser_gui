
/**
 * Created by WhiteNight on 2016-03-28.
 */
    package sample.model;

    import javafx.concurrent.Task;
    import org.jsoup.Jsoup;
    import org.jsoup.nodes.Document;
    import org.jsoup.nodes.Element;
    import org.jsoup.select.Elements;

    import java.io.*;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.Iterator;
    import java.util.List;

    /**
     * Created by ParkCheolHo on 2016-02-20.
     * 각 영화별 url과 영화 데이터를 크롤링 하는 Task
     */
    class CalculateModel extends Task {
        StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
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
            if(systeminfo.isUseDB()) {
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
                        Elements movieInfoElement = doc.select("div.mv_info");
                        String name = movieInfoElement.select("h3 > a:nth-child(1)").text();
                        String[] engName = movieInfoElement.select("strong").text().split(",");
                        Elements genreInfo = movieInfoElement.select("dl > dd:nth-child(2) > p > span > a");
                        //peoples 틀린듯
                        Elements peoples = doc.select("div.people > ul > li");

                        String h_tx_story = doc.select("h5.h_tx_story").text();
                        String con_tx = doc.select("div.story_area > p").text();
                        for (Element li : genreInfo) {
                            String genre = getIndex(li.attr("href"));
                            reader.reader(genre);
                        }
                        int country = reader.countrycode();

                        for (Element people : peoples) {
                            title.add(people.select("dl.staff > dt").text());
                           actors.add(jsoupactor(people.select("a.tx_people").attr("href")));
                        }

                        if(systeminfo.isUseDB()){
                            try {
                                mysql.insertMovie(value, name, engName[0], country, h_tx_story, con_tx, reader.getgreneList(), actors, title);
                            } catch (SQLException e) {
                                System.out.println("sql 에러");
                            }
                            getpageinfo.updateProgress();
                        }
                        else{
                            if (doc != null) {
//                                xml.add(value, name, engName[0], country, h_tx_story, con_tx, actors, title, reader.getgreneList());
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
                    } catch (IOException e) {
                        systeminfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + mobile + value);
                        if(flag ==true)
                            errors.add(value);
                    }
                }
            }
        }

        protected Actor jsoupactor(String i){
            int index = 0;
            String name = null;
            String[] info = null;
            Document doc = null;
                try {
                    doc = Jsoup.connect("http://movie.naver.com" + i).get();
                    Elements movieInfoElement = doc.select("div.mv_info_area");
                    index = Integer.parseInt(getIndex(i));
                    name = movieInfoElement.select("h3.h_movie > a").text();
                    String test = movieInfoElement.select("dt.step5 > em").text();
                    if(test.equals("출생")){
                        info = movieInfoElement.select("dl.info_spec > dd:nth-child(2)").text().split("/");
                        return new Actor(index, name, info);
                    }
                    return new Actor(index, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        return null;
        }

        //모바일용 페이지의 영화 정보를 크롤링 하는 메소드
        protected void getMobileMovieData(MakeXml xml, ArrayList<String> movie, ArrayList<String> errors, boolean flag) throws InterruptedException {

            ArrayList<String> actors = new ArrayList<>();
            Iterator<String> it;
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
                        //jsoup을 이용한 커넥션 document 생성 exception이 났을때는 error에 url 저장
                        doc = Jsoup.connect(mobile + value).get();
                        Elements movieInfoElement = doc.select("div.movie_spot");

                        Elements peoples = doc.select("#module_center > div > section:nth-child(3) > div:nth-child(1) > div.movie_card_cont > div > ul > li");
                        String name = movieInfoElement.select("div.movie_spot_header > h2 > a > span").text();
                        String grenecode = movieInfoElement.select("div.movie_basic_info > p.movie_rate > span.movie_genre").text();
                        String con_tx = doc.select("#navermovie_synopsis > div.movie_card_cont > div").text();
                        String director = null;
                        for (Element s : peoples) {
                            String identifier = s.select("div > a.movie_list_info > span.movie_desc_cast > em").text();
//                    GetPageInfo.logger.info(identifier);
                            if ("감독".equals(identifier)) {
                                director = s.select("div > a.movie_list_info > h4").text();
                            } else {
                                actors.add(s.select("div > a.movie_list_info > h4").text());
                            }
                        }
                        if (doc != null) {
                            xml.add(value, name, grenecode, actors, con_tx, director);
                            getpageinfo.updateProgress();
                        }
                        it.remove();
                        actors.clear();
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                    } catch (IOException e) {
                        systeminfo.logger.info(Thread.currentThread().getName() + " " + e.getMessage() + "/" + mobile + value);
                        if(flag ==true)
                            errors.add(value);
                    }
                    doc = null;
                }
            }
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
                    systeminfo.logger.info(Thread.currentThread().getName()+" jsoup 오류 " + i);
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



                if (systeminfo.getToggle() == "1") {
                    getMovieData(xml, movie, errors, true);
                    while (errors.isEmpty() == false) {
                        getMovieData(xml, errors, errors, false);
                    }
                } else {
                    getMobileMovieData(xml, movie, errors, true);
                    while (errors.isEmpty() == false) {
                        getMobileMovieData(xml, errors, errors, false);
                    }
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


