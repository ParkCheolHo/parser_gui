package sample.model;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by ParkCheolHo on 2016-02-20.
 * 해당년도의 전체 페이지를 구하는 클래스
 */
class GetPageNum {
    private StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?open=");
    GetPageNum(String year) {
        sb.append(year);
    }
    //해당 년도의 디렉토리의 마지막 페이지를 구하는 메소드
    String Calculate() {
        String url = sb.toString() + "&page=10000";
        String[] text = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements newsHeadlines = doc.select("div.pagenavigation");
            text = newsHeadlines.text().split(" ");
        } catch (IOException e) {
            SystemInfo.logger.info("error during get total page number");
            Calculate();
        }
        return text != null ? text[text.length - 1] : null;
    }
    //해당 년도의 총 영화의 갯수를 더하는 메소드
    int GetTotalMovieNumb(int page) throws IOException {
        int k = 0;
        Document doc = Jsoup.connect(sb.toString() + "&page=" + page).timeout(0).get();
        Elements newsHeadlines = doc.select("ul.directory_list > li");
        for (Element li : newsHeadlines) {
//            Elements iterElem = li.getElementsByTag("a");
            //System.out.println(iterElem.attr("href"));
            k++;
        }
        return (page-1)*20 + k;
    }
}