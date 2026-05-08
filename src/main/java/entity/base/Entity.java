package entity.base;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.net.URL;

/**
 * คลาสนามธรรม (Abstract Class) ที่เป็นต้นแบบของสิ่งต่างๆ ในเกม
 * จัดการเรื่องพิกัดตำแหน่ง ความเร็ว รูปภาพ และสถานะการคงอยู่ (ตายหรือยัง)
 */
public abstract class Entity implements Renderable {
    protected double x, y;
    protected double speed;
    protected Image image;
    protected boolean isDead;

    /**
     * สร้างออบเจกต์ Entity
     * @param x ตำแหน่งเริ่มต้นแกน X
     * @param y ตำแหน่งเริ่มต้นแกน Y
     * @param speed ความเร็วในการเคลื่อนที่ของวัตถุ
     * @param imageFileName ชื่อไฟล์รูปภาพ (ต้องอยู่ในโฟลเดอร์ resources/images/)
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

    /**
     * อัปเดตตรรกะหรือสถานะของวัตถุในแต่ละเฟรม (เช่น การขยับตำแหน่ง)
     */
    public abstract void update();

    /** @return พิกัด X ปัจจุบัน */
    public double getX() { return x; }

    /** @return พิกัด Y ปัจจุบัน */
    public double getY() { return y; }

    /** @return คืนค่าสถานะว่าวัตถุนี้ถูกทำลาย/ตายแล้วหรือไม่ (true = ตาย/ควรลบออก) */
    public boolean isDead() { return isDead; }

    /**
     * กำหนดสถานะการทำลายล้างของวัตถุ
     * @param dead สถานะ (true = ตาย/ถูกทำลาย)
     */
    public void setDead(boolean dead) { this.isDead = dead; }
}