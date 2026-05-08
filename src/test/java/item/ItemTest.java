package test.item;

import entity.character.Knight;
import entity.character.Player;
import entity.item.ExpGem;
import entity.item.Potion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * คลาสทดสอบการทำงานของไอเทมเมื่อถูกเก็บ (ICollectable)
 */
public class ItemTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Knight(100, 100);
    }

    @Test
    void testExpGemCollection() {
        ExpGem gem = new ExpGem(100, 100, "blue.png", 15);
        assertEquals(0, player.getExp());

        gem.collect(player); // จำลองการที่ผู้เล่นเดินชนแล้วระบบเรียก collect()

        assertEquals(15, player.getExp(), "EXP ของผู้เล่นต้องเพิ่มขึ้น 15");
        assertTrue(gem.isDead(), "เมื่อเก็บแล้ว เพชรต้องถูกกำหนดให้หายไป (isDead = true)");
    }

    @Test
    void testPotionCollection() {
        player.takeDamage(50); // ทำให้เลือดลดเหลือ 50
        Potion potion = new Potion(100, 100);

        potion.collect(player);

        assertEquals(80, player.getHp(), "เก็บขวดยาเลือดต้องเพิ่ม 30 (50 + 30 = 80)");
        assertTrue(potion.isDead(), "ขวดยาต้องหายไปหลังโดนเก็บ");
    }
}