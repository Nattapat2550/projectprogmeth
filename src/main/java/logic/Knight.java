package logic;
public class Knight extends Player {
    public Knight(double x, double y) {
        super(x, y, 2.5, "knight.png", 100, 100.0);
        this.addOrUpgradeWeapon(new Knife());// อัศวินเริ่มด้วยมีด
    }
}