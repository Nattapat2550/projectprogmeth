package logic;

/**
 * คลาสนักเวท (Mage)
 * อาชีพผู้เล่นที่เคลื่อนที่ได้รวดเร็วแต่พลังชีวิตน้อย เริ่มต้นด้วยอาวุธคทาเวท (MagicWand)
 */
public class Mage extends Player {

    /**
     * สร้างตัวละครนักเวทที่ตำแหน่งเริ่มต้น
     * @param x ตำแหน่งพิกัด X
     * @param y ตำแหน่งพิกัด Y
     */
    public Mage(double x, double y) {
        super(x, y, 3.0, "mage.png", 40, 200.0);
        this.addOrUpgradeWeapon(new MagicWand()); // เมจเริ่มด้วยคทาเวท
    }
}