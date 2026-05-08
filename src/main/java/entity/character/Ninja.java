package entity.character;
import entity.weapon.Garlic;

/** ตัวละครนินจา เดินเร็วปรี๊ด เลือดปานกลาง อาชีพเริ่มต้นพกกระเทียม (Garlic) (เดินเอาตัวชนได้เลย) */
public class Ninja extends Player {
    /** สร้างตัวละครนินจา */
    public Ninja(double x, double y) {
        super(x, y, 5.0, "ninja.png", 60, 70.0);
        this.addOrUpgradeWeapon(new Garlic());
    }
}