package logic;

public class Enemy extends Entity {
    private Player target;
    private int hp; // เพิ่มตัวแปรเก็บเลือดของศัตรู

    public Enemy(double x, double y, Player target) {
        super(x, y, 1.5, "enemy.png");
        this.target = target;
        this.hp = 30; // กำหนดเลือดของศัตรู (ตั้งไว้ที่ 30)
    }

    // เพิ่มเมธอดรับดาเมจจากอาวุธ
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.isDead = true; // ถ้าเลือดหมด ให้สถานะเป็นตาย
        }
    }

    @Override
    public void update() {
        if (target == null || target.isDead()) return;

        // เดินตาม Player
        double dx = target.getX() - this.x;
        double dy = target.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;
        }

        // ถ้าชน Player ให้ทำดาเมจใส่ Player
        if (distance < 20) {
            target.takeDamage(10);
            this.isDead = true; // ชน Player แล้วศัตรูหายไป
        }
    }
}