package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import java.util.List;

/** อาวุธคทาเวทมนตร์ ปล่อยกระสุนล็อกเป้ายิงเข้าหาศัตรูที่อยู่ใกล้ที่สุดอัตโนมัติ */
public class MagicWand extends Weapon {
    /** สร้างอาวุธคทาเวท กำหนดคูลดาวน์ 45, ดาเมจ 15 */
    public MagicWand() { super(45, 15, "magicwand.png"); }

    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        Enemy nearest = null;
        double minD = 9999;
        // หาศัตรูที่ใกล้ที่สุด
        for(Enemy e : enemies) {
            double d = Math.hypot(e.getX()-p.getX(), e.getY()-p.getY());
            if(d < minD) { minD = d; nearest = e; }
        }
        // ยิงกระสุนพุ่งไปหา
        if(nearest != null) {
            double angle = Math.atan2(nearest.getY()-p.getY(), nearest.getX()-p.getX());
            pr.add(new Projectile(p.getX(), p.getY(), Math.cos(angle)*6, Math.sin(angle)*6, 0, 0, 60, damage, "magicwand.png", 0, p));
        }
    }
}