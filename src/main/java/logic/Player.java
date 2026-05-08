package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * คลาสนามธรรมสำหรับตัวละครผู้เล่น (Player)
 * ควบคุมสถานะ พลังชีวิต ค่าประสบการณ์ และแอนิเมชันการเดิน
 */
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

    /** จำนวนช่องเก็บอาวุธสูงสุดของผู้เล่น */
    public final int MAX_WEAPON_SLOTS = 5;

    protected Image image1;
    protected Image image2;
    protected boolean isWalking = false;
    protected int walkTimer = 0;
    protected boolean showFrame1 = true;

    /**
     * สร้างออบเจกต์ผู้เล่นพร้อมกำหนดค่าสถานะพื้นฐาน
     * @param x ตำแหน่งเริ่มต้นแกน X
     * @param y ตำแหน่งเริ่มต้นแกน Y
     * @param speed ความเร็วในการเดิน
     * @param imagePath ชื่อไฟล์ภาพของตัวละคร (จะถูกนำไปต่อท้ายด้วย 1 และ 2 สำหรับทำแอนิเมชัน)
     * @param maxHp พลังชีวิตสูงสุด
     * @param attackRadius ระยะการเก็บไอเทมหรือโจมตี
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
        } catch (Exception e) {
            System.out.println("โหลดรูป Animation ไม่สำเร็จ: " + baseName);
        }
    }

    /**
     * เพิ่มอาวุธใหม่ หรืออัปเกรดอาวุธเดิมถ้ามีอยู่แล้ว
     * @param newW อาวุธชิ้นใหม่ที่ผู้เล่นเก็บได้หรืออัปเกรด
     */
    public void addOrUpgradeWeapon(Weapon newW) {
        for (Weapon w : weapons) {
            if (w.getClass().equals(newW.getClass())) {
                w.levelUp();
                return;
            }
        }
        if (weapons.size() < MAX_WEAPON_SLOTS) {
            weapons.add(newW);
        }
    }

    /**
     * ฟื้นฟูพลังชีวิตให้ผู้เล่น
     * @param amount ปริมาณพลังชีวิตที่ฟื้นฟู
     */
    public void heal(int amount) {
        this.hp = Math.min(maxHp, this.hp + amount);
    }

    /**
     * เพิ่มพลังชีวิตสูงสุด
     * @param amount ปริมาณพลังชีวิตที่เพิ่มขึ้น
     */
    public void increaseMaxHp(int amount) {
        this.maxHp += amount;
        this.hp += amount;
    }

    /**
     * สั่งให้ตัวละครเคลื่อนที่ตามปุ่มที่ผู้เล่นกด พร้อมจัดการเรื่องการหันหน้า
     * @param up กดปุ่มขึ้น
     * @param down กดปุ่มลง
     * @param left กดปุ่มซ้าย
     * @param right กดปุ่มขวา
     * @param screenWidth ความกว้างของหน้าจอ
     * @param screenHeight ความสูงของหน้าจอ
     */
    public void move(boolean up, boolean down, boolean left, boolean right, double screenWidth, double screenHeight) {
        double TOP_MARGIN = 1000;
        double BOTTOM_MARGIN = 50;
        double LEFT_MARGIN = 30;
        double RIGHT_MARGIN = 30;

        boolean moving = false;

        if (up && y > TOP_MARGIN) {
            y -= speed;
            moving = true;
        }
        if (down && y < screenHeight - BOTTOM_MARGIN) {
            y += speed;
            moving = true;
        }
        if (left && x > LEFT_MARGIN) {
            x -= speed;
            facingRight = false;
            moving = true;
        }
        if (right && x < screenWidth - RIGHT_MARGIN) {
            x += speed;
            facingRight = true;
            moving = true;
        }

        this.isWalking = moving;
    }

    /**
     * อัปเดตสถานะของผู้เล่น (ไม่มีการใช้งานเฉพาะในขณะนี้)
     */
    @Override
    public void update() {}

    /**
     * วาดตัวละครผู้เล่นลงบนจอภาพ พร้อมจัดการแอนิเมชันสลับเท้าเดิน และการพลิกซ้ายขวา
     * @param gc กราฟิกคอนเท็กซ์
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (isWalking) {
            walkTimer++;
            if (walkTimer >= 10) {
                showFrame1 = !showFrame1;
                walkTimer = 0;
            }
        } else {
            showFrame1 = true;
            walkTimer = 0;
        }

        Image currentImage = showFrame1 ? image1 : image2;

        if (currentImage != null) {
            gc.save();
            gc.translate(x, y);

            if (!facingRight) {
                gc.scale(-1, 1);
            }

            gc.drawImage(currentImage, -20, -20, 40, 40);
            gc.restore();
        } else {
            gc.setFill(Color.ORANGE);
            gc.fillRect(x - 20, y - 20, 40, 40);
        }
    }

    /**
     * รับค่าประสบการณ์ (EXP) เมื่อหลอดเต็มจะเลเวลอัป
     * @param amount ปริมาณ EXP ที่ได้รับ
     */
    public void gainExp(int amount) {
        exp += amount;
        if (exp >= maxExp) {
            exp -= maxExp;
            level++;
            maxExp += 20;
            pendingLevelUp = true;
        }
    }

    /**
     * ทำให้ผู้เล่นได้รับความเสียหาย
     * @param damage ปริมาณความเสียหาย
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        SoundManager.playSFX("hit.mp3"); // เล่นเสียงตอนโดนตี
        if (this.hp <= 0) this.isDead = true;
    }

    /** @return สถานะว่ารอเลเวลอัปอยู่หรือไม่ */
    public boolean hasPendingLevelUp() { return pendingLevelUp; }

    /** เคลียร์สถานะการรอเลเวลอัป */
    public void clearPendingLevelUp() { pendingLevelUp = false; }

    /** @return พลังชีวิตปัจจุบัน */
    public int getHp() { return hp; }

    /** @return พลังชีวิตสูงสุด */
    public int getMaxHp() { return maxHp; }

    /** @return ค่าประสบการณ์ปัจจุบัน */
    public int getExp() { return exp; }

    /** @return ค่าประสบการณ์ที่ต้องการเพื่อเลเวลอัปถัดไป */
    public int getMaxExp() { return maxExp; }

    /** @return เลเวลปัจจุบันของผู้เล่น */
    public int getLevel() { return level; }

    /** @return ทิศทางการหันหน้า (true = ขวา, false = ซ้าย) */
    public boolean isFacingRight() { return facingRight; }

    /** @return รายการอาวุธทั้งหมดที่ผู้เล่นถืออยู่ */
    public List<Weapon> getWeapons() { return weapons; }
}