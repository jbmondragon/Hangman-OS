import backend.Database;
import backend.Hangman;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class HangmanView {

    Hangman game;
    Database db = new Database();
    private SoundManager soundManager;

    private JLabel wordLabel;
    private JLabel livesLabel;
    private ImagePanel hangmanImagePanel;
    private ImagePanel mainBackground;
    private JPanel keyboardPanel;
    private JButton[] buttons = new JButton[26];
    private static long lastTime = 0;
    private static final long minimum = 200;

    private Timer s6DisplayTimer;
    private Timer s6SoundTimer;
    private Timer flatlineTimer;
    private Timer gameOverTransitionTimer;
    private String currentHangmanSound = null;
    private boolean isS6Playing = false;
    private boolean gameOverTriggered = false;

    private JPanel mainPanel;
    private RetroWindow[] windows;

    private static final int S6_SOUND_DURATION = 2000;
    private static final int FLATLINE_DURATION = 2000;
    private static final int S6_TOTAL_DISPLAY_TIME = 6000;

    public JPanel createHangmanPanel() {
        soundManager = SoundManager.getInstance();

        String word = db.getRandomWord();
        if (word == null)
            word = "error";
        game = new Hangman(word);

        mainBackground = new ImagePanel("images/MainBg.png");
        mainPanel = mainBackground;
        mainBackground.setLayout(null);
        mainBackground.setSize(1000, 800);

        JPanel guessContent = createGuessContent();
        JPanel virusContent = createVirusContent();
        JPanel keyboardContent = createKeyboardContent(mainBackground);

        RetroWindow guessWindow = new RetroWindow("GUESS.exe", guessContent, 135, 135, 350, 250);
        RetroWindow virusWindow = new RetroWindow("VIRUS.exe", virusContent, 525, 115, 325, 320);
        RetroWindow keyboardWindow = new RetroWindow("KEYBOARD.exe", keyboardContent, 90, 450, 800, 200);

        guessWindow.setVisible(false);
        virusWindow.setVisible(false);
        keyboardWindow.setVisible(false);

        mainBackground.add(guessWindow);
        mainBackground.add(virusWindow);
        mainBackground.add(keyboardWindow);

        windows = new RetroWindow[] { guessWindow, virusWindow, keyboardWindow };
        startFadeInAnimation();

        return mainBackground;
    }

    private void startFadeInAnimation() {
        Timer timer = new Timer(300, null);
        final int[] index = { 0 };
        timer.addActionListener(e -> {
            if (index[0] < windows.length) {
                windows[index[0]].setVisible(true);
                windows[index[0]].repaint();
                index[0]++;
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    private JPanel createGuessContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 220, 220));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        wordLabel = new JLabel(game.getGuessedWord(), SwingConstants.CENTER);
        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 32));
        wordLabel.setForeground(Color.BLACK);

        livesLabel = new JLabel("Lives Remaining: " + game.getRemainingAttempts(), SwingConstants.CENTER);
        livesLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        livesLabel.setForeground(new Color(150, 0, 0));
        livesLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        panel.add(wordLabel, BorderLayout.CENTER);
        panel.add(livesLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createVirusContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        hangmanImagePanel = new ImagePanel("images/S0.gif", false);
        hangmanImagePanel.setOpaque(false);

        panel.add(hangmanImagePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createKeyboardContent(JPanel main) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 220, 220));

        keyboardPanel = createKeys(main);

        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlBar.setBackground(new Color(200, 200, 200));
        controlBar.setBorder(BorderFactory.createEtchedBorder());

        JButton restartBtn = new JButton("RESTART");
        JButton exitBtn = new JButton("HOME");
        restartBtn.setFocusable(false);
        exitBtn.setFocusable(false);

        restartBtn.addActionListener(e -> restartGame(main));
        exitBtn.addActionListener(e -> exitToHome(main));

        controlBar.add(restartBtn);
        controlBar.add(exitBtn);

        panel.add(controlBar, BorderLayout.NORTH);
        panel.add(keyboardPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createKeys(JPanel main) {
        JPanel keyboard = new JPanel(new GridLayout(3, 9, 4, 4));
        keyboard.setBackground(new Color(192, 192, 192));
        keyboard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        AbstractAction actionA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long current = System.currentTimeMillis();
                if (current - lastTime < minimum)
                    return;

                String event = e.getActionCommand();
                char letter = Character.toLowerCase(event.charAt(0));
                int index = letter - 'a';
                if (index >= 0 && index < 26 && buttons[index].isEnabled()) {
                    if (soundManager != null)
                        soundManager.playSound(SoundManager.KEYBOARD);
                    buttons[index].setEnabled(false);
                    game.guess(letter);
                    updateUI();
                    if (game.isGameOver())
                        handleGameOver(main);
                }
                lastTime = current;
            }
        };

        InputMap im = main.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = main.getActionMap();

        for (char c = 'A'; c <= 'Z'; c++) {
            int index = c - 'A';
            buttons[index] = new JButton(String.valueOf(c));
            buttons[index].setFont(new Font("SansSerif", Font.BOLD, 12));
            buttons[index].setFocusable(false);
            buttons[index].setBackground(new Color(240, 240, 240));

            buttons[index].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED),
                    BorderFactory.createEmptyBorder(3, 5, 3, 5)));

            buttons[index].setUI(new javax.swing.plaf.basic.BasicButtonUI() {
                @Override
                public void paint(Graphics g, JComponent c) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(2, 2, c.getWidth() - 4, c.getHeight() - 4, 6, 6);
                    g2.dispose();
                    super.paint(g, c);
                }
            });

            char letter = Character.toLowerCase(c);
            buttons[index].addActionListener(e -> {
                if (soundManager != null)
                    soundManager.playSound(SoundManager.KEYBOARD);
                buttons[index].setEnabled(false);
                game.guess(letter);
                updateUI();
                if (game.isGameOver())
                    handleGameOver(main);
            });

            String actionMapKey = "press" + c;
            KeyStroke keyStroke = KeyStroke.getKeyStroke(c, 0);
            im.put(keyStroke, actionMapKey);
            am.put(actionMapKey, actionA);

            keyboard.add(buttons[index]);
        }
        return keyboard;
    }

    private void updateUI() {
        wordLabel.setText(game.getGuessedWord());
        livesLabel.setText("Lives: " + game.getRemainingAttempts());

        int lives = game.getRemainingAttempts();
        String bgImage = "images/MainBg.png";
        String barImage = "images/bar.png";

        if (lives == 5)
            bgImage = "images/5LivesBg.png";
        else if (lives == 4)
            bgImage = "images/4LivesBg.png";
        else if (lives == 3) {
            bgImage = "images/3LivesBg.png";
            barImage = "images/bar2.png";
        } else if (lives == 2) {
            bgImage = "images/2LivesBg.png";
            barImage = "images/bar2.png";
        } else if (lives == 1) {
            bgImage = "images/1LifeBg.png";
            barImage = "images/bar2.png";
        } else if (lives == 0) {
            bgImage = "images/GameOverBg.png";
            barImage = "images/bar2.png";
        }

        if (mainBackground != null) {
            mainBackground.updateBackgroundImage(bgImage);
            mainBackground.updatePatternImage(barImage);
        }

        String[] imageNames = { "S0.gif", "S1.gif", "S2.gif", "S3.gif", "S4.gif", "S5.gif", "S6.gif" };
        String[] soundNames = { null, SoundManager.S1, SoundManager.S2, SoundManager.S3,
                SoundManager.S4, SoundManager.S5, SoundManager.S6 };
        int index = 6 - game.getRemainingAttempts();
        if (index > 6)
            index = 6;

        if (isS6Playing && index != 6)
            stopS6Timers();

        if (currentHangmanSound != null && soundManager != null)
            soundManager.stopSound(currentHangmanSound);

        if (index > 0 && index <= 6 && soundManager != null) {
            currentHangmanSound = soundNames[index];
            if (index == 6)
                handleS6Sound();
            else
                soundManager.playSoundLoop(currentHangmanSound);
        }

        JPanel parent = (JPanel) hangmanImagePanel.getParent();
        parent.remove(hangmanImagePanel);

        hangmanImagePanel = new ImagePanel("images/" + imageNames[index], false);
        hangmanImagePanel.setOpaque(false);
        parent.add(hangmanImagePanel, BorderLayout.CENTER);

        parent.revalidate();
        parent.repaint();
    }

    private void handleS6Sound() {
        isS6Playing = true;
        gameOverTriggered = true;
        disableKeyboard();

        if (soundManager != null)
            soundManager.playSound(SoundManager.S6);

        s6SoundTimer = new Timer(S6_SOUND_DURATION, e -> {
            if (soundManager != null)
                soundManager.playSound(SoundManager.FLATLINE);

            flatlineTimer = new Timer(FLATLINE_DURATION, e2 -> {
                gameOverTransitionTimer = new Timer(2000, e3 -> transitionToGameOver());
                gameOverTransitionTimer.setRepeats(false);
                gameOverTransitionTimer.start();
            });
            flatlineTimer.setRepeats(false);
            flatlineTimer.start();
        });
        s6SoundTimer.setRepeats(false);
        s6SoundTimer.start();

        s6DisplayTimer = new Timer(S6_TOTAL_DISPLAY_TIME, e -> {
            if (isS6Playing)
                transitionToGameOver();
        });
        s6DisplayTimer.setRepeats(false);
        s6DisplayTimer.start();
    }

    private void transitionToGameOver() {
        stopS6Timers();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;

        Timer delay = new Timer(3000, e -> {
            if (game.isWordGuessed()) {
                mainFrame.showScreen(MainFrame.WIN);
            } else {
                mainFrame.showScreen(MainFrame.LOSE);
            }
        });
        delay.setRepeats(false);
        delay.start();
    }

    private void stopS6Timers() {
        if (s6SoundTimer != null && s6SoundTimer.isRunning())
            s6SoundTimer.stop();
        if (flatlineTimer != null && flatlineTimer.isRunning())
            flatlineTimer.stop();
        if (s6DisplayTimer != null && s6DisplayTimer.isRunning())
            s6DisplayTimer.stop();
        if (gameOverTransitionTimer != null && gameOverTransitionTimer.isRunning())
            gameOverTransitionTimer.stop();
        isS6Playing = false;
    }

    private void handleGameOver(JPanel main) {
        disableKeyboard();
        stopS6Timers();
        if (currentHangmanSound != null && soundManager != null)
            soundManager.stopSound(currentHangmanSound);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (!(frame instanceof MainFrame))
            return;

        // Always go through transitionToGameOver
        transitionToGameOver();
    }

    private void disableKeyboard() {
        for (JButton b : buttons)
            if (b != null)
                b.setEnabled(false);
    }

    private void restartGame(JPanel main) {
        if (soundManager != null)
            soundManager.stopAllSounds();
        stopS6Timers();
        gameOverTriggered = false;
        isS6Playing = false;
        currentHangmanSound = null;
        lastTime = 0;

        String word = db.getRandomWord();
        if (word == null)
            word = "error";
        game = new Hangman(word);

        updateUI();

        for (JButton b : buttons) {
            if (b != null) {
                b.setEnabled(true);
                b.setBackground(new Color(240, 240, 240));
            }
        }
    }

    private void exitToHome(JPanel main) {
        if (soundManager != null)
            soundManager.stopAllSounds();
        stopS6Timers();
        gameOverTriggered = false;
        isS6Playing = false;

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame)
            ((MainFrame) frame).showScreen(MainFrame.HOME);
    }

    class RetroWindow extends JPanel {
        public RetroWindow(String title, JPanel content, int x, int y, int w, int h) {
            setLayout(new BorderLayout());
            setBounds(x, y, w, h);
            setBackground(Color.LIGHT_GRAY);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createBevelBorder(BevelBorder.RAISED),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));

            JPanel titleBar = new JPanel(new BorderLayout());
            titleBar.setBackground(new Color(0, 0, 128));
            titleBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            titleBar.setPreferredSize(new Dimension(w, 25));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

            JButton closeBtn = new JButton(" X ");
            closeBtn.setMargin(new Insets(2, 0, 2, 0));
            closeBtn.setFont(new Font("Monospaced", Font.BOLD, 10));
            closeBtn.setFocusable(false);
            closeBtn.setBackground(Color.LIGHT_GRAY);
            closeBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            titleBar.add(titleLabel, BorderLayout.WEST);
            titleBar.add(closeBtn, BorderLayout.EAST);

            add(titleBar, BorderLayout.NORTH);

            JPanel paddedContent = new JPanel(new BorderLayout());
            paddedContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            paddedContent.setBackground(content.getBackground());
            paddedContent.add(content, BorderLayout.CENTER);

            add(paddedContent, BorderLayout.CENTER);
        }
    }
}
