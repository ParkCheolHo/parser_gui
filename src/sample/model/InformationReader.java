package sample.model;

import java.util.ArrayList;

/**
 * Created by ParkCheolHo on 2015-08-16
 * 모바일 페이지가 아닌 일반페이지에서 크롤링 할때 필요한 클래스
 */
class InformationReader {
    //    private ArrayList<String> genre = new ArrayList<String>();
    private int countrycode = 0;
    private ArrayList<Integer> genre = new ArrayList<Integer>();
    private ArrayList<String> genre_name = new ArrayList<>();
    private int gradecode = 0;
    private String open_date = null;
    private String running_time = null;

    public void reader(String val) {
        if (val.length() > 4) {
            open_date = val;
        } else {
            try {
                int code = Integer.parseInt(val);
//            switch(code){
//                case 1:
//                    genre.add("드라마");
//                    break;
//                case 2:
//                    genre.add("판타지");
//                    break;
//                case 3:
//                    genre.add("서부");
//                    break;
//                case 4:
//                    genre.add("공포");
//                    break;
//                case 5:
//                    genre.add("로맨스");
//                    break;
//                case 6:
//                    genre.add("모험");
//                    break;
//                case 7:
//                    genre.add("스릴러");
//                    break;
//                case 8:
//                    genre.add("느와르");
//                    break;
//                case 9:
//                    genre.add("컬트");
//                    break;
//                case 10:
//                    genre.add("다큐멘터리");
//                    break;
//                case 11:
//                    genre.add("코미디");
//                    break;
//                case 12:
//                    genre.add("가족");
//                    break;
//                case 13:
//                    genre.add("미스터리");
//                    break;
//                case 14:
//                    genre.add("전쟁");
//                    break;
//                case 15:
//                    genre.add("애니메이션");
//                    break;
//                case 16:
//                    genre.add("범죄");
//                    break;
//                case 17:
//                    genre.add("뮤지컬");
//                    break;
//                case 18:
//                    genre.add("SF");
//                    break;
//                case 19:
//                    genre.add("액션");
//                    break;
//                case 20:
//                    genre.add("무협");
//                    break;
//                case 21:
//                    genre.add("에로");
//                    break;
//                case 22:
//                    genre.add("서스펜스");
//                    break;
//                case 23:
//                    genre.add("서사");
//                    break;
//                case 24:
//                    genre.add("블랙코미디");
//                    break;
//                case 25:
//                    genre.add("실험");
//                    break;
//                case 26:
//                    genre.add("영화카툰");
//                    break;
//                case 27:
//                    genre.add("영화음악");
//                    break;
//                case 28:
//                    genre.add("영화패러디포스터");
//                    break;
//            }
                switch (code) {
                    case 1:
                        genre.add(1);
                        genre_name.add("드라마");
                        break;
                    case 2:
                        genre.add(2);
                        genre_name.add("판타지");
                        break;
                    case 3:
                        genre.add(3);
                        genre_name.add("서부");
                        break;
                    case 4:
                        genre.add(4);
                        genre_name.add("공포");
                        break;
                    case 5:
                        genre.add(5);
                        genre_name.add("로맨스");
                        break;
                    case 6:
                        genre.add(6);
                        genre_name.add("모험");
                        break;
                    case 7:
                        genre.add(7);
                        genre_name.add("스릴러");
                        break;
                    case 8:
                        genre.add(8);
                        genre_name.add("느와르");
                        break;
                    case 9:
                        genre.add(9);
                        genre_name.add("컬트");
                        break;
                    case 10:
                        genre.add(10);
                        genre_name.add("다큐멘터리");
                        break;
                    case 11:
                        genre.add(11);
                        genre_name.add("코미디");
                        break;
                    case 12:
                        genre.add(12);
                        genre_name.add("가족");
                        break;
                    case 13:
                        genre.add(13);
                        genre_name.add("미스터리");
                        break;
                    case 14:
                        genre.add(14);
                        genre_name.add("전쟁");
                        break;
                    case 15:
                        genre.add(15);
                        genre_name.add("애니메이션");
                        break;
                    case 16:
                        genre.add(16);
                        genre_name.add("범죄");
                        break;
                    case 17:
                        genre.add(17);
                        genre_name.add("뮤지컬");
                        break;
                    case 18:
                        genre.add(18);
                        genre_name.add("SF");
                        break;
                    case 19:
                        genre.add(19);
                        genre_name.add("액션");
                        break;
                    case 20:
                        genre.add(20);
                        genre_name.add("무협");
                        break;
                    case 21:
                        genre.add(21);
                        genre_name.add("에로");
                        break;
                    case 22:
                        genre.add(22);
                        genre_name.add("서스펜스");
                        break;
                    case 23:
                        genre.add(23);
                        genre_name.add("서사");
                        break;
                    case 24:
                        genre.add(24);
                        genre_name.add("블랙코미디");
                        break;
                    case 25:
                        genre.add(25);
                        genre_name.add("실험");
                        break;
                    case 26:
                        genre.add(26);
                        genre_name.add("영화카툰");
                        break;
                    case 27:
                        genre.add(27);
                        genre_name.add("영화음악");
                        break;
                    case 28:
                        genre.add(28);
                        genre_name.add("영화패러디포스터");
                        break;
                }
            } catch (Exception e) {

                switch (val) {
                    case "KR":
//                    countrycode = ("한국");
                        countrycode = 1;
                        break;
                    case "JP":
//                    countrycode = ("일본");
                        countrycode = 2;
                        break;
                    case "US":
//                    countrycode = ("미국");
                        countrycode = 3;
                        break;
                    case "HK":
//                    countrycode = ("홍콩");
                        countrycode = 4;
                        break;
                    case "GB":
//                    countrycode = ("영국");
                        countrycode = 5;
                        break;
                    case "FR":
//                    countrycode = ("프랑스 ");
                        countrycode = 6;
                        break;
                    case "ETC":
//                    countrycode = ("기타");
                        countrycode = 7;
                        break;
                }
            }
        }
    }

