package org.soichiro.bouyomisan;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import rx.functions.Action1;

/**
 * 起動のエントリーポイント
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        final Parent root = fxmlLoader.load();
        final MainController mainController = fxmlLoader.getController();
        primaryStage.setTitle("棒読さん");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        BouyomisanServerHandler bouyomisanServerHandler =
                new BouyomisanServerHandler(mainController.sayCommandExecutor);
        final BouyomisanServer bouyomisanServer = new BouyomisanServer(bouyomisanServerHandler);
        bouyomisanServer.start();

        // ダイアログを閉じた時に同時にサーバーを終了する
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                bouyomisanServer.shutdown();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignore) {}
                System.exit(0); // 1秒後強制終了
            }
        });

        // 読み上げが行われた時にその内容をテキストエリアにセットする
        bouyomisanServerHandler.getSayOptionObservable().subscribe(new Action1<String>() {
            @Override
            public void call(String readingText) {
                mainController.setReadingTextValue(readingText);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
