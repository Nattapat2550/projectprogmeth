package entity.item;
import entity.base.Entity;
import entity.base.ICollectable;
import entity.character.Player;
import logic.SoundManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * คลาสขวดยาเพิ่มพลังชีวิต (Potion)
 * ตกจากศัตรูแบบสุ่ม เมื่อผู้เล่นเก็บจะช่วยฟื้นฟู HP
 */
public class Potion extends Entity implements ICollectable {

    /**
     * สร้างขวดยาฟื้นฟูพลังชีวิต
     * @param x ตำแหน่งตกแกน X
     * @param y ตำแหน่งตกแกน Y
     */
    public Potion(double x, double y) {
        super(x, y, 0, "potion.png");
    }

    /**
     * เมื่อผู้เล่นเก็บขวดยา จะเพิ่ม HP ให้ 30 หน่วย พร้อมเล่นเสียงและทำลายตัวเอง
     * @param player ตัวละครผู้เล่นที่เก็บขวดยา
     */
    @Override
    public void collect(Player player) {
        player.heal(30);
        SoundManager.playSFX("heal.mp3");
        this.isDead = true; // เก็บแล้วหายไป
    }

    /** อัปเดตตรรกะ (ขวดยาอยู่นิ่ง ไม่ต้องทำอะไร) */
    @Override
    public void update() {}

    /**
     * วาดขวดยาลงบนจอภาพ (ถ้าไม่มีรูปจะวาดวงกลมสีชมพูแทน)
     * @param gc กราฟิกคอนเท็กซ์
     */
    @Override
    public void draw(GraphicsContext gc) {
        if (image != null) gc.drawImage(image, x - 10, y - 10, 20, 20);
        else { gc.setFill(Color.PINK); gc.fillOval(x - 10, y - 10, 20, 20); }
    }
}