package logic;

public class Enemy extends Entity {
    private Player target;

    public Enemy(double x, double y, Player target) {
        super(x, y, 1.5, "enemy.png");
        this.target = target;
    }

    @Override
    public void update() {
        if (target == null || target.isDead()) return;

        double dx = target.getX() - this.x;
        double dy = target.getY() - this.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            this.x += (dx / distance) * speed;
            this.y += (dy / distance) * speed;
        }

        if (distance < 20) {
            target.takeDamage(10);
            this.isDead = true;
        }
    }
}