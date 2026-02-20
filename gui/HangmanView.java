import backend.Database;
import backend.Hangman;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class HangmanView {

    Hangman game;
    Database db = new Database();
    private SoundManager soundManager; // Declared but was not initialized

    private JLabel wordLabel;
    private JLabel livesLabel;
    private ImagePanel hangmanImagePanel;
    private ImagePanel mainBackground;
    private JPanel keyboardPanel;
    private JButton[] buttons = new JButton[26];
    private static long lastTime = 0;
    private static final long minimum = 200;

    // Timers for S6 and flatline synchronization
    private Timer s6DisplayTimer;
    private Timer s6SoundTimer;
    private Timer flatlineTimer;
    private Timer gameOverTransitionTimer;
    private String currentHangmanSound = null;
    private boolean isS6Playing = false;
    private boolean gameOverTriggered = false;
    
    // FIX 2: Ensure this variable is actually used/assigned
    private JPanel mainPanel; 

    // Sound durations (in milliseconds)
    private static final int S6_SOUND_DURATION = 2000; // S6.wav is 2 seconds
    private static final int FLATLINE_DURATION = 2000; // Flatline.wav is 2 seconds
    private static final int S6_TOTAL_DISPLAY_TIME = 6000; // 6 seconds total for S6.gif

    // Window panels
    private RetroWindow[] windows;

    public JPanel createHangmanPanel() {
        // FIX 1: Initialize the SoundManager!
        soundManager = SoundManager.getInstance();
        
        String word = db.getRandomWord();
        if (word == null) word = "error";
        game = new Hangman(word);

        mainBackground = new ImagePanel("images/MainBg.png"); 
        
        // FIX 2: Assign mainBackground to mainPanel so transitionToGameOver can find the frame
        mainPanel = mainBackground;
    
        mainBackground.setLayout(null);
        mainBackground.setSize(1000, 800);

        JPanel guessContent = createGuessContent();
        JPanel virusContent = createVirusContent();
        JPanel keyboardContent = createKeyboardContent(mainBackground);

        // Windows, Coordinates: x, y, width, height
        RetroWindow guessWindow = new RetroWindow("GUESS.exe", guessContent, 135, 135, 350, 250);
        RetroWindow virusWindow = new RetroWindow("VIRUS.exe", virusContent, 525, 115, 325, 320);
        RetroWindow keyboardWindow = new RetroWindow("KEYBOARD.exe", keyboardContent, 90, 450, 800, 200);

        // Visibility set to 0
        guessWindow.setVisible(false);
        virusWindow.setVisible(false);
        keyboardWindow.setVisible(false);

        mainBackground.add(guessWindow);
        mainBackground.add(virusWindow);
        mainBackground.add(keyboardWindow);

        windows = new RetroWindow[]{guessWindow, virusWindow, keyboardWindow};

        startFadeInAnimation();

        return mainBackground;
    }

    // Pop In Animation for Windows
    private void startFadeInAnimation() {
        Timer timer = new Timer(300, null);
        final int[] index = {0};

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

    // Word to be guessed
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

    // Stickman animation
    private JPanel createVirusContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        hangmanImagePanel = new ImagePanel("images/S0.gif", false);
        hangmanImagePanel.setOpaque(false);
        
        panel.add(hangmanImagePanel, BorderLayout.CENTER);
        return panel;
    }

    // Keyboard
    private JPanel createKeyboardContent(JPanel main) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 220, 220));

        keyboardPanel = createKeys(main);
        
        // Restart/Home Button
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

    // Grid for the keys
    private JPanel createKeys(JPanel main) {
        JPanel keyboard = new JPanel(new GridLayout(3, 9, 4, 4));
        keyboard.setBackground(new Color(192, 192, 192));
        keyboard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        AbstractAction actionA = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long current = System.currentTimeMillis();
                if(current - lastTime < minimum) return;
                
                String event = e.getActionCommand();
                char c = event.charAt(0);
                char letter = Character.toLowerCase(c);
                int count = letter - 'a';
                if(count >= 0 && count < 26 && buttons[count].isEnabled()) {
                    // Play sound when button is pressed
                    if(soundManager != null) soundManager.playSound(SoundManager.KEYBOARD);
                    
                    buttons[count].setEnabled(false);
                    game.guess(letter);
                    updateUI();
                    if (game.isGameOver()) handleGameOver(main);
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
            buttons[index].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            
            char letter = Character.toLowerCase(c);
            buttons[index].addActionListener(e -> {
                // Play sound on click
                if(soundManager != null) soundManager.playSound(SoundManager.KEYBOARD);

                buttons[index].setEnabled(false);
                game.guess(letter);
                updateUI();
                if (game.isGameOver()) handleGameOver(main);
            });

            // Keybinds
            String actionMapKey = "press" + c;
            KeyStroke keyStroke = KeyStroke.getKeyStroke(c, 0);
            im.put(keyStroke, actionMapKey);
            am.put(actionMapKey, actionA);

            keyboard.add(buttons[index]);
        }
        return keyboard;
    }

    private void handleS6Sound() {
        isS6Playing = true;
        gameOverTriggered = true; // Mark that game is over but we're showing animation
        
        // Disable keyboard immediately
        disableKeyboard();
        
        // First, play S6.wav (2 seconds)
        if(soundManager != null) soundManager.playSound(SoundManager.S6);
        
        // Schedule Flatline-after-S6 to play immediately after S6 finishes
        s6SoundTimer = new Timer(S6_SOUND_DURATION, e -> {
            if(soundManager != null) soundManager.playSound(SoundManager.FLATLINE);
            
            // Schedule the transition to game over screen after flatline finishes
            flatlineTimer = new Timer(FLATLINE_DURATION, e2 -> {
                // After flatline finishes, wait a bit more then transition
                gameOverTransitionTimer = new Timer(2000, e3 -> { // Remaining 2 seconds of S6 display
                    transitionToGameOver();
                });
                gameOverTransitionTimer.setRepeats(false);
                gameOverTransitionTimer.start();
            });
            flatlineTimer.setRepeats(false);
            flatlineTimer.start();
        });
        s6SoundTimer.setRepeats(false);
        s6SoundTimer.start();
        
        // Set a timer for the total S6 display duration (6 seconds) as backup
        s6DisplayTimer = new Timer(S6_TOTAL_DISPLAY_TIME, e -> {
            // If we haven't transitioned yet for some reason, do it now
            if (isS6Playing) {
                transitionToGameOver();
            }
        });
        s6DisplayTimer.setRepeats(false);
        s6DisplayTimer.start();
    }

    // Transition to appropriate game over screen
    private void transitionToGameOver() {
        if (!isS6Playing) return; // Already transitioned
        
        stopS6Timers();
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;
        
        if (game.isWordGuessed()) {
            mainFrame.showScreen(MainFrame.WIN);
        } else {
            mainFrame.showScreen(MainFrame.LOSE);
        }
    }

    // Stop all S6-related timers
    private void stopS6Timers() {
        if (s6SoundTimer != null && s6SoundTimer.isRunning()) {
            s6SoundTimer.stop();
        }
        if (flatlineTimer != null && flatlineTimer.isRunning()) {
            flatlineTimer.stop();
        }
        if (s6DisplayTimer != null && s6DisplayTimer.isRunning()) {
            s6DisplayTimer.stop();
        }
        if (gameOverTransitionTimer != null && gameOverTransitionTimer.isRunning()) {
            gameOverTransitionTimer.stop();
        }
        isS6Playing = false;
    }

    private void updateUI() {
        wordLabel.setText(game.getGuessedWord());
        livesLabel.setText("Lives: " + game.getRemainingAttempts());
        
        int lives = game.getRemainingAttempts();
        String bgImage = "images/MainBg.png";
        String barImage = "images/bar.png"; 

        if (lives == 5) bgImage = "images/5LivesBg.png";
        else if (lives == 4) bgImage = "images/4LivesBg.png";
        else if (lives == 3) {
            bgImage = "images/3LivesBg.png";
            barImage = "images/bar2.png"; 
        }
        else if (lives == 2) {
            bgImage = "images/2LivesBg.png";
            barImage = "images/bar2.png";
        }
        else if (lives == 1) {
            bgImage = "images/1LifeBg.png";
            barImage = "images/bar2.png";
        }
        else if (lives == 0) {
            bgImage = "images/GameOverBg.png";
            barImage = "images/bar2.png";
        }

        if (mainBackground != null) {
            mainBackground.updateBackgroundImage(bgImage);
            mainBackground.updatePatternImage(barImage);
        }

        // Stickman gifs
        String[] imageNames = {"S0.gif", "S1.gif", "S2.gif", "S3.gif", "S4.gif", "S5.gif", "S6.gif"};
        String[] soundNames = {null, SoundManager.S1, SoundManager.S2, SoundManager.S3, 
                                SoundManager.S4, SoundManager.S5, SoundManager.S6};
        int index = 6 - game.getRemainingAttempts();
        if(index > 6) index = 6;

        // Stop all S6-related timers if we're leaving S6
        if (isS6Playing && index != 6) {
            stopS6Timers();
        }
        
        // Stop previous hangman sound if playing
        if (currentHangmanSound != null && soundManager != null) {
            soundManager.stopSound(currentHangmanSound);
        }
        
        // Play new hangman sound if index > 0 (S1 to S6)
        if (index > 0 && index <= 6 && soundManager != null) {
            currentHangmanSound = soundNames[index];
            
            if (index == 6) {
                // Special handling for S6
                handleS6Sound();
            } else {
                // For S1-S5, play in a continuous loop
                soundManager.playSoundLoop(currentHangmanSound);
            }
        }
        
        // Update the image panel
        JPanel parent = (JPanel) hangmanImagePanel.getParent();
        parent.remove(hangmanImagePanel);
        
        hangmanImagePanel = new ImagePanel("images/" + imageNames[index], false);
        hangmanImagePanel.setOpaque(false);
        parent.add(hangmanImagePanel, BorderLayout.CENTER);
        
        parent.revalidate();
        parent.repaint();
    }

    // Handle end of game
    private void handleGameOver(JPanel main) {
        // If we're already handling S6, don't do anything
        if (isS6Playing) {
            return;
        }
        
        // Check if this is a loss with 0 attempts (S6 case)
        if (game.getRemainingAttempts() <= 0 && !game.isWordGuessed()) {
            // This will trigger S6 display
            updateUI(); // This will call handleS6Sound() through updateUI
            return;
        }
        
        // For win cases or other game over scenarios
        disableKeyboard();
        
        // Stop any playing hangman sounds
        if (currentHangmanSound != null && soundManager != null) {
            soundManager.stopSound(currentHangmanSound);
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (!(frame instanceof MainFrame))
            return;

        MainFrame mainFrame = (MainFrame) frame;
        
        if (game.isWordGuessed()) {
            mainFrame.showScreen(MainFrame.WIN);
        }
        // Note: Loss with remaining attempts doesn't happen since we handle S6 above
    }

    private void disableKeyboard() {
        for (JButton b : buttons) {
            if(b != null) b.setEnabled(false);
        }
    }

    private void restartGame(JPanel main) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame) ((MainFrame) frame).restartGame();
    }

    // Go to home
    private void exitToHome(JPanel main) {
        // Stop all sounds and timers when exiting to home
        if(soundManager != null) soundManager.stopAllSounds();
        stopS6Timers();
        gameOverTriggered = false;
        isS6Playing = false;
        
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(main);
        if (frame instanceof MainFrame)
            ((MainFrame) frame).showScreen(MainFrame.HOME);
    }

    // Window Panel
    class RetroWindow extends JPanel {

        public RetroWindow(String title, JPanel content, int x, int y, int w, int h) {
            setLayout(new BorderLayout());
            setBounds(x, y, w, h);
            setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            setBackground(Color.LIGHT_GRAY);

            // Title
            JPanel titleBar = new JPanel(new BorderLayout());
            titleBar.setBackground(new Color(0, 0, 128));
            titleBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            titleBar.setPreferredSize(new Dimension(w, 25));

            JLabel titleLabel = new JLabel(title);
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

            // X button
            JButton closeBtn = new JButton(" X ");
            closeBtn.setMargin(new Insets(2, 0, 2, 0));
            closeBtn.setFont(new Font("Monospaced", Font.BOLD, 10));
            closeBtn.setFocusable(false);
            closeBtn.setBackground(Color.LIGHT_GRAY);
            closeBtn.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

            titleBar.add(titleLabel, BorderLayout.WEST);
            titleBar.add(closeBtn, BorderLayout.EAST);

            add(titleBar, BorderLayout.NORTH);
            add(content, BorderLayout.CENTER);
        }
    }
}