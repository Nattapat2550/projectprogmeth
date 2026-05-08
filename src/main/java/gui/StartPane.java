package gui;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * คลาสสร้างหน้าเมนูหลักหน้าแรก (Start Menu) ของเกม
 * ควบคุมการนำทางไปยังหน้าจอการตั้งค่า (เลือกตัว/ด่าน) และเริ่มเกม
 */
public class StartPane extends VBox {
    /** ตัวแปรคงค่า (Static) สำหรับเก็บตัวละครที่ผู้เล่นเลือกไว้ข้ามฉาก */
    public static String selectedChar = "knight";
    /** ตัวแปรคงค่า (Static) สำหรับเก็บด่านที่ผู้เล่นเลือกไว้ข้ามฉาก */
    public static String selectedStage = "bg.png";

    /**
     * คอนสตรักเตอร์จัดโครงสร้างหน้าเมนูหลัก
     * @param primaryStage หน้าต่างแอปพลิเคชัน
     */
    public StartPane(Stage primaryStage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(30);
        this.setStyle("-fx-background-color: #111;");

        Label title = new Label("VAMPIRE SURVIVORS");
        title.setFont(new Font("Arial Bold", 50));
        title.setTextFill(Color.RED);

        // ปุ่มกระโดดไปหน้าเลือกตัวละคร
        Button charBtn = new Button("SELECT CHARACTER : " + selectedChar.toUpperCase());
        charBtn.setPrefSize(350, 50);
        charBtn.setStyle("-fx-base: #333; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        charBtn.setOnAction(e -> primaryStage.getScene().setRoot(new CharacterSelectionPane(primaryStage)));

        // ปุ่มกระโดดไปหน้าเลือกด่าน
        String stageLabel = selectedStage.equals("bg.png") ? "FOREST" : (selectedStage.equals("bgcave.png") ? "CAVE" : "CHURCH");
        Button stageBtn = new Button("SELECT STAGE : " + stageLabel);
        stageBtn.setPrefSize(350, 50);
        stageBtn.setStyle("-fx-base: #333; -fx-text-fill: white; -fx-font-size: 16px; -fx-cursor: hand;");
        stageBtn.setOnAction(e -> primaryStage.getScene().setRoot(new StageSelectionPane(primaryStage)));

        // ปุ่มเริ่มเกม (จะสร้าง GamePane ใหม่และเปลี่ยนจอ)
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
}