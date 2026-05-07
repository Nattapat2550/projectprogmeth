package logic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class Garlic extends Weapon {
    public Garlic() { super(20, 10, "garlic.png"); }
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        for(Enemy e : enemies) if(Math.hypot(e.getX()-p.getX(), e.getY()-p.getY()) < 70) e.takeDamage(damage);
    }
    @Override
    public void draw(GraphicsContext gc, Player p) {
        if (image != null) gc.drawImage(image, p.getX()-70, p.getY()-70, 140, 140);
        else {
            gc.setStroke(Color.YELLOW); gc.setLineWidth(3);
            gc.strokeOval(p.getX()-70, p.getY()-70, 140, 140);
        }
    }
}