package entity.weapon;
import entity.character.Enemy;
import entity.character.Player;
import entity.projectile.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.List;

/**
 * คลาสนามธรรม (Abstract Class) ที่เป็นแม่แบบของอาวุธทุกชนิด
 * จัดการเรื่อง คูลดาวน์ (ความเร็วโจมตี), ดาเมจ, เลเวล และรูปไอคอนอาวุธ
 */
public abstract class Weapon {
    protected int cooldown, maxCooldown, damage, level = 1;
    protected String imageName;
    protected Image image;

    /**
     * สร้างอาวุธใหม่
     * @param maxCooldown คูลดาวน์สูงสุด (เฟรม) ยิ่งน้อยยิ่งโจมตีเร็ว
     * @param damage ความเสียหายเริ่มต้น
     * @param imageName ชื่อไฟล์รูปภาพของอาวุธ
     */
    public Weapon(int maxCooldown, int damage, String imageName) {
        this.maxCooldown = maxCooldown;
        this.damage = damage;
        this.imageName = imageName;
        try {
            URL res = ClassLoader.getSystemResource("images/" + imageName);
            if (res != null) this.image = new Image(res.toString());
        } catch (Exception e) {
            System.out.println("Error loading weapon: " + imageName);
        }
    }

    /**
     * อัปเกรดอาวุธ (เพิ่มเลเวล ดาเมจ และลดคูลดาวน์) จำกัดสูงสุดเลเวล 4
     */
    public void levelUp() {
        if (level < 4) {
            level++;
            damage += 10;
            maxCooldown = Math.max(10, maxCooldown - 5);
        }
    }

    /** @return ระดับเลเวลของอาวุธในปัจจุบัน */
    public int getLevel() { return level; }

    /** @return ชื่อไฟล์รูปไอคอนอาวุธ */
    public String getImageName() { return imageName; }

    /** @return ออบเจกต์รูปภาพของอาวุธ */
    public Image getImage() { return image; }

    /**
     * เมธอดสำหรับโจมตีหรือปล่อยกระสุน
     * คลาสลูกต้อง Override เพื่อกำหนดรูปแบบโจมตีเฉพาะของตัวเอง
     * @param p ผู้เล่นที่กำลังใช้อาวุธ
     * @param enemies รายการศัตรูทั้งหมดในสนาม
     * @param projs รายการกระสุนที่จะถูกเพิ่มเข้าไป
     */
    public abstract void fire(Player p, List<Enemy> enemies, List<Projectile> projs);

    /**
     * อัปเดตคูลดาวน์ เมื่อคูลดาวน์หมดจะโจมตี (fire) และรีเซ็ตคูลดาวน์
     * @param p ผู้เล่น
     * @param e ลิสต์ศัตรู
     * @param pr ลิสต์กระสุน
     */
    public void update(Player p, List<Enemy> e, List<Projectile> pr) {
        if (cooldown > 0) cooldown--;
        else { fire(p, e, pr); cooldown = maxCooldown; }
    }

    /**
     * วาดเอฟเฟกต์อาวุธรอบๆ ตัวผู้เล่น (ปล่อยว่างไว้ให้คลาสลูกเขียนทับหากจำเป็น)
     * @param gc กราฟิกคอนเท็กซ์
     * @param p ผู้เล่น
     */
    public void draw(GraphicsContext gc, Player p) {}
}