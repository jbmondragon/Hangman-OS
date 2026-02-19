import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Point;
import javax.swing.JButton;
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
    public static final String TITLE = "TITLE";

    private CardLayout cardLayout;
    private SoundManager soundManager;
    private JPanel cardPanel;
    private LevelTransition levelTransition;

    public MainFrame() {
        soundManager = SoundManager.getInstance();
        setUndecorated(true);
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
        cardPanel.setBackground(Color.BLACK);

        Opening opening = new Opening();
        Home home = new Home();
        HangmanView hangman = new HangmanView();
        About about = new About();
        Warning warning = new Warning();
        Win win = new Win();
        Lose lose = new Lose();
        levelTransition = new LevelTransition();
        TitlePage titlePage = new TitlePage();

        cardPanel.add(opening.createOpening(), OPENING);
        cardPanel.add(home.createHome(), HOME);
        cardPanel.add(hangman.createHangmanPanel(), HANGMAN);
        cardPanel.add(about.createAbout(), ABOUT);
        cardPanel.add(warning.createWarning(), WARNING);
        cardPanel.add(win.createWin(), WIN);
        cardPanel.add(lose.createLose(), LOSE);
        cardPanel.add(levelTransition.createTransitionPanel(), TRANSITION);
        cardPanel.add(titlePage.createTitlePanel(), TITLE);

        // padding
        JPanel outerWrapper = new JPanel(new BorderLayout());
        outerWrapper.setBackground(Color.BLACK);

        outerWrapper.setBorder(
                javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cardPanel.setBorder(
                javax.swing.BorderFactory.createLineBorder(Color.gray, 3));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(Color.BLACK);
        titleBar.setPreferredSize(new java.awt.Dimension(1000, 40));

        javax.swing.JLabel title = new javax.swing.JLabel("");
        title.setForeground(Color.black);
        title.setFont(new java.awt.Font("Monospaced", java.awt.Font.BOLD, 16));

        // Buttons panel
        JPanel buttons = new JPanel();
        buttons.setBackground(Color.BLACK);

        JButton minimize = new JButton("—");
        JButton close = new JButton("X");

        styleButton(minimize);
        styleButton(close);

        minimize.addActionListener(e -> setState(JFrame.ICONIFIED));
        close.addActionListener(e -> System.exit(0));

        buttons.add(minimize);
        buttons.add(close);

        titleBar.add(title, BorderLayout.WEST);
        titleBar.add(buttons, BorderLayout.EAST);

        final Point[] mousePoint = { null };

        titleBar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent e) {
                mousePoint[0] = e.getPoint();
            }
        });

        titleBar.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent e) {
                Point curr = e.getLocationOnScreen();
                setLocation(curr.x - mousePoint[0].x,
                        curr.y - mousePoint[0].y);
            }
        });

        outerWrapper.add(titleBar, BorderLayout.NORTH);
        outerWrapper.add(cardPanel, BorderLayout.CENTER);

        setContentPane(outerWrapper);

        cardLayout.show(cardPanel, TITLE);
        startDelayedTransition(WARNING, 8000);
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

    // restart game with transition
    public void restartGame() {
        levelTransition.startAnimation();
        cardLayout.show(cardPanel, TRANSITION);

        Timer loadTimer = new Timer(2000, e -> {
            levelTransition.stopAnimation();

            HangmanView newHangman = new HangmanView();
            JPanel newGamePanel = newHangman.createHangmanPanel();

            // new game, sound
            cardPanel.add(newGamePanel, HANGMAN);
            cardLayout.show(cardPanel, HANGMAN);
            soundManager.playSound(SoundManager.HANGMAN_START);
        });

        loadTimer.setRepeats(false);
        loadTimer.start();
    }

    private void styleButton(JButton button) {
        button.setForeground(Color.white);
        button.setBackground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(javax.swing.BorderFactory.createLineBorder(Color.black));
    }

}
