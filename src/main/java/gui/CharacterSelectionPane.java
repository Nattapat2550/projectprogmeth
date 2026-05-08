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
 * หน้าจอ GUI สำหรับเลือกตัวละคร
 * แสดงผลเป็นกล่องการ์ดโชว์รูปตัวละครและอาวุธเริ่มต้น
 */
public class CharacterSelectionPane extends VBox {

    /**
     * คอนสตรักเตอร์จัดโครงสร้างหน้าเลือกตัวละคร
     * @param stage หน้าต่างแอปพลิเคชัน
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

        // แอดการ์ดตัวละคร 3 ตัวลงจอ
        charsBox.getChildren().addAll(
                createCharCard("KNIGHT", "knight", "knight1.png", "Knife", "knife.png", stage),
                createCharCard("MAGE", "mage", "mage1.png", "Magic Wand", "magicwand.png", stage),
                createCharCard("NINJA", "ninja", "ninja1.png", "Garlic", "garlic.png", stage)
        );

        // ปุ่มย้อนกลับ
        Button backBtn = new Button("BACK TO MENU");
        backBtn.setPrefSize(200, 50);
        backBtn.setStyle("-fx-base: #555; -fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;");
        backBtn.setOnAction(e -> stage.getScene().setRoot(new StartPane(stage)));

        this.getChildren().addAll(title, charsBox, backBtn);
    }

    /**
     * สร้างกรอบรูปสี่เหลี่ยม (การ์ด) แสดงสถานะตัวละครหนึ่งตัว
     * @param titleName ชื่อที่จะโชว์
     * @param charType รหัสที่ระบบนำไปใช้งาน
     * @param charImgFile ชื่อรูปตัวละคร
     * @param wpnName ชื่ออาวุธ
     * @param wpnImgFile ชื่อรูปอาวุธ
     * @param stage อ้างอิงหน้าต่างเพื่อเด้งกลับไปเมนูหลัก
     * @return VBox กรอบการ์ด GUI
     */
    private VBox createCharCard(String titleName, String charType, String charImgFile, String wpnName, String wpnImgFile, Stage stage) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);

        boolean isSel = StartPane.selectedChar.equals(charType); // เช็คว่าตัวนี้โดนเลือกอยู่ไหม
        card.setStyle("-fx-background-color: #333; -fx-padding: 20; -fx-background-radius: 10; -fx-border-color: " + (isSel ? "#f1c40f" : "#555") + "; -fx-border-radius: 10; -fx-border-width: 3;");
        card.setPrefSize(220, 320);

        Label nameLbl = new Label(titleName);
        nameLbl.setFont(new Font("Arial Bold", 22));
        nameLbl.setTextFill(Color.WHITE);

        ImageView charView = new ImageView();
        try { charView.setImage(new Image(ClassLoader.getSystemResource("images/" + charImgFile).toString())); } catch(Exception e){}
        charView.setFitWidth(80); charView.setFitHeight(80);

        Label wpnLbl = new Label("Weapon: " + wpnName);
        wpnLbl.setTextFill(Color.LIGHTGRAY);

        ImageView wpnView = new ImageView();
        try { wpnView.setImage(new Image(ClassLoader.getSystemResource("images/" + wpnImgFile).toString())); } catch(Exception e){}
        wpnView.setFitWidth(40); wpnView.setFitHeight(40);

        Button selBtn = new Button(isSel ? "SELECTED" : "SELECT");
        selBtn.setPrefSize(120, 40);
        selBtn.setStyle(isSel ? "-fx-base: #27ae60; -fx-text-fill: white; -fx-font-weight: bold;" : "-fx-base: #555; -fx-text-fill: white;");
        selBtn.setDisable(isSel); // ถ้าเลือกอยู่แล้ว ห้ามกดซ้ำ

        // เมื่อกดเลือก อัปเดตค่า static ทั่วโลก แล้วโดดกลับไปเมนู
        selBtn.setOnAction(e -> { StartPane.selectedChar = charType; stage.getScene().setRoot(new StartPane(stage)); });

        card.getChildren().addAll(nameLbl, charView, wpnLbl, wpnView, selBtn);
        return card;
    }
}