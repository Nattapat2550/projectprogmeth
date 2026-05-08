package application;

/**
 * คลาสนี้สร้างขึ้นเพื่อใช้เป็นจุด Entry Point สำหรับการรันไฟล์ .jar (Fat JAR)
 * เพื่อป้องกันปัญหา JavaFX runtime components are missing
 */
public class Launcher {
    public static void main(String[] args) {
        // ให้เรียกคลาส Main หลักของเกมต่ออีกที
        Main.main(args);
    }
}