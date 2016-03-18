package sample.model;

import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2015-08-16
 * 모바일 페이지가 아닌 일반페이지에서 크롤링 할때 필요한 클래스
 */
public class InformationReader {
    private ArrayList<String> grene = new ArrayList<String>();
    private String countrycode = new String();


    public void reader(String string){
        try{
            int code = Integer.parseInt(string);

            switch(code){
                case 1:
                    grene.add("드라마");
                    break;
                case 2:
                    grene.add("판타리");
                    break;
                case 3:
                    grene.add("서부");
                    break;
                case 4:
                    grene.add("공포");
                    break;
                case 5:
                    grene.add("로맨스");
                    break;
                case 6:
                    grene.add("모험");
                    break;
                case 7:
                    grene.add("스릴러");
                    break;
                case 8:
                    grene.add("느와르");
                    break;
                case 9:
                    grene.add("컬트");
                    break;
                case 10:
                    grene.add("다큐멘터리");
                    break;
                case 11:
                    grene.add("코미디");
                    break;
                case 12:
                    grene.add("가족");
                    break;
                case 13:
                    grene.add("미스터리");
                    break;
                case 14:
                    grene.add("전쟁");
                    break;
                case 15:
                    grene.add("애니메이션");
                    break;
                case 16:
                    grene.add("범죄");
                    break;
                case 17:
                    grene.add("뮤지컬");
                    break;
                case 18:
                    grene.add("SF");
                    break;
                case 19:
                    grene.add("액션");
                    break;
                case 20:
                    grene.add("무협");
                    break;
                case 21:
                    grene.add("에로");
                    break;
                case 22:
                    grene.add("서스펜스");
                    break;
                case 23:
                    grene.add("서사");
                    break;
                case 24:
                    grene.add("블랙코미디");
                    break;
                case 25:
                    grene.add("실험");
                    break;
                case 26:
                    grene.add("영화카툰");
                    break;
                case 27:
                    grene.add("영화음악");
                    break;
                case 28:
                    grene.add("영화패러디포스터");
                    break;
            }
        }
        catch(Exception e){
            switch(string){
                case "KR" :
                    countrycode = ("한국");
                    break;
                case "JP" :
                    countrycode = ("일본");
                    break;
                case "US" :
                    countrycode = ("미국");
                    break;
                case "HK" :
                    countrycode = ("홍콩");
                    break;
                case "GB" :
                    countrycode = ("영국");
                    break;
                case "FR" :
                    countrycode = ("프랑스 ");
                    break;
                case "ETC" :
                    countrycode = ("기타");
                    break;

            }
        }
    }
    public ArrayList<String> getgreneList(){
        return grene;
    }
    public String countrycode(){
        return countrycode;
    }
    public void eraseList(){
        countrycode = null;
        grene.clear();
    }
}
