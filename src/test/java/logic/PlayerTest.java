package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    // @BeforeEach จะถูกรันก่อนเริ่มเทสทุกครั้ง เพื่อรีเซ็ตตัวละครใหม่
    @BeforeEach
    void setUp() {
        // สร้าง Knight เลือด 100 ระยะโจมตี 100
        player = new Knight(0, 0);
    }

    @Test
    void testTakeDamage() {
        // เทสว่าโดนตีแล้วเลือดลดจริงไหม
        player.takeDamage(20);
        assertEquals(80, player.getHp(), "เลือดควรเหลือ 80 หลังจากโดนดาเมจ 20");
        assertFalse(player.isDead(), "เลือดเหลือ 80 ยังไม่ควรตาย");

        // เทสว่าโดนตีจนเลือดหมด ต้องตาย
        player.takeDamage(100);
        assertTrue(player.isDead(), "เลือดตํ่ากว่าหรือเท่ากับ 0 สถานะ isDead ต้องเป็น true");
    }

    @Test
    void testGainExpAndLevelUp() {
        // หลอด EXP เริ่มต้นคือ 20
        assertEquals(1, player.getLevel(), "เลเวลเริ่มต้นควรเป็น 1");
        assertFalse(player.hasPendingLevelUp(), "ยังไม่ควรมีแจ้งเตือนเลเวลอัพ");

        // เก็บ EXP 10 (ยังไม่อัพ)
        player.gainExp(10);
        assertEquals(10, player.getExp(), "EXP ควรเพิ่มเป็น 10");
        assertEquals(1, player.getLevel(), "เลเวลยังต้องเป็น 1 อยู่");

        // เก็บเพิ่มอีก 15 (รวมเป็น 25 จะต้องทะลุหลอด 20 และเลเวลอัพ)
        player.gainExp(15);
        assertEquals(2, player.getLevel(), "เลเวลควรเพิ่มเป็น 2");
        assertEquals(5, player.getExp(), "EXP ที่เกินมา 5 ควรถูกเก็บไว้ในหลอดใหม่");
        assertTrue(player.hasPendingLevelUp(), "ต้องมีแจ้งเตือนรอเลือกอาวุธ (pendingLevelUp = true)");
    }
}