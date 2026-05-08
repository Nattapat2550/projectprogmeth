package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import java.util.List;

/** อาวุธคทาเพลิง ยิงลูกไฟกระจายออกไปแบบสุ่มทิศทางรอบตัว */
public class FireWand extends Weapon {
    /** สร้างอาวุธคทาเพลิง กำหนดคูลดาวน์ 50, ดาเมจ 35 */
    public FireWand() { super(50, 35, "firewand.png"); }

    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        double ang = Math.random() * Math.PI * 2; // สุ่มมุม 0-360 องศา
        pr.add(new Projectile(p.getX(), p.getY(), Math.cos(ang)*5, Math.sin(ang)*5, 0, 0, 80, damage, "firewand.png", 0, p));
    }
}