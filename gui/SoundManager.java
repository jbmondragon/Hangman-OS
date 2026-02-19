import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> clips;
    private boolean soundEnabled = true;
    private String currentlyPlayingLoop = null;

    // Keys
    public static final String ABOUT_PAGE = "About-Page";
    public static final String FLATLINE = "Flatline-after-S6";
    public static final String HANGMAN_START = "Hangman-Start";
    public static final String KEYBOARD = "Keyboard-Sound";
    public static final String LOSE = "Lose";
    public static final String S1 = "S1";
    public static final String S2 = "S2";
    public static final String S3 = "S3";
    public static final String S4 = "S4";
    public static final String S5 = "S5";
    public static final String S6 = "S6";
    public static final String TITLE_PAGE = "Title-Page"; // New constant

    private SoundManager() {
        clips = new HashMap<>();
        loadAllSounds();
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadAllSounds() {
        loadSound(ABOUT_PAGE, "About-Page.wav");
        loadSound(FLATLINE, "Flatline-after-S6.wav");
        loadSound(HANGMAN_START, "Hangman-Start.wav");
        loadSound(KEYBOARD, "Keyboard-Sound.wav");
        loadSound(LOSE, "Lose.wav");
        loadSound(S1, "S1.wav");
        loadSound(S2, "S2.wav");
        loadSound(S3, "S3.wav");
        loadSound(S4, "S4.wav");
        loadSound(S5, "S5.wav");
        loadSound(S6, "S6.wav");
        loadSound(TITLE_PAGE, "Title-Page.wav"); // Load the title page sound
    }

    private void loadSound(String key, String filename) {
        try {
            // Relative Filepath
            URL url = getClass().getResource("sounds/" + filename);

            if (url == null) {
                // Fallback: Check root level (if running from different context)
                url = getClass().getResource("/gui/sounds/" + filename);
            }

            if (url == null) {
                System.err.println("❌ Error: Sound file not found: " + filename);
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clips.put(key, clip);
            //System.out.println("✅ Loaded sound: " + key);

        } catch (Exception e) {
            System.err.println("❌ Error loading sound: " + filename);
            e.printStackTrace();
        }
    }

    public void playSound(String key) {
        if (!soundEnabled) return;
        
        Clip clip = clips.get(key);
        if (clip == null) return;

        if (clip.isRunning()) {
            clip.stop();
        }
        
        clip.setFramePosition(0);
        clip.start();
    }

    public void playSoundLoop(String key) {
        if (!soundEnabled) return;
        
        if (currentlyPlayingLoop != null) {
            stopSound(currentlyPlayingLoop);
        }
        
        Clip clip = clips.get(key);
        if (clip == null) return;

        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        currentlyPlayingLoop = key;
    }

    public void stopSound(String key) {
        Clip clip = clips.get(key);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
        }
        if (key.equals(currentlyPlayingLoop)) {
            currentlyPlayingLoop = null;
        }
    }

    public void stopAllSounds() {
        for (Map.Entry<String, Clip> entry : clips.entrySet()) {
            Clip clip = entry.getValue();
            if (clip != null && clip.isRunning()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
        currentlyPlayingLoop = null;
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        if (!enabled) {
            stopAllSounds();
        }
    }
}
