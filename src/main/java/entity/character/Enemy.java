package entity.character;
import entity.base.Entity;
import javafx.scene.canvas.GraphicsContext;

/**
 * คลาสนามธรรมสำหรับควบคุมมอนสเตอร์ (ศัตรู)
 * จัดการเรื่องระบบการเดินไล่ล่าตามตัวผู้เล่น (AI)
 */
public abstract class Enemy extends Entity {
    protected Player target;
    protected int hp;
    protected boolean facingRight = true;

    /**
     * สร้างศัตรู
     * @param x ตำแหน่งเกิดแกน X
     * @param y ตำแหน่งเกิดแกน Y
     * @param speed ความเร็วเดินไล่
     * @param imageName ไฟล์รูปศัตรู
     * @param hp เลือด
     * @param target เป้าหมายที่ศัตรูจะเดินตามล่า
     */
    public Enemy(double x, double y, double speed, String imageName, int hp, Player target) {
        super(x, y, speed, imageName);
        this.target = target;
        this.hp = hp;
    }

    /** * รับดาเมจ หากเลือดหมดจะตาย
     * @param damage ปริมาณที่โดนตี
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) this.isDead = true;
    }

    /**
     * อัปเดตพิกัด เดินเป็นเส้นตรงเข้าหาเป้าหมาย
     * และถ้าเข้าประชิดตัวผู้เล่นได้ ศัตรูจะทำดาเมจใส่ผู้เล่นแล้วตายเอง
     */
    @Override
    public void update() {
        if (target == null || target.isDead()) return;
        double dx = target.getX() - x, dy = target.getY() - y;
        double distance = Math.hypot(dx, dy); // หาระยะห่างจัดๆ

        // เดินเข้าหา
        if (distance > 0) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
        facingRight = dx > 0;

        // ชนผู้เล่นแล้ว
        if (distance < 20) {
            target.takeDamage(10);
            this.isDead = true;
        }
    }

    /**
     * วาดศัตรูพร้อมพลิกด้านหันหน้าไปทางที่เดิน
     * @param gc กราฟิกคอนเท็กซ์
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.save();
            gc.translate(x, y);
            if (!facingRight) gc.scale(-1, 1);
            gc.drawImage(image, -20, -20, 40, 40);
            gc.restore();
        }
    }
}