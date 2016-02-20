package sample.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ParkCheolHo on 2016-02-20.
 * 해당년도의 전체 페이지를 구하는 클래스
 */
public class GetPageNum {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    GetPageInfo getpagenum;
    public GetPageNum(String year, GetPageInfo getpagenum){
        this.year = year;
        sb.append(year);
        this.getpagenum = getpagenum;
    }
    public String CalCulate() {
        String url = sb.toString() + "&page=10000";
        String[] text = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements newsHeadlines = doc.select("div.pagenavigation");
            text = newsHeadlines.text().split(" ");
            getpagenum.update(text[text.length-1]);
        } catch (IOException e) {

        }
        return text[text.length-1];
    }


}
