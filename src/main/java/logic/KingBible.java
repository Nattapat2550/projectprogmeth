package logic;
import java.util.List;
public class KingBible extends Weapon {
    public KingBible() { super(120, 20, "kingbible.png"); }
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        int count = level; // จำนวนเล่มตามเลเวล
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI * 2 / count) * i; // แบ่งมุมให้สมมาตร (360 องศา / จำนวนเล่ม)
            pr.add(new Projectile(p.getX(), p.getY(), 0, 0, 0, 0, 150, damage, "kingbible.png", 3, p, angle));
        }
    }
}