package sample.model;

import javafx.concurrent.Task;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by ParkCheolHo on 2016-02-19.
 * 모든 쓰레드를 컨트롤 하는 쓰레드
 */
public class GetPageInfo extends Task {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    String totaled;
    ArrayList<Thread> threadlist =new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(GetPageInfo.class);
    public GetPageInfo(String year) {
        this.year = year;
        sb.append(year);
    }

    @Override
    protected String call() throws Exception {
        GetPageNum test = new GetPageNum(year, this);
        totaled = test.CalCulate();
        int result = test.GetTotalmoiveNum(Integer.parseInt(totaled));

        logger.info(year+"년도 전체 영화 갯수 : "+result);

        int testnum = 4;
        int[] value = split(totaled, testnum);

        for(int i=0;i<testnum;i++){
            Runnable test2 = new CalCulateModel(year,value[i],value[i+1]);
            Thread test1 = new Thread(test2);
            threadlist.add(test1);
            test1.start();
        }

        logger.info("Thread 총 갯수 : " + threadlist.size());

        for(int i=0;i<threadlist.size();i++){
            threadlist.get(i).join();
        }
        threadlist.clear();
        return null;
    }
    protected void update(String value){
        updateMessage(value);
    }

    int[] split(String totaled, int multithrednum){
        int[] result = new int[multithrednum+1];
        int i = Integer.parseInt(totaled);
        int share = i/multithrednum;
        int rest = i%multithrednum;
        result[0] = 0;


        for(int k=0;k<rest;k++){
            result[k+1]++;
        }

        for(int j=0;j <= multithrednum-1; j++){
            result[j+1] += share;
            if(j+2<=multithrednum){
                result[j+2] += result[j+1];
            }
        }
        return result;
    }

}
