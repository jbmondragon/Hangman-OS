import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainFrame extends JFrame {

    public static final String OPENING = "OPENING";
    public static final String HOME = "HOME";
    public static final String HANGMAN = "HANGMAN";
    public static final String ABOUT = "ABOUT";
    public static final String GAMEOVER = "GAMEOVER";
    public static final String WARNING = "WARNING";
    public static final String WIN = "WIN";
    public static final String LOSE = "LOSE";
    public static final String TRANSITION = "TRANSITION";

    private CardLayout cardLayout;
    private SoundManager soundManager;
    private JPanel cardPanel;
    private LevelTransition levelTransition;

    public MainFrame() {
        soundManager = SoundManager.getInstance();
        setupFrame();
        setupCards();
    }

    private void setupFrame() {
        setTitle("Hangman-OS");
        setBackground(Color.BLACK);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Stop all sounds when window is closed
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                soundManager.stopAllSounds();
            }
        });
    }

    private void setupCards() {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        Opening opening = new Opening();
        Home home = new Home();
        HangmanView hangman = new HangmanView();
        About about = new About();
        Warning warning = new Warning();
        Win win = new Win();
        Lose lose = new Lose();
        levelTransition = new LevelTransition();

        cardPanel.add(opening.createOpening(), OPENING);
        cardPanel.add(home.createHome(), HOME);
        cardPanel.add(hangman.createHangmanPanel(), HANGMAN);
        cardPanel.add(about.createAbout(), ABOUT);
        cardPanel.add(warning.createWarning(), WARNING);
        cardPanel.add(win.createWin(), WIN);
        cardPanel.add(lose.createLose(), LOSE);
        cardPanel.add(levelTransition.createTransitionPanel(), TRANSITION);

        setContentPane(cardPanel);

        // First Card
        cardLayout.show(cardPanel, OPENING);
        startDelayedTransition(WARNING, 2000);
    }

    // Loading effect
    private void startDelayedTransition(String nextScreen, int delayMs) {
        Timer timer = new Timer(delayMs, e -> {
            cardLayout.show(cardPanel, nextScreen);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void showScreen(String screen) {
        cardLayout.show(cardPanel, screen);
    }

    public void restartGame() {
        
        // Generating Level Screen
        levelTransition.startAnimation();
        cardLayout.show(cardPanel, TRANSITION);

        Timer loadTimer = new Timer(2000, e -> {
            levelTransition.stopAnimation();
            
            HangmanView newHangman = new HangmanView();
            JPanel newGamePanel = newHangman.createHangmanPanel();
            
            // Add the new game panel
            cardPanel.add(newGamePanel, HANGMAN);
            
            // Display Game
            cardLayout.show(cardPanel, HANGMAN);

            // Play Hangman-Start when restarting
            soundManager.playSound(SoundManager.HANGMAN_START);
        });
        
        loadTimer.setRepeats(false);
        loadTimer.start();
    }
}
