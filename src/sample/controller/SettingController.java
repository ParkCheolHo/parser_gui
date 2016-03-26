package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.SystemInfo;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ParkCheolHo on 2016-03-15.
 * 세팅 윈도우 컨트롤러
 */
public class SettingController implements Initializable {
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
    SystemInfo systeminfo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systeminfo = SystemInfo.getInstance();
        if(systeminfo.getYear()!=null)
            Year.setText(systeminfo.getYear());
        if(systeminfo.filpathempty()!=true){
            textField.setText(systeminfo.getFilePath().getPath());
        }
        if(systeminfo.getToggle()==null){
            systeminfo.setToggle("1");
            normal.setSelected(true);
        }
        else if(systeminfo.getToggle()=="1"){
            normal.setSelected(true);
        }
        else{
            mobile.setSelected(true);
        }

        yeast();

    }

    public void yeast() {
        Year.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (Year.getText().length() > 5) {
                    String s = Year.getText().substring(0, 5);
                    Year.setText(s);
                }
            }
        });
        systeminfo.setSpeed(4);
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                systeminfo.setSpeed(newValue.intValue()+1);
            }
        });
        Year.setOnKeyReleased(k -> systeminfo.setYear(Year.getText()));
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if(new_toggle==normal){
                    systeminfo.setToggle("1");
                }
                else{
                    systeminfo.setToggle("2");
                }
            }
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
}
