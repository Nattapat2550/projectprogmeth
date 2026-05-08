package logic;
import java.util.List;

/**
 * คลาสอาวุธคทาไฟ (FireWand)
 * โจมตีโดยการยิงลูกไฟออกไปในทิศทางแบบสุ่ม
 */
public class FireWand extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างอาวุธคทาไฟ
     */
    public FireWand() { super(50, 35, "firewand.png"); }

    /**
     * ทำการโจมตีโดยการสร้าง Projectile (ลูกไฟ) ยิงออกไปแบบสุ่มทิศทาง
     * @param p ตัวละครผู้เล่น
     * @param e รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม
     */
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        double ang = Math.random() * Math.PI * 2;
        pr.add(new Projectile(p.getX(), p.getY(), Math.cos(ang)*5, Math.sin(ang)*5, 0, 0, 80, damage, "firewand.png", 0, p));
    }
}