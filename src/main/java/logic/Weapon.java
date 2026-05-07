package logic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.List;

public abstract class Weapon {
    protected int cooldown, maxCooldown, damage;
    protected Image image;

    public Weapon(int maxCooldown, int damage, String imageName) {
        this.maxCooldown = maxCooldown; this.damage = damage; this.cooldown = 0;
        try {
            // โหลดรูปภาพเผื่อไว้สำหรับอาวุธที่ไม่ได้ยิงเป็นกระสุน (เช่น Garlic, Whip)
            URL resource = ClassLoader.getSystemResource("images/" + imageName);
            if (resource == null) resource = ClassLoader.getSystemResource(imageName);
            if (resource != null) this.image = new Image(resource.toString());
        } catch (Exception e) {}
    }
    public void update(Player p, List<Enemy> enemies, List<Projectile> projs) {
        if (cooldown > 0) cooldown--;
        else { fire(p, enemies, projs); cooldown = maxCooldown; }
    }
    public abstract void fire(Player p, List<Enemy> enemies, List<Projectile> projs);
    public void draw(GraphicsContext gc, Player p) {}
}