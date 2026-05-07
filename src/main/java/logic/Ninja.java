package logic;
public class Ninja extends Player {
    public Ninja(double x, double y) {
        super(x, y, 5.0, "ninja.png", 60, 70.0);
        this.addWeapon(new Garlic()); // นินจาเริ่มด้วยกระเทียม (เดินเร็วเอาไปถู)
    }
}