package org.soichiro.bouyomisan;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainController implements Initializable {

    @FXML
    private volatile TextArea sayText;

    @FXML
    private volatile TextArea readingText;

    public final SayCommandExecutor sayCommandExecutor = new SayCommandExecutor();

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String sayCommand = Config.getSingleton().sayCommand;
        if(!new File(sayCommand).isFile()) {
            readingText.setPromptText(
                    String.format("読み上げコマンド\"%s\"が見つかりませんでした。" +
                                    "SayKotoeri2などをインストールして再起動してください。",
                            sayCommand)
            );
        }
    }

    @FXML
    protected void handleSayExecuteAction(ActionEvent event) {
        String readingText = sayCommandExecutor.execute(
                new SayOption(getSayText(), null, null, null));
        setReadingTextValue(readingText);
    }

    @FXML
    protected void handleSayTextKeyReleased(KeyEvent event) {
        // ⌘ + Enterで読み上げ
        if(event.isMetaDown() && event.getCode().equals(KeyCode.ENTER)) {
            String readingText = sayCommandExecutor.execute(
                    new SayOption(getSayText(), null, null, null));
            setReadingTextValue(readingText);
        }
    }

    private String getSayText() {
        String oneLine = sayText.getText().replaceAll("\n", " ").replaceAll("\r", " ");
        return oneLine;
    }

    /**
     * 読み上げられたtextを設定
     * @param value
     */
    public void setReadingTextValue(final String value) {
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage(value);
                return null;
            }
        };
        readingText.textProperty().bind(task.messageProperty());
        executor.execute(task);
    }

}
