package application;

import gui.StartPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * คลาสหลักของโปรแกรม (Main Entry Point) สำหรับการเริ่มแอปพลิเคชัน JavaFX
 */
public class Main extends Application {

    /**
     * เมธอดเริ่มต้นสำหรับการสร้าง UI เมื่อรันโปรแกรม
     * @param primaryStage หน้าต่างหลักของแอปพลิเคชัน (Stage ของ JavaFX)
     */
    @Override
    public void start(Stage primaryStage) {
        // ให้เด้งไปหน้า StartPane (เมนูเริ่มเกม) เป็นอันดับแรก
        StartPane startPane = new StartPane(primaryStage);
        Scene startScene = new Scene(startPane, 800, 600);

        primaryStage.setTitle("Vampire Survivor");
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * เมธอด main จุดเริ่มต้นรันโปรแกรม Java
     * @param args พารามิเตอร์เริ่มต้น
     */
    public static void main(String[] args) {
        launch(args);
    }
}