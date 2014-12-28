package org.soichiro.bouyomisan;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField sayCommandPath;

    @FXML
    private TextArea sayText;

    private SayCommandExecutor sayCommandExecutor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sayCommandPath.setText("/usr/bin/say");
        sayCommandExecutor = new SayCommandExecutor(sayCommandPath.getText());
    }

    @FXML
    protected void handleSayCommandSelectButtonAction(ActionEvent event) {
        sayCommandExecutor.execute(sayText.getText());
    }

    @FXML
    protected void handleSayExecuteAction(ActionEvent event) {
        sayCommandExecutor.execute(sayText.getText());
    }

    @FXML
    protected void handleCommandPathTextChanged(KeyEvent event) {
        try {
            sayCommandExecutor.setCommandPath(sayCommandPath.getText());
        } catch (IOException ignore) {}
    }
}
