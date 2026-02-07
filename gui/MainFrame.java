import java.awt.CardLayout;
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

    public MainFrame() {
        setupFrame();
        setupCards();
    }

    private void setupFrame() {
        setTitle("Board yarn");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        // Show opening first
        cardLayout.show(cardPanel, OPENING);
        startDelayedTransition(WARNING, 2000);
    }

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

        cardPanel.remove(cardPanel.getComponent(
                java.util.Arrays.asList(cardPanel.getComponents())
                        .indexOf(cardPanel.getComponent(2))));

        HangmanView newHangman = new HangmanView();
        cardPanel.add(newHangman.createHangmanPanel(), HANGMAN);

        cardPanel.revalidate();
        cardPanel.repaint();

        cardLayout.show(cardPanel, HANGMAN);
    }
}
