package logic;

/**
 * คลาสอัศวิน (Knight)
 * อาชีพผู้เล่นที่มีพลังชีวิตสูง เริ่มต้นด้วยอาวุธมีดสั้น (Knife)
 */
public class Knight extends Player {

    /**
     * สร้างตัวละครอัศวินที่ตำแหน่งเริ่มต้น
     * @param x ตำแหน่งพิกัด X
     * @param y ตำแหน่งพิกัด Y
     */
    public Knight(double x, double y) {
        super(x, y, 2.5, "knight.png", 100, 100.0);
        this.addOrUpgradeWeapon(new Knife()); // อัศวินเริ่มด้วยมีด
    }
}