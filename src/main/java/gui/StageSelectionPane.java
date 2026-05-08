package gui;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * หน้าจอ GUI สำหรับเลือกแผนที่ (ด่านฉากหลัง) ในเกม
 */
public class StageSelectionPane extends VBox {

    /**
     * คอนสตรักเตอร์จัดหน้าจอ
     * @param stage หน้าต่างแอปพลิเคชัน
     */
    public StageSelectionPane(Stage stage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(40);
        this.setStyle("-fx-background-color: #222;");

        Label title = new Label("SELECT STAGE");
        title.setFont(new Font("Arial Bold", 40));
        title.setTextFill(Color.WHITE);

        HBox stagesBox = new HBox(30);
        stagesBox.setAlignment(Pos.CENTER);

        // วาดการ์ดของแต่ละฉาก
        stagesBox.getChildren().addAll(
                createStageCard("FOREST", "bg.png", stage),
                createStageCard("CAVE", "bgcave.png", stage),
                createStageCard("CHURCH", "bgchurch.png", stage)
        );

        // ปุ่มกลับ
        Button backBtn = new Button("BACK TO MENU");
        backBtn.setPrefSize(200, 50);
        backBtn.setStyle("-fx-base: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> stage.getScene().setRoot(new StartPane(stage)));

        this.getChildren().addAll(title, stagesBox, backBtn);
    }

    /**
     * สร้างการ์ดแสดงด่าน 1 อันพร้อมภาพตัวอย่างและปุ่มให้กด
     * @param titleName ชื่อที่โชว์
     * @param bgImgFile ชื่อไฟล์ฉากหลังที่ระบบใช้
     * @param stage หน้าต่างอ้างอิง
     * @return VBox ที่ประกอบร่างเป็น UI การ์ด
     */
    private VBox createStageCard(String titleName, String bgImgFile, Stage stage) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);

        boolean isSel = StartPane.selectedStage.equals(bgImgFile);
        card.setStyle("-fx-background-color: #333; -fx-padding: 20; -fx-background-radius: 10; -fx-border-color: " + (isSel ? "#f1c40f" : "#555") + "; -fx-border-radius: 10; -fx-border-width: 3;");
        card.setPrefSize(250, 320);

        Label nameLbl = new Label(titleName);
        nameLbl.setFont(new Font("Arial Bold", 20));
        nameLbl.setTextFill(Color.WHITE);

        ImageView bgView = new ImageView();
        try { bgView.setImage(new Image(ClassLoader.getSystemResource("images/" + bgImgFile).toString())); } catch(Exception e){}
        bgView.setFitWidth(200);
        bgView.setFitHeight(150);

        Button selBtn = new Button(isSel ? "SELECTED" : "SELECT");
        selBtn.setPrefSize(120, 40);
        selBtn.setStyle(isSel ? "-fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;" : "-fx-base: #555; -fx-text-fill: white;");
        selBtn.setDisable(isSel);

        // อัปเดตและกลับหน้าหลัก
        selBtn.setOnAction(e -> { StartPane.selectedStage = bgImgFile; stage.getScene().setRoot(new StartPane(stage)); });

        card.getChildren().addAll(nameLbl, bgView, selBtn);
        return card;
    }
}