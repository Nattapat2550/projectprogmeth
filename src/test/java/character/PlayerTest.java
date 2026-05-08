package character;

import entity.character.Knight;
import entity.character.Player;
import entity.weapon.Axe;
import entity.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * คลาสทดสอบการทำงานของ Player (เช่น การเพิ่ม HP, การรับ EXP, การเดิน และการเลเวลอัป)
 */
public class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        // สร้างอัศวินที่พิกัด (100, 100) ก่อนเริ่มเทสทุกครั้ง
        player = new Knight(100, 100);
    }

    @Test
    void testInitialStatus() {
        assertEquals(100, player.getHp(), "เลือดเริ่มต้นของอัศวินควรเป็น 100");
        assertEquals(100, player.getMaxHp(), "เลือดสูงสุดเริ่มต้นควรเป็น 100");
        assertEquals(1, player.getLevel(), "เลเวลเริ่มต้นควรเป็น 1");
        assertFalse(player.isDead(), "ผู้เล่นเกิดใหม่ต้องยังไม่ตาย");
        assertEquals(1, player.getWeapons().size(), "อัศวินควรมีมีดสั้นเป็นอาวุธเริ่มต้น 1 ชิ้น");
    }

    @Test
    void testTakeDamageAndDeath() {
        player.takeDamage(40);
        assertEquals(60, player.getHp(), "โดนดาเมจ 40 เลือดต้องเหลือ 60");
        assertFalse(player.isDead());

        player.takeDamage(70); // โดนดาเมจเกินเลือดที่เหลือ
        assertTrue(player.getHp() <= 0, "เลือดต้องเหลือน้อยกว่าหรือเท่ากับ 0");
        assertTrue(player.isDead(), "ผู้เล่นต้องตายเมื่อเลือดหมด");
    }

    @Test
    void testHealAndIncreaseMaxHp() {
        player.takeDamage(50);
        player.heal(30);
        assertEquals(80, player.getHp(), "ฮีล 30 เลือดต้องเด้งกลับมาเป็น 80");

        player.heal(100); // ฮีลเกิน Max HP
        assertEquals(100, player.getHp(), "เลือดต้องไม่เกิน Max HP");

        player.increaseMaxHp(20);
        assertEquals(120, player.getMaxHp(), "Max HP ต้องเพิ่มเป็น 120");
        assertEquals(120, player.getHp(), "การเพิ่ม Max HP ควรเพิ่ม HP ปัจจุบันด้วย");
    }

    @Test
    void testGainExpAndLevelUp() {
        int initialMaxExp = player.getMaxExp();
        player.gainExp(initialMaxExp - 5); // ได้ EXP เกือบเต็ม
        assertEquals(1, player.getLevel());
        assertFalse(player.hasPendingLevelUp());

        player.gainExp(10); // ได้ EXP ทะลุหลอด
        assertEquals(2, player.getLevel(), "เลเวลต้องเพิ่มขึ้น");
        assertTrue(player.hasPendingLevelUp(), "ต้องมีสถานะรอเลือกสกิล (Level Up)");
        assertTrue(player.getMaxExp() > initialMaxExp, "หลอด EXP ต้องยาวขึ้นในเลเวลถัดไป");
    }

    @Test
    void testAddAndUpgradeWeapon() {
        Weapon axe = new Axe();
        player.addOrUpgradeWeapon(axe);
        assertEquals(2, player.getWeapons().size(), "จำนวนอาวุธต้องเพิ่มเป็น 2 ชิ้น");
        assertEquals(1, axe.getLevel(), "ขวานใหม่ต้องเลเวล 1");

        player.addOrUpgradeWeapon(new Axe()); // เก็บขวานซ้ำ
        assertEquals(2, player.getWeapons().size(), "จำนวนชิ้นอาวุธต้องไม่เพิ่มขึ้นเพราะซ้ำ");
        assertEquals(2, player.getWeapons().get(1).getLevel(), "ขวานต้องถูกอัปเป็นเลเวล 2");
    }

    @Test
    void testPlayerMovement() {
        double startX = player.getX();
        double startY = player.getY();

        // เดินขวาและลง (สมมติหน้าจอขอบเขต 2000x2000)
        player.move(false, true, false, true, 2000, 2000);
        assertTrue(player.getX() > startX, "พิกัด X ต้องเพิ่มขึ้นเมื่อเดินขวา");
        assertTrue(player.getY() > startY, "พิกัด Y ต้องเพิ่มขึ้นเมื่อเดินลง");
        assertTrue(player.isFacingRight(), "ต้องหันหน้าไปทางขวา");

        // เดินซ้าย
        player.move(false, false, true, false, 2000, 2000);
        assertFalse(player.isFacingRight(), "ต้องหันหน้าไปทางซ้ายเมื่อเดินซ้าย");
    }
}