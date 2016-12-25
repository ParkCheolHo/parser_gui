package sample.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by WhiteNight on 2016-03-28.
 */
public class SqlWriter implements FileWriter {

    private Connection conn = null;
    private String hostname;
    private String db;
    private String id;
    private String password;
    public SqlWriter(String hostname, String db, String id, String password){
        this.hostname = hostname;
        this.db = db;
        this.id = id;
        this.password = password;
    }
    @Override
    public int start(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(hostname+ db, id, password);
            System.out.println("연결 성공");
            return 5;
        }catch (SQLException e) {
            return managementException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //TODO : 세팅창에서 에러난다. 확인 할것
    @Override
    public Object startup() {
        start();
        ArrayList<String> queryResult = new ArrayList<>();
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet rs = st.executeQuery("SHOW DATABASES");
            while (rs.next()) {
                String str = rs.getNString(1);
                queryResult.add(str);
            }
            return queryResult;
        } catch (SQLException e) {
            return managementException(e);
        }
    }

    @Override
    public void add(Movie movie) throws SQLException {
        String  sql = "INSERT INTO movies VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int open_year = Integer.parseInt(movie.getOpeningDate().substring(0,4));
        int open_month = Integer.parseInt(movie.getOpeningDate().substring(4,6));
        int open_day = Integer.parseInt(movie.getOpeningDate().substring(6,8));
//        System.out.println(read.getOpen_date() +" : " + open_year +" : " + open_month +" : " + open_day);
            Calendar cal = new GregorianCalendar(open_year,open_month,open_day);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(movie.getMovieIndex()));
            preparedStatement.setString(2,movie.getTitle());
            preparedStatement.setString(3,movie.getEngTitle());
            preparedStatement.setInt(4, Integer.parseInt(movie.getCountry()[0]));
            preparedStatement.setString(5,movie.getSummaryTitle());
            preparedStatement.setString(6,movie.getSummary());
            preparedStatement.setInt(7,open_year);
            preparedStatement.setString(9 ,movie.getRunningTime());
            preparedStatement.setDate(10,new Date(cal.getTimeInMillis()));
            preparedStatement.execute();

        for(int i=0; i<movie.getActors().size(); i++){
            PreparedStatement actorPreparedStatement = conn.prepareStatement("INSERT INTO actors (code, name, has_picture) SELECT ?,?,? FROM DUAL WHERE NOT EXISTS  (SELECT code FROM actors WHERE code = ? )");
            actorPreparedStatement.setInt(1, movie.getActors().get(i).getIndex());
            actorPreparedStatement.setString(2, movie.getActors().get(i).getName());
            if(movie.getActors().get(i).getImg() != null)
                actorPreparedStatement.setInt(3, 1);
            else
                actorPreparedStatement.setInt(3, 0);
            actorPreparedStatement.setInt(4, movie.getActors().get(i).getIndex());
            actorPreparedStatement.execute();
        }
        for(Integer value : movie.getGenre().keySet()){
            String  relationshipSql = "INSERT INTO relrationship_genre(movie_index, genre_index) VALUES (?, ?)";
            PreparedStatement relationshipRearedStatement = conn.prepareStatement(relationshipSql);
            relationshipRearedStatement.setInt(1, Integer.parseInt(movie.getMovieIndex()));
            relationshipRearedStatement.setInt(2, value);
            relationshipRearedStatement.execute();
        }
        for(int i=0; i<movie.getActors().size(); i++){
            Actor actor = movie.getActors().get(i);
            String  actorRuleSql = "INSERT INTO relrationship_actor(movie_index, actors_index, option_index) VALUES (?,?,?)";
            PreparedStatement actorRuleSqlStatement = conn.prepareStatement(actorRuleSql);
            actorRuleSqlStatement.setInt(1,Integer.parseInt(movie.getMovieIndex()));
            actorRuleSqlStatement.setInt(2, actor.getIndex());
            switch(actor.getRule()){
                case "감독" :
                    actorRuleSqlStatement.setInt(3, 1);
                    break;
                case "주연" :
                    actorRuleSqlStatement.setInt(3, 2);
                    break;
                case "조연" :
                    actorRuleSqlStatement.setInt(3, 3);
                    break;
                default:
                    actorRuleSqlStatement.setInt(3, 0);
                    break;
            }
            actorRuleSqlStatement.execute();
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
    private int managementException(SQLException e){
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
    }

}
