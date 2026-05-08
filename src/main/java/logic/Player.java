package logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Entity {
    protected int hp;
    protected int maxHp;
    protected double attackRadius;

    protected int exp = 0;
    protected int maxExp = 20;
    protected int level = 1;
    protected boolean pendingLevelUp = false;

    protected boolean facingRight = true;
    protected List<Weapon> weapons = new ArrayList<>();

    // ล็อกสล็อตอาวุธไว้ที่ 3 ชิ้นตามที่ขอครับ (ใช้ public final)
    public final int MAX_WEAPON_SLOTS = 5;

    public Player(double x, double y, double speed, String imagePath, int maxHp, double attackRadius) {
        super(x, y, speed, imagePath);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackRadius = attackRadius;
    }

    // เมธอดสำหรับเพิ่มอาวุธใหม่ หรืออัปเกรดของเดิม
    public void addOrUpgradeWeapon(Weapon newW) {
        for (Weapon w : weapons) {
            if (w.getClass().equals(newW.getClass())) {
                w.levelUp(); // ถ้ามีอยู่แล้วให้อัปเลเวล
                return;
            }
        }
        if (weapons.size() < MAX_WEAPON_SLOTS) {
            weapons.add(newW); // ถ้าช่องยังไม่เต็ม ให้เพิ่มชิ้นใหม่
        }
    }

    // ฟื้นฟูเลือด (ไม่ให้เกินหลอด)
    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    // เพิ่มหลอดเลือดสูงสุด (Max HP)
    public void increaseMaxHp(int amount) {
        this.maxHp += amount;
        this.hp += amount;
    }

    public void move(boolean up, boolean down, boolean left, boolean right, double screenWidth, double screenHeight) {
        if (up && y > 0) y -= speed;
        if (down && y < screenHeight) y += speed;
        if (left && x > 0) { x -= speed; facingRight = false; }
        if (right && x < screenWidth) { x += speed; facingRight = true; }
    }

    @Override
    public void update() {}

    public void gainExp(int amount) {
        exp += amount;
        if (exp >= maxExp) {
            exp -= maxExp;
            level++;
            maxExp += 20;
            pendingLevelUp = true;
        }
    }

    public boolean hasPendingLevelUp() { return pendingLevelUp; }
    public void clearPendingLevelUp() { pendingLevelUp = false; }

    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getExp() { return exp; }
    public int getMaxExp() { return maxExp; }
    public int getLevel() { return level; }
    public boolean isFacingRight() { return facingRight; }
    public List<Weapon> getWeapons() { return weapons; }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) this.isDead = true;
    }
}