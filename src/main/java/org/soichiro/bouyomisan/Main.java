package org.soichiro.bouyomisan;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 起動のエントリーポイント
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        MainController mainController = fxmlLoader.getController();
        primaryStage.setTitle("棒読さん");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        BouyomisanServerHandler bouyomisanServerHandler =
                new BouyomisanServerHandler(mainController.sayCommandExecutor);
        BouyomisanServer bouyomisanServer = new BouyomisanServer(bouyomisanServerHandler);
        bouyomisanServer.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
