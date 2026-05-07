package logic;
import java.util.List;
public class Knife extends Weapon {
    public Knife() { super(30, 20, "knife.png"); }
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?8:-8, 0, 0, 0, 60, damage, "knife.png", 0, p));
    }
}