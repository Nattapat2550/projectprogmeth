package test.weapon;

import entity.character.Enemy;
import entity.character.Knight;
import entity.character.Player;
import entity.projectile.Projectile;
import entity.weapon.Knife;
import entity.weapon.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * คลาสทดสอบอาวุธ (การเลเวลอัป และการยิงกระสุน)
 */
public class WeaponTest {
    private Weapon knife;
    private Player player;
    private List<Enemy> enemies;
    private List<Projectile> projectiles;

    @BeforeEach
    void setUp() {
        knife = new Knife();
        player = new Knight(100, 100);
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();
    }

    @Test
    void testWeaponLevelUp() {
        assertEquals(1, knife.getLevel());

        knife.levelUp();
        assertEquals(2, knife.getLevel(), "เลเวลต้องเพิ่มเป็น 2");

        // ทดสอบอัปเกรดจนตัน (เลเวล 4)
        knife.levelUp();
        knife.levelUp();
        knife.levelUp(); // อัปเกิน
        assertEquals(4, knife.getLevel(), "เลเวลอาวุธสูงสุดควรตันที่ 4");
    }

    @Test
    void testWeaponFiringAndCooldown() {
        // อาวุธ Knife มีคูลดาวน์เริ่มต้น 30 เฟรม
        // ตอนเริ่มเกม คูลดาวน์เป็น 0 จึงควรยิงได้เลยในเฟรมแรก
        knife.update(player, enemies, projectiles);

        assertEquals(1, projectiles.size(), "ต้องมีการสร้างกระสุน 1 นัด");

        // เฟรมถัดไป คูลดาวน์ถูกเซ็ตเป็น 30 แล้ว จึงต้องยังไม่ยิงกระสุนเพิ่ม
        knife.update(player, enemies, projectiles);
        assertEquals(1, projectiles.size(), "ต้องไม่สร้างกระสุนเพิ่มเพราะติดคูลดาวน์อยู่");
    }
}