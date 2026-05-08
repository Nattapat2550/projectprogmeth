package entity.character;

/** ค้างคาว ศัตรูที่บินไวมาก เลือดน้อย */
public class Bat extends Enemy {
    /** สร้างค้างคาว */
    public Bat(double x, double y, Player target) { super(x, y, 2.5, "bat.png", 15, target); }
}