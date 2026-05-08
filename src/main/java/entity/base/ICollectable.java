package entity.base;
import entity.character.Player;

/** * อินเตอร์เฟซสำหรับไอเทมหรือวัตถุที่ผู้เล่นสามารถเดินชนเพื่อเก็บได้
 */
public interface ICollectable {
    /**
     * เมธอดทำงานเมื่อผู้เล่นเก็บไอเทม
     * @param player ตัวละครผู้เล่นที่เดินมาเก็บไอเทม
     */
    void collect(Player player);

    /**
     * ดึงค่าพิกัด X ของไอเทม
     * @return พิกัด X
     */
    double getX();

    /**
     * ดึงค่าพิกัด Y ของไอเทม
     * @return พิกัด Y
     */
    double getY();
}