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

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private SoundManager soundManager;
    private String currentScreen = OPENING;
    private boolean gameStarted = false;

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

        cardPanel.add(opening.createOpening(), OPENING);
        cardPanel.add(home.createHome(), HOME);
        cardPanel.add(hangman.createHangmanPanel(), HANGMAN);
        cardPanel.add(about.createAbout(), ABOUT);
        cardPanel.add(warning.createWarning(), WARNING);
        cardPanel.add(win.createWin(), WIN);
        cardPanel.add(lose.createLose(), LOSE);

        setContentPane(cardPanel);

        // First Card
        cardLayout.show(cardPanel, OPENING);
        currentScreen = OPENING;
        
        // Start transition to warning after 2 seconds
        startDelayedTransition(WARNING, 2000);
    }

    // Loading effect
    private void startDelayedTransition(String nextScreen, int delayMs) {
        Timer timer = new Timer(delayMs, e -> {
            showScreen(nextScreen);
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void showScreen(String screen) {
        System.out.println("Switching to screen: " + screen);
        
        // Stop all sounds when switching screens except for specific cases
        if (!screen.equals(HANGMAN)) {
            soundManager.stopAllSounds();
        }
        
        // Play Hangman-Start when switching to HANGMAN screen
        if (screen.equals(HANGMAN)) {
            soundManager.playSound(SoundManager.HANGMAN_START);
        }
        
        cardLayout.show(cardPanel, screen);
        currentScreen = screen;
    }

    public void restartGame() {
        System.out.println("Restarting game...");
        
        // Stop all sounds before restarting
        soundManager.stopAllSounds();

        // Remove old Hangman panel and create new one
        int hangmanIndex = -1;
        for (int i = 0; i < cardPanel.getComponentCount(); i++) {
            if (cardPanel.getComponents()[i].getName() != null && 
                cardPanel.getComponents()[i].getName().equals(HANGMAN)) {
                hangmanIndex = i;
                break;
            }
        }
        
        if (hangmanIndex >= 0) {
            cardPanel.remove(hangmanIndex);
        }

        HangmanView newHangman = new HangmanView();
        JPanel newHangmanPanel = newHangman.createHangmanPanel();
        newHangmanPanel.setName(HANGMAN);
        cardPanel.add(newHangmanPanel, HANGMAN, hangmanIndex >= 0 ? hangmanIndex : cardPanel.getComponentCount());

        cardPanel.revalidate();
        cardPanel.repaint();

        cardLayout.show(cardPanel, HANGMAN);
        currentScreen = HANGMAN;
        
        // Play Hangman-Start when restarting
        soundManager.playSound(SoundManager.HANGMAN_START);
    }

    public String getCurrentScreen() {
        return currentScreen;
    }
}
