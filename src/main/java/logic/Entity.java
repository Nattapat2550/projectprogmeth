package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.net.URL;

/**
 * คลาสนามธรรมที่เป็นต้นแบบของวัตถุทุกอย่างในเกม
 * จัดการเรื่องพิกัดตำแหน่ง ความเร็ว และการโหลดรูปภาพผ่าน ClassLoader
 */
public abstract class Entity implements Renderable {
    protected double x, y;
    protected double speed;
    protected Image image;
    protected boolean isDead;

    /**
     * สร้าง Entity ใหม่พร้อมพิกัดเริ่มต้นและความเร็ว
     * @param x พิกัด X เริ่มต้น
     * @param y พิกัด Y เริ่มต้น
     * @param speed ความเร็วในการเคลื่อนที่
     * @param imageFileName ชื่อไฟล์รูปภาพในโฟลเดอร์ resources/images/
     */
    public Entity(double x, double y, double speed, String imageFileName) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isDead = false;
        try {
            URL res = ClassLoader.getSystemResource("images/" + imageFileName);
            if (res != null) this.image = new Image(res.toString());
        } catch (Exception e) {
            System.out.println("Error loading image: " + imageFileName);
        }
    }

    /** * อัปเดตตรรกะของ Entity ในแต่ละเฟรม
     */
    public abstract void update();

    /** * ดึงค่าพิกัด X ปัจจุบัน
     * @return พิกัด X
     */
    public double getX() { return x; }

    /** * ดึงค่าพิกัด Y ปัจจุบัน
     * @return พิกัด Y
     */
    public double getY() { return y; }

    /** * ตรวจสอบสถานะการตายของวัตถุ
     * @return สถานะการตาย (true ถ้ายตายแล้ว)
     */
    public boolean isDead() { return isDead; }

    /** * กำหนดสถานะการตายของวัตถุ
     * @param dead สถานะการตายที่ต้องการกำหนด
     */
    public void setDead(boolean dead) { this.isDead = dead; }
}