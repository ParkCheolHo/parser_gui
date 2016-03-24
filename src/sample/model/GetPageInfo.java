package sample.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;

import java.io.IOException;
import java.util.List;

import javafx.concurrent.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLStreamException;

/**
 * Created by ParkCheolHo on 2016-02-19.
 * 모든 쓰레드를 컨트롤 하는 쓰레드
 */
public class GetPageInfo extends Task {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    String totaled;
    List<Thread> threadlist;
    private double max = 0;
    private double count = 0;
    SystemInfo systeminfo;
    public static Logger logger = LoggerFactory.getLogger(GetPageInfo.class);

    public GetPageInfo(String year) {
        this.year = year;
        sb.append(year);
        this.systeminfo = SystemInfo.getInstance();
    }

    @Override
    protected String call() throws XMLStreamException {
        GetPageNum test = new GetPageNum(year, this);
        MakeXml makexml = new MakeXml(systeminfo.getFilePath());
        totaled = test.CalCulate();
        int result = 0;
        try {
            result = test.GetTotalmoiveNum(Integer.parseInt(totaled));
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info(year + "년도 전체 영화 갯수 : " + result); //console용
        systeminfo.addLog(year + "년도 검색된 전체 영화 갯수 : " + result + "개"); //scrollPane용

        makexml.Start(); //xml파일 만들기 스타트
        int testnum = 4;
        int[] value = split(totaled, testnum);
        for (int i = 0; i < testnum; i++) {
            Task task = new CalCulateModel(year, value[i], value[i + 1], this, makexml);
            task.stateProperty().addListener(new ChangeListener<State>() {
                @Override
                public void changed(ObservableValue<? extends State> observableValue, Worker.State oldState, Worker.State newState) {
                    if (newState == State.SUCCEEDED)
                        systeminfo.addLog("자식Task 성공적으로 종료됩니다.");
                }
            });
            Thread test1 = new Thread(task);
            systeminfo.addTheadlist(test1);
        }
        systeminfo.startThreadlist();
        threadlist = systeminfo.getTheadlist();

        logger.info("Thread 총 갯수 : " + threadlist.size());
        systeminfo.addLog("Thread 총 갯수 : " + threadlist.size());

        for (Thread i : threadlist)
            try {
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        makexml.End();
        systeminfo.addLog("종료되었습니다.");
        System.out.println("메인 쓰레드 죽는다");
        threadlist.clear();
        return null;
    }

    protected synchronized void updateProgress() {
        count++;
        updateProgress(count, max);
    }

    protected void setMax(double value) {
        max += value;
    }


    int[] split(String totaled, int multithrednum) {
        int[] result = new int[multithrednum + 1];
        int i = Integer.parseInt(totaled);
        int share = i / multithrednum;
        int rest = i % multithrednum;
        result[0] = 0;


        for (int k = 0; k < rest; k++) {
            result[k + 1]++;
        }

        for (int j = 0; j <= multithrednum - 1; j++) {
            result[j + 1] += share;
            if (j + 2 <= multithrednum) {
                result[j + 2] += result[j + 1];
            }
        }
        return result;
    }

}
