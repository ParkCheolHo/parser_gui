package sample.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ParkCheolHo on 2016-03-15.
 * 메인 윈도우 컨트롤러
 */
public class Controller implements Initializable {
    @FXML
    private BorderPane rootPane;
    @FXML
    private ProgressBar status;
    @FXML
    private ScrollPane console;
    @FXML
    private Button startbtn;
    @FXML
    private Button stopbtn;
    @FXML
    private Label perLabel;

    private SystemInfo systeminfo;
    // 컨트롤러 초기화.
    // view 로딩 될때 같이 초기화 된다.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systeminfo = SystemInfo.getInstance();
        systeminfo.setPane(console);
        systeminfo.setLabel(perLabel);
        stopbtn.setDisable(true);
    }

    public void Start() {
        try {
            //년도 필드의 값 확인 및 값이 존재하지 않을때 오류
            if (!checkYear(systeminfo.getYear())) {
                showAlert("ERROR!", "시스템 에러!", "해당년도를 정확히 입력하세요. ex)2012");
                throw new MyException();
            }
            //파일경로가 없을때 오류
            if(systeminfo.isUseDB()) {
                if(systeminfo.getDb()==null){
                    showAlert("ERROR!", "시스템 에러!", "데이터베이스를 선택하세요");
                    throw new MyException();
                }
            }else{
                if (systeminfo.filpathempty()) {
                    showAlert("ERROR!", "시스템 에러!", "xml파일 저장 경로를 확인하세요");
                    throw new MyException();
                }
            }
            if(systeminfo.isUsePoster()){
                if(systeminfo.poster_file_path_empty()){
                    showAlert("ERROR!", "시스템 에러!", "포스터 저장경로 확인");
                    throw new MyException();
                }
            }
            startbtn.getScene().setCursor(Cursor.WAIT);
            stopbtn.setDisable(false);
            Task task = new GetPageInfo(systeminfo.getYear());
            status.progressProperty().bind(task.progressProperty()); // progressbar 셋업 //자식 task 에서 updateProgress로 업데이트 가능
            startbtn.disableProperty().bind(task.runningProperty());
            systeminfo.addLog(systeminfo.getYear() + "년도 영화 크롤링을 시작합니다.");
            long start = System.currentTimeMillis(); // 시작시간
            task.stateProperty().addListener((ov, old_Status, new_State) -> {
                if (new_State == Worker.State.SUCCEEDED) {
                    SystemInfo.logger.info("GetPageInfo Task exit");
                    long end = System.currentTimeMillis();  //종료시간
                    SystemInfo.logger.info((end - start) / 1000 + "초");
                    rootPane.getScene().setCursor(Cursor.DEFAULT);
                    stopbtn.setDisable(true);
                }
            });
            Thread thread = new Thread(task);
            thread.start();
        } catch (MyException e) {
            SystemInfo.logger.error("년도나 저장경로 에러 발생");
        }
    }
    //크롤링을 정지할때 싱글톤에 저장된 하위 task들을 가져와서 인터럽트를 생성시키는 메소드
    public void Stop() {
        ArrayList<Thread> threads = systeminfo.getThreads();
        threads.forEach(Thread::interrupt);
        stopbtn.setDisable(true);
    }
    //설정 버튼을 눌렀을때 설정창을 보여주는 메소드
    public void showSetting() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/setting.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setX(rootPane.getScene().getWindow().getX() + 15);
        stage.setY(rootPane.getScene().getWindow().getY() + 15);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(rootPane.getScene().getWindow());
        stage.setTitle("Setting");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }

    // 정보창을 여는 메소드
    public void showInfo() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/sample/view/info.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setX(rootPane.getScene().getWindow().getX() - 30);
        stage.setY(rootPane.getScene().getWindow().getY() + 30);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Info");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
    // 사용자 정의 예외 생성 메소드
//    private void createException(String value) throws MyException {
//        throw new MyException();
//    }

    // 설정창에서 입력된 년도를 검사하는 메소드
    private boolean checkYear(String value) {
        int i = 0;
        try {
            i = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return !(1970 > i || i > 2020);
    }

    // 경고창을 보여주는 메소드
    private void showAlert(String title, String HeaderText, String ContentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(rootPane.getScene().getWindow());
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait();
    }
    // 어플리케이션 종료 메소드
    @FXML
    protected void Close() {
        Platform.exit();
        System.exit(0);
    }
}

