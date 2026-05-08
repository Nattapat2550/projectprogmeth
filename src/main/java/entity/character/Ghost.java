package entity.character;

/** ผี ศัตรูระดับความสามารถปานกลาง วิ่งความเร็วกลาง เลือดปานกลาง */
public class Ghost extends Enemy {
    /** สร้างผี */
    public Ghost(double x, double y, Player target) { super(x, y, 1.5, "ghost.png", 30, target); }
}