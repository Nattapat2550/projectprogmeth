package logic;
import java.util.List;
public class Cross extends Weapon {
    public Cross() { super(70, 25, "cross.png"); }
    @Override
    public void fire(Player p, List<Enemy> e, List<Projectile> pr) {
        pr.add(new Projectile(p.getX(), p.getY(), p.isFacingRight()?9:-9, 0, p.isFacingRight()?-0.2:0.2, 0, 100, damage, "cross.png", 0, p));
    }
}