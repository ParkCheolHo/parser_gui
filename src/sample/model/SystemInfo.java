package sample.model;


import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private List<Thread> threads = new ArrayList<>();
    private File xmlfile = null;
    private File posterfile = null;
    private ScrollPane pane;
    private String log;
    private Label label;
    private int speed = 4;
    private boolean useDB = false;
    private boolean useOption = false;
    private boolean usePoster = false;
    public static Logger logger = LoggerFactory.getLogger(SystemInfo.class);
    private String host = "jdbc:mysql://localhost/";
    private String db;
    private String id = "root";
    private String password;
    private SystemInfo() {

    }

    public void removetLog() {
        this.log = null;
    }

    public void addLog(String value) {
        if (this.log == null)
            this.log = value;
        else {
            this.log += "\n";
            this.log += value;
        }
        if (pane != null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    pane.setContent(new Text(log));
                    pane.setVvalue(1.0);
                }
            });
        }
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public ScrollPane getPane() {
        return this.pane;
    }

    public void setPane(ScrollPane pane) {
        this.pane = pane;
    }

    public void addTheadlist(Thread value) {
        this.threads.add(value);
    }

    public void clearTheadlist(Thread value) {
        this.threads.clear();
    }

    public File getFilePath() {
        return xmlfile;
    }

    public boolean filpathempty() {
        if (xmlfile == null)
            return true;
        else
            return false;
    }

    public void setFilePath(File filepath) {
        this.xmlfile = filepath;
    }

    public List getThreads() {
        return threads;
    }

    public void startThreadlist() {
        threads.forEach(Thread::start);
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean yearEmpty() {
        if (year == null)
            return true;
        else
            return false;
    }

    public String getYear() {
        return year;
    }

    public void showLabel(double value) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String str = String.format("%.2f",value);
                label.setText(str+"%");
            }
        });
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public boolean isUseDB() {
        return useDB;
    }

    public void setUseDB(boolean useDB) {
        this.useDB = useDB;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
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

    public boolean isUseOption() {
        return useOption;
    }

    public void setUseOption(boolean useOption) {
        this.useOption = useOption;
    }

    public boolean isUsePoster() {
        return usePoster;
    }

    public void setUsePoster(boolean usePoster) {
        this.usePoster = usePoster;
    }

    public File getPosterfile() {
        return posterfile;
    }

    public void setPosterfile(File posterfile) {
        this.posterfile = posterfile;
    }
}