    //    public ArrayList<String> getgreneList(){
//        return genre;
//    }
    public int[] getgreneList() {
        int[] result = new int[genre.size()];
        int count = 0;
        for (int val : genre) {
            result[count] = val;
            count++;
        }


        return result;
    }

    public ArrayList<Integer> getgrene() {
        return genre;
    }

    public int countrycode() {
        return countrycode;
    }

    public void eraseList() {
        countrycode = 0;
        gradecode = 0;
        genre.clear();
        genre_name.clear();
        running_time = null;
        open_date = null;
    }
    public int getGenreSize(){
      return genre.size();
    }
    public int getGradecode() {
        return gradecode;
    }

    public void setGradecode(String val) {

        switch (val) {
            case "":
                this.gradecode = 0;
                break;
            case "전체 관람가":
                this.gradecode = 1;
                break;
            case "12세 관람가":
                this.gradecode = 2;
                break;
            case "15세 관람가":
                this.gradecode = 3;
                break;
            case "청소년 관람불가":
                this.gradecode = 4;
                break;
            case "G":
                this.gradecode = 5;
                break;
            case "PG":
                this.gradecode = 6;
                break;
            case "PG-13":
                this.gradecode = 7;
                break;
            case "R":
                this.gradecode = 8;
                break;
            case "NC-17":
                this.gradecode = 9;
                break;
            case "X[NC-17]":
                this.gradecode = 10;
                break;
            case "NR":
                this.gradecode = 11;
                break;
        }

    }

    public String getOpen_date() {
        return open_date;
    }

    public void setOpen_date(String open_date) {
        this.open_date = open_date;
    }

    public String getRunning_time() {
        return running_time;
    }

    public void setRunning_time(String running_time) {
        this.running_time = running_time;
    }

    //xml write할때 쓰는 메소드
    public ArrayList<String> getGenre_name() {
        return genre_name;
    }

    public void setGenre_name(ArrayList<String> genre_name) {
        this.genre_name = genre_name;
    }
}
