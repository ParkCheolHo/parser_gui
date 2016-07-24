package sample.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by WhiteNight on 2016-03-28.
 */
public class MySql implements WriteFile{

    private Connection conn = null;
    private ArrayList<String> databaseList;
    private String hostname;
    private String name;
    private String id;
    private String password;
    public MySql(String hostname, String dbname, String id, String password){
        databaseList = new ArrayList<String>();
        this.hostname = hostname;
        this.name = dbname;
        this.id = id;
        this.password = password;
    }
    @Override
    public int start(boolean flag){
        try {
            databaseList.clear();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(hostname+ name, id, password);
            System.out.println("연결 성공");
            if(flag) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW DATABASES");
                while (rs.next()) {
                    String str = rs.getNString(1);
                    databaseList.add(str);
                }
                return 5;
            }
            return 5;
        }catch (SQLException e) {
            //여기에 마우스 돌아가는 모양으로 바꾸기
            switch(e.getErrorCode()){
                case  0:
                    SystemInfo.logger.info("url 이상으로 인한 연결 실패");
                    return e.getErrorCode();
                case 1045 :
                    SystemInfo.logger.info("아이디나 비밀번호 틀림");
                    return e.getErrorCode();
                default:
                    e.printStackTrace();
                    return e.getErrorCode();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    @Override
    public void add(String index, String name, String engname, int country,
                                         String storyname, String story, InformationReader reader, ArrayList<Actor> actors, ArrayList<String> title, int year) throws SQLException {
        String  sql = "INSERT INTO movies VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int open_year = Integer.parseInt(reader.getOpen_date().substring(0,4));
        int open_month = Integer.parseInt(reader.getOpen_date().substring(4,6));
        int open_day = Integer.parseInt(reader.getOpen_date().substring(6,8));
//        System.out.println(reader.getOpen_date() +" : " + open_year +" : " + open_month +" : " + open_day);
            Calendar cal = new GregorianCalendar(open_year,open_month,open_day);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(index));
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,engname);
            preparedStatement.setInt(4, country);
            preparedStatement.setString(5,storyname);
            preparedStatement.setString(6,story);
            preparedStatement.setInt(7,year);
            preparedStatement.setInt(8,reader.getGrade());
            preparedStatement.setString(9 ,reader.getRunning_time());
            preparedStatement.setDate(10,new Date(cal.getTimeInMillis()));
            preparedStatement.execute();

        for(int i=0; i<actors.size(); i++){
            PreparedStatement astmt = conn.prepareStatement("INSERT INTO actors (code, name, has_picture) SELECT ?,?,? FROM DUAL WHERE NOT EXISTS  (SELECT code FROM actors WHERE code = ? )");
            astmt.setInt(1, actors.get(i).index);
            astmt.setString(2, actors.get(i).name);
            if(actors.get(i).img != null)
                astmt.setInt(3, 1);
            else
                astmt.setInt(3, 0);
            astmt.setInt(4, actors.get(i).index);
            astmt.execute();
        }
        for(int value : reader.GetGenreList()){
            String  relSql = "INSERT INTO relrationship_genre(movie_index, genre_index) VALUES (?, ?)";
            PreparedStatement serstmt = conn.prepareStatement(relSql);
            serstmt.setInt(1, Integer.parseInt(index));
            serstmt.setInt(2, value);
            serstmt.execute();
        }
        for(int i=0; i<actors.size(); i++){
            String  bSql = "INSERT INTO relrationship_actor(movie_index, actors_index, option_index) VALUES (?,?,?)";
            PreparedStatement bstmt = conn.prepareStatement(bSql);
            bstmt.setInt(1,Integer.parseInt(index));
            bstmt.setInt(2, actors.get(i).index);
            switch(title.get(i)){
                case "감독" :
                    bstmt.setInt(3, 1);
                    break;
                case "주연" :
                    bstmt.setInt(3, 2);
                    break;
                case "조연" :
                    bstmt.setInt(3, 3);
                    break;
                default:
                    bstmt.setInt(3, 0);
                    break;
            }
            bstmt.execute();
        }
    }
    @Override
    public void end() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String> getDatabaseList() {
        return databaseList;
    }
//    public void SetTable(){
//        String sql ="create table movie";
//    }
//    public void setSqlresult(ArrayList<String> sqlresult) {
//        this.sqlresult = sqlresult;
//    }
//
//    public boolean isTestflag() {
//        return testflag;
//    }
}
