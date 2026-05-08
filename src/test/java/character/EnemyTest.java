package character;

import entity.character.Bat;
import entity.character.Enemy;
import entity.character.Knight;
import entity.character.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * คลาสทดสอบพฤติกรรมของศัตรู เช่น การเดินไล่ล่า และการรับดาเมจ
 */
public class EnemyTest {
    private Player target;
    private Enemy bat;

    @BeforeEach
    void setUp() {
        target = new Knight(500, 500); // ผู้เล่นอยู่ตรงกลาง
        bat = new Bat(100, 100, target); // ค้างคาวเกิดมุมซ้ายบน
    }

    @Test
    void testEnemyTakesDamage() {
        bat.takeDamage(10);
        assertFalse(bat.isDead(), "ค้างคาวเลือด 15 โดน 10 ต้องยังไม่ตาย");

        bat.takeDamage(10);
        assertTrue(bat.isDead(), "ค้างคาวโดนอีก 10 เลือดหมดต้องตาย");
    }

    @Test
    void testEnemyMovesTowardsPlayer() {
        double initialDistance = Math.hypot(target.getX() - bat.getX(), target.getY() - bat.getY());

        bat.update(); // สั่งให้อัปเดต 1 เฟรม

        double newDistance = Math.hypot(target.getX() - bat.getX(), target.getY() - bat.getY());
        assertTrue(newDistance < initialDistance, "ระยะห่างระหว่างศัตรูกับผู้เล่นต้องลดลง (เดินเข้าหา)");
    }

    @Test
    void testEnemyAttacksWhenClose() {
        // จับค้างคาวไปวางบนตัวผู้เล่นเลย
        Enemy closeBat = new Bat(500, 500, target);
        int initialPlayerHp = target.getHp();

        closeBat.update(); // อัปเดตเพื่อให้เช็คระยะชน

        assertTrue(target.getHp() < initialPlayerHp, "เลือดผู้เล่นต้องลดลงเพราะโดนชน");
        assertTrue(closeBat.isDead(), "ศัตรูเมื่อชนทำดาเมจแล้วต้องตาย (ตามกติกาเกมนี้)");
    }
}