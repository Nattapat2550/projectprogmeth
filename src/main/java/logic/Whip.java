package logic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class Whip extends Weapon {
    private int drawTimer = 0;
    public Whip() { super(60, 30, "whip.png"); }
    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        drawTimer = 15;
        for(Enemy e : enemies) {
            boolean inY = Math.abs(e.getY()-p.getY()) < 40;
            if(p.isFacingRight() && e.getX() > p.getX() && e.getX() < p.getX()+120 && inY) e.takeDamage(damage);
            else if(!p.isFacingRight() && e.getX() < p.getX() && e.getX() > p.getX()-120 && inY) e.takeDamage(damage);
        }
    }
    @Override
    public void draw(GraphicsContext gc, Player p) {
        if(drawTimer > 0) {
            drawTimer--;
            if (image != null) {
                if(p.isFacingRight()) gc.drawImage(image, p.getX(), p.getY()-20, 120, 40);
                else gc.drawImage(image, p.getX()-120, p.getY()-20, 120, 40);
            } else {
                gc.setFill(Color.WHITE);
                if(p.isFacingRight()) gc.fillRect(p.getX(), p.getY()-10, 120, 20);
                else gc.fillRect(p.getX()-120, p.getY()-10, 120, 20);
            }
        }
    }
}