package sample.model;

import javafx.concurrent.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * Created by ParkCheolHo on 2016-02-19.
 * 모든 쓰레드를 컨트롤 하는 쓰레드
 */
public class RootThread extends Task {
    private StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?open=");
    private String year;
    private double max = 0;
    private double count = 0;
    private SystemInfo systeminfo;


    public RootThread(String year) {
        this.year = year;
        sb.append(year);
        this.systeminfo = SystemInfo.getInstance();
    }

    @Override
    protected String call() throws XMLStreamException {
        //remove
        systeminfo.removeLog();
        FileWriter makefile = null;
        //TODO xml 파일 만드는것 다시 생각해볼것
        if(!systeminfo.isUseDB()){
            makefile = new MakeXml(systeminfo.getFilePath());
            makefile.start();
        }


        String maxPage = Calculate(sb.toString());
        int result = 0;
        int[] value = split(maxPage, systeminfo.getSpeed());
        try {
            result = GetTotalMovieNumb(Integer.parseInt(maxPage));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(systeminfo.isUsePoster()) {
            boolean posterDirectoryResult = new File(systeminfo.getPosterFile().getPath() + "/Poster/").mkdirs();
            boolean actorDirectoryResult = new File(systeminfo.getPosterFile().getPath() + "/Actors/").mkdirs();

            if(!(posterDirectoryResult && actorDirectoryResult)){
                //create fail exception
            }
        }
        SystemInfo.logger.info(year + "년도 전체 영화 갯수 : " + result); //console용
        systeminfo.addLog(year + "년도 검색된 전체 영화 갯수 : " + result + "개"); //scrollPane용

        for (int i = 0; i < systeminfo.getSpeed(); i++) {
            Task task = new CalculateModel(year, value[i], value[i+1], this, makefile);
            task.stateProperty().addListener((ov , oldVal, newState) -> {
                if (newState == State.SUCCEEDED)
                    systeminfo.addLog("자식Task 성공적으로 종료됩니다.");
            });
            Thread thread = new Thread(task);
            systeminfo.addThreadList(thread);
        }
        systeminfo.startThread();
        List<Thread> threads = systeminfo.getThreads();

        SystemInfo.logger.info("Thread 총 갯수 : " + threads.size());
        systeminfo.addLog("Thread 총 갯수 : " + threads.size());
        //모든 자식 쓰레드가 끝날때까지 기다린다.
        for (Thread i : threads)
            try {
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        if(!systeminfo.isUseDB())
            if (makefile != null) {
                makefile.end();
            }
        systeminfo.addLog("종료되었습니다.");
        SystemInfo.logger.info(Thread.currentThread().getName() + "is done");
        systeminfo.clearThreadList();
        threads.clear();
        return null;
    }
    // 전체 진행속도 표시
    synchronized void update() {
        count++;
        updateProgress(count, max);
        double s = (count / max) * 100;
        systeminfo.showLabel(s);
    }
    //전체 영화 갯수 설정
    void setMax(double value) {
        max += value;
    }


    // 자식 쓰레드의 갯수에 맞게 크롤링할 영화 페이지 갯수를 정하는 메소드
    private int[] split(String maxPage, int section) {
        int[] result = new int[section + 1];
        int i = Integer.parseInt(maxPage);
        int share = i / section;
        int rest = i % section;
        result[0] = 0;
        for (int k = 0; k < rest; k++) {
            result[k + 1]++;
        }
        for (int j = 1; j <= section ; j++) {
            result[j] += share;
            result[j] += result[j-1];
            }
        return result;
    }
    //해당년도의 영화 페이지 갯수를 구하는 메소드
    private String Calculate(String st) {
        String url = st + "&page=10000";
        String[] text = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements newsHeadlines = doc.select("div.pagenavigation");
            text = newsHeadlines.text().split(" ");

        } catch (IOException e) {
            SystemInfo.logger.info("error during get total page number");
            Calculate(st);
        }
        return text != null ? text[text.length - 1] : null;
    }
    //해당 년도의 총 영화의 갯수를 구하는 메소드
    private int GetTotalMovieNumb(int page) throws IOException {
        int result = 0;
        Document doc = Jsoup.connect(sb.toString() + "&page=" + page).timeout(0).get();
        Elements newsHeadlines = doc.select("ul.directory_list > li");
        for (Element li : newsHeadlines) {
            result++;
        }
        return (page-1)*20 + result;
    }
}
