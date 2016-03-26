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
    private File filepath = null;
    private ScrollPane pane;
    private String log;
    private String toggle;
    private Label label;


    private int speed;
    public static Logger logger = LoggerFactory.getLogger(SystemInfo.class);
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

    public String getToggle() {
        return toggle;
    }

    public void setToggle(String toggle) {
        this.toggle = toggle;
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
        return filepath;
    }

    public boolean filpathempty() {
        if (filepath == null)
            return true;
        else
            return false;
    }

    public void setFilePath(File filepath) {
        this.filepath = filepath;
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

}
