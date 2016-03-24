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

import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.*;

import java.io.IOException;
import java.util.List;

/**
 * Created by ParkCheolHo on 2016-03-15.
 * 메인 윈도우 컨트롤러
 */
public class Controller {
    @FXML
    private BorderPane rootPane;
    @FXML
    private ProgressBar status;
    @FXML
    private ScrollPane console;
    @FXML
    private Button startbtn;
    Task task;
    Thread thread;
    SystemInfo systeminfo;
    List<Thread> threadList;

    public Controller() {
        systeminfo = SystemInfo.getInstance();
    }

    public void Start() {
        systeminfo.setPane(console);
        try {
            //scrollpane을 싱글톤에 set

            //년도 필드의 값 확인 및 값이 존재하지 않을때 오류
            if (checkYear(systeminfo.getYear()) != true) {
                showAlert("ERROR!", "시스템 에러!", "해당년도를 정확히 입력하세요. ex)2012");
                createException("년도설정 오류");
            }

            //파일경로가 없을때 오류
            if (systeminfo.filpathempty()) {
                showAlert("ERROR!", "시스템 에러!", "저장 경로를 확인하세요");
                createException("저장경로 미설정");
            }
            task = new GetPageInfo(systeminfo.getYear()); //해당년도 전체 페이지 숫자 구하기 한페이지당 20개의 영화가 존재
            status.progressProperty().bind(task.progressProperty()); // progressbar 셋업 //자식 task 에서 updateProgress로 업데이트 가능
            startbtn.disableProperty().bind(task.runningProperty());
            systeminfo.addLog(systeminfo.getYear() + "년도 영화 크롤링을 시작합니다.");
            long start = System.currentTimeMillis(); // 시작시간
            thread = new Thread(task);
            thread.start();
            task.stateProperty().addListener(new ChangeListener<Worker.State>() {
                @Override
                public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State oldState, Worker.State newState) {
                    if (newState == Worker.State.SUCCEEDED) {
                        System.out.println("GetPageInfo Task exit");
                        long end = System.currentTimeMillis();  //종료시간
                        System.out.println((end - start) / 1000 + "초");
                        status.progressProperty().unbind();
                    }
                }
            });
        } catch (MyException e) {
            GetPageInfo.logger.error("년도나 저장경로 에러 발생");
        }

        //task 생성

    }

    //크롤링을 정지할때 싱글톤에 저장된 하위 task들을 가져와서 인터럽트를 생성시키는 메소드
    public void Stop() {
        threadList = systeminfo.getTheadlist();
        threadList.forEach(Thread::interrupt);
    }

    //설정 버튼을 눌렀을때 설정창을 보여주는 메소드
    public void showSetting() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/setting.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setX(rootPane.getScene().getWindow().getX() + 15);
        stage.setY(rootPane.getScene().getWindow().getY());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());
        stage.setTitle("Setting");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();

    }

    //사용자 정의 예외 생성 메소드
    protected void createException(String value) throws MyException {
        throw new MyException();
    }

    //설정창에서 입력된 년도를 검사하는 메소드
    protected boolean checkYear(String value) {
        int i = 0;
        try {
            i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (1970 > i || i > 2020) {
            return false;
        }
        return true;
    }

    //경고창을 보여주는 메소드
    protected void showAlert(String title, String HeaderText, String ContentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait();

    }


}

