package logic;
import java.util.List;

/**
 * คลาสอาวุธมีดสั้น (Knife)
 * โจมตีโดยการขว้างมีดพุ่งไปด้านหน้าที่ผู้เล่นหันหน้าอยู่
 */
public class Knife extends Weapon {

    /**
     * คอนสตรักเตอร์สำหรับสร้างอาวุธมีดสั้น
     */
    public Knife() { super(30, 20, "knife.png"); }

    /**
     * ยิงมีดสั้นออกไปตามทิศทางของผู้เล่น
     * @param p ตัวละครผู้เล่น
     * @param e รายการศัตรูทั้งหมดในสนาม
     * @param pr รายการกระสุนทั้งหมดในสนาม
     */
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?8:-8, 0, 0, 0, 60, damage, "knife.png", 0, p));
    }
}