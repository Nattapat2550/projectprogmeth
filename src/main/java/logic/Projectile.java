package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Projectile extends Entity {
    private double dx, dy;
    private double accelX, accelY;
    private int life;
    public int damage;
    private int type;
    private Player owner;
    private double angle; // มุมสำหรับการโคจร

    // 🎯 Constructor แบบ 12 ตัวแปร (ใช้สำหรับ KingBible ที่ต้องระบุมุมเริ่มต้น)
    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner, double startAngle) {
        super(x, y, 0, imgName);
        this.dx = dx; this.dy = dy; this.accelX = ax; this.accelY = ay;
        this.life = life; this.damage = damage;
        this.type = type; this.owner = owner; this.angle = startAngle;
    }

    // 🎯 Constructor แบบ 11 ตัวแปร (ใช้สำหรับอาวุธอื่นๆ เช่น Knife, Axe ที่ยิงออกไปตรงๆ)
    // มันจะดึงค่าไปเข้า Constructor ตัวบนให้อัตโนมัติ โดยเซ็ตค่ามุมเป็น 0 ให้
    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner) {
        this(x, y, dx, dy, ax, ay, life, damage, imgName, type, owner, 0);
    }

    @Override
    public void update() {
        if (type == 3) {
            // สำหรับ KingBible ให้โคจรรอบตัว
            angle += 0.05; // ความเร็วในการหมุน
            x = owner.getX() + Math.cos(angle) * 100; // รัศมีวงโคจร
            y = owner.getY() + Math.sin(angle) * 100;
        } else {
            // กระสุนปกติ (วิ่งไปตามทิศทาง)
            dx += accelX;
            dy += accelY;
            x += dx;
            y += dy;
        }
        life--;
        if (life <= 0) isDead = true;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x - 15, y - 15, 30, 30);
        } else {
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 6, y - 6, 12, 12);
        }
    }
}