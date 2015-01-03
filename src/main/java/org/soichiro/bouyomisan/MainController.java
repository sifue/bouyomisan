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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sayCommandExecutor = new SayCommandExecutor();
        String sayCommand = Config.getSingleton().sayCommand;
        if(!new File(sayCommand).isFile()) {
            sayText.setPromptText(
                    String.format("読み上げコマンド\"%s\"が見つかりませんでした。" +
                            "SayKotoeri2などをインストールして再起動してください。",
                            sayCommand)
            );
        }
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
