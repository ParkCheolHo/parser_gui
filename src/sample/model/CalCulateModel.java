package sample.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ParkCheolHo on 2016-02-20.
 */
public class CalCulateModel {
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?year=");
    String year;
    public CalCulateModel(String year){
        this.year=year;
    }
    public int execute(String num) throws IOException {
        int k = 0;
        for(int i=1;i<=Integer.parseInt(num);i++){
            Document doc = Jsoup.connect(sb.toString() +"&page="+String.valueOf(i)).get();
            Elements newsHeadlines = doc.select("ul.directory_list > li");
            for(Element li : newsHeadlines){
                Elements iterElem = li.getElementsByTag("a");
                System.out.println(iterElem.attr("href"));
                k++;
            }
        }
        return k;
    }
}
