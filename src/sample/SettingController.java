package sample;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by ParkCheolHo on 2016-03-15.
 */
public class SettingController {

    @FXML
    private TextArea yearField;
    @FXML
    private AnchorPane rootPane;
    Stage stage;

    public void initialize(){
        stage = (Stage) rootPane.getScene().getWindow();
        System.out.println("test");
    }
}
