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
 * คลาส StageSelectionPane สำหรับหน้าจอให้เลือกแผนที่/ด่าน ก่อนเริ่มเล่น
 * แสดงรูปภาพตัวอย่างของด่านพร้อมปุ่มให้กดเลือก
 */
public class StageSelectionPane extends VBox {

    /**
     * คอนสตรักเตอร์หน้าจอเลือกด่าน
     * @param stage หน้าต่างหลักแอปพลิเคชัน
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

        VBox forestCard = createStageCard("FOREST", "bg.png", stage);
        VBox caveCard = createStageCard("CAVE", "bgcave.png", stage);
        VBox churchCard = createStageCard("CHURCH", "bgchurch.png", stage);

        stagesBox.getChildren().addAll(forestCard, caveCard, churchCard);

        Button backBtn = new Button("BACK TO MENU");
        backBtn.setPrefSize(200, 50);
        backBtn.setStyle("-fx-base: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> stage.getScene().setRoot(new StartPane(stage)));

        this.getChildren().addAll(title, stagesBox, backBtn);
    }

    /**
     * สร้างการ์ดแสดงข้อมูลของด่าน
     * @param titleName ชื่อที่แสดงผล
     * @param bgImgFile ชื่อไฟล์รูปภาพของด่าน
     * @param stage หน้าต่างหลักของแอปพลิเคชัน
     * @return VBox ที่ประกอบไปด้วยรูปภาพด่านและปุ่มเลือก
     */
    private VBox createStageCard(String titleName, String bgImgFile, Stage stage) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);

        boolean isSelected = StartPane.selectedStage.equals(bgImgFile);
        String borderColor = isSelected ? "#f1c40f" : "#555";
        card.setStyle("-fx-background-color: #333; -fx-padding: 20; -fx-background-radius: 10; -fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-border-width: 3;");
        card.setPrefSize(250, 320);

        Label nameLbl = new Label(titleName);
        nameLbl.setFont(new Font("Arial Bold", 20));
        nameLbl.setTextFill(Color.WHITE);

        ImageView bgView = new ImageView();
        try { bgView.setImage(new Image(ClassLoader.getSystemResource("images/" + bgImgFile).toString())); } catch(Exception e){}
        bgView.setFitWidth(200);
        bgView.setFitHeight(150);

        Button selBtn = new Button(isSelected ? "SELECTED" : "SELECT");
        selBtn.setPrefSize(120, 40);
        selBtn.setStyle(isSelected ? "-fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;" : "-fx-base: #555; -fx-text-fill: white;");
        selBtn.setDisable(isSelected);

        selBtn.setOnAction(e -> {
            StartPane.selectedStage = bgImgFile;
            stage.getScene().setRoot(new StartPane(stage)); // กลับหน้าเมนูหลักอัตโนมัติ
        });

        card.getChildren().addAll(nameLbl, bgView, selBtn);
        return card;
    }
}