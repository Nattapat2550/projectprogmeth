package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/**
 * คลาสอาวุธแส้ (Whip) ซึ่งเป็นอาวุธประเภทฟาด
 * โจมตีศัตรูหลายตัวพร้อมกันในระยะด้านหน้าผู้เล่น
 */
public class Whip extends Weapon {
    private int drawTimer = 0;

    /** * สร้างแส้พร้อมค่าพลังเริ่มต้น
     */
    public Whip() { super(60, 30, "whip.png"); }

    /**
     * สร้างความเสียหายให้ศัตรูทั้งหมดที่อยู่ในขอบเขตด้านหน้า
     * @param p ตัวละครผู้เล่น
     * @param enemies รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม (ไม่ได้ใช้สำหรับแส้)
     */
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        drawTimer = 10; // ตั้งเวลาแสดงกราฟิกเอฟเฟกต์
        for (Enemy e : enemies) {
            // คำนวณขอบเขตการโจมตีตามทิศทางของผู้เล่น
            boolean inY = Math.abs(e.getY() - p.getY()) < 40;
            if (p.isFacingRight() && e.getX() > p.getX() && e.getX() < p.getX() + 80 && inY) e.takeDamage(damage);
            else if (!p.isFacingRight() && e.getX() < p.getX() && e.getX() > p.getX() - 80 && inY) e.takeDamage(damage);
        }
    }

    /**
     * วาดกราฟิกเอฟเฟกต์การฟาดแส้
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext)
     * @param p ตัวละครผู้เล่น (ใช้อ้างอิงพิกัดและทิศทาง)
     */
    @Override
    public void draw(GraphicsContext gc, Player p) {
        if (drawTimer > 0) {
            drawTimer--;
            double alpha = drawTimer / 10.0;
            gc.save();
            gc.translate(p.getX(), p.getY() - 10);
            if (!p.isFacingRight()) gc.scale(-1, 1);
            gc.setFill(Color.rgb(255, 255, 255, alpha));
            gc.fillRoundRect(10, -5, 70, 10, 10, 10);
            gc.restore();
        }
    }
}