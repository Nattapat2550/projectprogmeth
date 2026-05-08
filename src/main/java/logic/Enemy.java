package logic;

/**
 * คลาส Enemy เป็นตัวแทนของศัตรูภายในเกม
 * สืบทอดมาจาก Entity โดยมีการระบุเป้าหมาย (Player) ที่มันจะเดินตามไปโจมตี
 */
public class Enemy extends Entity {
    private Player target;
    private int hp;

    /**
     * คอนสตรัคเตอร์สำหรับสร้างศัตรู
     * * @param x ตำแหน่งเริ่มต้นในแกน X
     * @param y ตำแหน่งเริ่มต้นในแกน Y
     * @param target ผู้เล่นที่ศัตรูต้องเดินตาม
     */
    public Enemy(double x, double y, Player target) {
        super(x, y, 1.5, "enemy.png");
        this.target = target;
        this.hp = 30; // ตั้งค่าเลือดเริ่มต้นของศัตรู
    }

    /**
     * รับความเสียหายจากอาวุธของผู้เล่น
     * * @param damage จำนวนความเสียหายที่ศัตรูได้รับ
     */
    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) {
            this.isDead = true;
        }
    }

    /**
     * อัปเดตพฤติกรรมของศัตรูในแต่ละเฟรม (คำนวณการเดินเข้าหาผู้เล่นและทำดาเมจ)
     */
    @Override
    public void update() {
        if (target == null || target.isDead()) return;

        // คำนวณระยะห่างระหว่างศัตรูกับผู้เล่น
        double dx = target.getX() - this.x;
        double dy = target.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // เดินตาม Player
        if (distance > 0) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;
        }

        // ถ้าชน Player ให้ทำดาเมจ
        if (distance < 20) {
            target.takeDamage(10);
            this.isDead = true; // ชนแล้วตายไปพร้อมกันเลยเพื่อป้องกันการชนรัวๆ
        }
    }
}