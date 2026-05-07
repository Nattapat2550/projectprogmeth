package logic;
import java.util.List;
public class MagicWand extends Weapon {
    public MagicWand() { super(45, 15, "magicwand.png"); }
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        Enemy nearest = null; double minD = 9999;
        for(Enemy e : enemies) {
            double d = Math.hypot(e.getX()-p.getX(), e.getY()-p.getY());
            if(d < minD) { minD = d; nearest = e; }
        }
        if(nearest != null) {
            double angle = Math.atan2(nearest.getY()-p.getY(), nearest.getX()-p.getX());
            pr.add(new Projectile(p.getX(), p.getY(), Math.cos(angle)*6, Math.sin(angle)*6, 0, 0, 60, damage, "magicwand.png", 0, p));
        }
    }
}