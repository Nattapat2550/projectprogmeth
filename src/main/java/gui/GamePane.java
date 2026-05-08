package gui;
import entity.base.Entity;
import entity.base.ICollectable;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import entity.weapon.*;
import logic.GameConfig;
import logic.GameLogic;
import logic.SoundManager;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * คลาสทำหน้าที่เป็น "หน้าจอแสดงผลเกมเพลย์" รับปุ่มบังคับและวาดรูปเท่านั้น (View Layer)
 * ส่วนการคำนวณถูกย้ายไปให้คลาส GameLogic ที่แพ็กเกจ logic ดำเนินการหมดแล้ว
 */
public class GamePane extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean running, isPaused = false;
    private Image bgImage;
    private Stage stage;
    private boolean up, down, left, right;

    /** กลไกตรรกะเกมเบื้องหลัง */
    private GameLogic gameLogic;

    /**
     * สร้างหน้าจอเกม เตรียมแคนวาส และสร้างระบบตรรกะผูกคู่กัน
     * @param charType รหัสอาชีพตัวละคร
     * @param stageBg ไฟล์ภาพฉาก
     * @param primaryStage หน้าต่างแอปพลิเคชัน
     */
    public GamePane(String charType, String stageBg, Stage primaryStage) {
        this.stage = primaryStage;
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        // โยนหน้าที่คำนวณทั้งหมดให้ GameLogic สร้างและจัดการ
        this.gameLogic = new GameLogic(charType);

        try {
            URL bgUrl = ClassLoader.getSystemResource("images/" + stageBg);
            if (bgUrl != null) this.bgImage = new Image(bgUrl.toString());
        } catch (Exception e) {}

        // ปุ่มหยุดเกม (Pause) แบบลอยบนจอ
        Button pauseBtn = new Button("||");
        pauseBtn.setLayoutX(750);
        pauseBtn.setLayoutY(20);
        pauseBtn.setPrefSize(40, 40);
        pauseBtn.setFocusTraversable(false);
        pauseBtn.setStyle("-fx-base: #333; -fx-text-fill: white; -fx-font-size: 16px;");
        pauseBtn.setOnAction(e -> pauseGame());
        this.getChildren().add(pauseBtn);

        SoundManager.playBGM("bgm.mp3");
    }

    /**
     * เมธอดเรียกเปิดหน้าต่างหยุดเกมทับหน้าจอ
     */
    private void pauseGame() {
        if (isPaused) return;
        isPaused = true;

        VBox pauseMenu = new VBox(20);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setPrefSize(800, 600);
        pauseMenu.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        Label title = new Label("GAME PAUSED");
        title.setFont(new Font("Arial Bold", 40));
        title.setTextFill(Color.WHITE);

        Button resumeBtn = new Button("RESUME");
        resumeBtn.setPrefSize(200, 50);
        resumeBtn.setOnAction(e -> { this.getChildren().remove(pauseMenu); isPaused = false; });

        Button exitBtn = new Button("EXIT TO MENU");
        exitBtn.setPrefSize(200, 50);
        exitBtn.setOnAction(e -> { running = false; stage.setScene(new Scene(new StartPane(stage), 800, 600)); });

        pauseMenu.getChildren().addAll(title, resumeBtn, exitBtn);
        this.getChildren().add(pauseMenu);
    }

    /**
     * ผูกปุ่มคีย์บอร์ดกับตัวแปร up, down, left, right (WASD)
     * @param scene ฉากปัจจุบัน
     */
    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (isPaused) return;
            switch (e.getCode()) {
                case W: up = true; break;
                case S: down = true; break;
                case A: left = true; break;
                case D: right = true; break;
            }
        });
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W: up = false; break;
                case S: down = false; break;
                case A: left = false; break;
                case D: right = false; break;
            }
        });
    }

    /**
     * เปิดใช้งาน Game Loop ให้วิ่งที่ประมาณ 60 เฟรมต่อวินาที (16ms)
     */
    public void startGameLoop() {
        running = true;
        Thread gameLoop = new Thread(() -> {
            while (running) {
                try { Thread.sleep(16); } catch (InterruptedException e) {}
                Platform.runLater(() -> {
                    if (!isPaused) {
                        // 1. สั่ง GameLogic คำนวณขยับตำแหน่งและชน
                        gameLogic.update(up, down, left, right);

                        // 2. ถ้าหลอด EXP เต็ม ให้หยุดเกมแล้วโชว์หน้าต่างเลเวลอัป
                        if (gameLogic.getPlayer().hasPendingLevelUp()) {
                            isPaused = true;
                            showWeaponSelection();
                        } else {
                            // 3. เอาของใน GameLogic มาวาด (Draw) ลงจอ
                            drawGame();
                        }
                    }
                });
            }
        });
        gameLoop.setDaemon(true);
        gameLoop.start();
    }

    /**
     * โชว์หน้าต่าง 3 ตัวเลือก ให้สุ่มอัปเกรดอาวุธตอนเลเวลอัป
     */
    private void showWeaponSelection() {
        Player player = gameLogic.getPlayer();
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPrefSize(800, 600);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        HBox options = new HBox(15);
        options.setAlignment(Pos.CENTER);

        List<Weapon> all = new ArrayList<>(Arrays.asList(new Whip(), new MagicWand(), new Knife(), new Axe(), new Cross(), new KingBible(), new FireWand(), new Garlic()));
        Collections.shuffle(all);
        int count = 0;

        for (Weapon w : all) {
            Weapon existing = player.getWeapons().stream().filter(ex -> ex.getClass().equals(w.getClass())).findFirst().orElse(null);

            // กรณีมีอาวุธนี้อยู่แล้ว โชว์ให้เลเวลอัป
            if (existing != null && existing.getLevel() < 4) {
                options.getChildren().add(createSelectCard(w.getClass().getSimpleName(), "Level Up to " + (existing.getLevel()+1), w.getImage(), () -> player.addOrUpgradeWeapon(w), overlay));
                count++;
                // กรณียังไม่มี และช่องสล็อตอาวุธยังไม่เต็ม
            } else if (existing == null && player.getWeapons().size() < player.MAX_WEAPON_SLOTS) {
                options.getChildren().add(createSelectCard(w.getClass().getSimpleName(), "New Weapon!", w.getImage(), () -> player.addOrUpgradeWeapon(w), overlay));
                count++;
            }
            if (count >= 3) break;
        }

        // กรณีสุ่มอาวุธไม่ได้ (ของเต็มแม็กซ์หมด) ให้ยาเพิ่มเลือด หรือเพิ่มเลือดสูงสุดแทน
        while (count < 3) {
            if (Math.random() > 0.5) options.getChildren().add(createSelectCard("HEAL", "Restore 50 HP", null, () -> player.heal(50), overlay));
            else options.getChildren().add(createSelectCard("MAX HP", "Increase Max HP by 20", null, () -> player.increaseMaxHp(20), overlay));
            count++;
        }

        overlay.getChildren().addAll(options);
        this.getChildren().add(overlay);
    }

    /**
     * สร้างกล่อง GUI ของไอเทมแต่ละตัวสำหรับหน้าเลเวลอัป
     * @param name ชื่ออาวุธ/ไอเทม
     * @param desc คำอธิบาย
     * @param img รูปภาพ
     * @param action สิ่งที่เกิดขึ้น(ฟังก์ชัน) เมื่อกดปุ่มเลือก
     * @param overlay ป๊อปอัปใหญ่ (เพื่อให้สั่งปิดได้ตอนกด)
     * @return กล่อง VBox
     */
    private VBox createSelectCard(String name, String desc, Image img, Runnable action, VBox overlay) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: #333; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: gold;");
        card.setPrefSize(200, 250);

        if (img != null) {
            ImageView v = new ImageView(img);
            v.setFitWidth(60); v.setFitHeight(60);
            card.getChildren().add(v);
        }

        Label nLbl = new Label(name);
        nLbl.setTextFill(Color.WHITE);
        nLbl.setFont(new Font(18));

        Label dLbl = new Label(desc);
        dLbl.setTextFill(Color.LIGHTGRAY);

        Button btn = new Button("SELECT");
        btn.setStyle("-fx-font-size: 14px; -fx-base: #555; -fx-text-fill: white; -fx-cursor: hand;");
        btn.setOnAction(e -> {
            action.run();
            gameLogic.getPlayer().clearPendingLevelUp();
            this.getChildren().remove(overlay);
            isPaused = false;
        });

        card.getChildren().addAll(nLbl, dLbl, btn);
        return card;
    }

    /**
     * ดึงข้อมูลต่างๆ ใน GameLogic มาวาดจุดลงบนจอ (Canvas)
     * มีการคำนวณทำกล้องติดตาม (Camera) ผู้เล่นอัตโนมัติ
     */
    private void drawGame() {
        Player player = gameLogic.getPlayer();

        // คำนวณจุดตั้งต้นที่กล้องจะมอง (ล้อคให้อยู่ในกรอบแมพ)
        double camX = Math.max(0, Math.min(player.getX() - canvas.getWidth() / 2, GameConfig.MAP_WIDTH - canvas.getWidth()));
        double camY = Math.max(0, Math.min(player.getY() - canvas.getHeight() / 2, GameConfig.MAP_HEIGHT - canvas.getHeight()));

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.save();
        gc.translate(-camX, -camY); // ขยับกระดาษตามจุดที่กล้องอยู่

        // วาดฉากหลัง
        if (bgImage != null) gc.drawImage(bgImage, 0, 0, GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT);
        else { gc.setFill(Color.DARKGREEN); gc.fillRect(0, 0, GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT); }

        // ให้ของทุกอย่างวาดตัวเอง (Polymorphism ของ Renderable Interface)
        for (ICollectable item : gameLogic.getItems()) if(item instanceof Entity) ((Entity)item).draw(gc);
        for (Enemy e : gameLogic.getEnemies()) e.draw(gc);
        player.draw(gc);
        for (Projectile p : gameLogic.getProjectiles()) p.draw(gc);
        for (Weapon w : player.getWeapons()) w.draw(gc, player);

        gc.restore(); // ยกเลิกสถานะขยับกระดาษ กลับมาวาด UI แบบตรึงพิกัดตายตัว

        // วาด UI หลอด EXP ด้านบนสุด
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(0, 0, canvas.getWidth(), 12);
        gc.setFill(Color.CYAN);
        gc.fillRect(0, 0, ((double) player.getExp() / player.getMaxExp()) * canvas.getWidth(), 12);

        // วาด UI ข้อความ และหลอด HP สีเขียวแดง
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 18));
        gc.fillText("HP: " + player.getHp() + " / " + player.getMaxHp() + " | Level: " + player.getLevel(), 20, 40);
        gc.setFill(Color.RED);
        gc.fillRect(20, 50, 200, 15);
        gc.setFill(Color.LIME);
        gc.fillRect(20, 50, ((double)player.getHp() / player.getMaxHp()) * 200, 15);

        // วาด UI ช่องแสดงอาวุธมุมล่างซ้าย
        double startX = 20, startY = canvas.getHeight() - 70;
        gc.setFont(new Font("Arial", 14));
        for (Weapon w : player.getWeapons()) {
            gc.setFill(Color.rgb(0, 0, 0, 0.6));
            gc.fillRoundRect(startX, startY, 50, 50, 10, 10);
            gc.setStroke(Color.GRAY);
            gc.strokeRoundRect(startX, startY, 50, 50, 10, 10);
            if(w.getImage() != null) gc.drawImage(w.getImage(), startX + 5, startY + 5, 40, 40);
            gc.setFill(Color.YELLOW);
            gc.fillText("Lv." + w.getLevel(), startX + 5, startY - 5);
            startX += 65;
        }

        // หน้าจอ Game Over (เมื่อตาย)
        if (player.isDead()) {
            SoundManager.stopBGM();
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("GAME OVER", canvas.getWidth() / 2 - 140, canvas.getHeight() / 2);
        }
    }
}