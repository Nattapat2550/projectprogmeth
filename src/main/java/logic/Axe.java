package logic;
import java.util.List;

/**
 * คลาสอาวุธขวาน (Axe)
 * เป็นอาวุธที่สร้างกระสุนวิถีโค้ง (ขว้างขึ้นและตกลงมาตามแรงโน้มถ่วงจำลอง)
 */
public class Axe extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างอาวุธขวาน
     * กำหนดค่าคูลดาวน์เริ่มต้น พลังทำลาย และรูปภาพ
     */
    public Axe() { super(60, 40, "axe.png"); }

    /**
     * ทำการโจมตีโดยการสร้าง Projectile (กระสุนขวาน) ขว้างออกไป
     * @param p ตัวละครผู้เล่น (ใช้สำหรับอ้างอิงพิกัดและทิศทางการหันหน้า)
     * @param e รายการศัตรูทั้งหมดในสนาม (ไม่ได้ใช้โดยตรงในการสร้างขวาน แต่รับมาตามโครงสร้าง Polymorphism)
     * @param pr รายการกระสุนทั้งหมดในสนาม (ขวานที่ถูกขว้างจะถูกเพิ่มเข้าไปในลิสต์นี้)
     */
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?3:-3, -10, 0, 0.4, 90, damage, "axe.png", 0, p));
    }
}