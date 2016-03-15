package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.stage.FileChooser;
import sample.model.*;


import java.io.File;
import java.util.List;


public class Controller {
    @FXML
    private BorderPane rootPane;
    @FXML
    private TextField year;
    @FXML
    private ProgressBar status;
    @FXML
    private TextField pathlabel;
    @FXML
    private ScrollPane terminal;
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
            if(checkYear(year.getText())!=true){
                showAlert("ERROR!","시스템 에러!", "해당년도를 정확히 입력하세요. ex)2012");
                createExceuption("년도설정 오류");
            }
            task = new GetPageInfo(year.getText());
            status.progressProperty().bind(task.progressProperty());
            if (systemInfo.filpathempty()) {
                showAlert("ERROR!","시스템 에러!", "저장 경로를 확인하세요");
                createExceuption("저장경로 미설정");
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

    public void File() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
        if (file != null) {
            pathlabel.setText(file.getPath());
            systemInfo.setFilePath(file);
        }
    }

    protected void createExceuption(String value) throws MyException {
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

