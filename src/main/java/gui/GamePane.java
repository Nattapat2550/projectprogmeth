package gui;

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
import logic.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * คลาส GamePane คือหัวใจหลักของการประมวลผลเกม
 * รับผิดชอบ Game Loop, ระบบชนกัน, การวาดภาพลง Canvas และการควบคุมผู้เล่น
 */
public class GamePane extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean running;
    private boolean isPaused = false;
    private Image bgImage;
    private Stage stage;

    private boolean up, down, left, right;

    private Player player;
    private List<Enemy> enemies;
    private List<ExpGem> gems;
    private List<Potion> potions;
    private List<Projectile> projectiles;
    private int spawnTimer = 0;

    // ขนาดแผนที่
    private final double MAP_WIDTH = 2000;
    private final double MAP_HEIGHT = 2000;

    /**
     * คอนสตรักเตอร์เตรียมพื้นที่สำหรับเล่นเกม
     * @param charType อาชีพของผู้เล่นที่เลือก
     * @param stageBg ไฟล์ฉากหลัง
     * @param primaryStage หน้าต่างหลัก
     */
    public GamePane(String charType, String stageBg, Stage primaryStage) {
        this.stage = primaryStage;
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        this.enemies = new ArrayList<>();
        this.gems = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        try {
            URL bgUrl = ClassLoader.getSystemResource("images/" + stageBg);
            if (bgUrl != null) this.bgImage = new Image(bgUrl.toString());
        } catch (Exception e) {}

        double startX = MAP_WIDTH / 2;
        double startY = MAP_HEIGHT / 2;

        if (charType.equals("knight")) this.player = new Knight(startX, startY);
        else if (charType.equals("mage")) this.player = new Mage(startX, startY);
        else if (charType.equals("ninja")) this.player = new Ninja(startX, startY);
        else this.player = new Knight(startX, startY);

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
     * แสดงเมนูหยุดชั่วคราว (Pause)
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
        resumeBtn.setOnAction(e -> {
            this.getChildren().remove(pauseMenu);
            isPaused = false;
        });

        Button exitBtn = new Button("EXIT TO MENU");
        exitBtn.setPrefSize(200, 50);
        exitBtn.setOnAction(e -> {
            running = false;
            stage.setScene(new Scene(new StartPane(stage), 800, 600));
        });

        pauseMenu.getChildren().addAll(title, resumeBtn, exitBtn);
        this.getChildren().add(pauseMenu);
    }

    /**
     * ผูกปุ่มกด WASD เข้ากับสถานะการเคลื่อนที่
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
     * เริ่มรันวงจรการทำงานของเกมในระดับ Thread แยก
     */
    public void startGameLoop() {
        running = true;
        Thread gameLoop = new Thread(() -> {
            while (running) {
                try { Thread.sleep(16); } catch (InterruptedException e) {}
                Platform.runLater(() -> {
                    if (!isPaused) {
                        updateGame();
                        drawGame();
                    }
                });
            }
        });
        gameLoop.setDaemon(true);
        gameLoop.start();
    }

    /**
     * อัปเดตตรรกะในแต่ละเฟรม (พิกัดตัวละคร, ศัตรู, ของดรอป)
     */
    private void updateGame() {
        if (player.isDead() || isPaused) return;

        if (player.hasPendingLevelUp()) {
            isPaused = true;
            showWeaponSelection();
            return;
        }

        player.move(up, down, left, right, MAP_WIDTH, MAP_HEIGHT);

        for (Weapon w : player.getWeapons()) {
            w.update(player, enemies, projectiles);
        }

        Iterator<Projectile> pIt = projectiles.iterator();
        while (pIt.hasNext()) {
            Projectile p = pIt.next();
            p.update();
            if (p.isDead()) { pIt.remove(); continue; }
            for (Enemy e : enemies) {
                if (Math.hypot(p.getX() - e.getX(), p.getY() - e.getY()) < 20) {
                    e.takeDamage(p.damage);
                    SoundManager.playSFX("hit.mp3");
                    p.setDead(true);
                    break;
                }
            }
        }

        Iterator<Potion> potIt = potions.iterator();
        while (potIt.hasNext()) {
            Potion p = potIt.next();
            if (Math.hypot(p.getX() - player.getX(), p.getY() - player.getY()) < 30) {
                player.heal(30);
                SoundManager.playSFX("heal.mp3");
                potIt.remove();
            }
        }

        Iterator<ExpGem> gIt = gems.iterator();
        while (gIt.hasNext()) {
            ExpGem g = gIt.next();
            if (Math.hypot(g.getX() - player.getX(), g.getY() - player.getY()) < 30) {
                player.gainExp(g.getExpValue());
                SoundManager.playSFX("pickup.mp3");
                gIt.remove();
            }
        }

        spawnTimer++;
        if (spawnTimer >= 45) {
            double angle = Math.random() * Math.PI * 2;
            double radius = 600;
            double spawnX = player.getX() + Math.cos(angle) * radius;
            double spawnY = player.getY() + Math.sin(angle) * radius;

            spawnX = Math.max(0, Math.min(spawnX, MAP_WIDTH));
            spawnY = Math.max(0, Math.min(spawnY, MAP_HEIGHT));

            double randEnemy = Math.random();
            if (randEnemy < 0.33) {
                enemies.add(new Bat(spawnX, spawnY, player));
            } else if (randEnemy < 0.66) {
                enemies.add(new Zombie(spawnX, spawnY, player));
            } else {
                enemies.add(new Ghost(spawnX, spawnY, player));
            }
            spawnTimer = 0;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update();
            if (e.isDead()) {
                double dropRate = Math.random();
                if (dropRate < 0.05) {
                    potions.add(new Potion(e.getX(), e.getY()));
                } else {
                    double expRate = Math.random();
                    if (expRate < 0.6) gems.add(new ExpGem(e.getX(), e.getY(), "blue.png", 10));
                    else if (expRate < 0.9) gems.add(new ExpGem(e.getX(), e.getY(), "red.png", 20));
                    else gems.add(new ExpGem(e.getX(), e.getY(), "purple.png", 50));
                }
                it.remove();
            }
        }
    }

    /**
     * แสดงหน้าจอให้เลือกไอเทม/อาวุธ เมื่อผู้เล่นเลเวลอัป
     */
    private void showWeaponSelection() {
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPrefSize(800, 600);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.8);");

        HBox options = new HBox(15);
        options.setAlignment(Pos.CENTER);

        List<SelectionOption> choices = generateThreeChoices();

        for (SelectionOption opt : choices) {
            VBox card = new VBox(10);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-background-color: #333; -fx-padding: 15; -fx-background-radius: 10; -fx-border-color: gold;");
            card.setPrefSize(200, 250);

            if (opt.image != null) {
                ImageView imgView = new ImageView(opt.image);
                imgView.setFitWidth(60); imgView.setFitHeight(60);
                card.getChildren().add(imgView);
            }

            Label name = new Label(opt.name);
            name.setTextFill(Color.WHITE);
            name.setFont(new Font(18));

            Label desc = new Label(opt.description);
            desc.setTextFill(Color.LIGHTGRAY);

            Button btn = new Button("SELECT");
            btn.setStyle("-fx-font-size: 14px; -fx-base: #555; -fx-text-fill: white; -fx-cursor: hand;");
            btn.setOnAction(e -> {
                opt.action.run();
                resumeFromLevelUp(overlay);
            });

            card.getChildren().addAll(name, desc, btn);
            options.getChildren().add(card);
        }

        overlay.getChildren().addAll(options);
        this.getChildren().add(overlay);
    }

    /**
     * สร้างรายการสุ่มตัวเลือกไอเทม 3 อย่าง สำหรับหน้าเลเวลอัป
     * @return ลิสต์ตัวเลือกที่สุ่มได้
     */
    private List<SelectionOption> generateThreeChoices() {
        List<SelectionOption> list = new ArrayList<>();
        List<Weapon> allAvailable = new ArrayList<>(Arrays.asList(new Whip(), new MagicWand(), new Knife(), new Axe(), new Cross(), new KingBible(), new FireWand(), new Garlic()));
        Collections.shuffle(allAvailable);

        for (Weapon w : allAvailable) {
            Weapon existing = player.getWeapons().stream().filter(ex -> ex.getClass().equals(w.getClass())).findFirst().orElse(null);

            if (existing != null && existing.getLevel() < 4) {
                list.add(new SelectionOption(w.getClass().getSimpleName(), "Level Up to " + (existing.getLevel()+1), w.getImage(), () -> player.addOrUpgradeWeapon(w)));
            } else if (existing == null && player.getWeapons().size() < player.MAX_WEAPON_SLOTS) {
                list.add(new SelectionOption(w.getClass().getSimpleName(), "New Weapon!", w.getImage(), () -> player.addOrUpgradeWeapon(w)));
            }
            if (list.size() >= 3) break;
        }

        while (list.size() < 3) {
            if (Math.random() > 0.5) {
                Image potionImg = null;
                try { potionImg = new Image(ClassLoader.getSystemResource("images/potion.png").toString()); } catch (Exception e) {}
                list.add(new SelectionOption("HEAL", "Restore 50 HP", potionImg, () -> player.heal(50)));
            } else {
                Image knightImg = null;
                try { knightImg = new Image(ClassLoader.getSystemResource("images/knight.png").toString()); } catch (Exception e) {}
                list.add(new SelectionOption("MAX HP", "Increase Max HP by 20", knightImg, () -> player.increaseMaxHp(20)));
            }
        }
        return list;
    }

    /**
     * คลาสภายในสำหรับจัดเก็บโครงสร้างตัวเลือกอัปเกรด
     */
    class SelectionOption {
        String name, description; Image image; Runnable action;
        SelectionOption(String n, String d, Image i, Runnable a) { name=n; description=d; image=i; action=a; }
    }

    /**
     * ยกเลิกหน้าจอเลเวลอัปเพื่อเล่นต่อ
     * @param overlay VBox ป๊อปอัปหน้าจอเลเวลอัป
     */
    private void resumeFromLevelUp(VBox overlay) {
        player.clearPendingLevelUp();
        this.getChildren().remove(overlay);
        isPaused = false;
    }

    /**
     * วาดวัตถุทั้งหมดบนหน้าจอ โดยมีกล้อง (Camera) ตามติดผู้เล่น
     */
    private void drawGame() {
        double camX = player.getX() - canvas.getWidth() / 2;
        double camY = player.getY() - canvas.getHeight() / 2;

        camX = Math.max(0, Math.min(camX, MAP_WIDTH - canvas.getWidth()));
        camY = Math.max(0, Math.min(camY, MAP_HEIGHT - canvas.getHeight()));

        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.save();
        gc.translate(-camX, -camY);

        if (bgImage != null) {
            gc.drawImage(bgImage, 0, 0, MAP_WIDTH, MAP_HEIGHT);
        } else {
            gc.setFill(Color.DARKGREEN);
            gc.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);
        }

        for (ExpGem g : gems) g.draw(gc);
        for (Potion p : potions) p.draw(gc);
        for (Enemy e : enemies) e.draw(gc);
        player.draw(gc);
        for (Projectile p : projectiles) p.draw(gc);
        for (Weapon w : player.getWeapons()) w.draw(gc, player);

        gc.restore();

        // --- วาด UI ด้านบนกล้อง ---
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(0, 0, canvas.getWidth(), 12);
        gc.setFill(Color.CYAN);
        double expWidth = ((double) player.getExp() / player.getMaxExp()) * canvas.getWidth();
        gc.fillRect(0, 0, expWidth, 12);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 18));
        gc.fillText("HP: " + player.getHp() + " / " + player.getMaxHp() + " | Level: " + player.getLevel(), 20, 40);

        gc.setFill(Color.RED);
        gc.fillRect(20, 50, 200, 15);
        gc.setFill(Color.LIME);
        double hpBarWidth = ((double)player.getHp() / player.getMaxHp()) * 200;
        gc.fillRect(20, 50, hpBarWidth, 15);

        double startX = 20;
        double startY = canvas.getHeight() - 70;
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

        if (player.isDead()) {
            SoundManager.stopBGM();
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("GAME OVER", canvas.getWidth() / 2 - 140, canvas.getHeight() / 2);
        }
    }
}