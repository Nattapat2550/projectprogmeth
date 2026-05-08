package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import java.util.List;

/** อาวุธขวาน โจมตีโดยขว้างขึ้นเป็นเส้นโค้ง (มีแรงโน้มถ่วงตกพื้น) */
public class Axe extends Weapon {
    /** สร้างอาวุธขวาน กำหนดคูลดาวน์ 60, ดาเมจ 40 */
    public Axe() { super(60, 40, "axe.png"); }

    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        // เพิ่มกระสุนที่มีความเร่งแกน Y (accelY = 0.4) เพื่อให้ย้อยตกเหมือนขว้างของ
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?3:-3, -10, 0, 0.4, 90, damage, "axe.png", 0, p));
    }
}