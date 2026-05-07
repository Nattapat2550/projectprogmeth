package logic;
import java.util.List;
public class Axe extends Weapon {
    public Axe() { super(60, 40, "axe.png"); }
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?3:-3, -10, 0, 0.4, 90, damage, "axe.png", 0, p));
    }
}