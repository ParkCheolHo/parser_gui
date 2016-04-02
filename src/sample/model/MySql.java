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
    String id, password, hostname, dbname;
    boolean testflag = false;
    ArrayList<String> sqlresult;
    public MySql(){
        sqlresult  = new ArrayList<String>();
    }

    public int Connection(){
        try {
            sqlresult.clear();
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(hostname+dbname, id, password);
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
    public void SetTable(){
        String sql ="create table movie";
    }
    public ArrayList<String> getSqlresult() {
        return sqlresult;
    }

    public void setSqlresult(ArrayList<String> sqlresult) {
        this.sqlresult = sqlresult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public boolean isTestflag() {
        return testflag;
    }

    public void setTestflag(boolean testflag) {
        this.testflag = testflag;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
