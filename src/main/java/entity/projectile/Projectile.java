package entity.projectile;
import entity.base.Entity;
import entity.character.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * คลาสโปรเจกไทล์ (กระสุน, ลูกไฟ, มีด ฯลฯ) ที่ใช้โจมตีศัตรู
 * จัดการเรื่องทิศทาง, ความเร็ว, ระยะเวลาที่คงอยู่ และความเร่ง
 */
public class Projectile extends Entity {
    private double dx, dy, accelX, accelY;
    private int life;
    public int damage;
    private int type;
    private Player owner;
    private double angle;

    /**
     * คอนสตรักเตอร์แบบกำหนดมุมโคจร (สำหรับกระสุนประเภทโคจรรอบตัวผู้เล่น เช่น KingBible)
     * @param x ตำแหน่งเริ่มต้นแกน X
     * @param y ตำแหน่งเริ่มต้นแกน Y
     * @param dx ความเร็วแกน X
     * @param dy ความเร็วแกน Y
     * @param ax ความเร่งแกน X
     * @param ay ความเร่งแกน Y
     * @param life ระยะเวลาที่จะคงอยู่ (นับเป็นเฟรม)
     * @param damage พลังความเสียหายเมื่อชนศัตรู
     * @param imgName ชื่อไฟล์รูปภาพ
     * @param type ชนิดกระสุน (3 = โคจรรอบตัว, อื่นๆ = พุ่งอิสระ)
     * @param owner ผู้เล่นที่ปล่อยกระสุน
     * @param startAngle มุมเริ่มต้น (เรเดียน)
     */
    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner, double startAngle) {
        super(x, y, 0, imgName);
        this.dx = dx; this.dy = dy; this.accelX = ax; this.accelY = ay;
        this.life = life; this.damage = damage; this.type = type; this.owner = owner; this.angle = startAngle;
    }

    /**
     * คอนสตรักเตอร์สำหรับกระสุนยิงตรงปกติ
     * (พารามิเตอร์เหมือนกับคอนสตรักเตอร์หลัก แต่ไม่ใช้มุมโคจร)
     */
    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner) {
        this(x, y, dx, dy, ax, ay, life, damage, imgName, type, owner, 0);
    }

    /**
     * อัปเดตการเคลื่อนที่ของกระสุน ลดอายุลง และลบออกเมื่อหมดอายุ
     */
    @Override
    public void update() {
        if (type == 3) { // ประเภทหมุนรอบตัว
            angle += 0.05;
            x = owner.getX() + Math.cos(angle) * 100;
            y = owner.getY() + Math.sin(angle) * 100;
        } else {
            dx += accelX; dy += accelY;
            x += dx; y += dy;
        }
        life--;
        if (life <= 0) isDead = true;
    }

    /**
     * วาดกระสุนลงบนจอภาพ พร้อมปรับการสะท้อน (Flip) ตามทิศทางวิ่ง
     * @param gc กราฟิกคอนเท็กซ์
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.save();
            gc.translate(x, y);
            if (dx < 0) gc.scale(-1, 1); // หันหัวกระสุนไปทางซ้ายถ้าวิ่งซ้าย
            gc.drawImage(image, -15, -15, 30, 30);
            gc.restore();
        } else {
            gc.setFill(Color.WHITE); gc.fillOval(x - 6, y - 6, 12, 12);
        }
    }
}