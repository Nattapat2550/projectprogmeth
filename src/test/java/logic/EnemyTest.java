package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {
    private Player player;
    private Enemy zombie;

    @BeforeEach
    void setUp() {
        player = new Knight(100, 100);
        zombie = new Zombie(100, 50, player);
    }

    @Test
    void testEnemyMovement() {
        double initialY = zombie.getY();
        zombie.update();
        assertTrue(zombie.getY() > initialY, "Zombie should move towards player");
    }

    @Test
    void testTakeDamage() {
        zombie.takeDamage(60);
        assertTrue(zombie.isDead(), "Zombie with 60 HP should die after 60 damage");
    }
}