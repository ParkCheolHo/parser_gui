package sample.model;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ParkCheolHo on 2016-03-05.
 * 프로그램 전체에 사용되는 정보를 저장하는 싱글톤 클래스
 */
public class SystemInfo {
    private static SystemInfo ourInstance = new SystemInfo();

    public static SystemInfo getInstance() {
        return ourInstance;
    }
    private String year = null;
    private List<Thread> theadlist = new ArrayList<>();
    private File filepath = null;
    @FXML
    private ScrollPane pane;
    private String log;


    private SystemInfo() {
    }
    public void removetLog() {
        this.log = null;
    }

    public void addLog(String value) {
        if(this.log==null)
            this.log = value;
        else {
            this.log += "\n";
            this.log += value;
        }
        if(pane!=null){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pane.setContent(new Text(log));
                }
            });
        }
    }

    public ScrollPane getPane() {
        return this.pane;
    }

    public void setPane(ScrollPane pane) {
        this.pane = pane;
    }

    public void addTheadlist(Thread value) {
        this.theadlist.add(value);
    }
    public void clearTheadlist(Thread value) {
        this.theadlist.clear();
    }

    public File getFilePath(){ return filepath;}
    public boolean filpathempty(){
        if(filepath==null)
            return true;
        else
            return false;
    }
    public void setFilePath(File filepath){this.filepath = filepath; }

    public List getTheadlist() {
        return theadlist;
    }
    public void startThreadlist(){
        theadlist.forEach(Thread::start);
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }
}
