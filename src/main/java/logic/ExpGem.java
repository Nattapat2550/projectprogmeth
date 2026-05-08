package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * คลาส ExpGem (อัญมณีค่าประสบการณ์)
 * เป็นไอเทมดรอปจากศัตรู เมื่อผู้เล่นเก็บจะได้รับค่าประสบการณ์ (EXP)
 */
public class ExpGem extends Entity {
    private int expValue;

    /**
     * สร้างอัญมณีค่าประสบการณ์ที่ตำแหน่งที่ระบุ
     * @param x ตำแหน่ง X เริ่มต้น
     * @param y ตำแหน่ง Y เริ่มต้น
     * @param imgName ชื่อไฟล์รูปภาพอัญมณี (เช่น blue.png, red.png, purple.png)
     * @param expValue ปริมาณค่าประสบการณ์ที่จะได้รับเมื่อเก็บ
     */
    public ExpGem(double x, double y, String imgName, int expValue) {
        super(x, y, 0, imgName); // ความเร็วเป็น 0 เพราะตกอยู่กับที่
        this.expValue = expValue;
    }

    /**
     * คืนค่าปริมาณค่าประสบการณ์ของอัญมณีเม็ดนี้
     * @return ค่าประสบการณ์ (EXP)
     */
    public int getExpValue() {
        return expValue;
    }

    /**
     * อัปเดตตรรกะของอัญมณี (อยู่นิ่งๆ ไม่ต้องขยับ)
     */
    @Override
    public void update() {}

    /**
     * วาดอัญมณีลงบนหน้าจอ
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext)
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            gc.drawImage(image, x - 10, y - 10, 20, 20); // วาดรูปเพชร
        } else {
            gc.setFill(Color.AQUA);
            gc.fillRect(x - 5, y - 5, 10, 10); // ถ้าไม่มีรูปให้วาดสี่เหลี่ยมสีฟ้า
        }
    }
}