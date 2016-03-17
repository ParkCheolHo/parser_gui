package sample.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.SystemInfo;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ParkCheolHo on 2016-03-15.
 */
public class SettingController  implements Initializable {


    @FXML
    private TabPane rootTabPane;

    @FXML
    private Stage stage;
    @FXML
    private TextField Year;
    @FXML
    private TextField pathfield;
    SystemInfo systeminfo;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        systeminfo = SystemInfo.getInstance();
        Year.setText(systeminfo.getYear());
        yeast();

    }


    public void exit(){
        stage = (Stage) rootTabPane.getScene().getWindow();
        System.out.println("test");
    }
    public void yeast(){
        Year.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (Year.getText().length() > 5) {
                    String s = Year.getText().substring(0, 5);
                    Year.setText(s);
                }
            }
        });
        Year.setOnKeyReleased(k -> {
            System.out.println(k.getCode().toString());
            systeminfo.setYear(Year.getText());
            System.out.println(systeminfo.getYear());
            //handle
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
            pathfield.setText(file.getPath());
            systeminfo.setFilePath(file);
        }
    }
}
