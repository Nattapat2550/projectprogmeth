package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/** อาวุธกระเทียม สร้างออร่าวงกลมรอบตัวผู้เล่น ศัตรูที่เข้าใกล้จะโดนดาเมจ */
public class Garlic extends Weapon {
    /** สร้างอาวุธกระเทียม กำหนดคูลดาวน์ 20 (ตีรัวๆ), ดาเมจ 10 */
    public Garlic() { super(20, 10, "garlic.png"); }

    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        // เช็คศัตรูในระยะวงกลมรัศมี 70 พิกเซล
        for(Enemy e : enemies) {
            if(Math.hypot(e.getX()-p.getX(), e.getY()-p.getY()) < 70) e.takeDamage(damage);
        }
    }

    /** วาดเอฟเฟกต์วงกลมกระเทียมรอบตัว */
    @Override
    public void draw(GraphicsContext gc, Player p) {
        gc.setStroke(Color.rgb(255, 255, 200, 0.5));
        gc.setLineWidth(4);
        gc.strokeOval(p.getX() - 70, p.getY() - 70, 140, 140);
    }
}