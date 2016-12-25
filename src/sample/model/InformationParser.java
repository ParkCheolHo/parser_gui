package sample.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ParkCheolHo on 2015-08-16
 */
class  InformationParser  {

    private HashMap<Integer, String> genre = new HashMap<>();
    private String[] country = null;
    private String open_date = null;
    private String running_time = null;
    private boolean dbFlag;

    public InformationParser(boolean dbFlag){
        this.dbFlag = dbFlag;
    }

    void read(String val) {
        if (val.length() > 4) {
            open_date = val;
        } else {
            try {
                int code = Integer.parseInt(val);
                switch (code) {
                    case 1:
                        genre.put(1, "드라마");
                        break;
                    case 2:
                        genre.put(2, "판타지");
                        break;
                    case 3:
                        genre.put(3, "서부");
                        break;
                    case 4:
                        genre.put(4, "공포");
                        break;
                    case 5:
                        genre.put(5, "멜로/로맨스");
                        break;
                    case 6:
                        genre.put(6, "모험");
                        break;
                    case 7:
                        genre.put(7, "스릴러");
                        break;
                    case 8:
                        genre.put(8, "느와르");
                        break;
                    case 9:
                        genre.put(9, "컬트");
                        break;
                    case 10:
                        genre.put(10, "다큐멘터리");
                        break;
                    case 11:
                        genre.put(11, "코미디");
                        break;
                    case 12:
                        genre.put(12, "가족");
                        break;
                    case 13:
                        genre.put(13, "미스터리");
                        break;
                    case 14:
                        genre.put(14, "전쟁");
                        break;
                    case 15:
                        genre.put(15, "애니메이션");
                        break;
                    case 16:
                        genre.put(16, "범죄");
                        break;
                    case 17:
                        genre.put(17, "뮤지컬");
                        break;
                    case 18:
                        genre.put(18, "SF");
                        break;
                    case 19:
                        genre.put(19, "액션");
                        break;
                    case 20:
                        genre.put(20, "무협");
                        break;
                    case 21:
                        genre.put(21, "에로");
                        break;
                    case 22:
                        genre.put(22, "서스펜스");
                        break;
                    case 23:
                        genre.put(23, "서사");
                        break;
                    case 24:
                        genre.put(24, "블랙코미디");
                        break;
                    case 25:
                        genre.put(25, "실험");
                        break;
                    case 26:
                        genre.put(26, "영화카툰");
                        break;
                    case 27:
                        genre.put(27, "영화음악");
                        break;
                    case 28:
                        genre.put(28, "영화패러디포스터");
                        break;
                }
            } catch (Exception e) {
                switch (val) {
                    case "KR":
                        country = new String[]{"1", "한국"};
                        break;
                    case "JP":
                        country = new String[]{"2", "일본"};
                        break;
                    case "US":
                        country = new String[]{"3", "미국"};
                        break;
                    case "HK":
                        country = new String[]{"4", "홍콩"};
                        break;
                    case "GB":
                        country = new String[]{"5", "영국"};
                        break;
                    case "FR":
                        country = new String[]{"6", "프랑스"};
                        break;
                    case "ETC":
                        country = new String[]{"7", "기타"};
                        break;
                    default :
                        country = new String[]{"0", "none"};
                        break;
                }
            }
        }
    }


    HashMap<Integer, String> getGenreList(){
        return genre;
    }
    String[] getCountry() {
        if(country != null)
            return country;
        else{
            return new String[]{"0", "none"};
        }
    }

    void clearAll() {
        genre.clear();
        running_time = null;
        open_date = null;
    }

    String[] getGrade(String val) {
        switch (val) {
            case "":
                return new String[]{"0", "0"};
            case "전체 관람가":
                return new String[]{"1", val};
            case "12세 관람가":
                return new String[]{"2", val};
            case "15세 관람가":
                return new String[]{"3", val};
            case "청소년 관람불가":
                return new String[]{"4", val};
            case "G":
                return new String[]{"5", val};
            case "PG":
                return new String[]{"6", val};
            case "PG-13":
                return new String[]{"7", val};
            case "R":
                return new String[]{"8", val};
            case "NC-17":
                return new String[]{"9", val};
            case "X[NC-17]":
                return new String[]{"10", val};
            case "NR":
                return new String[]{"11", val};
            default:
                return new String[]{"0","0"};

        }
    }

    String getOpen_date() {
        return open_date;
    }

    String getRunning_time() {
        return running_time;
    }

    void setRunning_time(String running_time) {
        this.running_time = (running_time != null) ? running_time : null;
    }

}
