package application;

import gui.StartPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // ให้เด้งไปหน้า StartPane (เมนูเลือกตัวละคร) เป็นอันดับแรก
        StartPane startPane = new StartPane(primaryStage);
        Scene startScene = new Scene(startPane, 800, 600);

        primaryStage.setTitle("Vampire Survivor");
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}