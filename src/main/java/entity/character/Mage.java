package entity.character;
import entity.weapon.MagicWand;

/** ตัวละครนักเวท เลือดน้อย เดินเร็วระดับกลาง อาชีพเริ่มต้นพกคทาเวท (MagicWand) */
public class Mage extends Player {
    /** สร้างตัวละครนักเวท */
    public Mage(double x, double y) {
        super(x, y, 3.0, "mage.png", 40, 200.0);
        this.addOrUpgradeWeapon(new MagicWand());
    }
}