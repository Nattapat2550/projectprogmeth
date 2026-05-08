package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import java.util.List;

/** อาวุธกางเขน โจมตีแบบบูมเมอแรง ขว้างไปด้านหน้าแล้วมีแรงดึงกลับมาด้านหลัง */
public class Cross extends Weapon {
    /** สร้างอาวุธไม้กางเขน กำหนดคูลดาวน์ 70, ดาเมจ 25 */
    public Cross() { super(70, 25, "cross.png"); }

    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        // กำหนดความเร่งแกน X สวนทางกับความเร็วเพื่อให้กางเขนบินย้อนกลับ
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?9:-9, 0, p.isFacingRight()?-0.2:0.2, 0, 100, damage, "cross.png", 0, p));
    }
}