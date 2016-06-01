package sample.model;

import javafx.scene.Cursor;
import javafx.scene.Scene;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by WhiteNight on 2016-03-28.
 */
public class MySql {

    Connection conn;
    boolean testflag = false;
    ArrayList<String> sqlresult;
    String hostname ;
    String dbname ;
    String id;
    String password;
    public MySql(String hostname, String dbname, String id, String password){
        sqlresult  = new ArrayList<String>();
        this.hostname = hostname;
        this.dbname = dbname;
        this.id = id;
        this.password = password;
    }

    public int Connection(){
        try {
            sqlresult.clear();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(hostname+dbname, id, password);
            System.out.println("연결 성공");
            if(testflag) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SHOW DATABASES");
                while (rs.next()) {
                    String str = rs.getNString(1);
                    sqlresult.add(str);
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

    public  void insertMovie(String index, String name, String engname, int contry_id,
                                         String storyname, String story, int grade,int[] genre, ArrayList<Actor> actors, ArrayList<String> title, int year) throws SQLException {

        String  sql = "INSERT INTO movies VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(index));
            preparedStatement.setString(2,name);
            preparedStatement.setString(3,engname);
            preparedStatement.setInt(4, contry_id);
            preparedStatement.setString(5,storyname);
            preparedStatement.setString(6,story);
            preparedStatement.setInt(7,year);
            preparedStatement.setInt(8,grade);
            preparedStatement.execute();

        for(int value : genre){
            String  relSql = "INSERT INTO relrationship_genre VALUES (?, ?)";
            PreparedStatement serstmt = conn.prepareStatement(relSql);
            serstmt.setInt(1, Integer.parseInt(index));
            serstmt.setInt(2, value);
            serstmt.execute();
        }


        for(int i=0; i<actors.size(); i++){

            PreparedStatement astmt = conn.prepareStatement("INSERT INTO actors (code, name) SELECT ?,? FROM DUAL WHERE NOT EXISTS  (SELECT code FROM actors WHERE code = ? )");
            astmt.setInt(1, actors.get(i).index);
            astmt.setString(2, actors.get(i).name);
            astmt.setInt(3 , actors.get(i).index);
            astmt.execute();
        }

        for(int i=0; i<actors.size(); i++){
            String  bSql = "INSERT INTO relrationship_actor VALUES (?,?,?)";
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


    public void SetTable(){
        String sql ="create table movie";
    }
    public ArrayList<String> getSqlresult() {
        return sqlresult;
    }

    public void setSqlresult(ArrayList<String> sqlresult) {
        this.sqlresult = sqlresult;
    }

    public boolean isTestflag() {
        return testflag;
    }

    public void setTestflag(boolean testflag) {
        this.testflag = testflag;
    }

}
