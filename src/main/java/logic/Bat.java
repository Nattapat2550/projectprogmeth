package logic;

/**
 * คลาสค้างคาว (Bat) เป็นศัตรูประเภทที่เคลื่อนที่เร็ว
 * สืบทอดคุณสมบัติมาจากคลาส Enemy
 */
public class Bat extends Enemy {
    /**
     * สร้างค้างคาวที่ตำแหน่งเริ่มต้นและกำหนดเป้าหมาย
     * @param x ตำแหน่ง X เริ่มต้น
     * @param y ตำแหน่ง Y เริ่มต้น
     * @param target ผู้เล่นที่ต้องการให้ค้างคาวติดตาม
     */
    public Bat(double x, double y, Player target) {
        super(x, y, 2.5, "bat.png", 15, target);
    }
}