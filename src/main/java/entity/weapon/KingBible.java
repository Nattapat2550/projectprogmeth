package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import java.util.List;

/** อาวุธคัมภีร์ เสกคัมภีร์มาบินหมุนวนรอบๆ ตัวผู้เล่น (จำนวนเล่มตามเลเวลอาวุธ) */
public class KingBible extends Weapon {
    /** สร้างอาวุธคัมภีร์ กำหนดคูลดาวน์ 120, ดาเมจ 20 */
    public KingBible() { super(120, 20, "kingbible.png"); }

    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        for (int i = 0; i < level; i++) {
            double angle = (Math.PI * 2 / level) * i; // แบ่งมุมระยะห่างให้สมมาตร
            // ใช้ Type 3 เพื่อส่งสัญญาณให้กระสุนโคจรรอบตัว
            pr.add(new Projectile(p.getX(), p.getY(), 0, 0, 0, 0, 150, damage, "kingbible.png", 3, p, angle));
        }
    }
}