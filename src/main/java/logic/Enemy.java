package logic;

import javafx.scene.canvas.GraphicsContext;

/**
 * คลาสนามธรรมที่เป็นต้นแบบของศัตรูทุกประเภทในเกม
 * จัดการเรื่องการไล่ตามผู้เล่นและการคำนวณความเสียหาย
 */
public abstract class Enemy extends Entity {
    protected Player target;
    protected int hp;
    protected boolean facingRight = true;

    /**
     * สร้างศัตรูพร้อมกำหนดเป้าหมายในการโจมตี
     * @param x ตำแหน่ง X เริ่มต้น
     * @param y ตำแหน่ง Y เริ่มต้น
     * @param speed ความเร็วในการเคลื่อนที่
     * @param imageName ชื่อไฟล์รูปภาพศัตรู
     * @param hp พลังชีวิตเริ่มต้น
     * @param target ตัวละครผู้เล่นที่ศัตรูจะเดินตาม
     */
    public Enemy(double x, double y, double speed, String imageName, int hp, Player target) {
        super(x, y, speed, imageName);
        this.target = target;
        this.hp = hp;
    }

    /**
     * ลดพลังชีวิตของศัตรู และตรวจสอบสถานะการตาย
     * @param damage ปริมาณความเสียหายที่ได้รับ
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) this.isDead = true;
    }

    /**
     * คำนวณทิศทางเพื่อเดินเข้าหาผู้เล่นและตรวจสอบการชน
     * จะอัปเดตตำแหน่ง x, y ของศัตรูให้เคลื่อนที่เข้าหาเป้าหมาย
     */
    @Override
    public void update() {
        if (target == null || target.isDead()) return;
        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
        if (dx > 0) facingRight = true;
        else if (dx < 0) facingRight = false;

        // หากศัตรูเข้าใกล้ผู้เล่นมากพอ จะทำการโจมตีและตาย
        if (distance < 20) {
            target.takeDamage(10);
            this.isDead = true;
        }
    }

    /**
     * วาดรูปศัตรูลงบนหน้าจอ โดยมีการพลิกด้านตามทิศทางที่เดิน
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext)
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