package sample.model;

import javafx.concurrent.Task;

import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2016-02-19.
 * 모든 쓰레드를 컨트롤 하는 쓰레드
 */
public class GetPageInfo extends Task {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    String totaled;
    ArrayList<String> moives;
    int moviecount = 0;
    public GetPageInfo(String year) {
        this.year = year;
        sb.append(year);
    }



    @Override
    protected String call() throws Exception {
        GetPageNum test = new GetPageNum(year, this);
        totaled = test.CalCulate();
        int[] value = split(totaled, 3);

        return null;
    }
    protected void update(String value){
        updateMessage(value);
    }

    int[] split(String totaled, int multithrednum){
        int[] result = new int[multithrednum];
        int i = Integer.parseInt(totaled);
        int share = i/multithrednum;
        int rest = i%multithrednum;

        for(int j=0;j <= multithrednum-1; j++){
            result[j] = share;
        }
        for(int k=0;k<rest;k++){
            result[k]++;
        }
        return result;
    }
    protected void addMovies(String value){
        synchronized (this) {
            moives.add(value);
            moviecount++;
        }
    }
}
