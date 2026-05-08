package entity.item;
import entity.base.Entity;
import entity.base.ICollectable;
import entity.character.Player;
import logic.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * คลาสอัญมณีค่าประสบการณ์ (ExpGem)
 * ตกจากศัตรู เมื่อผู้เล่นเก็บจะเพิ่มค่า EXP ให้ตัวละคร
 */
public class ExpGem extends Entity implements ICollectable {
    private int expValue;

    /**
     * สร้างอัญมณีค่าประสบการณ์
     * @param x ตำแหน่งตกแกน X
     * @param y ตำแหน่งตกแกน Y
     * @param imgName ชื่อไฟล์รูปภาพของอัญมณี
     * @param expValue จำนวนค่าประสบการณ์ที่จะได้รับเมื่อเก็บ
     */
    public ExpGem(double x, double y, String imgName, int expValue) {
        super(x, y, 0, imgName); // ไอเทมตกพื้น ความเร็ว = 0
        this.expValue = expValue;
    }

    /**
     * เมื่อถูกเก็บ จะเพิ่ม EXP ให้ผู้เล่น เล่นเสียงเอฟเฟกต์ และทำลายตัวเองทิ้ง
     * @param player ตัวละครผู้เล่นที่เก็บอัญมณี
     */
    @Override
    public void collect(Player player) {
        player.gainExp(this.expValue);
        SoundManager.playSFX("pickup.mp3");
        this.isDead = true; // เก็บแล้วหายไป
    }

    /** อัปเดตตรรกะ (อัญมณีอยู่นิ่ง ไม่ต้องทำอะไร) */
    @Override
    public void update() {}

    /**
     * วาดรูปอัญมณีลงบนหน้าจอ (ถ้าโหลดรูปไม่สำเร็จจะวาดสี่เหลี่ยมสีฟ้าแทน)
     * @param gc กราฟิกคอนเท็กซ์
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) gc.drawImage(image, x - 10, y - 10, 20, 20);
        else { gc.setFill(Color.AQUA); gc.fillRect(x - 5, y - 5, 10, 10); }
    }
}