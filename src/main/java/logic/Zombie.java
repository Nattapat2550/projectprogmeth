package logic;

/**
 * คลาสซอมบี้ (Zombie)
 * เป็นศัตรูประเภทที่มีความเร็วช้า แต่มีพลังชีวิตสูง
 */
public class Zombie extends Enemy {
    /**
     * สร้างซอมบี้ตำแหน่งเริ่มต้นและกำหนดเป้าหมาย
     * @param x ตำแหน่ง X เริ่มต้น
     * @param y ตำแหน่ง Y เริ่มต้น
     * @param target ผู้เล่นที่ต้องการติดตาม
     */
    public Zombie(double x, double y, Player target) {
        // วิ่งช้า 0.8, เลือด 60
        super(x, y, 0.8, "zombie.png", 60, target);
    }
}