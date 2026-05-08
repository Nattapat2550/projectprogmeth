package logic;
import entity.base.ICollectable;
import entity.character.*;
import entity.item.*;
import entity.projectile.Projectile;
import entity.weapon.Weapon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * คลาสประมวลผลหลักของเกม (Game Manager / Logic Controller)
 * แบ่งแยกส่วนการคำนวณ (Business Logic) ออกจากหน้าจอ (GUI) อย่างเด็ดขาด
 * เพื่อให้โค้ดทดสอบได้ง่ายและเป็นระเบียบตามาตรฐาน OOP
 */
public class GameLogic {
    private Player player;
    private List<Enemy> enemies;
    private List<ICollectable> items;
    private List<Projectile> projectiles;
    private int spawnTimer = 0;

    /**
     * สร้างกลไกเกมเริ่มต้น
     * @param charType รหัสตัวละครที่ผู้เล่นเลือกจากหน้าเมนู (knight, mage, ninja)
     */
    public GameLogic(String charType) {
        enemies = new ArrayList<>();
        items = new ArrayList<>();
        projectiles = new ArrayList<>();

        double startX = GameConfig.MAP_WIDTH / 2;
        double startY = GameConfig.MAP_HEIGHT / 2;

        if (charType.equals("knight")) this.player = new Knight(startX, startY);
        else if (charType.equals("mage")) this.player = new Mage(startX, startY);
        else if (charType.equals("ninja")) this.player = new Ninja(startX, startY);
        else this.player = new Knight(startX, startY);
    }

    /**
     * เมธอดเรียกใช้ทุกเฟรม ทำหน้าที่สั่งคำนวณตำแหน่ง อาวุธ เช็คการชนทั้งหมด
     * @param up กดปุ่ม W
     * @param down กดปุ่ม S
     * @param left กดปุ่ม A
     * @param right กดปุ่ม D
     */
    public void update(boolean up, boolean down, boolean left, boolean right) {
        // หากผู้เล่นตายหรือรออัปเลเวล ไม่ต้องคำนวณขยับอะไร
        if (player.isDead() || player.hasPendingLevelUp()) return;

        // 1. ขยับผู้เล่น
        player.move(up, down, left, right, GameConfig.MAP_WIDTH, GameConfig.MAP_HEIGHT);

        // 2. อัปเดตคูลดาวน์และสาดอาวุธ
        for (Weapon w : player.getWeapons()) w.update(player, enemies, projectiles);

        // 3. ขยับกระสุน และเช็คชนกับศัตรู
        Iterator<Projectile> pIt = projectiles.iterator();
        while (pIt.hasNext()) {
            Projectile p = pIt.next();
            p.update();
            if (p.isDead()) { pIt.remove(); continue; }
            for (Enemy e : enemies) {
                // เช็คระยะห่างระหว่างกระสุนกับศัตรู (20 พิกเซล = โดน)
                if (Math.hypot(p.getX() - e.getX(), p.getY() - e.getY()) < 20) {
                    e.takeDamage(p.damage);
                    SoundManager.playSFX("hit.mp3");
                    p.setDead(true);
                    break;
                }
            }
        }

        // 4. เช็คระยะการเก็บไอเทม (ด้วย Interface ICollectable)
        Iterator<ICollectable> itemIt = items.iterator();
        while (itemIt.hasNext()) {
            ICollectable item = itemIt.next();
            // เช็คผู้เล่นเดินไปทับของดรอปในระยะ 30 พิกเซล
            if (Math.hypot(item.getX() - player.getX(), item.getY() - player.getY()) < 30) {
                item.collect(player);
                itemIt.remove();
            }
        }

        // 5. ระบบสุ่มเกิดศัตรูขอบจอ (Spawning System)
        spawnTimer++;
        if (spawnTimer >= GameConfig.ENEMY_SPAWN_INTERVAL) {
            double angle = Math.random() * Math.PI * 2;
            double radius = 600; // เกิดห่างจากตัวผู้เล่น 600
            double spawnX = Math.max(0, Math.min(player.getX() + Math.cos(angle) * radius, GameConfig.MAP_WIDTH));
            double spawnY = Math.max(0, Math.min(player.getY() + Math.sin(angle) * radius, GameConfig.MAP_HEIGHT));

            double randEnemy = Math.random();
            if (randEnemy < 0.33) enemies.add(new Bat(spawnX, spawnY, player));
            else if (randEnemy < 0.66) enemies.add(new Zombie(spawnX, spawnY, player));
            else enemies.add(new Ghost(spawnX, spawnY, player));
            spawnTimer = 0;
        }

        // 6. อัปเดตการขยับของศัตรู และดรอปของเมื่อศัตรูตาย
        Iterator<Enemy> eIt = enemies.iterator();
        while (eIt.hasNext()) {
            Enemy e = eIt.next();
            e.update();
            if (e.isDead()) {
                if (Math.random() < 0.05) items.add(new Potion(e.getX(), e.getY()));
                else {
                    double expRate = Math.random();
                    if (expRate < 0.6) items.add(new ExpGem(e.getX(), e.getY(), "blue.png", 10));
                    else if (expRate < 0.9) items.add(new ExpGem(e.getX(), e.getY(), "red.png", 20));
                    else items.add(new ExpGem(e.getX(), e.getY(), "purple.png", 50));
                }
                eIt.remove();
            }
        }
    }

    /** @return ออบเจกต์ตัวผู้เล่น */
    public Player getPlayer() { return player; }
    /** @return ลิสต์ศัตรูในจอ */
    public List<Enemy> getEnemies() { return enemies; }
    /** @return ลิสต์ไอเทมที่ตกพื้น */
    public List<ICollectable> getItems() { return items; }
    /** @return ลิสต์กระสุนที่กำลังบินอยู่ */
    public List<Projectile> getProjectiles() { return projectiles; }
}