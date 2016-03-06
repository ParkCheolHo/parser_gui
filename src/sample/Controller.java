package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import sample.model.*;


import java.util.List;


public class Controller {

    @FXML
    private TextField year;
    @FXML
    private ProgressBar status;
    @FXML
    private Label statusLabel;
    Task task;
    Thread thread;
    SystemInfo systemInfo;
    List<Thread> threadList;
    public void getStart() {
        task = new GetPageInfo(year.getText());
        systemInfo = SystemInfo.getInstance();
        statusLabel.textProperty().bind(task.messageProperty());
        status.progressProperty().bind(task.progressProperty());
        long start = System.currentTimeMillis(); // 시작시간
        thread = new Thread(task);
        thread.start();
        task.stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                if (newState == Worker.State.SUCCEEDED) {
                    System.out.println("This is ok, this thread " + Thread.currentThread() + " is the JavaFX Application thread.");
                    long end = System.currentTimeMillis();  //종료시간
                    System.out.println((end - start) / 1000 + "초");
                    statusLabel.textProperty().unbind();
                    status.progressProperty().unbind();
                }
            }
        });
    }
    public void getStop(){
        threadList = systemInfo.getTheadlist();
        threadList.forEach(Thread::interrupt);

    }
}

