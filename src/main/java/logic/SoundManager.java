package logic;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * คลาสสำหรับจัดการระบบเสียงทั้งหมดในเกม (Background Music และ Sound Effects)
 * มีระบบ Cache และ Cooldown เพื่อป้องกันเสียงซ้อนและกระตุก
 */
public class SoundManager {
    private static MediaPlayer bgmPlayer;
    private static Map<String, AudioClip> sfxCache = new HashMap<>();
    private static Map<String, Long> lastPlayed = new HashMap<>();

    /**
     * เล่นเพลงประกอบพื้นหลังแบบวนซ้ำ (MediaPlayer)
     * @param fileName ชื่อไฟล์เพลงในโฟลเดอร์ sounds/
     */
    public static void playBGM(String fileName) {
        try {
            URL url = ClassLoader.getSystemResource("sounds/" + fileName);
            if (url != null) {
                if (bgmPlayer != null) bgmPlayer.stop();
                bgmPlayer = new MediaPlayer(new Media(url.toExternalForm()));
                bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                bgmPlayer.setVolume(0.15); // ปรับความดัง BGM
                bgmPlayer.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * เล่นเสียงเอฟเฟกต์แบบสั้น (AudioClip) พร้อมระบบลดความดังและป้องกันเสียงซ้อน
     * @param fileName ชื่อไฟล์เสียงเอฟเฟกต์
     */
    public static void playSFX(String fileName) {
        long now = System.currentTimeMillis();

        // กำหนด Cooldown: ถ้าเพิ่งเล่นเสียงนี้ไปใน 80ms ที่ผ่านมา ให้ข้ามไปเลยกันเสียงช็อต
        if (lastPlayed.getOrDefault(fileName, 0L) + 80 > now) {
            return;
        }

        if (!sfxCache.containsKey(fileName)) {
            URL url = ClassLoader.getSystemResource("sounds/" + fileName);
            if (url != null) {
                sfxCache.put(fileName, new AudioClip(url.toExternalForm()));
            }
        }

        AudioClip clip = sfxCache.get(fileName);
        if (clip != null) {
            // ปรับระดับความดังแยกตามประเภทของเสียง
            if (fileName.contains("hit")) {
                clip.setVolume(0.05); // เสียงตีมอนสเตอร์ (เบาๆ)
            } else if (fileName.contains("pickup")) {
                clip.setVolume(0.4);  // เสียงเก็บของ
            } else {
                clip.setVolume(0.2);  // เสียงอื่นๆ ทั่วไป
            }

            clip.play();
            lastPlayed.put(fileName, now); // บันทึกเวลาที่เล่นล่าสุด
        }
    }

    /**
     * หยุดเพลงประกอบพื้นหลังทั้งหมด
     */
    public static void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
        }
    }
}