package application;

import gui.StartPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * คลาสหลัก (Entry Point) สำหรับเริ่มต้นโปรแกรมแอปพลิเคชัน JavaFX
 */
public class Main extends Application {

    /**
     * เมธอดทำงานลำดับแรกของ JavaFX ใช้ในการจัดตั้งหน้าต่าง UI
     * @param primaryStage หน้าต่างแอปพลิเคชันหลัก (Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        // ให้เด้งไปหน้า StartPane (เมนูเริ่มเกม) เป็นอันดับแรก
        StartPane startPane = new StartPane(primaryStage);
        Scene startScene = new Scene(startPane, 800, 600);

        primaryStage.setTitle("Vampire Survivor"); // ตั้งชื่อหน้าต่างโปรแกรม
        primaryStage.setScene(startScene);
        primaryStage.setResizable(false); // ล็อคขนาดหน้าต่าง ไม่ให้ขยาย
        primaryStage.show();
    }

    /**
     * เมธอด main หลักของจาวาที่รันเป็นจุดแรกเมื่อเริ่มโปรแกรม
     * @param args ข้อความส่งต่อจากการรัน Command Line
     */
    public static void main(String[] args) {
        launch(args);
    }
}