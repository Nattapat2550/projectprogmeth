package logic;
import java.util.List;

/**
 * คลาสอาวุธไม้กางเขน (Cross)
 * อาวุธประเภทปาออกไปด้านหน้าและจะย้อนกลับมา (วิถีบูมเมอแรง)
 */
public class Cross extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างอาวุธไม้กางเขน
     * กำหนดค่าคูลดาวน์ ดาเมจ และรูปภาพ
     */
    public Cross() { super(70, 25, "cross.png"); }

    /**
     * ทำการโจมตีโดยการสร้าง Projectile ไม้กางเขนขว้างออกไป
     * @param p ตัวละครผู้เล่น (ใช้สำหรับอ้างอิงพิกัดและทิศทางการหันหน้า)
     * @param e รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม
     */
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?9:-9, 0, p.isFacingRight()?-0.2:0.2, 0, 100, damage, "cross.png", 0, p));
    }
}