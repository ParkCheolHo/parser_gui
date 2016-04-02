package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.MySql;
import sample.model.SystemInfo;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ParkCheolHo on 2016-03-15.
 * 세팅 윈도우 컨트롤러
 */
public class SettingController implements Initializable {
    //일반 메뉴
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private RadioButton normal;
    @FXML
    private RadioButton mobile;
    @FXML
    private TabPane rootTabPane;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private Stage stage;
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
    SystemInfo systeminfo;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setOption(true);
        systeminfo = SystemInfo.getInstance();
        if (systeminfo.getYear() != null)
            Year.setText(systeminfo.getYear());
        if (systeminfo.filpathempty() != true) {
            textField.setText(systeminfo.getFilePath().getPath());
        }
        if (systeminfo.getToggle() == null) {
            systeminfo.setToggle("1");
            normal.setSelected(true);
        } else if (systeminfo.getToggle() == "1") {
            normal.setSelected(true);
        } else {
            mobile.setSelected(true);
        }
        yeast();
        host.setText("jdbc:mysql://localhost/");
        if(systeminfo.isUseDB()){
            DataBaseEnable.setSelected(true);
            host.setText(systeminfo.getMysql().getHostname());
            idField.setText(systeminfo.getMysql().getId());
            password.setText(systeminfo.getMysql().getPassword());
        }
    }

    public void yeast() {
        systeminfo.setSpeed(4);
        // 년도 필드 4자 이상 쓰지 못하게 하는 리스너
        Year.textProperty().addListener((observable, oldValue, newValue) -> {
            if (Year.getText().length() > 5) {
                String s = Year.getText().substring(0, 5);
                Year.setText(s);
            }
        });
        // 파싱 속도 조절 리스너
        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> systeminfo.setSpeed(newValue.intValue() + 1));
        // 키 입력시 싱글톤에 년도 입력 리스너
        Year.setOnKeyReleased(k -> systeminfo.setYear(Year.getText()));
        // 토글그룹 리스너 (일반페이지//모바일페이지)
        toggleGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (new_toggle == normal) {
                systeminfo.setToggle("1");
            } else {
                systeminfo.setToggle("2");
            }
        });
        confirm.setOnMouseEntered(event -> confirm.getScene().setCursor(Cursor.HAND));
        confirm.setOnMouseExited(event -> confirm.getScene().setCursor(Cursor.DEFAULT));
        // DB 사용 체크박스
        DataBaseEnable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setOption(false);
                systeminfo.setUseDB(true);
            }
            else {
                systeminfo.setUseDB(false);
                setOption(true);
            }
        });
        showDatabase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            systeminfo.getMysql().setDbname(newValue);
        });
    }

    public void File() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")
        );
        File file = fileChooser.showSaveDialog(rootTabPane.getScene().getWindow());
        if (file != null) {
            textField.setText(file.getPath());
            systeminfo.setFilePath(file);
        }
    }

    @FXML
    public void CheckMysql() throws InterruptedException {
        showDatabase.getItems().clear();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                MySql mysql = systeminfo.getMysql();
                mysql.setHostname(host.getText());
                mysql.setId(idField.getText());
                mysql.setPassword(password.getText());
                mysql.setDbname("");
                mysql.setTestflag(true);
                confirm.getScene().setCursor(Cursor.WAIT);
                int result = mysql.Connection();
                showDatabase.setItems(FXCollections.observableList(mysql.getSqlresult()));
                return result;
            }
        };
        task.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                if (newValue != null) {
                    switch(newValue){
                        case 0 :
                            setLabel(Color.rgb(204, 0, 0), "Host address wrong!");
                            break;
                        case 5 :
                            setLabel(Color.rgb(0, 153, 151),"Connected!");
                            break;
                        case 1045 :
                            setLabel(Color.rgb(204, 0, 0), "id or password wrong!");
                            break;
                        default:
                            setLabel(Color.rgb(255, 255, 0), "something wrong!");
                    }
                    confirm.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
        Thread th = new Thread(task);
        th.start();
    }

    void setLabel(Color color, String value){
        label.setTextFill(color);
        label.setText("");
        label.setText(value);
    }
    void setOption(boolean flag) {
        idField.setDisable(flag);
        password.setDisable(flag);
        confirm.setDisable(flag);
        label.setDisable(flag);
        host.setDisable(flag);
        showDatabase.setDisable(flag);
    }
}
