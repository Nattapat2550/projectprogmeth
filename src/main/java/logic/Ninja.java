package logic;

/**
 * คลาสนินจา (Ninja)
 * อาชีพผู้เล่นที่เคลื่อนที่ได้เร็วที่สุด เริ่มต้นด้วยอาวุธกระเทียม (Garlic)
 */
public class Ninja extends Player {

    /**
     * สร้างตัวละครนินจาที่ตำแหน่งเริ่มต้น
     * @param x ตำแหน่งพิกัด X
     * @param y ตำแหน่งพิกัด Y
     */
    public Ninja(double x, double y) {
        super(x, y, 5.0, "ninja.png", 60, 70.0);
        this.addOrUpgradeWeapon(new Garlic()); // นินจาเริ่มด้วยกระเทียม (เดินเร็วเอาไปถู)
    }
}