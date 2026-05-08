package logic;
import java.util.List;

/**
 * คลาสอาวุธคัมภีร์ (KingBible)
 * สร้างคัมภีร์บินวนรอบตัวผู้เล่นเพื่อโจมตีศัตรู
 */
public class KingBible extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างอาวุธคัมภีร์
     */
    public KingBible() { super(120, 20, "kingbible.png"); }

    /**
     * ปล่อยคัมภีร์บินโคจรรอบตัวผู้เล่น (จำนวนคัมภีร์เพิ่มขึ้นตามเลเวล)
     * @param p ตัวละครผู้เล่น
     * @param enemies รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม
     */
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        int count = level; // จำนวนเล่มตามเลเวล
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2 / count) * i; // แบ่งมุมให้สมมาตร (360 องศา / จำนวนเล่ม)
            pr.add(new Projectile(p.getX(), p.getY(), 0, 0, 0, 0, 150, damage, "kingbible.png", 3, p, angle));
        }
    }
}