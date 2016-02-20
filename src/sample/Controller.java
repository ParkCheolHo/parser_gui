package sample;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import sample.model.GetPageInfo;

public class Controller {

    @FXML
    private TextField year;
    @FXML
    private ProgressBar status;


    @FXML
    void initialize(){

    }

    public void getStart()  {

        Task test = new GetPageInfo(year.getText());
        status.progressProperty().bind(test.progressProperty());
        year.textProperty().bind(test.messageProperty());
        new Thread(test).start();
    }

}
