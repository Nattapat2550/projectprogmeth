package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import java.util.List;

/** อาวุธมีดสั้น ขว้างมีดพุ่งไปด้านหน้าทิศทางเดียวกับที่ผู้เล่นหัน */
public class Knife extends Weapon {
    /** สร้างอาวุธมีดสั้น กำหนดคูลดาวน์ 30, ดาเมจ 20 */
    public Knife() { super(30, 20, "knife.png"); }

    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?8:-8, 0, 0, 0, 60, damage, "knife.png", 0, p));
    }
}