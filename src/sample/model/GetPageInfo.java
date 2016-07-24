package sample.model;

import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

/**
 * Created by ParkCheolHo on 2016-02-19.
 * 모든 쓰레드를 컨트롤 하는 쓰레드
 */
public class GetPageInfo extends Task {
    private StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?open=");
    private String year;
    private double max = 0;
    private double count = 0;
    private SystemInfo systeminfo;


    public GetPageInfo(String year) {
        this.year = year;
        sb.append(year);
        this.systeminfo = SystemInfo.getInstance();
    }

    @Override
    protected String call() throws XMLStreamException {
        //remove
        systeminfo.removeLog();
        GetPageNum getPageNum = new GetPageNum(year);
        WriteFile makefile = null;
        if(!systeminfo.isUseDB())
            makefile = new MakeXml(systeminfo.getFilePath());

        String maxPage = getPageNum.Calculate();
        int result = 0;
        try {
            result = getPageNum.GetTotalMovieNumb(Integer.parseInt(maxPage));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SystemInfo.logger.info(year + "년도 전체 영화 갯수 : " + result); //console용
        systeminfo.addLog(year + "년도 검색된 전체 영화 갯수 : " + result + "개"); //scrollPane용
        if(!systeminfo.isUseDB())
            if (makefile != null) {
                makefile.start(false); //xml파일 만들기 스타트
            }
        if(systeminfo.isUsePoster()) {
            new File(systeminfo.getPosterfile().getPath() + "/Poster/").mkdirs();
            new File(systeminfo.getPosterfile().getPath() + "/Actors/").mkdirs();
        }

        int[] value = split(maxPage, systeminfo.getSpeed());
        for (int i = 0; i < systeminfo.getSpeed(); i++) {
            Task task = new CalculateModel(year, value[i], value[i+1], this, makefile);
            task.stateProperty().addListener((ov , oldVal, newState) -> {
                if (newState == State.SUCCEEDED)
                    systeminfo.addLog("자식Task 성공적으로 종료됩니다.");
            });
//            task.stateProperty().addListener(new ChangeListener<State>() {
//                @Override
//                public void changed(ObservableValue<? extends State> observableValue, Worker.State oldState, Worker.State newState) {
//                    if (newState == State.SUCCEEDED)
//                        systeminfo.addLog("자식Task 성공적으로 종료됩니다.");
//                }
//            });
            Thread thread = new Thread(task);
            systeminfo.addTheadlist(thread);
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
    // 자식 쓰레드의 갯수에 맞게 파싱할 영화 페이지 갯수를 정하는 메소드
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
}
