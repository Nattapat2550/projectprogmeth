package logic;
import java.util.List;
public class KingBible extends Weapon {
    public KingBible() { super(120, 20, "kingbible.png"); }
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), 0, 0, 0, 0, 150, damage, "kingbible.png", 3, p));
    }
}