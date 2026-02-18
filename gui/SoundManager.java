// SoundManager.java
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.*;

public class SoundManager {
    private static SoundManager instance;
    private Map<String, Clip> clips;
    private boolean soundEnabled = true;
    private String currentlyPlayingLoop = null;

    // Sound file paths - relative to project root
    private static final String SOUND_PATH = "gui/sounds/";
    
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
        // Use the exact filenames as shown in your image
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
    }

    private void loadSound(String key, String filename) {
        try {
            File soundFile = new File(SOUND_PATH + filename);
            if (!soundFile.exists()) {
                System.err.println("Sound file not found: " + soundFile.getAbsolutePath());
                System.err.println("Current working directory: " + System.getProperty("user.dir"));
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clips.put(key, clip);
            System.out.println("Loaded sound: " + key + " from " + filename);
        } catch (Exception e) {
            System.err.println("Error loading sound: " + filename);
            e.printStackTrace();
        }
    }

    public void playSound(String key) {
        if (!soundEnabled) return;
        
        Clip clip = clips.get(key);
        if (clip == null) {
            System.err.println("Sound not loaded: " + key);
            return;
        }

        // Stop the clip if it's currently playing
        if (clip.isRunning()) {
            clip.stop();
        }
        
        clip.setFramePosition(0);
        clip.start();
        System.out.println("Playing sound: " + key);
    }

    public void playSoundLoop(String key) {
        if (!soundEnabled) return;
        
        // Stop current loop if any
        if (currentlyPlayingLoop != null) {
            stopSound(currentlyPlayingLoop);
        }
        
        Clip clip = clips.get(key);
        if (clip == null) {
            System.err.println("Sound not loaded: " + key);
            return;
        }

        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        currentlyPlayingLoop = key;
        System.out.println("Playing loop: " + key);
    }

    public void stopSound(String key) {
        Clip clip = clips.get(key);
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.setFramePosition(0);
            System.out.println("Stopped sound: " + key);
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
                System.out.println("Stopped sound: " + entry.getKey());
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

    public boolean isSoundEnabled() {
        return soundEnabled;
    }
}