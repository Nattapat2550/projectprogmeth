package logic;

/** * คลาสเก็บค่าคงที่ (Constants/Configurations) ของเกม
 * ทำให้สามารถแก้ไขค่าตัวเลขสำคัญได้จากจุดเดียว ไม่ต้องตามแก้ทั่วทั้งโค้ด
 */
public class GameConfig {
    /** ความกว้างสุดของแผนที่ (ด่าน) */
    public static final double MAP_WIDTH = 2000;
    /** ความสูงสุดของแผนที่ (ด่าน) */
    public static final double MAP_HEIGHT = 2000;
    /** จำนวนเฟรมดีเลย์ก่อนที่มอนสเตอร์ตัวใหม่จะเกิด (ยิ่งน้อยยิ่งเกิดรัว) */
    public static final int ENEMY_SPAWN_INTERVAL = 45;
}