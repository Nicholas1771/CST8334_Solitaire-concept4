import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class AudioPlayer {

    public static final String DRAG = "DRAG";
    public static final String DROP = "DROP";
    public static final String MISS = "MISS";
    public static final String CLICK = "CLICK";

    public static boolean muted;

    public static void playSound (String sound) {
        String path = "";

        switch (sound) {
            case DRAG:
                path = "sounds/drag.wav";
                break;
            case DROP:
                path = "sounds/drop.wav";
                break;
            case MISS:
            case CLICK:
                path = "sounds/miss.wav";
                break;
            default:
                path = "sounds/drag.wav";
        }

        try {
            File file = new File(path);

            if (file.exists()) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                if (!muted) {
                    clip.open(audioInputStream);
                    clip.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
