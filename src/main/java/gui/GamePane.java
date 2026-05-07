package gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import logic.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GamePane extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean running;
    private javafx.scene.image.Image bgImage;
    private boolean up, down, left, right;

    private Player player;
    private List<Enemy> enemies;
    private int spawnTimer = 0;

    public GamePane(String charType) {
        this.canvas = new Canvas(800, 600);
        this.gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        this.enemies = new ArrayList<>();
        try {
            java.net.URL bgUrl = ClassLoader.getSystemResource("images/bg.png");
            if (bgUrl == null) bgUrl = ClassLoader.getSystemResource("bg.png");
            if (bgUrl != null) {
                this.bgImage = new javafx.scene.image.Image(bgUrl.toString());
            }
        } catch (Exception e) {
            System.out.println("หาไฟล์ bg.png ไม่พบ");
        }
        if (charType.equals("knight")) {
            this.player = new Knight(400, 300);
        } else if (charType.equals("mage")) {
            this.player = new Mage(400, 300);
        } else if (charType.equals("ninja")) {
            this.player = new Ninja(400, 300);
        } else {
            this.player = new Knight(400, 300); // Default
        }
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
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
        if (player.isDead()) {
            running = false;
            return;
        }

        player.move(up, down, left, right, canvas.getWidth(), canvas.getHeight());
        player.update();

        spawnTimer++;
        if (spawnTimer >= 60) {
            double spawnX = Math.random() > 0.5 ? 0 : canvas.getWidth();
            double spawnY = Math.random() * canvas.getHeight();
            enemies.add(new Enemy(spawnX, spawnY, player));
            spawnTimer = 0;
        }

        if (player.canAttack()) {
            boolean attacked = false;
            for (Enemy e : enemies) {
                double dx = e.getX() - player.getX();
                double dy = e.getY() - player.getY();
                double dist = Math.sqrt(dx*dx + dy*dy);
                if (dist <= player.getAttackRadius()) {
                    e.setDead(true);
                    attacked = true;
                }
            }
            if (attacked) player.resetCooldown();
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update();
            if (e.isDead()) it.remove();
        }
    }

    private void drawGame() {
        if (bgImage != null) {
            // ถ้ารูป bg.png โหลดสำเร็จ ให้วาดรูปให้เต็มจอ
            gc.drawImage(bgImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            // ถ้าหาไฟล์ไม่เจอ ให้เทสีเขียวเหมือนเดิม
            gc.setFill(Color.DARKGREEN);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(1);
        gc.strokeOval(player.getX() - player.getAttackRadius(),
                player.getY() - player.getAttackRadius(),
                player.getAttackRadius() * 2,
                player.getAttackRadius() * 2);

        for (Enemy e : enemies) e.draw(gc);
        player.draw(gc);

        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + player.getHp(), 20, 30);

        if (player.isDead()) {
            gc.setFill(Color.RED);
            gc.fillText("GAME OVER", canvas.getWidth()/2 - 30, canvas.getHeight()/2);
        }
    }
}