package sample.model;

import javafx.concurrent.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    SystemInfo systemInfo;
    public static Logger logger = LoggerFactory.getLogger(GetPageInfo.class);
    public GetPageInfo(String year) {
        this.year = year;
        sb.append(year);
        this.systemInfo = SystemInfo.getInstance();
    }

    @Override
    protected String call() throws XMLStreamException {
        GetPageNum test = new GetPageNum(year, this);
        MakeXml makexml = new MakeXml(systemInfo.getFilePath());
        totaled = test.CalCulate();
        int result = 0;
        try {
            result = test.GetTotalmoiveNum(Integer.parseInt(totaled));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info(year+"년도 전체 영화 갯수 : "+result);
        makexml.Start();
        int testnum = 4;
        int[] value = split(totaled, testnum);
        for(int i=0;i<testnum;i++){
            Thread test1 = new Thread( new CalCulateModel(year,value[i],value[i+1], this, makexml));
            systemInfo.addTheadlist(test1);
        }
        systemInfo.startThreadlist();
        threadlist = systemInfo.getTheadlist();
        logger.info("Thread 총 갯수 : " + threadlist.size());
        for(Thread i : threadlist)
            try {
                i.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        makexml.End();
        System.out.println("쓰레드 죽는다");
        threadlist.clear();
        return null;
    }

    protected synchronized void updateProgress(){
        count++;
        updateProgress(count,max);
    }
    protected void setMax(double value){
        max += value;
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
