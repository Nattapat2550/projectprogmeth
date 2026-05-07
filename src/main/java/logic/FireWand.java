package logic;
import java.util.List;
public class FireWand extends Weapon {
    public FireWand() { super(50, 35, "firewand.png"); }
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        double ang = Math.random() * Math.PI * 2;
        pr.add(new Projectile(p.getX(), p.getY(), Math.cos(ang)*5, Math.sin(ang)*5, 0, 0, 80, damage, "firewand.png", 0, p));
    }
}