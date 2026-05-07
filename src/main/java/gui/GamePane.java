package gui;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import logic.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class GamePane extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean running;
    private boolean isPaused = false;
    private Image bgImage;

    private boolean up, down, left, right;

    private Player player;
    private List<Enemy> enemies;
    private List<ExpGem> gems;
    private List<Projectile> projectiles;
    private int spawnTimer = 0;

    public GamePane(String charType) {
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        this.enemies = new ArrayList<>();
        this.gems = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        // โหลดพื้นหลังด้วย ClassLoader ตามสไลด์ GUI หน้า 81
        try {
            URL bgUrl = ClassLoader.getSystemResource("images/bg.png");
            if (bgUrl == null) bgUrl = ClassLoader.getSystemResource("bg.png");
            if (bgUrl != null) this.bgImage = new Image(bgUrl.toString());
        } catch (Exception e) {
            System.out.println("ไม่สามารถโหลดพื้นหลังได้");
        }

        // สร้างตัวละครตามที่เลือก
        if (charType.equals("knight")) this.player = new Knight(400, 300);
        else if (charType.equals("mage")) this.player = new Mage(400, 300);
        else if (charType.equals("ninja")) this.player = new Ninja(400, 300);
        else this.player = new Knight(400, 300);
    }

    public void setupInput(Scene scene) {
        scene.setOnKeyPressed(e -> {
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

    public void startGameLoop() {
        running = true;
        // ใช้ Thread แยกเพื่อไม่ให้ UI ค้าง ตามสไลด์ Thread
        Thread gameLoop = new Thread(() -> {
            while (running) {
                try { Thread.sleep(16); } catch (InterruptedException e) {}
                // ใช้ Platform.runLater เพื่ออัปเดต UI จาก Thread อื่น
                Platform.runLater(() -> {
                    updateGame();
                    drawGame();
                });
            }
        });
        gameLoop.setDaemon(true);
        gameLoop.start();
    }

    private void updateGame() {
        if (player.isDead() || isPaused) return;

        // ตรวจสอบระบบ Level Up
        if (player.hasPendingLevelUp()) {
            isPaused = true;
            showWeaponSelection();
            return;
        }

        player.move(up, down, left, right, canvas.getWidth(), canvas.getHeight());

        // อัปเดตอาวุธ
        for (Weapon w : player.getWeapons()) {
            w.update(player, enemies, projectiles);
        }

        // อัปเดตกระสุนและการชน
        Iterator<Projectile> pIt = projectiles.iterator();
        while (pIt.hasNext()) {
            Projectile p = pIt.next();
            p.update();
            if (p.isDead()) { pIt.remove(); continue; }
            for (Enemy e : enemies) {
                if (Math.hypot(p.getX() - e.getX(), p.getY() - e.getY()) < 20) {
                    e.takeDamage(p.damage);
                    p.setDead(true);
                    break;
                }
            }
        }

        // การเก็บเม็ด EXP
        Iterator<ExpGem> gIt = gems.iterator();
        while (gIt.hasNext()) {
            ExpGem g = gIt.next();
            if (Math.hypot(g.getX() - player.getX(), g.getY() - player.getY()) < 30) {
                player.gainExp(g.getExpValue());
                gIt.remove();
            }
        }

        // การเกิดของศัตรูและการดรอปไอเทม
        spawnTimer++;
        if (spawnTimer >= 45) {
            enemies.add(new Enemy(Math.random() > 0.5 ? 0 : 800, Math.random() * 600, player));
            spawnTimer = 0;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update();
            if (e.isDead()) {
                // สุ่มดรอปเพชร 3 สี
                double dropRate = Math.random();
                if (dropRate < 0.6) gems.add(new ExpGem(e.getX(), e.getY(), "blue.png", 10));
                else if (dropRate < 0.9) gems.add(new ExpGem(e.getX(), e.getY(), "red.png", 20));
                else gems.add(new ExpGem(e.getX(), e.getY(), "purple.png", 50));
                it.remove();
            }
        }
    }

    private void showWeaponSelection() {
        // สร้างหน้าจอ Overlay เลือกอาวุธ
        VBox overlay = new VBox(20);
        overlay.setAlignment(Pos.CENTER);
        overlay.setPrefSize(800, 600);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);");

        Label title = new Label("LEVEL UP! CHOOSE A WEAPON");
        title.setTextFill(Color.WHITE);
        title.setFont(new Font("Arial", 32));

        HBox options = new HBox(15);
        options.setAlignment(Pos.CENTER);

        // รายการอาวุธทั้งหมดเพื่อสุ่มให้เลือก
        List<Weapon> pool = Arrays.asList(
                new Whip(), new MagicWand(), new Knife(), new Axe(),
                new Cross(), new KingBible(), new FireWand(), new Garlic()
        );
        Collections.shuffle(pool);

        for (int i = 0; i < 3; i++) {
            Weapon choice = pool.get(i);
            Button btn = new Button(choice.getClass().getSimpleName());
            btn.setPrefSize(160, 100);
            btn.setStyle("-fx-font-size: 16px; -fx-base: #444; -fx-text-fill: white;");
            btn.setOnAction(e -> {
                player.addWeapon(choice);
                player.clearPendingLevelUp();
                this.getChildren().remove(overlay);
                isPaused = false;
            });
            options.getChildren().add(btn);
        }

        overlay.getChildren().addAll(title, options);
        this.getChildren().add(overlay);
    }

    private void drawGame() {
        if (bgImage != null) gc.drawImage(bgImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        else { gc.setFill(Color.DARKGREEN); gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); }

        for (ExpGem g : gems) g.draw(gc);
        for (Enemy e : enemies) e.draw(gc);
        player.draw(gc);
        for (Projectile p : projectiles) p.draw(gc);
        for (Weapon w : player.getWeapons()) w.draw(gc, player);

        // วาด UI (HP, Level, EXP Bar)
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 18));
        gc.fillText("HP: " + player.getHp() + " | Level: " + player.getLevel(), 20, 40);

        // หลอด EXP ด้านบน
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(0, 0, canvas.getWidth(), 12);
        gc.setFill(Color.CYAN);
        double expWidth = ((double) player.getExp() / player.getMaxExp()) * canvas.getWidth();
        gc.fillRect(0, 0, expWidth, 12);

        if (player.isDead()) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("GAME OVER", canvas.getWidth() / 2 - 140, canvas.getHeight() / 2);
        }
    }
}