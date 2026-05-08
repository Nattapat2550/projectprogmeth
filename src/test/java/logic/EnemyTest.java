package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    private Enemy enemy;
    private Player player;

    @BeforeEach
    void setUp() {
        // สร้างผู้เล่นไว้ที่พิกัด (100, 100)
        player = new Knight(100, 100);
        // สร้างศัตรูไว้ที่พิกัด (100, 50) ห่างจากผู้เล่น 50 พิกเซล
        enemy = new Enemy(100, 50, player);
    }

    @Test
    void testEnemyTakeDamage() {
        // ศัตรูเริ่มต้นมี HP = 30
        enemy.takeDamage(10);
        assertFalse(enemy.isDead(), "ศัตรูโดนดาเมจ 10 เลือดเหลือ 20 ยังไม่ควรตาย");

        enemy.takeDamage(20);
        assertTrue(enemy.isDead(), "ศัตรูโดนดาเมจรวม 30 เลือดหมดพอดี ต้องตาย");
    }

    @Test
    void testEnemyMovementTowardsPlayer() {
        double initialY = enemy.getY();

        // สั่งให้ศัตรูอัปเดต 1 เฟรม
        enemy.update();

        // ศัตรูอยู่ด้านบน (y=50) ผู้เล่นอยู่ด้านล่าง (y=100) ศัตรูต้องมีค่า Y เพิ่มขึ้น
        assertTrue(enemy.getY() > initialY, "ศัตรูควรจะเดินลงมาหาผู้เล่น (ค่า Y ต้องเพิ่มขึ้น)");
    }
}