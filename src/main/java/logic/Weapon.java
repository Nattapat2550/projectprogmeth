package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.net.URL;
import java.util.List;

/**
 * คลาสนามธรรม (Abstract Class) สำหรับระบบอาวุธทั้งหมดในเกม
 * กำหนดคุณสมบัติพื้นฐาน เช่น พลังโจมตี เลเวล คูลดาวน์ และรูปภาพไอคอน
 * เป็นคลาสแม่ให้คลาสอาวุธอื่นๆ สืบทอดไปใช้งาน
 */
public abstract class Weapon {
    protected int cooldown;
    protected int maxCooldown;
    protected int damage;
    protected int level = 1;
    protected String imageName;
    protected Image image;

    /**
     * สร้างอาวุธใหม่พร้อมระบุความเร็วในการโจมตีและพลังทำลาย
     * @param maxCooldown ระยะเวลาที่ต้องรอก่อนโจมตีครั้งต่อไป
     * @param damage พลังความเสียหายเริ่มต้นที่ทำได้
     * @param imageName ชื่อไฟล์ภาพไอคอนของอาวุธ (ต้องอยู่ในโฟลเดอร์ resources/images/)
     */
    public Weapon(int maxCooldown, int damage, String imageName) {
        this.maxCooldown = maxCooldown;
        this.damage = damage;
        this.imageName = imageName;
        try {
            URL res = ClassLoader.getSystemResource("images/" + imageName);
            if (res != null) {
                this.image = new Image(res.toString());
            }
        } catch (Exception e) {
            System.out.println("Error loading weapon image: " + imageName);
        }
    }

    /**
     * อัปเกรดเลเวลของอาวุธ (จำกัดสูงสุดที่เลเวล 4)
     * ช่วยเพิ่มพลังโจมตีและลดระยะเวลาคูลดาวน์ลง
     */
    public void levelUp() {
        if (level < 4) {
            level++;
            damage += 10; // เพิ่มดาเมจ 10 หน่วยทุกระดับ
            maxCooldown = Math.max(10, maxCooldown - 5); // ลดคูลดาวน์ลง (ความเร็วในการโจมตีสูงสุดคือคูลดาวน์ 10)
        }
    }

    /** * รับระดับเลเวลปัจจุบันของอาวุธ
     * @return เลเวลอาวุธ
     */
    public int getLevel() {
        return level;
    }

    /** * รับชื่อไฟล์ของรูปภาพไอคอนอาวุธ
     * @return ชื่อไฟล์ภาพ
     */
    public String getImageName() {
        return imageName;
    }

    /** * รับออบเจกต์รูปภาพอาวุธสำหรับนำไปแสดงใน GUI
     * @return รูปภาพ
     */
    public Image getImage() {
        return image;
    }

    /**
     * เมธอดนามธรรมสำหรับการโจมตีหรือปล่อยโปรเจกไทล์ (Polymorphism)
     * คลาสลูก (Subclass) ต้องทำการ Override เพื่อสร้างรูปแบบการโจมตีเฉพาะของตัวเอง
     * @param p ตัวละครผู้เล่นที่เป็นคนเรียกใช้อาวุธ
     * @param enemies รายการของศัตรูทั้งหมดที่อยู่ในฉาก
     * @param projs รายการของกระสุน (ใช้สำหรับอาวุธประเภทยิงหรือปา)
     */
    public abstract void fire(Player p, List<Enemy> enemies, List<Projectile> projs);

    /**
     * อัปเดตสถานะคูลดาวน์ของอาวุธในทุกๆ เฟรม
     * ถ้าคูลดาวน์เหลือ 0 จะทำการเรียกเมธอด fire() เพื่อโจมตีอัตโนมัติ
     * @param p ตัวละครผู้เล่น
     * @param e รายการศัตรูทั้งหมด
     * @param pr รายการกระสุนทั้งหมด
     */
    public void update(Player p, List<Enemy> e, List<Projectile> pr) {
        if (cooldown > 0) {
            cooldown--;
        } else {
            fire(p, e, pr);
            cooldown = maxCooldown;
        }
    }

    /**
     * วาดเอฟเฟกต์ของอาวุธ (ถ้ามี) ลงบนหน้าจอ
     * @param gc กราฟิกคอนเท็กซ์ (GraphicsContext) สำหรับการวาดภาพบน Canvas
     * @param p ตัวละครผู้เล่นเพื่อใช้อ้างอิงพิกัดตำแหน่งในการวาดเอฟเฟกต์
     */
    public void draw(GraphicsContext gc, Player p) {
        // ปล่อยว่างไว้เป็น Default
        // ให้คลาสลูก (เช่น Whip, Garlic) มา Override ทับถ้าต้องการวาดเอฟเฟกต์การโจมตี
    }
}