package logic;

import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Entity {
    protected int hp, maxHp;
    protected double attackRadius;

    // ระบบ EXP และ Level
    protected int exp = 0;
    protected int maxExp = 20;
    protected int level = 1;
    protected boolean pendingLevelUp = false;
    // ทิศทางการหันหน้า (ซ้าย/ขวา) สำหรับอาวุธบางชนิด
    protected boolean facingRight = true;

    // ลิสต์เก็บอาวุธทั้งหมดที่มี
    protected List<Weapon> weapons = new ArrayList<>();

    public Player(double x, double y, double speed, String imagePath, int maxHp, double attackRadius) {
        super(x, y, speed, imagePath);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackRadius = attackRadius;
    }

    public void addWeapon(Weapon w) {
        this.weapons.add(w);
    }

    public List<Weapon> getWeapons() { return weapons; }

    public void move(boolean up, boolean down, boolean left, boolean right, double screenWidth, double screenHeight) {
        if (up && y > 0) y -= speed;
        if (down && y < screenHeight) y += speed;
        if (left && x > 0) { x -= speed; facingRight = false; }
        if (right && x < screenWidth) { x += speed; facingRight = true; }
    }

    @Override
    public void update() {
        // ให้ GamePane อัปเดตอาวุธแทน
    }

    // เมื่อเก็บเม็ด EXP
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= maxExp) {
            exp -= maxExp;
            level++;
            maxExp += 20;
            pendingLevelUp = true; // เมื่อเลเวลถึง ให้รอเลือกอาวุธ
        }
    }
    public boolean hasPendingLevelUp() { return pendingLevelUp; }
    public void clearPendingLevelUp() { pendingLevelUp = false; }


    public int getHp() { return hp; }
    public int getExp() { return exp; }
    public int getMaxExp() { return maxExp; }
    public int getLevel() { return level; }
    public boolean isFacingRight() { return facingRight; }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) this.isDead = true;
    }
}