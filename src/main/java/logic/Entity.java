package logic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.net.URL;

public abstract class Entity {
    protected double x, y;
    protected double speed;
    protected Image image;
    protected boolean isDead;

    public Entity(double x, double y, double speed, String imageFileName) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.isDead = false;

        try {
            // ค้นหาไฟล์รูปภาพ (ลองหาในโฟลเดอร์ images ก่อน ถ้าไม่เจอให้หาหน้าสุด)
            URL resource = ClassLoader.getSystemResource("images/" + imageFileName);
            if (resource == null) {
                resource = ClassLoader.getSystemResource(imageFileName);
            }

            if (resource != null) {
                this.image = new Image(resource.toString());
            } else {
                System.out.println("หาไฟล์รูปไม่พบ: " + imageFileName);
            }
        } catch (Exception e) {
            System.out.println("หาไฟล์รูปไม่พบ: " + imageFileName);
        }
    }

    public abstract void update();

    public void draw(GraphicsContext gc) {
        if (image != null) {
            // วาดรูปกึ่งกลาง x, y
            gc.drawImage(image, x - 20, y - 20, 40, 40);
        } else {
            // กรณีรูปไม่ขึ้น ให้วาดเป็นกล่องสีส้มแทน จะได้เล่นได้
            gc.setFill(Color.ORANGE);
            gc.fillRect(x - 15, y - 15, 30, 30);
        }
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isDead() { return isDead; }
    public void setDead(boolean dead) { this.isDead = dead; }
}