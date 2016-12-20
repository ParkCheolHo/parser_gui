package sample.model;

import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2015-08-16
 *
 */
class InformationParser {
    //    private ArrayList<String> genre = new ArrayList<String>();
    private int country = 0;
    private ArrayList<Integer> genre = new ArrayList<Integer>();
    private ArrayList<String> genre_String = new ArrayList<>();
    private String open_date = null;
    private String running_time = null;

    void read(String val) {
        if (val.length() > 4) {
            open_date = val;
        } else {
            try {
                int code = Integer.parseInt(val);
                switch (code) {
                    case 1:
                        genre.add(1);
                        genre_String.add("드라마");
                        break;
                    case 2:
                        genre.add(2);
                        genre_String.add("판타지");
                        break;
                    case 3:
                        genre.add(3);
                        genre_String.add("서부");
                        break;
                    case 4:
                        genre.add(4);
                        genre_String.add("공포");
                        break;
                    case 5:
                        genre.add(5);
                        genre_String.add("로맨스");
                        break;
                    case 6:
                        genre.add(6);
                        genre_String.add("모험");
                        break;
                    case 7:
                        genre.add(7);
                        genre_String.add("스릴러");
                        break;
                    case 8:
                        genre.add(8);
                        genre_String.add("느와르");
                        break;
                    case 9:
                        genre.add(9);
                        genre_String.add("컬트");
                        break;
                    case 10:
                        genre.add(10);
                        genre_String.add("다큐멘터리");
                        break;
                    case 11:
                        genre.add(11);
                        genre_String.add("코미디");
                        break;
                    case 12:
                        genre.add(12);
                        genre_String.add("가족");
                        break;
                    case 13:
                        genre.add(13);
                        genre_String.add("미스터리");
                        break;
                    case 14:
                        genre.add(14);
                        genre_String.add("전쟁");
                        break;
                    case 15:
                        genre.add(15);
                        genre_String.add("애니메이션");
                        break;
                    case 16:
                        genre.add(16);
                        genre_String.add("범죄");
                        break;
                    case 17:
                        genre.add(17);
                        genre_String.add("뮤지컬");
                        break;
                    case 18:
                        genre.add(18);
                        genre_String.add("SF");
                        break;
                    case 19:
                        genre.add(19);
                        genre_String.add("액션");
                        break;
                    case 20:
                        genre.add(20);
                        genre_String.add("무협");
                        break;
                    case 21:
                        genre.add(21);
                        genre_String.add("에로");
                        break;
                    case 22:
                        genre.add(22);
                        genre_String.add("서스펜스");
                        break;
                    case 23:
                        genre.add(23);
                        genre_String.add("서사");
                        break;
                    case 24:
                        genre.add(24);
                        genre_String.add("블랙코미디");
                        break;
                    case 25:
                        genre.add(25);
                        genre_String.add("실험");
                        break;
                    case 26:
                        genre.add(26);
                        genre_String.add("영화카툰");
                        break;
                    case 27:
                        genre.add(27);
                        genre_String.add("영화음악");
                        break;
                    case 28:
                        genre.add(28);
                        genre_String.add("영화패러디포스터");
                        break;
                }
            } catch (Exception e) {
                switch (val) {
                    case "KR":
//                    country = ("한국");
                        country = 1;
                        break;
                    case "JP":
//                    country = ("일본");
                        country = 2;
                        break;
                    case "US":
//                    country = ("미국");
                        country = 3;
                        break;
                    case "HK":
//                    country = ("홍콩");
                        country = 4;
                        break;
                    case "GB":
//                    country = ("영국");
                        country = 5;
                        break;
                    case "FR":
//                    country = ("프랑스 ");
                        country = 6;
                        break;
                    case "ETC":
//                    country = ("기타");
                        country = 7;
                        break;
                }
            }
        }
    }
    int[] GetGenreList() {
        int[] result = new int[genre.size()];
        int count = 0;
        for (int val : genre) {
            result[count] = val;
            count++;
        }
        return result;
    }
    int getCountry() {
        return country;
    }
    void eraseList() {
        country = 0;
        genre.clear();
        genre_String.clear();
        running_time = null;
        open_date = null;
    }
    int getGrade(String val) {
        switch (val) {
            case "":
                return 0;
            case "전체 관람가":
                return 1;
            case "12세 관람가":
                return 2;
            case "15세 관람가":
                return 3;
            case "청소년 관람불가":
                return 4;
            case "G":
                return 5;
            case "PG":
                return 6;
            case "PG-13":
                return  7;
            case "R":
                return 8;
            case "NC-17":
                return 9;
            case "X[NC-17]":
                return 10;
            case "NR":
                return 11;
        }
        return 0;
    }
    String getOpen_date() {
        return open_date;
    }
    String getRunning_time() {
        return running_time;
    }
    void setRunning_time(String running_time) {
        this.running_time = running_time;
    }
//    public int getGenreSize(){
//        return genre.size();
//    }
//    public ArrayList<String> getGenre_name() {
//        return genre_String;
//    }
//    public void setGenre_name(ArrayList<String> genre_String) {
//        this.genre_String = genre_String;
//    }
//    public void setOpen_date(String open_date) {
//        this.open_date = open_date;
//    }
}
