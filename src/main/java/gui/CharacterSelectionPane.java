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
 * คลาส CharacterSelectionPane สำหรับสร้างหน้าจอเลือกตัวละคร
 * แสดงรูปภาพตัวละครและอาวุธเริ่มต้นแบบการ์ด
 */
public class CharacterSelectionPane extends VBox {

    /**
     * คอนสตรักเตอร์สร้างหน้าจอเลือกตัวละคร
     * @param stage หน้าต่างหลักของแอปพลิเคชัน
     */
    public CharacterSelectionPane(Stage stage) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(40);
        this.setStyle("-fx-background-color: #222;");

        Label title = new Label("CHOOSE YOUR HERO");
        title.setFont(new Font("Arial Bold", 40));
        title.setTextFill(Color.WHITE);

        HBox charsBox = new HBox(30);
        charsBox.setAlignment(Pos.CENTER);

        // สร้างการ์ดของแต่ละตัวละคร
        VBox knightCard = createCharCard("KNIGHT", "knight", "knight1.png", "Knife", "knife.png", stage);
        VBox mageCard = createCharCard("MAGE", "mage", "mage1.png", "Magic Wand", "magicwand.png", stage);
        VBox ninjaCard = createCharCard("NINJA", "ninja", "ninja1.png", "Garlic", "garlic.png", stage);

        charsBox.getChildren().addAll(knightCard, mageCard, ninjaCard);

        // ปุ่มกลับไปเมนูหลัก
        Button backBtn = new Button("BACK TO MENU");
        backBtn.setPrefSize(200, 50);
        backBtn.setStyle("-fx-base: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> stage.getScene().setRoot(new StartPane(stage)));

        this.getChildren().addAll(title, charsBox, backBtn);
    }

    /**
     * สร้างการ์ดแสดงข้อมูลตัวละครและปุ่มเลือก
     * @param titleName ชื่อตัวละครที่แสดงผล
     * @param charType รหัสตัวละครสำหรับใช้งานในระบบ
     * @param charImgFile ชื่อไฟล์รูปภาพตัวละคร
     * @param wpnName ชื่ออาวุธที่แสดงผล
     * @param wpnImgFile ชื่อไฟล์รูปภาพอาวุธ
     * @param stage หน้าต่างหลัก (ใช้สำหรับเปลี่ยนฉาก)
     * @return VBox ที่ประกอบไปด้วยข้อมูลการ์ดทั้งหมด
     */
    private VBox createCharCard(String titleName, String charType, String charImgFile, String wpnName, String wpnImgFile, Stage stage) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);

        // หากเป็นการ์ดที่กำลังเลือกอยู่ ให้แสดงกรอบสีเขียวทอง
        boolean isSelected = StartPane.selectedChar.equals(charType);
        String borderColor = isSelected ? "#f1c40f" : "#555";
        card.setStyle("-fx-background-color: #333; -fx-padding: 20; -fx-background-radius: 10; -fx-border-color: " + borderColor + "; -fx-border-radius: 10; -fx-border-width: 3;");
        card.setPrefSize(220, 320);

        Label nameLbl = new Label(titleName);
        nameLbl.setFont(new Font("Arial Bold", 22));
        nameLbl.setTextFill(Color.WHITE);

        ImageView charView = new ImageView();
        try { charView.setImage(new Image(ClassLoader.getSystemResource("images/" + charImgFile).toString())); } catch(Exception e){}
        charView.setFitWidth(80); charView.setFitHeight(80);

        Label wpnLbl = new Label("Weapon: " + wpnName);
        wpnLbl.setTextFill(Color.LIGHTGRAY);
        wpnLbl.setFont(new Font("Arial", 14));

        ImageView wpnView = new ImageView();
        try { wpnView.setImage(new Image(ClassLoader.getSystemResource("images/" + wpnImgFile).toString())); } catch(Exception e){}
        wpnView.setFitWidth(40); wpnView.setFitHeight(40);

        Button selBtn = new Button(isSelected ? "SELECTED" : "SELECT");
        selBtn.setPrefSize(120, 40);
        selBtn.setStyle(isSelected ? "-fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;" : "-fx-base: #555; -fx-text-fill: white;");
        selBtn.setDisable(isSelected); // ปิดปุ่มถ้าถูกเลือกอยู่แล้ว

        selBtn.setOnAction(e -> {
            StartPane.selectedChar = charType;
            stage.getScene().setRoot(new StartPane(stage)); // กลับหน้าเมนูหลักอัตโนมัติ
        });

        card.getChildren().addAll(nameLbl, charView, wpnLbl, wpnView, selBtn);
        return card;
    }
}