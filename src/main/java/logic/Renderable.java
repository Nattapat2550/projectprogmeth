package logic;

import javafx.scene.canvas.GraphicsContext;

/**
 * อินเตอร์เฟซสำหรับวัตถุที่สามารถวาดบนหน้าจอเกมได้
 * ใช้เพื่อกำหนดมาตรฐานให้ทุกวัตถุในเกมมีวิธีการวาดที่สอดคล้องกัน
 */
public interface Renderable {
    /**
     * วาดวัตถุลงบน GraphicsContext
     * @param gc กราฟิกคอนเท็กซ์ที่ใช้ในการวาดภาพบน Canvas
     */
    void draw(GraphicsContext gc);
}