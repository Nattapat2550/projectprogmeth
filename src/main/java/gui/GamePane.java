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
    private List<Potion> potions;
    private List<Projectile> projectiles;
    private int spawnTimer = 0;

    public GamePane(String charType) {
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        this.enemies = new ArrayList<>();
        this.gems = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        try {
            URL bgUrl = ClassLoader.getSystemResource("images/bg.png");
            if (bgUrl == null) bgUrl = ClassLoader.getSystemResource("bg.png");
            if (bgUrl != null) this.bgImage = new Image(bgUrl.toString());
        } catch (Exception e) {
            System.out.println("ไม่สามารถโหลดพื้นหลังได้");
        }

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
        Thread gameLoop = new Thread(() -> {
            while (running) {
                try { Thread.sleep(16); } catch (InterruptedException e) {}
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

        if (player.hasPendingLevelUp()) {
            isPaused = true;
            showWeaponSelection();
            return;
        }

        player.move(up, down, left, right, canvas.getWidth(), canvas.getHeight());

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
                potIt.remove();
            }
        }

        Iterator<ExpGem> gIt = gems.iterator();
        while (gIt.hasNext()) {
            ExpGem g = gIt.next();
            if (Math.hypot(g.getX() - player.getX(), g.getY() - player.getY()) < 30) {
                player.gainExp(g.getExpValue());
                gIt.remove();
            }
        }

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
                resumeGame(overlay);
            });

            card.getChildren().addAll(name, desc, btn);
            options.getChildren().add(card);
        }

        overlay.getChildren().addAll(options);
        this.getChildren().add(overlay);
    }

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

        // ถ้าตัวเลือกไม่ครบ 3 (อาวุธเต็มสล็อตและตันหมดแล้ว) ให้เติม "Heal" หรือ "Max HP"
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

    // Inner class สำหรับเก็บข้อมูลตัวเลือก
    class SelectionOption {
        String name, description; Image image; Runnable action;
        SelectionOption(String n, String d, Image i, Runnable a) { name=n; description=d; image=i; action=a; }
    }

    private void resumeGame(VBox overlay) {
        player.clearPendingLevelUp();
        this.getChildren().remove(overlay);
        isPaused = false;
    }

    private void drawGame() {
        if (bgImage != null) gc.drawImage(bgImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        else { gc.setFill(Color.DARKGREEN); gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); }

        for (ExpGem g : gems) g.draw(gc);
        for (Potion p : potions) p.draw(gc);

        for (Enemy e : enemies) e.draw(gc);
        player.draw(gc);
        for (Projectile p : projectiles) p.draw(gc);
        for (Weapon w : player.getWeapons()) w.draw(gc, player);

        // --- แก้ไข UI ด้านบน ---

        // 1. หลอด EXP สีฟ้า (อยู่บนสุด)
        gc.setFill(Color.rgb(50, 50, 50));
        gc.fillRect(0, 0, canvas.getWidth(), 12);
        gc.setFill(Color.CYAN);
        double expWidth = ((double) player.getExp() / player.getMaxExp()) * canvas.getWidth();
        gc.fillRect(0, 0, expWidth, 12);

        // 2. ตัวหนังสือบอก HP และ Level
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 18));
        gc.fillText("HP: " + player.getHp() + " / " + player.getMaxHp() + " | Level: " + player.getLevel(), 20, 40);

        // 3. หลอดเลือด HP สีเขียว (อยู่ใต้ตัวหนังสือ)
        gc.setFill(Color.RED);
        gc.fillRect(20, 50, 200, 15);
        gc.setFill(Color.LIME);
        double hpBarWidth = ((double)player.getHp() / player.getMaxHp()) * 200;
        gc.fillRect(20, 50, hpBarWidth, 15);


        // UI แสดงช่องอาวุธด้านล่างของหน้าจอ
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
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 50));
            gc.fillText("GAME OVER", canvas.getWidth() / 2 - 140, canvas.getHeight() / 2);
        }
    }
}