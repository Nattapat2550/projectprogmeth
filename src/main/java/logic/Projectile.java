package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * คลาส Projectile (กระสุนหรือวัตถุโจมตี)
 * จัดการเรื่องพิกัด การเคลื่อนที่ ความเร็ว ความเร่ง ทิศทาง และระยะเวลาคงอยู่
 */
public class Projectile extends Entity {
    private double dx, dy;
    private double accelX, accelY;
    private int life;
    /** ดาเมจของกระสุน */
    public int damage;
    private int type;
    private Player owner;
    private double angle; // มุมสำหรับการโคจร

    /**
     * Constructor แบบระบุมุมเริ่มต้น (ใช้สำหรับอาวุธแบบโคจรรอบตัว เช่น KingBible)
     * @param x ตำแหน่งเริ่มต้นแกน X
     * @param y ตำแหน่งเริ่มต้นแกน Y
     * @param dx ความเร็วแกน X
     * @param dy ความเร็วแกน Y
     * @param ax ความเร่งแกน X
     * @param ay ความเร่งแกน Y
     * @param life ระยะเวลาคงอยู่ของกระสุน (เฟรม)
     * @param damage พลังทำลาย
     * @param imgName ชื่อไฟล์รูปภาพ
     * @param type ประเภทของกระสุน (เช่น 3 = หมุนรอบตัว)
     * @param owner ผู้ที่สร้างกระสุน
     * @param startAngle มุมเริ่มต้นในการโคจร
     */
    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner, double startAngle) {
        super(x, y, 0, imgName);
        this.dx = dx; this.dy = dy; this.accelX = ax; this.accelY = ay;
        this.life = life; this.damage = damage;
        this.type = type; this.owner = owner; this.angle = startAngle;
    }

    /**
     * Constructor แบบ 11 ตัวแปร (สำหรับกระสุนทั่วไปที่พุ่งตรงไป)
     * @param x ตำแหน่งเริ่มต้นแกน X
     * @param y ตำแหน่งเริ่มต้นแกน Y
     * @param dx ความเร็วแกน X
     * @param dy ความเร็วแกน Y
     * @param ax ความเร่งแกน X
     * @param ay ความเร่งแกน Y
     * @param life ระยะเวลาคงอยู่ของกระสุน (เฟรม)
     * @param damage พลังทำลาย
     * @param imgName ชื่อไฟล์รูปภาพ
     * @param type ประเภทของกระสุน
     * @param owner ผู้ที่สร้างกระสุน
     */
    public Projectile(double x, double y, double dx, double dy, double ax, double ay, int life, int damage, String imgName, int type, Player owner) {
        this(x, y, dx, dy, ax, ay, life, damage, imgName, type, owner, 0);
    }

    /**
     * อัปเดตตำแหน่งและการเคลื่อนที่ของกระสุน
     */
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

    /**
     * วาดกระสุนลงบนหน้าจอ
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext)
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.save();
            gc.translate(x, y); // เลื่อนจุดกึ่งกลางมาที่พิกัด x, y ของกระสุน

            // เช็คว่ากระสุนวิ่งไปทางซ้ายหรือไม่ (dx ติดลบ)
            // ถ้ายิงไปทางซ้าย ให้ Flip กระสุนแนวนอน
            if (dx < 0) {
                gc.scale(-1, 1);
            }

            gc.drawImage(image, -15, -15, 30, 30);
            gc.restore();
        } else {
            gc.setFill(Color.WHITE);
            gc.fillOval(x - 6, y - 6, 12, 12);
        }
    }
}