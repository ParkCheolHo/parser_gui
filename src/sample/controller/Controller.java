package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Controller {
    @FXML
    private BorderPane rootPane;
    @FXML
    private ProgressBar status;
    @FXML
    private TextField pathlabel;

    Task task;
    Thread thread;
    SystemInfo systemInfo;
    List<Thread> threadList;

    public Controller() {
        systemInfo = SystemInfo.getInstance();
    }
    public void Start() {
        try {
            //year.getText() 판별 메소드를 만들것
            if(checkYear(systemInfo.getYear())!=true){
                showAlert("ERROR!","시스템 에러!", "해당년도를 정확히 입력하세요. ex)2012");
                createException("년도설정 오류");
            }
            task = new GetPageInfo(systemInfo.getYear());
            status.progressProperty().bind(task.progressProperty());
            if (systemInfo.filpathempty()) {
                showAlert("ERROR!","시스템 에러!", "저장 경로를 확인하세요");
                createException("저장경로 미설정");
            }
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
                        status.progressProperty().unbind();
                    }
                }
            });

        } catch (MyException e) {

        }
    }

    public void Stop() {
        threadList = systemInfo.getTheadlist();
        threadList.forEach(Thread::interrupt);
    }


    public void showSetting() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/setting.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());
        stage.setTitle("Setting");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

    }

    protected void createException(String value) throws MyException {
        GetPageInfo.logger.error(value);
        throw new MyException();
    }
    protected boolean checkYear(String value){
        int i = 0;
        try {
            i = Integer.parseInt(value);
        }catch(NumberFormatException e){
            return false;
        }
        if(1970>i || i>2020) {
            return false;
        }
    return true;
    }

    protected void showAlert(String title, String HeaderText, String ContentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait();

    }

}

