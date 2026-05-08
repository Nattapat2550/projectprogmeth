package entity.character;

/** ซอมบี้ ศัตรูที่เดินอืดอาดแต่ถึกมาก เลือดเยอะสุด */
public class Zombie extends Enemy {
    /** สร้างซอมบี้ */
    public Zombie(double x, double y, Player target) { super(x, y, 0.8, "zombie.png", 60, target); }
}