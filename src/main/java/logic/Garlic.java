package logic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/**
 * คลาสอาวุธกระเทียม (Garlic)
 * อาวุธประเภทสร้างวงแหวนป้องกันรอบตัวผู้เล่น ศัตรูที่เข้ามาใกล้จะได้รับความเสียหาย
 */
public class Garlic extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างอาวุธกระเทียม
     */
    public Garlic() { super(20, 10, "garlic.png"); }

    /**
     * โจมตีศัตรูทั้งหมดที่อยู่ภายในระยะของวงแหวนกระเทียม
     * @param p ตัวละครผู้เล่น
     * @param enemies รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม (ไม่ได้ใช้สำหรับกระเทียม)
     */
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        for(Enemy e : enemies) {
            if(Math.hypot(e.getX()-p.getX(), e.getY()-p.getY()) < 70) {
                e.takeDamage(damage);
            }
        }
    }

    /**
     * วาดเอฟเฟกต์วงแหวนกระเทียมรอบตัวผู้เล่น
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext)
     * @param p ตัวละครผู้เล่น (ใช้อ้างอิงพิกัด)
     */
    @Override
    public void draw(GraphicsContext gc, Player p) {
        gc.setStroke(Color.rgb(255, 255, 200, 0.5)); // สีเหลืองจางๆ
        gc.setLineWidth(4);
        gc.strokeOval(p.getX() - 70, p.getY() - 70, 140, 140);
    }
}