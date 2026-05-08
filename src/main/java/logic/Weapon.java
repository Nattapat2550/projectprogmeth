package logic;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.List;

public abstract class Weapon {
    protected int cooldown, maxCooldown, damage, level = 1;
    protected String imageName;
    protected Image image;

    public Weapon(int maxCooldown, int damage, String imageName) {
        this.maxCooldown = maxCooldown; this.damage = damage; this.imageName = imageName;
        try {
            URL res = ClassLoader.getSystemResource("images/" + imageName);
            if (res != null) this.image = new Image(res.toString());
        } catch (Exception e) {}
    }

    public void levelUp() {
        if (level < 4) {
            level++;
            damage += 10; // เพิ่มดาเมจทุกระดับ
            maxCooldown = Math.max(10, maxCooldown - 5); // ลดคูลดาวน์
        }
    }

    public int getLevel() { return level; }
    public String getImageName() { return imageName; }
    public Image getImage() { return image; }

    public abstract void fire(Player p, List<Enemy> enemies, List<Projectile> projs);
    public void update(Player p, List<Enemy> e, List<Projectile> pr) {
        if (cooldown > 0) cooldown--;
        else { fire(p, e, pr); cooldown = maxCooldown; }
    }
    public void draw(GraphicsContext gc, Player p) {}
}