package entity.base;
import javafx.scene.canvas.GraphicsContext;

/**
 * อินเตอร์เฟซสำหรับวัตถุที่สามารถวาดลงบนหน้าจอ (Canvas) ได้
 * วัตถุใดที่แสดงผลในเกมต้องอิมพลีเมนต์อินเตอร์เฟซนี้
 */
public interface Renderable {
    /**
     * วาดวัตถุลงบน GraphicsContext
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext) สำหรับการวาดภาพ
     */
    void draw(GraphicsContext gc);
}