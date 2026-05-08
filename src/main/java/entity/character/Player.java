package entity.character;
import entity.base.Entity;
import entity.weapon.Weapon;
import logic.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * คลาสนามธรรมที่เป็นศูนย์รวมข้อมูลของผู้เล่น
 * ควบคุม HP, EXP, เลเวล, อาวุธในครอบครอง, และแอนิเมชันการเดิน
 */
public abstract class Player extends Entity {
    protected int hp, maxHp, exp = 0, maxExp = 20, level = 1;
    protected double attackRadius;
    protected boolean pendingLevelUp = false, facingRight = true;

    /** รายการอาวุธที่ผู้เล่นมี */
    protected List<Weapon> weapons = new ArrayList<>();
    /** ผู้เล่นพกอาวุธได้สูงสุด 3 ชิ้น */
    public final int MAX_WEAPON_SLOTS = 3;

    protected Image image1, image2;
    protected boolean isWalking = false, showFrame1 = true;
    protected int walkTimer = 0;

    /**
     * คอนสตรักเตอร์สำหรับสร้างตัวละครผู้เล่นพื้นฐาน
     * @param x ตำแหน่งเริ่มต้นบนแผนที่แกน X
     * @param y ตำแหน่งเริ่มต้นบนแผนที่แกน Y
     * @param speed ความเร็วในการเดิน
     * @param imagePath ชื่อไฟล์ภาพหลัก (ตัด .png ออกและเติม 1,2 อัตโนมัติเพื่อทำแอนิเมชัน)
     * @param maxHp เลือดสูงสุด
     * @param attackRadius ระยะวงกลมการโจมตี/เก็บของ
     */
    public Player(double x, double y, double speed, String imagePath, int maxHp, double attackRadius) {
        super(x, y, speed, imagePath);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackRadius = attackRadius;
        String baseName = imagePath.replace(".png", "");
        try {
            URL res1 = ClassLoader.getSystemResource("images/" + baseName + "1.png");
            URL res2 = ClassLoader.getSystemResource("images/" + baseName + "2.png");
            if (res1 != null) this.image1 = new Image(res1.toString());
            if (res2 != null) this.image2 = new Image(res2.toString());
        } catch (Exception e) {}
    }

    /**
     * เพิ่มอาวุธชิ้นใหม่ลงช่อง ถ้ามีอยู่แล้วจะทำการอัปเกรด (Level Up) อาวุธเดิม
     * @param newW อาวุธชิ้นใหม่
     */
    public void addOrUpgradeWeapon(Weapon newW) {
        for (Weapon w : weapons) {
            if (w.getClass().equals(newW.getClass())) { w.levelUp(); return; }
        }
        if (weapons.size() < MAX_WEAPON_SLOTS) weapons.add(newW);
    }

    /** ฟื้นฟูเลือด @param amount ปริมาณเลือดที่เพิ่ม */
    public void heal(int amount) { this.hp = Math.min(maxHp, this.hp + amount); }

    /** เพิ่มขีดจำกัดเลือดสูงสุด @param amount ปริมาณที่บวกเพิ่ม */
    public void increaseMaxHp(int amount) { this.maxHp += amount; this.hp += amount; }

    /** * เพิ่มค่าประสบการณ์ (EXP) ถ้าหลอดเต็มจะเลเวลอัป
     * @param amount ปริมาณ EXP ที่ได้
     */
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= maxExp) {
            exp -= maxExp;
            level++;
            maxExp += 20;
            pendingLevelUp = true; // เตรียมพักเกมโชว์หน้าจอเลือกไอเทม
        }
    }

    /**
     * รับความเสียหาย
     * @param damage ปริมาณเลือดที่ลด
     */
    public void takeDamage(int damage) {
        this.hp -= damage; SoundManager.playSFX("hit.mp3");
        if (this.hp <= 0) this.isDead = true;
    }

    /**
     * บังคับตัวละครเดินไปตามปุ่มที่กดบนคีย์บอร์ด พร้อมจำกัดขอบไม่ให้ตกแมพ
     * @param up,down,left,right สถานะปุ่มทิศทางต่างๆ
     * @param mapW ความกว้างสูงสุดของแผนที่
     * @param mapH ความสูงสูงสุดของแผนที่
     */
    public void move(boolean up, boolean down, boolean left, boolean right, double mapW, double mapH) {
        boolean moving = false;
        if (up && y > 50) { y -= speed; moving = true; }
        if (down && y < mapH - 50) { y += speed; moving = true; }
        if (left && x > 30) { x -= speed; facingRight = false; moving = true; }
        if (right && x < mapW - 30) { x += speed; facingRight = true; moving = true; }
        this.isWalking = moving;
    }

    @Override
    public void update() {}

    /**
     * วาดตัวละคร สลับเท้าซ้ายขวาเมื่อเดิน พลิกภาพซ้ายขวาตามทิศที่หัน
     * @param gc กราฟิกคอนเท็กซ์
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (isWalking) {
            walkTimer++;
            if (walkTimer >= 10) { showFrame1 = !showFrame1; walkTimer = 0; }
        } else {
            showFrame1 = true; walkTimer = 0;
        }
        Image img = showFrame1 ? image1 : image2;
        if (img != null) {
            gc.save(); gc.translate(x, y);
            if (!facingRight) gc.scale(-1, 1);
            gc.drawImage(img, -20, -20, 40, 40); gc.restore();
        } else {
            gc.setFill(Color.ORANGE); gc.fillRect(x - 20, y - 20, 40, 40);
        }
    }

    /** @return สถานะว่ารอเลือกของตอนเลเวลอัปอยู่หรือไม่ */
    public boolean hasPendingLevelUp() { return pendingLevelUp; }
    /** ยกเลิกสถานะรอเลเวลอัป (ไปเล่นเกมต่อ) */
    public void clearPendingLevelUp() { pendingLevelUp = false; }
    public int getHp() { return hp; }
    public int getMaxHp() { return maxHp; }
    public int getExp() { return exp; }
    public int getMaxExp() { return maxExp; }
    public int getLevel() { return level; }
    public boolean isFacingRight() { return facingRight; }
    public List<Weapon> getWeapons() { return weapons; }
}