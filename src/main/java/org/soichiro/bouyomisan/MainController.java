package org.soichiro.bouyomisan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;



public class MainController implements Initializable {

    @FXML
    private TextArea sayText;

    private SayCommandExecutor sayCommandExecutor;

    private static final String SAY_KOTOERI_2_COMMAND = "/usr/local/bin/SayKotoeri2";

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(!new File(SAY_KOTOERI_2_COMMAND).isFile()) {

        }

        sayCommandExecutor = new SayCommandExecutor(SAY_KOTOERI_2_COMMAND);
    }

    @FXML
    protected void handleSayExecuteAction(ActionEvent event) {
        sayCommandExecutor.execute(getSayText());
    }

    private String getSayText() {
        String oneLine = sayText.getText().replaceAll("\n", " ").replaceAll("\r", " ");
        return oneLine;
    }
}
