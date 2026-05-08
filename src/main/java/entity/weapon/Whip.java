package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

/** อาวุธแส้ โจมตีกวาดศัตรูในระยะเป็นเส้นตรงด้านหน้า */
public class Whip extends Weapon {
    private int drawTimer = 0;

    /** สร้างอาวุธแส้ กำหนดคูลดาวน์ 60, ดาเมจ 30 */
    public Whip() { super(60, 30, "whip.png"); }

    @Override
    public void fire(Player p, List<Enemy> enemies, List<Projectile> pr) {
        drawTimer = 10; // ตั้งเวลาโชว์เอฟเฟกต์ฟาดแส้
        for (Enemy e : enemies) {
            // เช็คว่าศัตรูอยู่ในกรอบด้านหน้าที่หันไปหรือไม่
            boolean inY = Math.abs(e.getY() - p.getY()) < 40;
            if (p.isFacingRight() && e.getX() > p.getX() && e.getX() < p.getX() + 80 && inY) e.takeDamage(damage);
            else if (!p.isFacingRight() && e.getX() < p.getX() && e.getX() > p.getX() - 80 && inY) e.takeDamage(damage);
        }
    }

    /** วาดเอฟเฟกต์แสงสีขาวจางๆ ขณะฟาดแส้ */
    @Override
    public void draw(GraphicsContext gc, Player p) {
        if (drawTimer > 0) {
            drawTimer--;
            gc.save();
            gc.translate(p.getX(), p.getY() - 10);
            if (!p.isFacingRight()) gc.scale(-1, 1);
            gc.setFill(Color.rgb(255, 255, 255, drawTimer / 10.0));
            gc.fillRoundRect(10, -5, 70, 10, 10, 10);
            gc.restore();
        }
    }
}