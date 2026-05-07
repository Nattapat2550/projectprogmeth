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
    private double angle;

    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner) {
        super(x, y, 0, imgName); // ส่งชื่อไฟล์ เช่น "knife.png" ไปให้ Entity โหลดรูป
        this.dx = dx; this.dy = dy; this.accelX = ax; this.accelY = ay;
        this.life = life; this.damage = damage;
        this.type = type; this.owner = owner; this.angle = 0;
    }

    @Override
    public void update() {
        if (type == 3) { // สำหรับ KingBible ให้โคจรรอบตัว
            angle += 0.1;
            x = owner.getX() + Math.cos(angle) * 80;
            y = owner.getY() + Math.sin(angle) * 80;
        } else {
            dx += accelX; dy += accelY;
            x += dx; y += dy;
        }
        life--;
        if (life <= 0) isDead = true;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            // ถ้ารูปโหลดสำเร็จ ให้วาดรูปอาวุธ
            gc.drawImage(image, x - 15, y - 15, 30, 30);
        } else {
            // ถ้าไม่มีรูป ให้วาดลูกกลมๆ สีขาวแทน
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 6, y - 6, 12, 12);
        }
    }
}