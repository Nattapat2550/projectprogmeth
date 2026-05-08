package logic;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * คลาสบริหารจัดการระบบเสียงทั้งหมดของเกม (Sound Manager)
 * ป้องกันปัญหาเสียงทับซ้อนกันเกินไป (Spamming) ด้วยระบบ Cooldown
 */
public class SoundManager {
    private static MediaPlayer bgmPlayer;
    private static Map<String, AudioClip> sfxCache = new HashMap<>();
    private static Map<String, Long> lastPlayed = new HashMap<>();

    /**
     * เปิดเพลงพื้นหลังแบบลูป (BGM)
     * @param fileName ชื่อไฟล์เสียง .mp3
     */
    public static void playBGM(String fileName) {
        try {
            URL url = ClassLoader.getSystemResource("sounds/" + fileName);
            if (url != null) {
                if (bgmPlayer != null) bgmPlayer.stop();
                bgmPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(0.15); // ปรับเบาหน่อยไม่ให้กลบเอฟเฟกต์
                bgmPlayer.play();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    /**
     * เล่นเสียงเอฟเฟกต์ (SFX) ครั้งเดียว (เช่น ตีโดน เก็บของ)
     * มีระบบคูลดาวน์ป้องกันเสียงรัวจนลำโพงแตก
     * @param fileName ชื่อไฟล์เสียง
     */
    public static void playSFX(String fileName) {
        long now = System.currentTimeMillis();
        // ดีเลย์ห่างกันอย่างน้อย 80ms ต่อเสียงชิ้นเดิม
        if (lastPlayed.getOrDefault(fileName, 0L) + 80 > now) return;

        if (!sfxCache.containsKey(fileName)) {
            URL url = ClassLoader.getSystemResource("sounds/" + fileName);
            if (url != null) sfxCache.put(fileName, new AudioClip(url.toExternalForm()));
        }

        AudioClip clip = sfxCache.get(fileName);
        if (clip != null) {
            // ปรับโวลุ่มตามประเภทเสียงอัตโนมัติ
            if (fileName.contains("hit")) clip.setVolume(0.05);
            else if (fileName.contains("pickup") || fileName.contains("heal")) clip.setVolume(0.4);
            else clip.setVolume(0.2);

            clip.play();
            lastPlayed.put(fileName, now);
        }
    }

    /** หยุดเสียง BGM */
    public static void stopBGM() { if (bgmPlayer != null) bgmPlayer.stop(); }
}