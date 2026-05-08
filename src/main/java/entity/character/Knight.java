package entity.character;
import entity.weapon.Knife;

/** ตัวละครอัศวิน เลือดเยอะที่สุด อาชีพเริ่มต้นพกมีดสั้น (Knife) */
public class Knight extends Player {
    /** สร้างตัวละครอัศวิน */
    public Knight(double x, double y) {
        super(x, y, 2.5, "knight.png", 100, 100.0);
        this.addOrUpgradeWeapon(new Knife());
    }
}