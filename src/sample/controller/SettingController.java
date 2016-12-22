package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import sample.model.MySql;
import sample.model.SystemInfo;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by ParkCheolHo on 2016-03-15.
 * 세팅 윈도우 컨트롤러
 * 람다식이란
 * 식별자 없이 실행가능한 함수 표현식
 */
public class SettingController implements Initializable {
    //일반 메뉴
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private TabPane rootTabPane;
    @FXML
    private Button pathbtn;
    @FXML
    private TextField Year;
    @FXML
    private TextField textField;
    //고급 메뉴
    @FXML
    private TextField idField;
    @FXML
    private PasswordField password;
    @FXML
    private Button confirm;
    @FXML
    private Label label;
    @FXML
    private TextField host;
    @FXML
    private CheckBox DataBaseEnable;
    @FXML
    private ListView<String> showDatabase;
    @FXML
    private CheckBox nomaloption;
    @FXML
    private CheckBox checkposter;
    @FXML
    private TextField posterTextField;
    @FXML
    private Button posterbtn;

    private SystemInfo systeminfo;


    //컨트롤러 초기화
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systeminfo = SystemInfo.getInstance();
        if (systeminfo.isUseDB()) {
            DataBaseEnable.setSelected(true);
            SetDbOptionDisable(false);
            setXmlDisable(true);
            host.setText(systeminfo.getHost());
            idField.setText(systeminfo.getId());
            password.setText(systeminfo.getPassword());
        } else {
            DataBaseEnable.setSelected(false);
            SetDbOptionDisable(true);
            if (!systeminfo.filPathEmpty()) {
                textField.setText(systeminfo.getFilePath().getPath());
            }
        }

        if (systeminfo.isUsePoster()) {
            checkposter.setSelected(true);
            setPosterDisable(false);
            if (!systeminfo.poster_file_path_empty())
                posterTextField.setText(systeminfo.getPosterFile().getPath());
        } else {
            checkposter.setSelected(false);
            setPosterDisable(true);
        }
        if (systeminfo.getYear() != null)
            Year.setText(systeminfo.getYear());

        nomaloption.setSelected(systeminfo.isUseOption());
        addLicensor();
    }

    private void addLicensor() {
        // 년도 필드 4자 이상 쓰지 못하게 하는 리스너
        Year.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Year.getText().length() > 4) {
                String s = Year.getText().substring(0, 4);
                Year.setText(s);
            }
        });
        // 파싱 속도 조절 리스너
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            systeminfo.setSpeed(newValue.intValue() + 1);
            System.out.println(systeminfo.getSpeed());
        });
        // 키 입력시 싱글톤에 년도 입력 리스너
        Year.setOnKeyReleased(k -> systeminfo.setYear(Year.getText()));
        // DB 사용 체크박스
        DataBaseEnable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                SetDbOptionDisable(false);
                systeminfo.setUseDB(true);
                setXmlDisable(true);
                host.setText(systeminfo.getHost());
                idField.setText(systeminfo.getId());
                password.setText(systeminfo.getPassword());
            } else {
                systeminfo.setUseDB(false);
                SetDbOptionDisable(true);
                setXmlDisable(false);
            }
        });
        showDatabase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            systeminfo.setDb(newValue);
        });
        nomaloption.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue)
                systeminfo.setUseOption(true);
            else
                systeminfo.setUseOption(false);
        });
        checkposter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                systeminfo.setUsePoster(true);
                setPosterDisable(!newValue);
            } else {
                systeminfo.setUsePoster(false);
                setPosterDisable(!newValue);
            }
        });
        host.setOnKeyReleased(k->systeminfo.setHost(host.getText()));
        idField.setOnKeyReleased(k->systeminfo.setId(idField.getText()));
        password.setOnKeyReleased(k->systeminfo.setPassword(password.getText()));
    }

    // FileChooser 이용해서 file 객체 생성하여 싱글톤에
    @FXML
    public void File() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML");
        //오직 .xml 로만 저장 가능하게 하기 위해 filter 생성
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showSaveDialog(rootTabPane.getScene().getWindow());
        textField.setText(file.getPath());
        systeminfo.setFilePath(file);

    }

    // 포스터가 저장 되는 디렉토리를 선택하는 메소드
    @FXML
    public void Directory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Directory");
        File file = chooser.showDialog(rootTabPane.getScene().getWindow());
        if (file != null) {
            posterTextField.setText(file.getPath());
            systeminfo.setPosterFile(file);
        }
    }

    // 설정창에서 mysql 커넥션을 체킹하는 메소드

    //TODO : 에러 고칠것
    @FXML
    public void CheckMysql() {
        Task task = new Task<Void>() {
            @Override
            protected Void call() {
                MySql mysql = new MySql(host.getText(), "", idField.getText(), password.getText());
                showDatabase.setItems(null);
                ArrayList<String> list = (ArrayList<String>) mysql.startup();
                showDatabase.setItems(FXCollections.observableList(list));
                return null;
            }
        };
        task.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                System.out.println((Integer)newValue);
                if (newValue != null) {
                    switch (newValue) {
                        case 0:
                            setLabel(Color.rgb(204, 0, 0), "Host address wrong!");
                            break;
                        case 5:
                            setLabel(Color.rgb(0, 153, 151), "Connected!");
                            systeminfo.setHost(host.getText());
                            systeminfo.setId(idField.getText());
                            systeminfo.setPassword(password.getText());
                            systeminfo.setDb("");
                            break;
                        case 1045:
                            setLabel(Color.rgb(204, 0, 0), "id or password wrong!");
                            break;
                        default:
                            setLabel(Color.rgb(255, 255, 0), "something wrong!");
                    }
                }
            }
        });
        Thread th = new Thread(task);
        th.start();
    }
    //mysql 커넥션 할때 결과를 출력하는 메소드
    private void setLabel(Color color, String value) {
        label.setTextFill(color);
        label.setText("");
        label.setText(value);
    }
    // db 옵션을 사용할때 쓰는 메소드
    private void SetDbOptionDisable(boolean flag) {
        idField.setDisable(flag);
        password.setDisable(flag);
        confirm.setDisable(flag);
        label.setDisable(flag);
        host.setDisable(flag);
        showDatabase.setDisable(flag);
    }
    // xml 관련 요소들을 처리하는 메소드
    private void setXmlDisable(boolean flag) {
        textField.setDisable(flag);
        pathbtn.setDisable(flag);
    }
    //포스터 관련 요소들을 처리하는 메소드
    private void setPosterDisable(boolean flag) {
        posterTextField.setDisable(flag);
        posterbtn.setDisable(flag);
    }
}
