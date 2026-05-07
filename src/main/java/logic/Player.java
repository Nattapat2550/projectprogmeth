package logic;

public abstract class Player extends Entity {
    protected int hp;
    protected int maxHp;
    protected double attackRadius;
    protected int attackCooldown;
    protected int maxCooldown;

    public Player(double x, double y, double speed, String imagePath, int maxHp, double attackRadius, int maxCooldown) {
        super(x, y, speed, imagePath);
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackRadius = attackRadius;
        this.maxCooldown = maxCooldown;
        this.attackCooldown = 0;
    }

    public void move(boolean up, boolean down, boolean left, boolean right, double screenWidth, double screenHeight) {
        if (up && y > 0) y -= speed;
        if (down && y < screenHeight) y += speed;
        if (left && x > 0) x -= speed;
        if (right && x < screenWidth) x += speed;
    }

    @Override
    public void update() {
        if (attackCooldown > 0) attackCooldown--;
    }

    public boolean canAttack() { return attackCooldown <= 0; }
    public void resetCooldown() { this.attackCooldown = maxCooldown; }
    public double getAttackRadius() { return attackRadius; }
    public int getHp() { return hp; }

    public void takeDamage(int damage) {
        this.hp -= damage;
        if (this.hp <= 0) this.isDead = true;
    }
}