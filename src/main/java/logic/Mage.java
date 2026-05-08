package logic;
public class Mage extends Player {
    public Mage(double x, double y) {
        super(x, y, 3.0, "mage.png", 40, 200.0);
        this.addOrUpgradeWeapon(new MagicWand()); // เมจเริ่มด้วยคทาเวท
    }
}