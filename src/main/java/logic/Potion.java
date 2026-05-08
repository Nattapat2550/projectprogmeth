package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * คลาส Potion (ขวดยาเพิ่มเลือด)
 * ดรอปจากศัตรู เมื่อผู้เล่นเดินไปเก็บจะช่วยฟื้นฟู HP
 */
public class Potion extends Entity {

    public Potion(double x, double y) {
        // ส่งค่าไปให้ Entity (x, y, speed=0, ชื่อไฟล์รูปภาพ)
        super(x, y, 0, "potion.png");
    }

    @Override
    public void update() {
        // ขวดยาตกอยู่บนพื้นนิ่งๆ ไม่ต้องขยับ
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) {
            // ถ้ารูปโหลดสำเร็จ ให้วาดรูปขวดยา
            gc.drawImage(image, x - 10, y - 10, 20, 20);
        } else {
            // ถ้าไม่มีรูป ให้วาดวงกลมสีชมพูแทน
            gc.setFill(Color.PINK);
            gc.fillOval(x - 10, y - 10, 20, 20);
        }
    }
}