package logic;
import java.util.List;

/**
 * คลาสอาวุธคทาเวทมนตร์ (MagicWand)
 * โจมตีโดยการยิงกระสุนเวทมนตร์ล็อกเป้าศัตรูที่อยู่ใกล้ที่สุดอัตโนมัติ
 */
public class MagicWand extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างคทาเวทมนตร์
     */
    public MagicWand() { super(45, 15, "magicwand.png"); }

    /**
     * ยิงกระสุนเวทไปยังศัตรูเป้าหมายที่อยู่ใกล้ที่สุด
     * @param p ตัวละครผู้เล่น
     * @param enemies รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม
     */
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        Enemy nearest = null;
        double minD = 9999;
        // ค้นหาศัตรูที่อยู่ใกล้ที่สุด
        for(Enemy e : enemies) {
            double d = Math.hypot(e.getX()-p.getX(), e.getY()-p.getY());
            if(d < minD) { minD = d; nearest = e; }
        }

        // ถ้ายิงเจอศัตรูให้ปล่อยกระสุนไปทางนั้น
        if(nearest != null) {
            double angle = Math.atan2(nearest.getY()-p.getY(), nearest.getX()-p.getX());
            pr.add(new Projectile(p.getX(), p.getY(), Math.cos(angle)*6, Math.sin(angle)*6, 0, 0, 60, damage, "magicwand.png", 0, p));
        }
    }
}