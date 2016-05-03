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
    StringBuilder sb = new StringBuilder("http://movie.naver.com/movie/sdb/browsing/bmovie.nhn?open=");
    String year;
    GetPageInfo getPageInfo;

    public GetPageNum(String year, GetPageInfo getPageInfo) {
        this.year = year;
        sb.append(year);
        this.getPageInfo = getPageInfo;
    }

    public String Calculate() {
        String url = sb.toString() + "&page=10000";
        String[] text = null;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements newsHeadlines = doc.select("div.pagenavigation");
            text = newsHeadlines.text().split(" ");
        } catch (IOException e) {

        }
        return text[text.length - 1];
    }

    public int GetTotalMotiveNumb(int page) throws IOException {
        int k = 0;

        Document doc = Jsoup.connect(sb.toString() + "&page=" + page).timeout(0).get();
        Elements newsHeadlines = doc.select("ul.directory_list > li");
        for (Element li : newsHeadlines) {
            Elements iterElem = li.getElementsByTag("a");
            //System.out.println(iterElem.attr("href"));
            k++;
        }
        int result = (page-1)*20 + k;
        return result;
    }
}