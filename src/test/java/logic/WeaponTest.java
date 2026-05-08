package logic;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WeaponTest {

    @Test
    void testKnifeFire() {
        Player player = new Knight(0, 0);
        List<Enemy> enemies = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();

        Weapon knife = new Knife();

        // สั่งยิงมีด (fire)
        knife.fire(player, enemies, projectiles);

        // ต้องมี Projectile 1 ชิ้นถูกสร้างขึ้นไปอยู่ใน List
        assertEquals(1, projectiles.size(), "เมื่ออาวุธ Knife สั่ง fire() จะต้องมีการสร้าง Projectile 1 ลูก");
        assertEquals(20, projectiles.get(0).damage, "ดาเมจของมีดที่ถูกสร้างขึ้นต้องเท่ากับ 20");
    }
}