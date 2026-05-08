package logic;

/**
 * คลาสผี (Ghost)
 * เป็นศัตรูประเภทที่มีความเร็วและพลังชีวิตระดับปานกลาง
 */
public class Ghost extends Enemy {
    /**
     * สร้างผีที่ตำแหน่งเริ่มต้นและกำหนดเป้าหมาย
     * @param x ตำแหน่ง X เริ่มต้น
     * @param y ตำแหน่ง Y เริ่มต้น
     * @param target ผู้เล่นที่ต้องการติดตาม
     */
    public Ghost(double x, double y, Player target) {
        // วิ่ง 1.5, เลือด 30
        super(x, y, 1.5, "ghost.png", 30, target);
    }
}