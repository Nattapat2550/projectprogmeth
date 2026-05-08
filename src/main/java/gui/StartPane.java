package gui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * คลาส StartPane หน้าจอหลักหน้าแรกเมื่อเข้าเกม (หน้าจอ Title)
 * ทำหน้าที่เป็นศูนย์กลางในการเก็บค่าการตั้งค่า และนำทางไปยังหน้าจอเลือกต่างๆ
 */
public class StartPane extends VBox {

    // ตัวแปรแบบ static เพื่อจดจำค่าที่ผู้เล่นเลือกไว้แม้จะเปลี่ยนหน้าต่าง
    public static String selectedChar = "knight";
    public static String selectedStage = "bg.png";

    /**
     * คอนสตรักเตอร์สร้างหน้าจอเริ่มเกม
     * @param primaryStage หน้าต่างหลักของโปรแกรม
     */
    public StartPane(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setStyle("-fx-background-color: #111;");

        // หัวข้อเกม
        Label title = new Label("VAMPIRE SURVIVORS");
        title.setFont(new Font("Arial Bold", 50));
        title.setTextFill(Color.RED);

        // ปุ่มไปหน้าเลือกตัวละคร (แสดงค่าที่เลือกอยู่ปัจจุบัน)
        Button charBtn = new Button("SELECT CHARACTER : " + selectedChar.toUpperCase());
        charBtn.setPrefSize(350, 50);
        charBtn.setStyle("-fx-base: #333; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        charBtn.setOnAction(e -> {
            primaryStage.getScene().setRoot(new CharacterSelectionPane(primaryStage));
        });

        // ปุ่มไปหน้าเลือกด่าน (แสดงค่าที่เลือกอยู่ปัจจุบัน)
        Button stageBtn = new Button("SELECT STAGE : " + getStageName(selectedStage));
        stageBtn.setPrefSize(350, 50);
        stageBtn.setStyle("-fx-base: #333; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        stageBtn.setOnAction(e -> {
            primaryStage.getScene().setRoot(new StageSelectionPane(primaryStage));
        });

        // ปุ่มเริ่มเกม
        Button playBtn = new Button("ENTER THE NIGHT (START)");
        playBtn.setPrefSize(350, 60);
        playBtn.setStyle("-fx-base: #c0392b; -fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-cursor: hand;");
        playBtn.setOnAction(e -> {
            GamePane gamePane = new GamePane(selectedChar, selectedStage, primaryStage);
            primaryStage.getScene().setRoot(gamePane);
            gamePane.setupInput(primaryStage.getScene());
            gamePane.startGameLoop();
        });

        this.getChildren().addAll(title, charBtn, stageBtn, playBtn);
    }

    /**
     * ดึงชื่อด่านที่อ่านง่ายเพื่อนำมาแสดงผลบนปุ่ม
     * @param file ชื่อไฟล์ของด่าน
     * @return ชื่อด่านที่เป็นตัวพิมพ์ใหญ่
     */
    private String getStageName(String file) {
        if (file.equals("bg.png")) return "FOREST";
        if (file.equals("bgcave.png")) return "CAVE";
        if (file.equals("bgchurch.png")) return "CHURCH";
        return "UNKNOWN";
    }
}