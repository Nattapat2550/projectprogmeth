package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ExpGem extends Entity {
    private int expValue;

    public ExpGem(double x, double y, String imgName, int expValue) {
        super(x, y, 0, imgName); // โหลดรูปภาพ blue.png, red.png, หรือ purple.png
        this.expValue = expValue;
    }

    public int getExpValue() {
        return expValue;
    }

    @Override
    public void update() {} // อยู่นิ่งๆ ไม่ต้องขยับ

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