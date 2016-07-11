package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
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
    private TabPane rootTabPane;
    @FXML
    private Button pathbtn;
    @FXML
    private ToggleGroup toggleGroup;

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
    @FXML
    private CheckBox nomaloption;
    @FXML
    private CheckBox checkposter;
    @FXML
    private TextField posterTextField;
    @FXML
    private Button posterbtn;

    SystemInfo systeminfo;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systeminfo = SystemInfo.getInstance();
        if(systeminfo.isUseDB()){
            DataBaseEnable.setSelected(true);
            setOption(false);
            setDisable(true);
            host.setText(systeminfo.getHost());
            idField.setText(systeminfo.getId());
            password.setText(systeminfo.getPassword());
        }else{
            DataBaseEnable.setSelected(false);

            setOption(true);
        }
        if(systeminfo.isUsePoster()){
            checkposter.setSelected(true);
            setPosterDisbale(false);
            posterTextField.setText(systeminfo.getPosterfile().getPath());
        }else{
            checkposter.setSelected(false);
            setPosterDisbale(true);
        }


        if (systeminfo.getYear() != null)
            Year.setText(systeminfo.getYear());
        if (systeminfo.filpathempty() != true) {
            textField.setText(systeminfo.getFilePath().getPath());
        }


        nomaloption.setSelected(systeminfo.isUseOption());
        addLicensor();
    }

    public void addLicensor() {
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
        confirm.setOnMouseEntered(event -> confirm.getScene().setCursor(Cursor.HAND));

        confirm.setOnMouseExited(event -> confirm.getScene().setCursor(Cursor.DEFAULT));
        // DB 사용 체크박스
        DataBaseEnable.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                setOption(false);
                systeminfo.setUseDB(true);
                setDisable(true);
                host.setText(systeminfo.getHost());
                idField.setText(systeminfo.getId());
                password.setText(systeminfo.getPassword());
            }
            else {
                systeminfo.setUseDB(false);
                setOption(true);
                setDisable(false);
            }
        });
        showDatabase.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            systeminfo.setDb(newValue);
        });
        nomaloption.selectedProperty().addListener((observable, oldValue, newValue) -> {
          if(newValue)
              systeminfo.setUseOption(true);
          else
              systeminfo.setUseOption(false);
        });
        checkposter.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                systeminfo.setUsePoster(true);
                setPosterDisbale(!newValue);
            }
            else {
                systeminfo.setUsePoster(false);
                setPosterDisbale(!newValue);
            }
        });
    }
    @FXML
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
    public void Directory(){
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Directory");
        File file = chooser.showDialog(rootTabPane.getScene().getWindow());
        if (file != null) {
            posterTextField.setText(file.getPath());
            systeminfo.setPosterfile(file);
        }
    }

    @FXML
    public void CheckMysql() throws InterruptedException {
        showDatabase.getItems().clear();
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                MySql mysql = new MySql(host.getText(), "", idField.getText(), password.getText());
                mysql.setTestflag(true);
                confirm.getScene().setCursor(Cursor.WAIT);
                int result = mysql.start();
                showDatabase.setItems(FXCollections.observableList(mysql.getSqlresult()));
                mysql.setTestflag(false);
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
                            systeminfo.setHost(host.getText());
                            systeminfo.setId(idField.getText());
                            systeminfo.setPassword(password.getText());
                            systeminfo.setDb("");
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
    void setDisable(boolean flag){
        textField.setDisable(flag);
        pathbtn.setDisable(flag);
    }
    void setPosterDisbale(boolean flag){
        posterTextField.setDisable(flag);
        posterbtn.setDisable(flag);
    }
}
